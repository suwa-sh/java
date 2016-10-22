package me.suwash.gen.classification;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import me.suwash.util.classification.Classification;

/**
 * 処理ステータス2。
 */
public enum ProcessStatus2 implements Classification {
    /** 処理中。 */
    Processing("PRC"),
    /** 成功。 */
    Success("SCC"),
    /** 警告。 */
    Warning("WRN"),
    /** 失敗。 */
    Failure("FLR");

    /** グループ名配列。 */
    private static final String[] groups;
    /** 区分値グループMap。 */
    private static final Map<String, ProcessStatus2[]> groupValuesMap;
    /** グループ内デフォルト区分値Map。 */
    private static final Map<String, ProcessStatus2> groupDefaultMap;

    /** グループ：デフォルト。 */
    public static final String GROUP_DEFAULT = "default";
    /** グループ：終了ステータス。 */
    public static final String GROUP_FINISHED = "finished";

    /** データディクショナリID。 */
    private String ddId;
    /** 永続化値。 */
    private String storeValue;

    static {
        // グループMap
        groupValuesMap = new HashMap<String, ProcessStatus2[]>();
        groupValuesMap.put(GROUP_DEFAULT, new ProcessStatus2[]{
            Processing,
            Success,
            Warning,
            Failure
            });
        groupValuesMap.put(GROUP_FINISHED, new ProcessStatus2[]{
            Success,
            Warning,
            Failure
            });

        // グループ内デフォルト値Map
        groupDefaultMap = new HashMap<String, ProcessStatus2>();
        groupDefaultMap.put(GROUP_DEFAULT, Processing);
        groupDefaultMap.put(GROUP_FINISHED, Success);

        // グループ名配列
        groups = groupValuesMap.keySet().toArray(new String[0]);
    }

    /**
     * デフォルト区分値を返します。
     *
     * @return デフォルト区分値
     */
    public static ProcessStatus2 defaultValue() {
        return groupDefaultMap.get(GROUP_DEFAULT);
    }

    /**
     * グループ内のデフォルト区分値を返します。
     *
     * @param group グループ名
     * @return デフォルトの区分値
     */
    public static ProcessStatus2 defaultValue(final String group) {
        return groupDefaultMap.get(group);
    }

    /**
     * 区分が持つグループ群を返します。
     *
     * @return グループ名配列
     */
    public static String[] groups() {
        return Arrays.copyOf(groups, groups.length);
    }

    /**
     * 指定したグループ名に属する区分値を返します。
     *
     * @param group グループ名
     * @return 区分値配列
     */
    public static ProcessStatus2[] values(final String group) {
        return groupValuesMap.get(group);
    }

    /**
     * データディクショナリIDから区分値を返します。
     * 見つからない場合はnullを返します。
     *
     * @param ddId データディクショナリID
     * @return 区分値
     */
    public static ProcessStatus2 valueOfByDdId(final String ddId) {
        for (final ProcessStatus2 curEnum : ProcessStatus2.values()) {
            if (curEnum.ddId().equals(ddId)) {
                return curEnum;
            }
        }
        return null;
    }

    /**
     * 永続化値から区分値を返します。
     * 見つからない場合はnullを返します。
     *
     * @param storeValue 永続化値
     * @return 区分値
     */
    public static ProcessStatus2 valueOfByStoreValue(final String storeValue) {
        for (final ProcessStatus2 curEnum : ProcessStatus2.values()) {
            if (curEnum.storeValue().equals(storeValue)) {
                return curEnum;
            }
        }
        return null;
    }

    /**
     * 区分内に、指定した区分値名が存在するか確認します。
     *
     * @param name 区分値名
     * @return 存在する場合 true
     */
    public static boolean containsName(final String name) {
        for (final ProcessStatus2 curEnum : ProcessStatus2.values()) {
            if (curEnum.name().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 区分内の、指定したグループに、指定した区分値名が存在するか確認します。
     *
     * @param group グループ名
     * @param name 区分値名
     * @return 存在する場合 true
     */
    public static boolean containsName(final String group, final String name) {
        for (final ProcessStatus2 curEnum : ProcessStatus2.values(group)) {
            if (curEnum.name().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 区分内に、指定したデータディクショナリIDが存在するか確認します。
     *
     * @param ddId データディクショナリID
     * @return 存在する場合 true
     */
    public static boolean containsDdId(final String ddId) {
        for (final ProcessStatus2 curEnum : ProcessStatus2.values()) {
            if (curEnum.ddId().equals(ddId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 区分内の、指定したグループに、指定したデータディクショナリIDが存在するか確認します。
     *
     * @param group グループ名
     * @param ddId データディクショナリID
     * @return 存在する場合 true
     */
    public static boolean containsDdId(final String group, final String ddId) {
        for (final ProcessStatus2 curEnum : ProcessStatus2.values(group)) {
            if (curEnum.ddId().equals(ddId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 区分内に、指定した永続化値が存在するか確認します。
     *
     * @param storeValue 永続化値
     * @return 存在する場合 true
     */
    public static boolean containsStoreValue(final String storeValue) {
        for (final ProcessStatus2 curEnum : ProcessStatus2.values()) {
            if (curEnum.storeValue().equals(storeValue)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 区分内の、指定したグループに、指定した永続化値が存在するか確認します。
     *
     * @param group グループ名
     * @param storeValue 永続化値
     * @return 存在する場合 true
     */
    public static boolean containsStoreValue(final String group, final String storeValue) {
        for (final ProcessStatus2 curEnum : ProcessStatus2.values(group)) {
            if (curEnum.storeValue().equals(storeValue)) {
                return true;
            }
        }
        return false;
    }

    /**
     * コンストラクタ。
     *
     * @param storeValue 永続化値
     */
    private ProcessStatus2(final String storeValue) {
        this.ddId = this.getClass().getSimpleName() + "." + name();
        this.storeValue = storeValue;
    }

    /* (非 Javadoc)
     * @see me.suwash.util.classification.Classification#ddId()
     */
    @Override
    public String ddId() {
        return ddId;
    }

    /* (非 Javadoc)
     * @see me.suwash.util.classification.Classification#storeValue()
     */
    @Override
    public String storeValue() {
        return storeValue;
    }

    /* (非 Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return ddId() + "(" + storeValue() + ")";
    }
}

