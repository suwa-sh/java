package me.suwash.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.suwash.util.constant.UtilMessageConst;
import me.suwash.util.exception.UtilException;
import me.suwash.util.i18n.MessageSource;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.lang3.StringUtils;

/**
 * 圧縮関連ユーティリティ。
 * TODO uncompress
 */
@lombok.extern.slf4j.Slf4j
public final class CompressUtils {

    private static final byte[] BUFFER = new byte[1024];

    /**
     * ユーティリティ向けに、コンストラクタはprivate宣言。
     */
    private CompressUtils() {}

    /**
     * 指定ディレクトリをtar.gzに圧縮します。
     *
     * @param targetDirPath 対象ディレクトリ
     * @param paraOutputFilePath 出力ファイルパス
     * @param isRelative 相対パスで圧縮するか？
     */
    public static void tarGz(final String targetDirPath, final String paraOutputFilePath, final boolean isRelative) {
        // --------------------------------------------------
        // 入力チェック
        // --------------------------------------------------
        // 対象ディレクトリ
        FileUtils.readDirCheck(targetDirPath);

        // 出力ファイルパス
        String outputFilePath = StringUtils.EMPTY;
        if (StringUtils.isEmpty(paraOutputFilePath)) {
            // 未指定の場合、対象ディレクトリパスにtar.gzを付与したファイル ※対象ディレクトリの並び
            outputFilePath = targetDirPath + ".tar.gz";
        } else {
            outputFilePath = paraOutputFilePath;
        }

        // ファイル上書き共通処理
        FileUtils.setupOverwrite(outputFilePath);

        // --------------------------------------------------
        // tar.gz圧縮
        // --------------------------------------------------
        // tar圧縮
        final String tarFilePath = outputFilePath.replace(".tar.gz", ".tar");
        tar(targetDirPath, tarFilePath, isRelative);

        // gzip圧縮
        gzip(tarFilePath, outputFilePath);

        // tarファイル削除
        final File tarFile = new File(tarFilePath);
        if (! tarFile.delete()) {
            throw new UtilException(UtilMessageConst.FILE_CANTDELETE, new Object[] {
                "(temp file)" + tarFile
            });
        }
    }

    /**
     * 指定ファイルをgz圧縮します。
     *
     * @param targetFilePath 対象ファイルパス
     * @param paraOutputFilePath 出力ファイルパス
     */
    public static void gzip(final String targetFilePath, final String paraOutputFilePath) {
        // --------------------------------------------------
        // 入力チェック
        // --------------------------------------------------
        // 対象ファイル
        FileUtils.readBinaryCheck(targetFilePath);

        // 出力ファイルパス
        String outputFilePath = StringUtils.EMPTY;
        if (StringUtils.isEmpty(paraOutputFilePath)) {
            // 未指定の場合、対象ディレクトリパスにtarを付与したファイル ※対象ディレクトリの並び
            outputFilePath = targetFilePath + ".gz";
        } else {
            outputFilePath = paraOutputFilePath;
        }

        // ファイル上書き共通処理
        FileUtils.setupOverwrite(outputFilePath);

        // --------------------------------------------------
        // gz圧縮
        // --------------------------------------------------
        final File targetFile = new File(targetFilePath);
        // ファイル入力ストリーム
        BufferedInputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(targetFile));
        } catch (Exception e) {
            throw new UtilException(UtilMessageConst.STREAM_CANTOPEN_INPUT, new Object[] {
                targetFile
            }, e);
        }

        final File outputFile = new File(outputFilePath);
        // gz出力ストリーム
        GzipCompressorOutputStream outputStream = null;
        try {
            outputStream = new GzipCompressorOutputStream(new FileOutputStream(outputFile));
        } catch (Exception e) {
            try {
                inputStream.close();
            } catch (IOException e1) {
                final String curErrorMessage = MessageSource.getInstance().getMessage(UtilMessageConst.STREAM_CANTCLOSE_INPUT, new Object[] {
                    targetFile
                });
                final String message = MessageSource.getInstance().getMessage(UtilMessageConst.ERROR_ON_ERRORHANDLE, new Object[] {
                    "openOutputStream", curErrorMessage
                });
                log.error(message, e1);
            }
            throw new UtilException(UtilMessageConst.STREAM_CANTOPEN_OUTPUT, new Object[] {
                outputFile
            }, e);
        }

        // read & write
        try {
            int size = inputStream.read(BUFFER);
            while (size > 0) {
                outputStream.write(BUFFER, 0, size);
                size = inputStream.read(BUFFER);
            }
        } catch (IOException e) {
            try {
                inputStream.close();
            } catch (IOException e1) {
                final String curErrorMessage = MessageSource.getInstance().getMessage(UtilMessageConst.STREAM_CANTCLOSE_INPUT, new Object[] {
                    targetFilePath
                });
                final String message = MessageSource.getInstance().getMessage(UtilMessageConst.ERROR_ON_ERRORHANDLE, new Object[] {
                    CompressUtils.class.getName() + ".compressGz", curErrorMessage
                });
                log.error(message, e1);
            }
            try {
                outputStream.close();
            } catch (IOException e1) {
                final String curErrorMessage = MessageSource.getInstance().getMessage(UtilMessageConst.STREAM_CANTCLOSE_OUTPUT, new Object[] {
                    outputFilePath
                });
                final String message = MessageSource.getInstance().getMessage(UtilMessageConst.ERROR_ON_ERRORHANDLE, new Object[] {
                    CompressUtils.class.getName() + ".compressGz", curErrorMessage
                });
                log.error(message, e1);
            }
            throw new UtilException(UtilMessageConst.ERRORHANDLE, new Object[] {
                CompressUtils.class.getName() + ".compressGz",
                "targetFile=" + targetFilePath + ", outputFile=" + paraOutputFilePath
            }, e);

        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e1) {
                    throw new UtilException(UtilMessageConst.STREAM_CANTCLOSE_INPUT, new Object[] {
                        targetFile
                    }, e1);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e1) {
                    throw new UtilException(UtilMessageConst.STREAM_CANTCLOSE_OUTPUT, new Object[] {
                        outputFile
                    }, e1);
                }
            }
        }

    }

    /**
     * 指定ディレクトリをtarに圧縮します。
     *
     * @param targetDirPath 対象ディレクトリ
     * @param paraOutputFilePath 出力ファイルパス
     * @param isRelative 相対パスで圧縮するか？
     */
    public static void tar(final String targetDirPath, final String paraOutputFilePath, final boolean isRelative) {
        // --------------------------------------------------
        // 入力チェック
        // --------------------------------------------------
        // 対象ディレクトリ
        FileUtils.readDirCheck(targetDirPath);

        // 出力ファイルパス
        String outputFilePath = StringUtils.EMPTY;
        if (StringUtils.isEmpty(paraOutputFilePath)) {
            // 未指定の場合、対象ディレクトリパスにtarを付与したファイル ※対象ディレクトリの並び
            outputFilePath = targetDirPath + ".tar";
        } else {
            outputFilePath = paraOutputFilePath;
        }

        // ファイル上書き共通処理
        FileUtils.setupOverwrite(outputFilePath);

        // --------------------------------------------------
        // tar作成
        // --------------------------------------------------
        final File outputFile = new File(outputFilePath);
        // tar出力ストリーム
        TarArchiveOutputStream tarStream = null;
        try {
            tarStream = new TarArchiveOutputStream(new FileOutputStream(outputFilePath));
        } catch (FileNotFoundException e) {
            throw new UtilException(UtilMessageConst.STREAM_CANTOPEN_OUTPUT, new Object[] {
                outputFile
            }, e);
        }
        tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_POSIX);

        // 相対パス判断用親ディレクトリパス
        final File targetDir = new File(targetDirPath);
        final String parentDirPath = targetDir.getParentFile().getAbsolutePath();

        // 対象ディレクトリ配下を再帰的に検索
        final List<File> findedFileList = FindUtils.find(targetDirPath);

        // 対象ディレクトリ + 検索結果を対象にして全件ループ
        final List<File> targetFileList = new ArrayList<File>();
        targetFileList.add(targetDir);
        targetFileList.addAll(findedFileList);
        try {
            for (final File targetFile : targetFileList) {
                // 相対パスを考慮してエントリ作成
                putArchiveEntry(isRelative, tarStream, parentDirPath, targetFile);

                // ファイルの場合、コンテンツ出力
                if (targetFile.isFile()) {
                    writeContent(targetFile, tarStream);
                }

                // エントリをクローズ
                tarStream.closeArchiveEntry();
            }

        } catch (Exception e) {
            try {
                tarStream.close();
            } catch (IOException ie) {
                final String curErrorMessage = MessageSource.getInstance().getMessage(UtilMessageConst.STREAM_CANTCLOSE_OUTPUT, new Object[] {
                    outputFilePath
                });
                final String message = MessageSource.getInstance().getMessage(UtilMessageConst.ERROR_ON_ERRORHANDLE, new Object[] {
                    CompressUtils.class.getName() + ".compressTar", curErrorMessage
                });
                log.error(message, ie);
            }
            throw new UtilException(UtilMessageConst.ERRORHANDLE, new Object[] {
                CompressUtils.class.getName() + ".compressTar",
                "targetDir=" + targetDirPath + ", outputFile=" + paraOutputFilePath + ", isRelative=" + isRelative
            }, e);

        } finally {
            if (tarStream != null) {
                try {
                    tarStream.close();
                } catch (IOException e) {
                    throw new UtilException(UtilMessageConst.STREAM_CANTCLOSE_OUTPUT, new Object[] {
                        outputFile
                    }, e);
                }
            }
        }
    }

    /**
     * tarファイルにパスを追加します。
     *
     * @param isRelative 相対パスフラグ
     * @param tarStream tar出力ストリーム
     * @param parentDirPath 親ディレクトリ
     * @param targetFile 追加ファイル
     * @throws IOException 予期せぬエラー
     */
    private static void putArchiveEntry(final boolean isRelative, final TarArchiveOutputStream tarStream, final String parentDirPath, final File targetFile) throws IOException {
        String pathAdd = null;
        if (isRelative) {
            pathAdd = targetFile.getAbsolutePath().replace(parentDirPath, ".");
        } else {
            pathAdd = targetFile.getAbsolutePath();
        }
        final TarArchiveEntry entry = new TarArchiveEntry(targetFile, pathAdd);
        tarStream.putArchiveEntry(entry);
    }

    /**
     * tar出力ストリームにファイルの内容を追加します。
     *
     * @param targetFile 対象ファイル
     * @param tarStream tar出力ストリーム
     */
    private static void writeContent(final File targetFile, final TarArchiveOutputStream tarStream) {
        BufferedInputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(targetFile));
        } catch (Exception e) {
            throw new UtilException(UtilMessageConst.STREAM_CANTOPEN_INPUT, new Object[] {
                targetFile
            }, e);
        }

        try {
            int size = inputStream.read(BUFFER);
            while (size > 0) {
                tarStream.write(BUFFER, 0, size);
                size = inputStream.read(BUFFER);
            }

        } catch (Exception e) {
            try {
                inputStream.close();
            } catch (Exception ie) {
                final String curErrorMessage = MessageSource.getInstance().getMessage(UtilMessageConst.STREAM_CANTCLOSE_INPUT, new Object[] {
                    targetFile
                });
                final String message = MessageSource.getInstance().getMessage(UtilMessageConst.ERROR_ON_ERRORHANDLE, new Object[] {
                    CompressUtils.class.getName() + ".writeContent", curErrorMessage
                });
                log.error(message, ie);
            }
            throw new UtilException(UtilMessageConst.ERRORHANDLE, new Object[] {
                CompressUtils.class.getName() + ".writeContent",
                "targetFile=" + targetFile + ", tarStream=" + tarStream
            }, e);

        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    throw new UtilException(UtilMessageConst.STREAM_CANTCLOSE_INPUT, new Object[] {
                        targetFile
                    }, e);
                }
            }
        }
    }

}
