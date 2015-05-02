package me.suwash.ddd.classification;

import java.util.HashMap;
import java.util.Map;

import me.suwash.util.classification.Classification;

/**
 * 処理ステータス。
 */
public enum ProcessStatus implements Classification {
    /** 処理中 */
    Processing("PRC"),
    /** 成功 */
    Success("SCC"),
    /** 警告 */
    Warning("WRN"),
    /** 失敗 */
    Failure("FLR");

    /** グループ名配列 */
    protected static String[] groups;
    /** 区分値グループMap */
    protected static Map<String, ProcessStatus[]> groupValuesMap;
    /** グループ内デフォルト区分値Map */
    private static Map<String, ProcessStatus> groupDefaultMap;

    /** グループ：デフォルト */
    public static final String GROUP_DEFAULT = "default";
    /** グループ：終了ステータス */
    public static final String GROUP_FINISHED = "finished";

    static {
        // グループMap
        groupValuesMap = new HashMap<String, ProcessStatus[]>();
        groupValuesMap.put(GROUP_DEFAULT, new ProcessStatus[]{
            Processing,
            Success,
            Warning,
            Failure
            });
        groupValuesMap.put(GROUP_FINISHED, new ProcessStatus[]{
            Success,
            Warning,
            Failure
            });

        // グループ内デフォルト値Map
        groupDefaultMap = new HashMap<String, ProcessStatus>();
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
    public static ProcessStatus defaultValue() {
        return groupDefaultMap.get(GROUP_DEFAULT);
    }

    /**
     * グループ内のデフォルト区分値を返します。
     *
     * @param group グループ名
     * @return デフォルトの区分値
     */
    public static ProcessStatus defaultValue(String group) {
        return groupDefaultMap.get(group);
    }

    /**
     * 区分が持つグループ群を返します。
     * @return グループ名配列
     */
    public static String[] groups() {
        return groups;
    }

    /**
     * 指定したグループ名に属する区分値を返します。
     * @param group グループ名
     * @return 区分値配列
     */
    public static ProcessStatus[] values(String group) {
        return groupValuesMap.get(group);
    }

    /**
     * データディクショナリIDから区分値を返します。
     * 見つからない場合はnullを返します。
     *
     * @param ddId データディクショナリID
     * @return 区分値
     */
    public static ProcessStatus valueOfByDdId(String ddId) {
        for (ProcessStatus curEnum : ProcessStatus.values()) {
            if(curEnum.ddId().equals(ddId)) {
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
    public static ProcessStatus valueOfByStoreValue(String storeValue) {
        for (ProcessStatus curEnum : ProcessStatus.values()) {
            if(curEnum.storeValue().equals(storeValue)) {
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
    public static boolean containsName(String name) {
        for (ProcessStatus curEnum : ProcessStatus.values()) {
            if(curEnum.name().equals(name)) {
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
    public static boolean containsName(String group, String name) {
        for (ProcessStatus curEnum : ProcessStatus.values(group)) {
            if(curEnum.name().equals(name)) {
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
    public static boolean containsDdId(String ddId) {
        for (ProcessStatus curEnum : ProcessStatus.values()) {
            if(curEnum.ddId().equals(ddId)) {
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
    public static boolean containsDdId(String group, String ddId) {
        for (ProcessStatus curEnum : ProcessStatus.values(group)) {
            if(curEnum.ddId().equals(ddId)) {
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
    public static boolean containsStoreValue(String storeValue) {
        for (ProcessStatus curEnum : ProcessStatus.values()) {
            if(curEnum.storeValue().equals(storeValue)) {
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
    public static boolean containsStoreValue(String group, String storeValue) {
        for (ProcessStatus curEnum : ProcessStatus.values(group)) {
            if(curEnum.storeValue().equals(storeValue)) {
                return true;
            }
        }
        return false;
    }


    /** データディクショナリID */
    private String ddId;
    /** 永続化値 */
    private String storeValue;

    /**
     * コンストラクタ。
     *
     * @param storeValue 永続化値
     */
    private ProcessStatus(String storeValue) {
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

