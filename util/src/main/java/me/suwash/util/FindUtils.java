package me.suwash.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import me.suwash.util.constant.UtilMessageConst;
import me.suwash.util.exception.UtilException;

import org.apache.commons.lang3.StringUtils;

/**
 * ファイル検索関連ユーティリティ。
 */
public final class FindUtils {

    /**
     * ユーティリティ向けに、コンストラクタはprivate宣言。
     */
    private FindUtils() {}

    /**
     * 検索対象ファイルタイプ。
     */
    public enum FileType {
        /** ディレクトリ。 */
        Directory,
        /** ファイル。 */
        File,
        /** 全て。 */
        All
    }

    /**
     * ファイル検索。
     *
     * @param dirPath 対象ディレクトリ
     * @return 再帰的に検索したファイルリスト
     */
    public static List<File> find(final String dirPath) {
        return findMain(dirPath, null, null, null, null);
    }

    /**
     * ファイル検索。
     * 最小深度指定
     *
     * @param dirPath 対象ディレクトリ
     * @param minDepth 最小深度
     * @return 再帰的に検索したファイルリスト
     */
    public static List<File> find(final String dirPath, final int minDepth) {
        return findMain(dirPath, minDepth, null, null, null);
    }

    /**
     * ファイル検索。
     * 最小、最大深度指定
     *
     * @param dirPath 対象ディレクトリ
     * @param minDepth 最小深度
     * @param maxDepth 最大深度
     * @return 再帰的に検索したファイルリスト
     */
    public static List<File> find(final String dirPath, final int minDepth, final int maxDepth) {
        return findMain(dirPath, minDepth, maxDepth, null, null);
    }

    /**
     * ファイル検索。
     * 最小、最大深度、ファイルタイプ指定
     *
     * @param dirPath 対象ディレクトリ
     * @param minDepth 最小深度
     * @param maxDepth 最大深度
     * @param fileType 対象ファイルタイプ
     * @return 再帰的に検索したファイルリスト
     */
    public static List<File> find(final String dirPath, final int minDepth, final int maxDepth, final FileType fileType) {
        return findMain(dirPath, minDepth, maxDepth, fileType, null);
    }

    /**
     * ファイル検索。
     * 最小、最大深度、ファイルタイプ、ファイル名パターン指定
     *
     * @param dirPath 対象ディレクトリ
     * @param minDepth 最小深度
     * @param maxDepth 最大深度
     * @param fileType 対象ファイルタイプ
     * @param namePattern ファイル名パターン
     * @return 再帰的に検索したファイルリスト
     */
    public static List<File> find(final String dirPath, final int minDepth, final int maxDepth, final FileType fileType, final String namePattern) {
        return findMain(dirPath, minDepth, maxDepth, fileType, namePattern);
    }

    /**
     * ファイル検索。
     * ファイルタイプ指定
     *
     * @param dirPath 対象ディレクトリ
     * @param fileType 対象ファイルタイプ
     * @return 再帰的に検索したファイルリスト
     */
    public static List<File> find(final String dirPath, final FileType fileType) {
        return findMain(dirPath, null, null, fileType, null);
    }

    /**
     * ファイル検索。
     * ファイルタイプ、ファイル名パターン指定
     *
     * @param dirPath 対象ディレクトリ
     * @param fileType 対象ファイルタイプ
     * @param namePattern ファイル名パターン
     * @return 再帰的に検索したファイルリスト
     */
    public static List<File> find(final String dirPath, final FileType fileType, final String namePattern) {
        return findMain(dirPath, null, null, fileType, namePattern);
    }

    /**
     * ファイル検索。
     * ファイル名パターン指定
     *
     * @param dirPath 対象ディレクトリ
     * @param namePattern ファイル名パターン
     * @return 再帰的に検索したファイルリスト
     */
    public static List<File> find(final String dirPath, final String namePattern) {
        return findMain(dirPath, null, null, null, namePattern);
    }

    /**
     * ファイル検索 主処理。
     *
     * @param dirPath 対象ディレクトリ
     * @param paraMinDepth 最小深度
     * @param paraMaxDepth 最大深度
     * @param paraFileType 対象ファイルタイプ
     * @param paraNamePattern ファイル名パターン
     * @return 再帰的に検索したファイルリスト
     */
    private static List<File> findMain(
        final String dirPath,
        final Integer paraMinDepth,
        final Integer paraMaxDepth,
        final FileType paraFileType,
        final String paraNamePattern
    ) {

        // --------------------------------------------------
        // 事前処理
        // --------------------------------------------------
        // 対象ディレクトリ
        final File targetDir = new File(dirPath);
        if (! targetDir.exists()) {
            throw new UtilException(UtilMessageConst.CHECK_NOTEXIST, new Object[] {targetDir});
        }
        if (! targetDir.isDirectory()) {
            throw new UtilException(UtilMessageConst.DIR_CHECK, new Object[] {targetDir});
        }
        // 最小深度
        int minDepth = 1;
        if (paraMinDepth != null) {
            minDepth = paraMinDepth;
        }
        // 最大深度
        int maxDepth = Integer.MAX_VALUE;
        if (paraMaxDepth != null) {
            maxDepth = paraMaxDepth;
        }
        // 対象ファイルタイプ
        FileType fileType = FileType.All;
        if (paraFileType != null) {
            fileType = paraFileType;
        }
        // ファイル名パターン
        String namePattern = ".*";
        if (! StringUtils.isEmpty(paraNamePattern)) {
            namePattern = paraNamePattern;
        }

        // --------------------------------------------------
        // 本処理
        // --------------------------------------------------
        // 走査結果 ※ソート向けにTreeSetを利用
        final Set<File> finded = new TreeSet<File>();

        // 再帰的にファイル走査
        recursiveFind(targetDir, minDepth, maxDepth, fileType, namePattern, finded, 0);

        // --------------------------------------------------
        // 事後処理
        // --------------------------------------------------
        // 操作結果をListに変換
        final List<File> returnList = new ArrayList<File>();
        returnList.addAll(finded);
        return returnList;
    }

    /**
     * 再帰呼び出し用 ファイル検索処理。
     *
     * @param targetDir 対象ディレクトリ
     * @param minDepth 最小深度
     * @param maxDepth 最大深度
     * @param fileType 対象ファイルタイプ
     * @param namePattern ファイル名パターン
     * @param finded 走査結果Set
     * @param beforeDepth 呼び出し元深度
     */
    private static void recursiveFind(
        final File targetDir,
        final int minDepth,
        final int maxDepth,
        final FileType fileType,
        final String namePattern,
        final Set<File> finded,
        final int beforeDepth
    ) {

        // 現在の走査深度を確認
        final int curDepth = beforeDepth + 1;
        boolean isInAddDepthRange = false;
        if (minDepth <= curDepth && curDepth <= maxDepth) {
            isInAddDepthRange = true;
        } else if (maxDepth < curDepth) {
            // maxDepthより大きい場合は、ここで終了
            return;
        }

        // ディレクトリ内のファイルをループ
        final File[] children = targetDir.listFiles();
        if (children == null) {
            // nullが返却された場合は、ここで終了 ※ありえないはず。
            return;
        }

        for (final File curChild : children) {

            // 走査対象深度の確認
            if (isInAddDepthRange) {

                // 走査対象の深度の場合、ファイルタイプを確認
                if (curChild.isFile()) {
                    // ファイルの場合、対象ファイルタイプが「全て」または「ファイル」
                    // かつ、ファイル名パターンにマッチする場合のみ
                    if ((FileType.All.equals(fileType) || FileType.File.equals(fileType))
                        && curChild.getAbsolutePath().matches(namePattern)) {
                        finded.add(curChild);
                    }

                } else {
                    // ディレクトリの場合、対象ファイルタイプが「全て」または「ディレクトリ」
                    // かつ、ファイル名パターンにマッチする場合のみ
                    if (FileType.All.equals(fileType) || FileType.Directory.equals(fileType)
                        && curChild.getAbsolutePath().matches(namePattern)) {
                        finded.add(curChild);
                    }
                }
            }

            // ディレクトリの場合のみ再帰呼び出し
            if (curChild.isDirectory()) {
                recursiveFind(curChild, minDepth, maxDepth, fileType, namePattern, finded, curDepth);
            }

        }
    }

}
