package me.suwash.util;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import me.suwash.util.FindUtils.FileType;

public class TestUtils {
    public static void assertDirEquals(String expectDir, String actualDir) {
        //--------------------------------------------------
        // ディレクトリ存在チェック
        //--------------------------------------------------
        File expect = new File(expectDir);
        File actual = new File(actualDir);

        List<File> expectDirs = FindUtils.find(expectDir, FileType.Directory);
        List<File> actualDirs = FindUtils.find(actualDir, FileType.Directory);

        List<File> expectOnlyDirList = new ArrayList<File>();
        List<File> actualOnlyDirList = new ArrayList<File>();
        List<String> existRelPathList = new ArrayList<String>();

        // 期待値→実績値チェック
        for (File curExpectDir : expectDirs) {
            if (isExistSameRelPath(curExpectDir, expect, actual)) {
                existRelPathList.add(getRelPath(curExpectDir, expect));
            } else {
                expectOnlyDirList.add(curExpectDir);
            }
        }

        // 実績値→期待値チェック
        for (File curActualDir : actualDirs) {
            if (isExistSameRelPath(curActualDir, actual, expect)) {
                existRelPathList.add(getRelPath(curActualDir, actual));
            } else {
                actualOnlyDirList.add(curActualDir);
            }
        }

        // ディレクトリ存在チェック
        boolean isSame = true;
        if (expectOnlyDirList.size() != 0) {
            isSame = false;
            for (File curDir : expectOnlyDirList) {
                System.err.println("[Diff]期待値のみに存在 - " + curDir);
            }
        }
        if (actualOnlyDirList.size() != 0) {
            isSame = false;
            for (File curDir : actualOnlyDirList) {
                System.err.println("[Dirr]実績値のみに存在 - " + curDir);
            }
        }
        if (! isSame) {
            assertTrue("ディレクトリ構成が一致していること", isSame);
        }

        //--------------------------------------------------
        // ファイル比較
        //--------------------------------------------------
        for (String curRelPath : existRelPathList) {
            assertFilesEquals(expectDir + SEP + curRelPath, actualDir + SEP + curRelPath, "utf8");
        }

    }

    private static final String SEP = "/";

    private static boolean isExistSameRelPath(File baseFile, File removeDir, File targetBaseDir) {
        String baseRelPath = getRelPath(baseFile, removeDir);
        String targetBasePath = targetBaseDir.getAbsolutePath().replace(File.separator, SEP);
        String targetPath = targetBasePath + SEP + baseRelPath;
        File target = new File(targetPath);

        return target.exists();
    }

    private static String getRelPath(File baseFile, File removeDir) {
        String basePath = baseFile.getAbsolutePath().replace(File.separator, SEP);
        String removePath = removeDir.getAbsolutePath().replace(File.separator, SEP);
        String baseRelPath = basePath.replace(removePath + SEP, StringUtils.EMPTY);

        return baseRelPath;
    }

    /**
     * 指定された期待値ディレクトリ、実績値ディレクトリに配置されたファイル群が完全一致することを確認します。
     *
     * @param expectDir 期待値ディレクトリ
     * @param actualDir 実績値ディレクトリ
     */
    public static void assertFilesEquals(String expectDir, String actualDir, String charset) {
        File expect = new File(expectDir);
        assertTrue("期待値ディレクトリが存在しません。ディレクトリ：" + expectDir, expect.exists());
        File actual = new File(actualDir);
        assertTrue("実績値ディレクトリが存在しません。ディレクトリ：" + actualDir, actual.exists());

        // 期待値ファイル配列
        File[] expectFiles = expect.listFiles();
        // 実績値ファイル配列
        File[] actualFiles = actual.listFiles();
        // 比較済のファイル名リスト
        List<String> comparedFileNameList = new ArrayList<String>();

        // 比較結果サマリー
        boolean isSame = true;
        // 期待値ファイル件数
        int expectFileCount = 0;
        // 実績値ファイル件数
        int actualFileCount = 0;
        // 相違件数
        int diffFileCount = 0;

        // 期待値ファイルを全件ループ
        for (int i = 0; i < expectFiles.length; i++) {

            // 期待値ファイルの確認
            File curExpectFile = expectFiles[i];
            if (curExpectFile.isDirectory() || curExpectFile.isHidden()) {
                // ディレクトリ、隠しファイルの場合、スキップ
                continue;
            }

            // --------------------------------------------------
            //  期待値のみに存在するファイルの確認
            // --------------------------------------------------
            File curActualFile = new File(actualDir + File.separator + curExpectFile.getName());
            if (! curActualFile.exists()) {
                System.err.println("[Diff]期待値のみに存在 - " + curExpectFile);
                isSame = false;
                diffFileCount++;
                continue;
            }

            // --------------------------------------------------
            //  ファイル内容の確認
            // --------------------------------------------------
            // 内容比較
            if (! isSameFile(curExpectFile, curActualFile, charset)) {
                // 不一致の場合
                isSame = false;
                diffFileCount++;
                System.err.println("[Diff]内容相違 - " + curActualFile);
            }

            // 期待値件数のインクリメント
            expectFileCount++;

            // 比較済ファイル名リストに追加
            comparedFileNameList.add(curExpectFile.getName());
        }

        // --------------------------------------------------
        //  実績値のみに存在するファイルの確認
        // --------------------------------------------------
        for (File curActualFile : actualFiles) {
            if (curActualFile.isDirectory() || curActualFile.isHidden()) {
                // ディレクトリ、隠しファイルの場合、スキップ
                continue;
            }

            // 比較していないことの確認
            if (! comparedFileNameList.contains(curActualFile.getName())) {
                // 比較していない場合
                isSame = false;
                diffFileCount++;
                System.err.println("[Diff]実績値のみに存在 - " + curActualFile);
            }

            // 実績値件数のインクリメント
            actualFileCount++;
        }

        // --------------------------------------------------
        //  結果確認
        // --------------------------------------------------
        // ファイル数の確認
        assertEquals("出力ファイル数が一致していること", expectFileCount, actualFileCount);

        // ファイル比較結果
        assertTrue("全ファイルの比較結果が一致していること", isSame);
        assertEquals("全ファイルの比較結果が一致していること", 0, diffFileCount);
    }

    public static void assertFileEquals(String expectFilePath, String actualFilePath, String charset) {
        // 期待値ファイルの存在確認
        File expectFile = new File(expectFilePath);
        if (! expectFile.exists()) {
            fail("期待値ファイルが存在しません。期待値ファイル：" + expectFile);
        }

        // 実績値ファイルの存在確認
        File actualFile = new File(actualFilePath);
        if (! actualFile.exists()) {
            fail("実績値ファイルが存在しません。実績値ファイル：" + actualFile);
        }

        // 内容比較
        if (! isSameFile(expectFile, actualFile, charset)) {
            fail("ファイルの内容が一致しません。期待値ファイル：" + expectFile + ", 実績値ファイル：" + actualFile);
        }
    }

    private static boolean isSameFile(File expectFile, File actualFile, String charset) {
        // 期待値ファイルの読み込み
        String expectFileContent = StringUtils.EMPTY;
        try {
            expectFileContent = FileUtils.readFileToString(expectFile, charset);
        } catch (Exception e) {
            throw new RuntimeException("期待値ファイル：" + expectFile.getName() + " の読み込みに失敗しました。", e);
        }

        // 実績値ファイルの読み込み
        String actualFileContent = StringUtils.EMPTY;
        try {
            actualFileContent = FileUtils.readFileToString(actualFile, charset);
        } catch (Exception e) {
            throw new RuntimeException("実績値ファイル：" + actualFile.getName() + " の読み込みに失敗しました。", e);
        }

        // 内容比較
        return  actualFileContent.equals(expectFileContent);
    }

}
