package me.suwash.util.i18n;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import me.suwash.util.exception.UtilException;

import org.apache.commons.lang3.StringUtils;

/**
 * メッセージ用プロパティファイルの定義保持の基底クラス。
 */
public class MessageSource extends ReloadableLocalazedSource {
    /** Singletonパターン */
    protected static MessageSource instance;
    /** 親MessageSource */
    protected MessageSource parent;
    /** DataDictioanry */
    protected DdSource dataDictionary;

    public static MessageSource getInstance() {
        if (instance == null) {
            instance = new MessageSource();
        }
        return instance;
    }
    protected MessageSource() {
        parent = getParent();
        dataDictionary = getDd();
    }

    /**
     * 親MessageSourceを返します。
     * @return 親MessageSource
     */
    protected MessageSource getParent() {
        return null;
    }

    /**
     * データディクショナリを返します。
     * @return データディクショナリ
     */
    protected DdSource getDd() {
        return DdSource.getInstance();
    }

    /**
     * メッセージIDを、再帰的に親MessageSourceまで検索し、一致する文言を返します。（子の定義を優先します）
     *
     * @param messageId メッセージID
     * @return メッセージ文言
     */
    public String getMessage(String messageId) {
        return getMessage(messageId, null, null);
    }


    /**
     * メッセージIDを、再帰的に親MessageSourceまで検索し、一致する文言を返します。（子の定義を優先します）
     *
     * @param messageId メッセージID
     * @param locale ロケール（デフォルトは、マシンロケール）
     * @return メッセージ文言
     */
    public String getMessage(String messageId, Locale locale) {
        return getMessage(messageId, null, locale);
    }

    /**
     * メッセージIDを、再帰的に親MessageSourceまで検索し、一致する文言を返します。（子の定義を優先します）
     * メッセージ引数には、データディクショナリIDを設定すると、DDから取得した項目名を設定します。
     *
     * @param messageId メッセージID
     * @param args メッセージ引数
     * @return メッセージ文言
     */
    public String getMessage(String messageId, Object[] args) {
        return getMessage(messageId, args, null);
    }

    /**
     * メッセージIDを、再帰的に親MessageSourceまで検索し、一致する文言を返します。（子の定義を優先します）
     * メッセージ引数には、データディクショナリIDを設定すると、DDから取得した項目名を設定します。
     *
     * @param messageId メッセージID
     * @param args メッセージ引数
     * @param locale ロケール（デフォルトは、マシンロケール）
     * @return メッセージ文言
     */
    public String getMessage(String messageId, Object[] args, Locale locale) {
        // 必須チェック
        if (StringUtils.isEmpty(messageId)) {
            throw new UtilException("check.notNull", new Object[]{"messageId"});
        }

        // Locale判定
        if (locale == null) {
            locale = Locale.getDefault();
        }

        // Locale毎のメッセージ取得
        Properties props = getProperties(locale);
        String message = props.getProperty(messageId);

        // 取得結果の確認
        if (StringUtils.isEmpty(message)) {
            // 未定義の場合、親MessageSourceの存在確認
            if (parent != null) {
                // 親MessageSourceが存在する場合、メッセージ取得
                message = parent.getMessage(messageId, args, locale);
            }
        }

        // 親MessageSource考慮後の取得結果確認
        if (StringUtils.isEmpty(message)) {
            // それでも存在しない場合、エラー
            throw new UtilException("check.undefined", new Object[]{getPropertiesFileName(locale) + "." + messageId});
        }

        // メッセージ引数の適用
        if (args != null) {
            List<Object> ddReplacedArgList = new ArrayList<Object>();
            for (int i = 0; i < args.length; i++) {

                // DataDictionayからddIdとLocaleで項目名を取得
                String ddId = StringUtils.EMPTY;
                if (args[i] != null) {
                    ddId = args[i].toString();
                }
                String ddName = dataDictionary.getName(ddId, locale);

                // 取得した項目名の確認
                if (ddName.equals(ddId)) {
                    // DD定義されていない値の場合、引数をそのまま設定
                    ddReplacedArgList.add(args[i]);
                } else {
                    // DD定義された値の場合、DataDictionayから取得した項目名を設定
                    ddReplacedArgList.add(ddName);
                }
            }

            // フォーマットを実行
            MessageFormat messageFormat = new MessageFormat(message);
            try {
                message = messageFormat.format(ddReplacedArgList.toArray());
            } catch(Exception e) {
                throw new UtilException("check.illegalArguments", new Object[]{message, "messageArgs", ddReplacedArgList.toString()}, e);
            }
        }

        // メッセージ文言を返却
        return message;
    }
}
