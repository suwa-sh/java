package me.suwash.util;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import me.suwash.util.constant.UtilMessageConst;
import me.suwash.util.exception.UtilException;
import me.suwash.util.i18n.MessageSource;

import org.apache.commons.lang3.StringUtils;

/**
 * データツリー関連ユーティリティ。
 */
@lombok.extern.slf4j.Slf4j
public final class ObjectTreeUtils {

    private static final char PATH_SEPARATOR = '.';

    /**
     * ユーティリティ向けに、コンストラクタはprivate宣言。
     */
    private ObjectTreeUtils() {}

    /**
     * オブジェクトツリーから、パスにマッチするサブデータツリーを返します。
     *
     * <pre>
     * 例
     * ・取得元オブジェクトツリー
     *   MapA
     *     "KeyA1" : List
     *       - MapB
     *         "KeyB1" : "ValueB1",
     *         "KeyB2" : Map
     *           "KeyB2-1" : "ValueB2-1",
     *           "KeyB2-2" : "ValueB2-2",
     *           "KeyB2-3" : "ValueB2-3",
     *      - MapC
     *         "KeyC1" : "ValueC1"
     *     "KeyA2" : "ValueA2"
     *
     * ・取得対象パス
     *   "KeyA1.[0].KeyB2"
     *
     * ・戻り値
     *   Map
     *     "KeyB2-1" : "ValueB2-1",
     *     "KeyB2-2" : "ValueB2-2",
     *     "KeyB2-3" : "ValueB2-3"
     * </pre>
     *
     * @param target 取得元オブジェクトツリー
     * @param paraPath 取得対象パス
     * @return サブデータツリー
     */
    public static Object getSubTree(final Object target, final String paraPath) {
        if (target == null) {
            throw new UtilException(UtilMessageConst.CHECK_NOTNULL, new Object[] {
                "target"
            });
        }
        if (StringUtils.isEmpty(paraPath)) {
            throw new UtilException(UtilMessageConst.CHECK_NOTNULL, new Object[] {
                "path"
            });
        }

        String path = paraPath;
        final char firstChar = paraPath.charAt(0);
        if (firstChar == PATH_SEPARATOR) {
            // . で始まる場合は、除去
            path = paraPath.substring(1);
        }
        // 変数として括られている場合は、除去
        path = path.replace("${", StringUtils.EMPTY).replace("}", StringUtils.EMPTY);

        try {
            return getSubTreeMain(target, path);
        } catch (Exception e) {
            log.debug(MessageSource.getInstance().getMessage(UtilMessageConst.ERRORHANDLE, new Object[] {
                ObjectTreeUtils.class.getName() + ".getSubData",
                "path=" + path + ", target=" + target
            }), e);
            return null;
        }
    }

    /**
     * 再帰呼び出し用 サブデータツリー取得処理。
     *
     * @param target 取得元オブジェクトツリー
     * @param path 取得対象パス
     * @return サブデータツリー
     */
    @SuppressWarnings("rawtypes")
    private static Object getSubTreeMain(final Object target, final String path) {
        // パスを確認
        if (path.isEmpty()) {
            // --------------------------------------------------------------------------------
            // 空文字の場合
            // --------------------------------------------------------------------------------
            // 自オブジェクトを返却
            return target;

        } else if (path.contains(".")) {
            // --------------------------------------------------------------------------------
            // 末端に到達していない場合
            // --------------------------------------------------------------------------------
            // 現在パス
            final String curPath = path.substring(0, path.indexOf(PATH_SEPARATOR));
            // 子パス
            final String childPath = path.substring(path.indexOf(PATH_SEPARATOR) + 1);

            // 対象オブジェクトの型を確認
            if (target instanceof List) {
                // --------------------------------------------------
                // 対象オブジェクトがListの場合
                // --------------------------------------------------
                // 現在パスから [ ] を除去したものをインデックスとして、子オブジェクトを取得
                final Object curObj = getByList((List) target, curPath);
                // 再帰呼び出し
                return getSubTreeMain(curObj, childPath);

            } else if (target instanceof Map) {
                // --------------------------------------------------
                // 対象オブジェクトがMapの場合
                // --------------------------------------------------
                // 現在パスをキーとして、子オブジェクトを取得
                final Object curObj = getByMap((Map) target, curPath);
                // 再帰呼び出し
                return getSubTreeMain(curObj, childPath);

            } else {
                // --------------------------------------------------
                // 対象オブジェクトがその他の場合
                // --------------------------------------------------
                // 現在パスをプロパティ名として、子オブジェクトを取得
                final Object curObj = getByBean(target, curPath);
                // 再帰呼び出し
                return getSubTreeMain(curObj, childPath);
            }

        } else {
            // --------------------------------------------------------------------------------
            // 末端に到達している場合、対象オブジェクトを返却
            // --------------------------------------------------------------------------------
            // 対象オブジェクトの型を確認
            if (target instanceof List) {
                // --------------------------------------------------
                // 対象オブジェクトがListの場合
                // --------------------------------------------------
                // 現在パスから [ ] を除去したものをインデックスとして、取得したオブジェクトを返却
                return getByList((List) target, path);

            } else if (target instanceof Map) {
                // --------------------------------------------------
                // 対象オブジェクトがMapの場合
                // --------------------------------------------------
                // 現在パスをキーとして、取得したオブジェクトを返却
                return getByMap((Map) target, path);

            } else {
                // --------------------------------------------------
                // 対象オブジェクトがその他の場合
                // --------------------------------------------------
                return getByBean(target, path);
            }
        }
    }

    /**
     * Mapからvalueを取得します。
     *
     * @param target 対象Map
     * @param path キー
     * @return value
     */
    @SuppressWarnings("rawtypes")
    private static Object getByMap(final Map target, final String path) {
        if (!target.containsKey(path)) {
            throw new UtilException(UtilMessageConst.DATA_NOTFOUND, new Object[] {
                target, "Key", path
            });
        }
        return target.get(path);
    }

    /**
     * Listから要素を取得します。
     *
     * @param target 対象List
     * @param path インデックス文字列 ※[0]などのインデックス番号を示す文字列
     * @return 要素
     */
    @SuppressWarnings("rawtypes")
    private static Object getByList(final List target, final String path) {
        final String indexStr = path.replace("[", StringUtils.EMPTY).replace("]", StringUtils.EMPTY);
        final int index = Integer.parseInt(indexStr);
        final int targetLastIndex = target.size() - 1;
        if (index < 0 || targetLastIndex < index) {
            throw new UtilException(UtilMessageConst.DATA_NOTFOUND, new Object[] {
                target, "Index", path
            });
        }
        return target.get(index);
    }

    /**
     * Beanからフィールド値を取得します。
     *
     * @param target 対象Bean
     * @param path フィールド名
     * @return フィールド設定値
     */
    private static Object getByBean(final Object target, final String path) {
        // TODO Bean対応。
        throw new UtilException(UtilMessageConst.UNSUPPORTED_PATTERN, new Object[] {
            ObjectTreeUtils.class.getName() + ".getSubData",
            "target=" + target.getClass().getSimpleName() + ", path=" + path
        });
        // curPathにマッチするsetterの存在確認
            // setterが存在する場合、getterの存在確認
                // getterが存在する場合、getterで値を取得
                // getterが存在しない場合、エラー
            // setterが存在しない場合、エラー
    }

    /**
     * 指定パスに合わせて、空のオブジェクトツリーを返します。
     *
     * @param path 作成するツリー構造のパス
     * @return 作成したオブジェクトツリー
     */
    public static Map<String, Object> createTree(final String path) {
        if (StringUtils.isEmpty(path)) {
            return null;
        }

        final Map<String, Object> returnTree = new ConcurrentHashMap<String, Object>();
        Map<String, Object> curTree = returnTree;
        final String[] pathContents = path.split("\\.");
        for (final String curPathContent : pathContents) {
            // 現在Mapに次のMapを登録して、次のMapを現在と差し替える
            final Map<String, Object> nextTree = getNextTree(curTree, curPathContent);
            curTree = nextTree;
        }

        return returnTree;
    }

    /**
     * 現在Mapに次のMapを登録します。
     *
     * @param curTree 現在Map
     * @param curPathContent 現在パス
     * @return 次のMap
     */
    private static Map<String, Object> getNextTree(final Map<String, Object> curTree, final String curPathContent) {
        final Map<String, Object> nextTree = new ConcurrentHashMap<String, Object>();
        curTree.put(curPathContent, nextTree);
        return nextTree;
    }

    /**
     * オブジェクトツリーの指定パスに、サブツリーを登録します。
     *
     * @param target 登録先オブジェクトツリー
     * @param path 登録先パス
     * @param addData 登録するサブツリー
     */
    @SuppressWarnings("unchecked")
    public static void setSubTree(final Map<String, Object> target, final String path, final Object addData) {
        if (path.contains(".")) {
            // 階層指定されている場合、指定の親を取得してput
            final String parentPath = path.substring(0, path.lastIndexOf(PATH_SEPARATOR));
            final String addKey = path.substring(path.lastIndexOf(PATH_SEPARATOR) + 1);

            final Object parentObj = getSubTree(target, parentPath);
            if (parentObj instanceof Map) {
                // Mapの場合、put
                final Map<String, Object> parentMap = (Map<String, Object>) getSubTree(target, parentPath);
                parentMap.put(addKey, addData);

            } else {
                // TODO Bean対応
                // Map以外の型の場合、エラー
                throw new UtilException(UtilMessageConst.UNSUPPORTED_PATTERN, new Object[] {"setSubData.target", parentObj.getClass().getSimpleName()});
            }

        } else {
            // 階層指定されていない場合、対象に直接put
            target.put(path, addData);
        }
    }

    /**
     * 2つのオブジェクトツリーをマージします。
     * キーが一致する場合、マージ元オブジェクトツリーの内容で上書きします。
     *
     * @param baseTree マージ先オブジェクトツリー
     * @param addTree マージ元オブジェクトツリー
     */
    @SuppressWarnings("unchecked")
    public static void mergeTree(final Map<String, Object> baseTree, final Map<String, ?> addTree) {
        if (baseTree == null || addTree == null) {
            return;
        }

        // addTreeのキーでループ
        for (final Map.Entry<String, ?> curAddEntry : addTree.entrySet()) {
            // baseTreeに含まれているか確認
            final String curKey = curAddEntry.getKey();
            if (baseTree.containsKey(curKey)) {
                // baseTreeに含まれている場合、型を確認
                final Object curBaseValue = baseTree.get(curKey);
                if (curBaseValue instanceof Map) {
                    // addTree側のValueも型を確認
                    final Object curAddValue = curAddEntry.getValue();
                    if (curAddValue instanceof Map) {
                        // base、addどちらもMapの場合、再帰呼び出しで子を追加
                        mergeTree((Map<String, Object>) curBaseValue, (Map<String, Object>) curAddValue);
                    } else {
                        // その他の場合、エラー
                        throw new UtilException(UtilMessageConst.CHECK_NOTSAME, new Object[] {
                            "curBaseValueType:" + curBaseValue.getClass().getSimpleName(), "curAddValueType:" + curAddValue.getClass().getSimpleName()
                        });
                    }

                } else {
                    // Map以外の場合、上書き
                    baseTree.put(curKey, curAddEntry.getValue());

                }

            } else {
                // baseTreeに含まれていない場合、追加
                baseTree.put(curKey, curAddEntry.getValue());
            }
        }
    }

}
