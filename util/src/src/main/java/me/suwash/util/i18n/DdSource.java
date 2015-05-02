package me.suwash.util.i18n;

import java.util.Locale;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

/**
 * データディクショナリ用プロパティファイルの定義保持の基底クラス。
 */
public class DdSource extends ReloadableLocalazedSource {
    /** Singletonパターン */
    protected static DdSource instance;
    /** 親DdSource */
    protected DdSource parent;

    public static DdSource getInstance() {
        if (instance == null) {
            instance = new DdSource();
        }
        return instance;
    }
    protected DdSource() {
        parent = getParent();
    }

    /**
     * 親DdSourceを返します。
     * @return 親DdSource
     */
    protected DdSource getParent() {
        return null;
    }

    /**
     * データディクショナリIDを、再帰的に親DdSourceまで検索し、一致する項目名を返します。（子の定義を優先します）
     *
     * @param ddId データディクショナリID
     * @return 項目名
     */
    public String getName(String ddId) {
        return getName(ddId, null);
    }

    /**
     * データディクショナリIDを、再帰的に親DdSourceまで検索し、一致する項目名を返します。（子の定義を優先します）
     *
     * @param ddId データディクショナリID
     * @param locale ロケール（デフォルトは、マシンロケール）
     * @return 項目名
     */
    public String getName(String ddId, Locale locale) {
        // null、空文字チェック
        if (StringUtils.isEmpty(ddId)) {
            return StringUtils.EMPTY;
        }

        // Locale判定
        if (locale == null) {
            locale = Locale.getDefault();
        }

        // Locale毎のメッセージ取得
        Properties props = getProperties(locale);
        String name = props.getProperty(ddId);

        // 取得結果の確認
        if (StringUtils.isEmpty(name)) {
            // 未定義の場合、親メッセージの存在確認
            if (parent != null) {
                // 親メッセージが存在する場合、メッセージ取得
                name = parent.getName(ddId, locale);
            }
        }

        // 親メッセージ考慮後の取得結果確認
        if (StringUtils.isEmpty(name)) {
            // それでも存在しない場合、ddIdをそのまま返却
            name = ddId;
        }

        // メッセージ文言を返却
        return name;
    }

}
