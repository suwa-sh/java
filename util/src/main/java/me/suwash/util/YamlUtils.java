package me.suwash.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.TimeZone;

import me.suwash.util.constant.UtilMessageConst;
import me.suwash.util.exception.UtilException;

import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.DumperOptions.ScalarStyle;
import org.yaml.snakeyaml.Yaml;

/**
 * YAML関連ユーティリティ。
 */
public final class YamlUtils {

    /** メッセージ出力用データ文字列長。 */
    private static final int PART_CONTENT_LENGTH = 200;
    /** YAMLパーサ。 */
    private static Yaml yaml;

    /**
     * ユーティリティ向けに、コンストラクタはprivate宣言。
     */
    private YamlUtils() {}

    static {
        final DumperOptions dumperOpts = new DumperOptions();
        dumperOpts.setDefaultFlowStyle(FlowStyle.BLOCK);
        dumperOpts.setDefaultScalarStyle(ScalarStyle.PLAIN);
        dumperOpts.setTimeZone(TimeZone.getDefault());

        yaml = new Yaml(dumperOpts);
    }

    /**
     * YAMLファイルをオブジェクトに変換します。
     *
     * @param <T> 変換先クラス
     * @param filePath ファイルパス
     * @param charset 文字コード
     * @param type 変換先クラス
     * @return 変換したオブジェクト ※Object型で返却されます。キャストして利用してください。
     */
    public static <T> T parseFile(final String filePath, final String charset, final Class<T> type) {
        //--------------------------------------------------
        // 事前処理
        //--------------------------------------------------
        // ファイル共通チェック
        FileUtils.readCheck(filePath, charset);

        // 変換先クラス
        if (type == null) {
            throw new UtilException(UtilMessageConst.CHECK_NOTNULL, new Object[] {"type"});
        }

        //--------------------------------------------------
        // 本処理
        //--------------------------------------------------
        try {
            final File file = new File(filePath);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
            return yaml.loadAs(reader, type);
        } catch (Exception e) {
            throw new UtilException(UtilMessageConst.ERRORHANDLE, new Object[] {
                YamlUtils.class.getName() + ".parseFileByClasspath",
                "file=" + filePath + ", charset=" + charset + ", type=" + type.getSimpleName()
            }, e);
        }

    }

    /**
     * YAMLファイルをオブジェクトに変換します。
     *
     * @param <T> 変換先クラス
     * @param filePath ファイルパス（クラスパス）
     * @param charset 文字コード
     * @param type 変換先クラス
     * @return 変換したオブジェクト
     */
    public static <T> T parseFileByClasspath(final String filePath, final String charset, final Class<T> type) {
        //--------------------------------------------------
        // 事前処理
        //--------------------------------------------------
        // ファイル共通チェック（クラスパス）
        FileUtils.readCheckByClasspath(filePath, charset, type);

        // 変換先クラス
        if (type == null) {
            throw new UtilException(UtilMessageConst.CHECK_NOTNULL, new Object[] {"type"});
        }

        //--------------------------------------------------
        // 本処理
        //--------------------------------------------------
        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(YamlUtils.class.getResourceAsStream(filePath), charset));
            return yaml.loadAs(reader, type);
        } catch (Exception e) {
            throw new UtilException(UtilMessageConst.ERRORHANDLE, new Object[] {
                YamlUtils.class.getName() + ".parseFileByClasspath",
                "file=" + filePath + ", charset=" + charset + ", type=" + type.getSimpleName()
            }, e);
        }

    }

    /**
     * YAML文字列をオブジェクトに変換します。
     *
     * @param <T> 変換先クラス
     * @param target YAML形式の文字列
     * @param type 変換先クラス
     * @return 変換したオブジェクト
     */
    public static <T> T parseString(final String target, final Class<T> type) {
        //--------------------------------------------------
        // 事前処理
        //--------------------------------------------------
        // 変換先クラス
        if (type == null) {
            throw new UtilException(UtilMessageConst.CHECK_NOTNULL, new Object[] {"type"});
        }

        //--------------------------------------------------
        // 本処理
        //--------------------------------------------------
        if (StringUtils.isEmpty(target)) {
            return null;
        }

        try {
            return yaml.loadAs(target, type);
        } catch (Exception e) {
            String partTarget = target;
            if (target.length() > PART_CONTENT_LENGTH) {
                partTarget = target.substring(0, PART_CONTENT_LENGTH);
            }
            throw new UtilException(UtilMessageConst.ERRORHANDLE, new Object[] {
                YamlUtils.class.getName() + ".parseString",
                "target(part)=" + partTarget + ", type=" + type.getSimpleName()
            }, e);
        }
    }

    /**
     * オブジェクトをYAML文字列に変換します。
     *
     * @param target オブジェクト
     * @return 変換した文字列
     */
    public static String writeString(final Object target) {
        if (target == null) {
            return null;
        }

        try {
            return yaml.dump(target);
        } catch (Exception e) {
            throw new UtilException(UtilMessageConst.ERRORHANDLE, new Object[] {
                YamlUtils.class.getName() + ".writeString",
                "target=" + target
            }, e);
        }
    }

}
