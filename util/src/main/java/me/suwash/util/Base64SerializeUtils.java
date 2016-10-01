package me.suwash.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;

import me.suwash.util.constant.UtilMessageConst;
import me.suwash.util.exception.UtilException;
import me.suwash.util.i18n.MessageSource;

import org.apache.commons.codec.binary.Base64;

/**
 * Base64文字列でのシリアライズユーティリティ。
 */
@lombok.extern.slf4j.Slf4j
public final class Base64SerializeUtils {

    private static final char START = '[';
    private static final char END = ']';
    private static final String CHARS = "0123456789ABCDEF";

    /**
     * ユーティリティ向けに、コンストラクタはprivate宣言。
     */
    private Base64SerializeUtils() {}

    /**
     * Base64文字列として直列化する。
     *
     * @param object 直列化するオブジェクト
     * @return 直列化された文字列
     */
    public static String serialize(final Object object) {
        final ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        try {
            final ObjectOutputStream os = new ObjectOutputStream(byteArray);
            try {
                os.writeObject(object);
            } finally {
                os.close();
            }
        } catch (IOException ex) {
            throw new UtilException(UtilMessageConst.FILE_CANTWRITE, new Object[] {ObjectOutputStream.class.getSimpleName(), ex.getMessage()}, ex);
        }

        final byte[] bytes = byteArray.toByteArray();
        final StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            final int curByte = bytes[i] & 0xFF;
            if (curByte < ' ' || curByte > '~' || curByte == START || curByte == END) {
                buffer.append(START);
                buffer.append(toHexString(bytes[i]));
                buffer.append(END);
            } else {
                buffer.append((char) curByte);
            }
        }

        // Base64エンコード
        final byte[] encodedBytes = Base64.encodeBase64(buffer.toString().getBytes(Charset.defaultCharset()));
        return new String(encodedBytes, Charset.defaultCharset());
    }

    /**
     * Base64シリアライズされた文字列からオブジェクトを復元する。
     *
     * @param string シリアライズされた文字列
     * @return 復元されたオブジェクト
     */
    public static Object deserialize(final String string) {
        // Base64デコード
        final byte[] decodedBytes = Base64.decodeBase64(string.getBytes(Charset.defaultCharset()));
        final String decoded = new String(decodedBytes, Charset.defaultCharset());

        Object obj = null;
        try {
            final ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                final int n = decoded.length();
                for (int i = 0; i < n; i++) {
                    final char curChar = (char) decoded.charAt(i);
                    if (curChar == START) {
                        final String hex = decoded.substring(i + 1, i + 3);
                        os.write(Integer.parseInt(hex, 16));
                        i += 3;
                    } else {
                        os.write(curChar);
                    }
                }
            } finally {
                os.close();
            }
            final ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(os.toByteArray()));
            try {
                obj = is.readObject();
            } catch (ClassNotFoundException e) {
                if (log.isTraceEnabled()) {
                    log.warn(MessageSource.getInstance().getMessage(UtilMessageConst.ERRORHANDLE, new Object[] {
                        Base64SerializeUtils.class.getName() + ".deserialize", e.getMessage()
                    }), e);
                }
            } finally {
                is.close();
            }

        } catch (IOException ex) {
            if (log.isTraceEnabled()) {
                log.warn(MessageSource.getInstance().getMessage(UtilMessageConst.ERRORHANDLE, new Object[] {
                    Base64SerializeUtils.class.getName() + ".deserialize", ex.getMessage()
                }), ex);
            }
        }

        return obj;
    }

    /**
     * バイト値を16進数表記にする。
     *
     * @param targetByte バイト値
     * @return 16進数表記
     */
    private static String toHexString(final byte targetByte) {
        final int lo = targetByte & 0xF;
        final int hi = targetByte >> 4 & 0xF;
        return new String(new char[] {
            CHARS.charAt(hi), CHARS.charAt(lo)
        });
    }
}
