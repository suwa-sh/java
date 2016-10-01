package me.suwash.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.suwash.util.constant.UtilMessageConst;
import me.suwash.util.exception.UtilException;
import me.suwash.util.i18n.MessageSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ResourceNotFoundException;

/**
 * 自動生成関連ユーティリティ。
 */
@lombok.extern.slf4j.Slf4j
public final class GenerateUtils {

    private static final String LINE_SEPARATOR = "line.separator";
    private static final String CHARSET_TEMPLATE = "utf8";
    private static final String LOGTAG = GenerateUtils.class.getSimpleName();

    /**
     * ユーティリティ向けに、コンストラクタはprivate宣言。
     */
    private GenerateUtils() {}

    /**
     * VMファイルと変数定義Mapからファイルを生成します。
     *
     * @param templateDirPathList VMファイル配置ディレクトリリスト
     * @param templateFileName VMファイル名
     * @param contextMap 変数定義Map
     * @param filePath 出力ファイルパス
     * @param charset 出力ファイル文字コード
     * @param lineSp 出力ファイル改行コード
     */
    @lombok.Synchronized
    public static void vmPath2File(
        final List<String> templateDirPathList,
        final String templateFileName,
        final Map<String, Object> contextMap,
        final String filePath,
        final String charset,
        final String lineSp
    ) {
        // ファイル書き出し共通チェック
        FileUtils.writeCheck(filePath, charset);

        // ファイル上書き共通処理
        FileUtils.setupOverwrite(filePath);

        // 出力改行コード
        if (StringUtils.isEmpty(lineSp)) {
            throw new UtilException(UtilMessageConst.CHECK_NOTNULL, new Object[] {"lineSp"});
        }

        // Writer
        final File outputFile = new File(filePath);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), charset));
        } catch (Exception e) {
            throw new UtilException(UtilMessageConst.STREAM_CANTOPEN_OUTPUT, new Object[] {outputFile}, e);
        }

        final String beforeLineSp = System.getProperty(LINE_SEPARATOR);
        try {
            System.setProperty(LINE_SEPARATOR, lineSp);
            vmPath2Writer(templateDirPathList, templateFileName, contextMap, writer);
            System.setProperty(LINE_SEPARATOR, beforeLineSp);

        } catch (RuntimeException e) {
            // 例外発生時も改行コードのデフォルト値を戻す
            System.setProperty(LINE_SEPARATOR, beforeLineSp);

            // 入力チェックエラー時はストリームが閉じられていないので、ここでクローズ
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ie) {
                    final String message = MessageSource.getInstance().getMessage(UtilMessageConst.STREAM_CANTCLOSE_OUTPUT, new Object[] {outputFile});
                    log.error(message, ie);
                }
            }

            // テンプレートエラー時は、空ファイルが出力されてしまうので、ここで削除
            if (outputFile.exists() && outputFile.delete()) {
                final String curErrorMessage = MessageSource.getInstance().getMessage(UtilMessageConst.FILE_CANTDELETE, new Object[] {outputFile});
                final String message = MessageSource.getInstance().getMessage(UtilMessageConst.ERROR_ON_ERRORHANDLE, new Object[] { GenerateUtils.class.getName() + ".vmPath2Writer", curErrorMessage });
                log.debug(message);
            }

            // 例外をそのままthrow
            throw e;
        }
    }

    /**
     * VMファイルと変数定義Mapからファイルを生成します。
     *
     * @param templateClasspathList VMファイル配置クラスパスリスト
     * @param templateFileName VMファイル名
     * @param contextMap 変数定義Map
     * @param outputFilePath 出力ファイルパス
     * @param outputCharset 出力ファイル文字コード
     * @param outputLineSp 出力ファイル改行コード
     */
    public static void vmClasspath2File(
        final List<String> templateClasspathList,
        final String templateFileName,
        final Map<String, Object> contextMap,
        final String outputFilePath,
        final String outputCharset,
        final String outputLineSp
    ) {
        final List<String> templateDirPathList = classpathList2pathList(templateClasspathList);
        vmPath2File(templateDirPathList, templateFileName, contextMap, outputFilePath, outputCharset, outputLineSp);
    }

    /**
     * VMファイル、変数定義Mapからの文字列生成。
     *
     * @param templateDirPathList VMファイル配置ディレクトリリスト
     * @param templateFileName VMファイル名
     * @param contextMap 変数定義Map
     * @return 生成した文字列
     */
    public static String vmPath2String(
        final List<String> templateDirPathList,
        final String templateFileName,
        final Map<String, Object> contextMap
    ) {
        // 文字列生成
        final StringWriter writer = new StringWriter();

        try {
            vmPath2Writer(templateDirPathList, templateFileName, contextMap, writer);

        } catch (Exception e) {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ie) {
                    final String curErrorMessage = MessageSource.getInstance().getMessage(UtilMessageConst.STREAM_CANTCLOSE_OUTPUT, new Object[] {writer});
                    final String message = MessageSource.getInstance().getMessage(UtilMessageConst.ERROR_ON_ERRORHANDLE, new Object[] {GenerateUtils.class.getName() + ".vmPath2Writer", curErrorMessage});
                    log.error(message, ie);
                }
            }
            throw e;
        }

        return writer.toString();
    }

    /**
     * VMファイルと変数定義Mapから生成した文字列を返します。
     *
     * @param templateClasspathList VMファイル配置クラスパスリスト
     * @param templateFileName VMファイル名
     * @param contextMap 変数定義Map
     * @return 生成した文字列
     */
    public static String vmClasspath2String(
        final List<String> templateClasspathList,
        final String templateFileName,
        final Map<String, Object> contextMap
    ) {
        final List<String> templateDirPathList = classpathList2pathList(templateClasspathList);
        return vmPath2String(templateDirPathList, templateFileName, contextMap);
    }

    /**
     * VTLテンプレート文字列、変数定義Mapの内容からの文字列生成。
     *
     * @param template VTLテンプレート文字列
     * @param contextMap 変数定義Map
     * @return 生成した文字列
     */
    public static String template2String(final String template, final Map<String, Object> contextMap) {
        final VelocityContext context = getVelocityContext(contextMap);

        // 文字列生成
        final StringWriter writer = new StringWriter();
        try {
            Velocity.init();
            Velocity.evaluate(context, writer, LOGTAG, template);
            writer.close();

        } catch (Exception e) {
            // 入力チェックエラー時はストリームが閉じられていないので、ここでクローズ
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ie) {
                    final String curErrorMessage = MessageSource.getInstance().getMessage(UtilMessageConst.STREAM_CANTCLOSE_OUTPUT, new Object[] {writer});
                    final String message = MessageSource.getInstance().getMessage(UtilMessageConst.ERROR_ON_ERRORHANDLE, new Object[] {GenerateUtils.class.getName() + ".template2String", curErrorMessage});
                    log.error(message, ie);
                }
            }
            throw new UtilException(UtilMessageConst.ERRORHANDLE, new Object[] {
                GenerateUtils.class.getName() + ".template2String",
                "template=" + template + ", contextMap=" + contextMap
            }, e);
        }

        return writer.toString();
    }

    /**
     * VMファイル、変数定義Mapから生成した文字列のwriter出力。
     *
     * @param templateDirPathList VMファイル配置ディレクトリリスト
     * @param templateFileName VMファイル名
     * @param contextMap 変数定義Map
     * @param writer 出力先
     */
    private static void vmPath2Writer(
        final List<String> templateDirPathList,
        final String templateFileName,
        final Map<String, Object> contextMap,
        final Writer writer
    ) {
        // VMファイル配置ディレクトリリスト
        if (templateDirPathList == null) {
            throw new UtilException(UtilMessageConst.CHECK_NOTNULL, new Object[] {"templateDirPathList"});
        }

        // VMファイル名
        if (StringUtils.isEmpty(templateFileName)) {
            throw new UtilException(UtilMessageConst.CHECK_NOTNULL, new Object[] {"templateFileName"});
        }

        // テンプレート配置ディレクトリの存在チェック
        for (final String templateDirPath : templateDirPathList) {
            checkDirExist(templateDirPath);
        }

        // コンテキスト設定
        final VelocityContext context = getVelocityContext(contextMap);

        // ファイル生成
        try {
            final String templateDirPathsDef =
                    templateDirPathList.toString()
                    .substring(1, templateDirPathList.toString().length() - 1) // リストの囲み文字を除去
                    .replaceAll(" ", ""); // スペースを詰める
            final VelocityEngine ve = new VelocityEngine();
            ve.setProperty("resource.loader", "FILE");
            ve.setProperty("FILE.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
            ve.setProperty("FILE.resource.loader.path", templateDirPathsDef);

            final Template template = ve.getTemplate(templateFileName, CHARSET_TEMPLATE);
            template.merge(context, writer);
            writer.close();

        } catch (ResourceNotFoundException re) {
            // リソースが見つからない場合
            throw new UtilException(UtilMessageConst.CHECK_NOTEXIST, new Object[] {templateFileName}, re);

        } catch (Exception e) {
            throw new UtilException(UtilMessageConst.ERRORHANDLE, new Object[] {
                GenerateUtils.class.getName() + ".vmPath2Writer",
                "templateDirList=" + templateDirPathList + ", templateFile=" + templateFileName + ", contextMap=" + contextMap + ", writer=" + writer
            }, e);

        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    throw new UtilException(UtilMessageConst.STREAM_CANTCLOSE_OUTPUT, new Object[] {writer}, e);
                }
            }
        }
    }

    /**
     * ディレクトリの存在チェック。
     *
     * @param dirPath 対象ディレクトリ
     */
    private static void checkDirExist(final String dirPath) {
        final File templateDir = new File(dirPath);
        if (!templateDir.exists()) {
            throw new UtilException(UtilMessageConst.CHECK_NOTEXIST, new Object[] {templateDir});
        }
        if (!templateDir.isDirectory()) {
            throw new UtilException(UtilMessageConst.DIR_CHECK, new Object[] {templateDir});
        }
    }

    /**
     * String:ObjectのMapから、Velocityコンテキストを返します。
     *
     * @param contextMap コンテキストMap
     * @return Velocityコンテキスト
     */
    private static VelocityContext getVelocityContext(final Map<String, Object> contextMap) {
        final VelocityContext context = new VelocityContext();
        if (contextMap != null) {
            for (final Map.Entry<String, Object> entry : contextMap.entrySet()) {
                final String key = entry.getKey();
                final Object value = entry.getValue();
                context.put(key, value);
            }
        }
        return context;
    }

    /**
     * 指定したクラスパスが存在するか否かを返します。
     *
     * @param classpathStr 対象クラスパス
     * @return 存在する場合、OK
     */
    public static boolean isExistClasspath(final String classpathStr) {
        boolean isExist = true;
        final URL classpath = getClasspathUrl(classpathStr);
        if (classpath == null) {
            isExist = false;
        }
        return isExist;
    }

    /**
     * 指定したクラスパスからURLオブジェクトを返します。
     *
     * @param classpathStr 対象クラスパス
     * @return URLオブジェクト
     */
    private static URL getClasspathUrl(final String classpathStr) {
        return GenerateUtils.class.getResource(classpathStr);
    }

    /**
     * クラスパスリストをフルパスリストに変換します。
     *
     * @param classpathList クラスパスリスト
     * @return 変換したフルパスリスト
     */
    private static List<String> classpathList2pathList(final List<String> classpathList) {
        final List<String> pathList = new ArrayList<String>();
        for (final String classpathStr : classpathList) {
            if (! isExistClasspath(classpathStr)) {
                throw new UtilException(UtilMessageConst.CHECK_NOTEXIST, new Object[] {"classpath:" + classpathStr});
            }
            pathList.add(getClasspathUrl(classpathStr).getPath());
        }
        return pathList;
    }

}
