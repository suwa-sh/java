package me.suwash.util;

import org.apache.commons.lang3.text.StrLookup;
import org.apache.commons.lang3.text.StrSubstitutor;

/**
 * 文字列置換関連ユーティリティ。
 */
public final class StringReplaceUtils {

    /**
     * ユーティリティ向けに、コンストラクタはprivate宣言。
     */
    private StringReplaceUtils() {}

    /**
     * 変数を含む文字列を、変数オブジェクトツリーのキーで置換した結果を返します。
     *
     * <pre>
     *  source:
     *    ${depth1.path}/${depth1.depth2.name}
     *  varTree:
     *    {
     *        "depth1" : {
     *            "path" : "/path/to/dest",
     *            "depth2" : {
     *                "name" : "FILENAME.txt"
     *            }
     *        }
     *    }
     *  output:
     *    /path/to/dest/FILENAME.txt
     * </pre>
     *
     * @param source 変数を含む文字列
     * @param varTree 変数オブジェクトツリー
     * @return 置換後の文字列
     */
    public static String replace(final String source, final Object varTree) {
        final StrLookup<Object> lookup = new ObjectTreeLookup(varTree);
        final StrSubstitutor subst = new StrSubstitutor(lookup);
        try {
            return subst.replace(source);
        } catch (Exception e) {
            return source;
        }
    }

    /**
     * org.apache.commons.lang3.text.StrSubstitutor向けのLookup実装。
     * 変数の展開に、ObjectTreeUtilisを利用します。
     */
    static class ObjectTreeLookup extends StrLookup<Object> {

        /** Map keys are variable names and value. */
        private final transient Object varObject;

        /**
         * Creates a new instance backed by a Map.
         *
         * @param varObject the map of keys to values, may be null
         */
        ObjectTreeLookup(final Object varObject) {
            super();
            this.varObject = varObject;
        }

        /**
         * Looks up a String key to a String value using the map.
         * <p>
         * If the map is null, then null is returned.
         * The map result object is converted to a string using toString().
         * </p>
         *
         * @param key  the key to be looked up, may be null
         * @return the matching value, null if no match
         */
        @Override
        public String lookup(final String key) {
            final Object result = ObjectTreeUtils.getSubTree(varObject, key);
            return result.toString();
        }
    }

}
