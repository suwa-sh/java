package me.suwash.util.gen.classification;

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

import me.suwash.util.exception.UtilException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 区分クラスジェネレータ。
 */
public class ClassificationGenerator {

    /**
     * 区分定義JSONファイルの配置ディレクトリを指定して、区分クラスとDDプロパティを出力します。
     *
     * @param args 1:入力ディレクトリ 2:出力ディレクトリ 3:DDプロパティファイル名
     */
    public static void main(String[] args) {
        // --------------------------------------------------
        //  事前処理
        // --------------------------------------------------
        // TODO Sysoutはロガーに切り替える！
        System.out.println("process start.");

        // 引数の数
        if (args.length != 3) {
            // 存在しない場合、エラー
            throw new UtilException("check.countSame", new Object[] {"arg", 3, args.length});
        }

        // 対象ディレクトリの存在確認
        String inputDirPath = args[0];
        File inputDir = new File(inputDirPath);
        if (! inputDir.isDirectory()) {
            // 存在しない場合、エラー
            throw new UtilException("check.cantRead", new Object[] {inputDirPath});
        }

        // 出力ディレクトリの存在確認
        String outputDirPath = args[1];
        File outputDir = new File(outputDirPath);
        if (! outputDir.isDirectory()) {
            // 存在しない場合、ディレクトリを作成
            outputDir.mkdirs();
        }

        // DDソースファイル名
        String ddOutputFileName = args[2];
        if (StringUtils.isEmpty(ddOutputFileName)) {
            // 存在しない場合、エラー
            throw new UtilException("check.notNull", new Object[] {ddOutputFileName});
        }


        // --------------------------------------------------
        //  本処理
        // --------------------------------------------------
        System.out.println("  ・" + "settings");
        System.out.println("    ・" + "inputDir:" + inputDirPath);
        System.out.println("    ・" + "outputDir:" + outputDirPath);

        System.out.println("  ・" + "read template");
        // テンプレートファイルの読み込み
        Properties p = new Properties();
        p.setProperty("resource.loader", "file, class");
        p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init( p );
        Template templateClassification;
        Template templateDdSource;
        try {
            templateClassification = Velocity.getTemplate("/classification.vm", "UTF-8");
            templateDdSource = Velocity.getTemplate("/dd_source.vm", "UTF-8");
        } catch (Exception e) {
            // テンプレートファイルが読み込めない場合、エラー
            throw new UtilException("check.cantRead", new Object[] {"/classification.vm"}, e);
        }

        System.out.println("  ・" + "generate classification");
        // 入力ディレクトリのファイルを全件ループ
        ObjectMapper mapper = new ObjectMapper();
        List<ClassificationConfig> configList = new ArrayList<ClassificationConfig>();
        int errorCount = 0;
        for (File inputFile : inputDir.listFiles()) {
            System.out.println("    ・" + inputFile.getName());

            // 入力ファイルのparse
            System.out.println("      ・" + "parse");
            ClassificationConfig config;
            try {
                config = mapper.readValue(inputFile, ClassificationConfig.class);
                configList.add(config);
            } catch (Exception e) {
                e.printStackTrace();
                errorCount++;
                continue;
            }

            // ------------------------------
            //  区分Java
            // ------------------------------
            // 区分Javaテンプレート置換
            System.out.println("      ・" + "replace classification");
            StringWriter sw = new StringWriter();
            try {
                VelocityContext context = new VelocityContext();
                context.put("classification",config);
                templateClassification.merge(context, sw);
            } catch (Exception e) {
                e.printStackTrace();
                errorCount++;
                continue;
            }

            // 区分Javaファイル出力
            System.out.println("      ・" + "output classification");
            String outputFileName = inputFile.getName().replace("json", "java");
            String outputFilePath = outputDirPath + SystemUtils.FILE_SEPARATOR + outputFileName;
            Path pathOutput = FileSystems.getDefault().getPath(outputFilePath);

            List<String> writeContentList = new ArrayList<String>();
            writeContentList.add(sw.toString());
            sw.flush();

            try {
                Files.write(pathOutput, writeContentList, Charset.forName("UTF-8"), StandardOpenOption.CREATE);
            } catch (Exception e) {
                e.printStackTrace();
                errorCount++;
                continue;
            }
        }

        // ------------------------------
        //  DDソース
        // ------------------------------
        System.out.println("  ・" + "generate dd_source");

        // DDソーステンプレート置換
        System.out.println("    ・" + "replace dd_source");
        StringWriter sw = new StringWriter();
        try {
            VelocityContext context = new VelocityContext();
            context.put("classificationList",configList);
            templateDdSource.merge(context, sw);
        } catch (Exception e) {
            e.printStackTrace();
            errorCount++;
        }

        // DDソースファイル出力
        System.out.println("    ・" + "output dd_source");
        String outputFilePath = outputDirPath + SystemUtils.FILE_SEPARATOR + ddOutputFileName;
        Path pathOutput = FileSystems.getDefault().getPath(outputFilePath);

        List<String> writeContentList = new ArrayList<String>();
        writeContentList.clear();
        writeContentList.add(sw.toString());
        sw.flush();

        try {
            Files.write(pathOutput, writeContentList, Charset.forName("UTF-8"), StandardOpenOption.CREATE);
        } catch (Exception e) {
            e.printStackTrace();
            errorCount++;
        }

        // --------------------------------------------------
        //  事後処理
        // --------------------------------------------------
        int exitStatus = 0;
        if (errorCount == 0) {
            System.out.println("process end.");
        } else {
            System.out.println("process end with error.");
            System.out.println("error count:" + errorCount);
            exitStatus = -1;
        }
        System.exit(exitStatus);
    }
}
