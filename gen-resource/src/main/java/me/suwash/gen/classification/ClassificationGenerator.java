package me.suwash.gen.classification;

import java.io.File;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import me.suwash.util.FileUtils;
import me.suwash.util.JsonUtils;
import me.suwash.util.constant.UtilMessageConst;
import me.suwash.util.exception.UtilException;
import me.suwash.util.i18n.MessageSource;

import org.apache.commons.lang3.SystemUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

/**
 * 区分関連ファイルジェネレータ。
 */
@lombok.extern.slf4j.Slf4j
public final class ClassificationGenerator {

    private static final String CHARSET = "UTF-8";
    private static final int ARG_LENGTH = 4;

    /**
     * mainクラス向けに、コンストラクタはprivate宣言。
     */
    private ClassificationGenerator() {}

    /**
     * 区分定義JSONファイルの配置ディレクトリを指定して、区分クラスとDDプロパティを出力します。
     *
     * @param args 1:定義ファイル配置ディレクトリ, 2:java出力ディレクトリ, 3:properties出力ディレクトリ, 4:DD propertiesファイル名
     */
    public static void main(final String... args) {
        // --------------------------------------------------
        // 事前処理
        // --------------------------------------------------
        log.info("process start.");

        // 引数の数
        if (args.length != ARG_LENGTH) {
            // 一致しない場合、エラー
            throw new UtilException(UtilMessageConst.CHECK_COUNTSAME, new Object[] {
                "args", ARG_LENGTH, args.length
            });
        }

        // 対象ディレクトリ
        final String inputDirPath = args[0];
        // java出力ディレクトリ
        final String javaOutputDirPath = args[1];
        // properties出力ディレクトリ
        final String propsOutputDirPath = args[2];
        // DD propertiesファイル名
        final String ddOutputFileName = args[3];

        // --------------------------------------------------
        // 本処理
        // --------------------------------------------------
        final int errorCount = generate(inputDirPath, javaOutputDirPath, propsOutputDirPath, ddOutputFileName);

        // --------------------------------------------------
        // 事後処理
        // --------------------------------------------------
        int exitStatus = 0;
        if (errorCount == 0) {
            log.info("process end.");
        } else {
            log.error("process end with error.");
            log.error("error count:" + errorCount);
            exitStatus = -1;
        }
        System.exit(exitStatus);
    }

    /**
     * 区分定義JSONファイルの配置ディレクトリを指定して、区分クラスとDDプロパティを出力します。
     *
     * @param inputDirPath 区分定義JSONファイルの配置ディレクトリ
     * @param javaOutputDirPath java出力ディレクトリ
     * @param propsOutputDirPath properties出力ディレクトリ
     * @param ddOutputFileName DDファイル名
     * @return 発生エラー数
     */
    public static int generate(
        final String inputDirPath,
        final String javaOutputDirPath,
        final String propsOutputDirPath,
        final String ddOutputFileName) {
        // 対象ディレクトリの存在確認
        FileUtils.readDirCheck(inputDirPath);
        // java出力ディレクトリの存在確認
        FileUtils.writeCheck(javaOutputDirPath + '/' + ddOutputFileName, CHARSET);
        // 出力準備
        FileUtils.setupOverwrite(javaOutputDirPath + '/' + ddOutputFileName);

        // properties出力ディレクトリの存在確認
        FileUtils.writeCheck(propsOutputDirPath + '/' + ddOutputFileName, CHARSET);
        // 出力準備
        FileUtils.setupOverwrite(propsOutputDirPath + '/' + ddOutputFileName);

        // --------------------------------------------------
        // 本処理
        // --------------------------------------------------
        log.info("  ・settings");
        log.info("    ・inputDir           :" + inputDirPath);
        log.info("    ・javaOutputDir      :" + javaOutputDirPath);
        log.info("    ・propsOutputDirPath :" + propsOutputDirPath);
        log.info("    ・ddFileName         :" + ddOutputFileName);

        log.info("  ・read template");
        // テンプレートファイルの読み込み
        final Properties prop = new Properties();
        prop.setProperty("resource.loader", "file, class");
        prop.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);
        Template templateClassification;
        Template templateDdSource;
        try {
            templateClassification = Velocity.getTemplate("/classification.vm", CHARSET);
            templateDdSource = Velocity.getTemplate("/dd_source.vm", CHARSET);
        } catch (Exception e) {
            // テンプレートファイルが読み込めない場合、エラー
            throw new UtilException(UtilMessageConst.FILE_CANTREAD, new Object[] {
                "/classification.vm"
            }, e);
        }

        log.info("  ・generate classification");
        // 入力ディレクトリのファイルを全件ループ
        final File inputDir = new File(inputDirPath);
        final List<ClassificationConfig> configList = new ArrayList<ClassificationConfig>();
        int errorCount = 0;
        final File[] inputFiles = inputDir.listFiles();
        if (inputFiles != null) {
            for (final File inputFile : inputFiles) {
                log.info("    ・" + inputFile.getName());

                // 入力ファイルのparse
                log.info("      ・parse");
                ClassificationConfig config;
                try {
                    config = JsonUtils.parseFile(inputFile.getPath(), CHARSET, ClassificationConfig.class);
                    configList.add(config);
                } catch (Exception e) {
                    log.error(MessageSource.getInstance().getMessage(UtilMessageConst.ERRORHANDLE, new Object[] {
                        "parse",
                        "inputFile=" + inputFile + ", charset=" + CHARSET + ", class=" + ClassificationConfig.class.getSimpleName()
                    }), e);
                    errorCount++;
                    continue;
                }

                // ------------------------------
                // 区分Java
                // ------------------------------
                log.info("      ・replace classification");
                try {
                    writeClassification(config, templateClassification, inputFile, javaOutputDirPath);
                } catch (Exception e) {
                    log.error(MessageSource.getInstance().getMessage(UtilMessageConst.ERRORHANDLE, new Object[] {
                        "replace classification",
                        "config=" + config + ", template=" + templateClassification.getName() + ", inputFile=" + inputFile + ", outputDir=" + javaOutputDirPath
                    }), e);
                    errorCount++;
                    continue;
                }
            }
        }

        // ------------------------------
        // DDソース
        // ------------------------------
        log.info("  ・generate dd_source");

        // エラーチェック
        if (errorCount == 0) {
            // エラーが発生していない場合、DDソーステンプレート置換
            log.info("    ・replace dd_source");
            final StringWriter sw = new StringWriter();
            try {
                final VelocityContext context = new VelocityContext();
                context.put("classificationList", configList);
                templateDdSource.merge(context, sw);
            } catch (Exception e) {
                log.error(MessageSource.getInstance().getMessage(UtilMessageConst.ERRORHANDLE, new Object[] {
                    "replace dd_source",
                    "template=" + templateDdSource.getName() + ", context.classificationList=" + configList
                }), e);
                errorCount++;
            }

            // DDソースファイル出力
            log.info("    ・output dd_source");
            final String outputFilePath = propsOutputDirPath + SystemUtils.FILE_SEPARATOR + ddOutputFileName;
            log.info("        ・outputFilePath: " + outputFilePath);

            final List<String> writeContentList = new ArrayList<String>();
            writeContentList.clear();
            writeContentList.add(sw.toString());
            sw.flush();

            final Path pathOutput = FileSystems.getDefault().getPath(outputFilePath);
            try {
                Files.write(pathOutput, writeContentList, Charset.forName(CHARSET), StandardOpenOption.CREATE);
            } catch (Exception e) {
                log.error(MessageSource.getInstance().getMessage(UtilMessageConst.ERRORHANDLE, new Object[] {
                    "output dd_source",
                    "outputFile=" + outputFilePath
                }), e);
                errorCount++;
            }

        } else {
            // エラーが発生している場合、スキップ
            log.info("    ・skipped");
        }

        return errorCount;
    }

    /**
     * 区分クラスのJavaファイルを出力します。
     *
     * @param config 区分設定
     * @param templateClassification 区分Java向けのVelocityテンプレート
     * @param inputFile 入力ファイル
     * @param outputDirPath 出力ディレクトリ
     */
    private static void writeClassification(
        final ClassificationConfig config,
        final Template templateClassification,
        final File inputFile,
        final String outputDirPath
    ) {

        // 区分Javaテンプレート置換
        final StringWriter sw = new StringWriter();
        try {
            final VelocityContext context = new VelocityContext();
            context.put("classification", config);
            templateClassification.merge(context, sw);
        } catch (Exception e) {
            throw new UtilException(UtilMessageConst.ERRORHANDLE, new Object[] {
                ClassificationGenerator.class.getName() + ".GenerateClass",
                "template=" + templateClassification.getName() + ", context.classification=" + config
            }, e);
        }

        // 区分Javaファイル出力
        log.info("      ・output classification");
        final String outputFileName = inputFile.getName().replace("json", "java");
        final String packageDirRelPath = config.getPackageName().replaceAll("\\.", SystemUtils.FILE_SEPARATOR);
        final String outputFilePath = outputDirPath + SystemUtils.FILE_SEPARATOR + packageDirRelPath + SystemUtils.FILE_SEPARATOR + outputFileName;
        log.info("        ・outputFilePath: " + outputFilePath);
        FileUtils.setupOverwrite(outputFilePath);

        final List<String> writeContentList = new ArrayList<String>();
        writeContentList.add(sw.toString());
        sw.flush();

        final Path pathOutput = FileSystems.getDefault().getPath(outputFilePath);
        try {
            Files.write(pathOutput, writeContentList, Charset.forName(CHARSET), StandardOpenOption.CREATE);
        } catch (Exception e) {
            throw new UtilException(UtilMessageConst.FILE_CANTWRITE, new Object[] {
                pathOutput, e.getMessage()
            }, e);
        }
    }
}
