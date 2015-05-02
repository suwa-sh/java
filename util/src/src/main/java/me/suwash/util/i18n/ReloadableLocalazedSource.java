package me.suwash.util.i18n;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import me.suwash.util.exception.UtilException;

/**
 * ロケールを考慮したプロパティファイルの定義保持クラス。
 * プロパティファイルのキャッシュは、リロードすることができます。
 *
 * 読み込み対象のプロパティファイルは、クラスパス直下の下記のファイル名です。
 * 「実装クラス名の小文字表現("_"区切り) + _ + ロケール + .properties」
 * ※XxxMessageSource → xxx_message_source_ja.properties, xxx_message_source_en.properties など
 */
public class ReloadableLocalazedSource {
    /** Locale毎のプロパティMap */
    protected Map<String, Properties> propsMap = new HashMap<String, Properties>();

    /**
     * ロケールに合わせたプロパティファイルをクラスパス直下から読み込みます。
     * ロケールごとにプロパティファイルの内容をキャッシュします。
     * キャッシュのクリアはclearCacheメソッドを利用してください。
     *
     * @param locale ロケール
     * @return 対象ロケールのプロパティファイル。
     */
    protected Properties getProperties(Locale locale) {
        if (! propsMap.containsKey(locale.getLanguage())) {
            Properties props = new Properties();
            try (
                    InputStream inStream = getClass().getResourceAsStream("/" + getPropertiesFileName(locale));
                    Reader reader = new InputStreamReader(inStream, "UTF-8");
                ) {
                props.load(reader);
            } catch (Exception e) {
                throw new UtilException("check.cantRead", new Object[] {getPropertiesFileName(locale)}, e);
            }
            propsMap.put(locale.getLanguage(), props);
        }
        return propsMap.get(locale.getLanguage());
    }

    /**
     * ロケールに合わせて、対象のプロパティファイル名を返します。
     * 「実装クラス名の小文字表現("_"区切り) + _ + ロケール + .properties」
     *
     * @param locale ロケール
     * @return 指定されたロケールの対象プロパティファイル名
     */
    protected String getPropertiesFileName(Locale locale) {
        StringBuilder fileNameBuilder = new StringBuilder();

        // クラス名のUpperCamelCaseから、小文字の"_"区切りに置換
        String className = this.getClass().getSimpleName();
        String lowerCaseClassName = className.toLowerCase();
        for (int pos = 0; pos < className.length(); pos++) {
            if (pos > 1) {
                if (Character.isUpperCase(className.charAt(pos))) {
                    fileNameBuilder.append("_");
                }
            }
            fileNameBuilder.append(lowerCaseClassName.charAt(pos));
        }

        fileNameBuilder.append("_" + locale.getLanguage());
        fileNameBuilder.append(".properties");
        return fileNameBuilder.toString();
    }

    /**
     * 読み込んだpropertiesファイルのキャッシュをリフレッシュします。
     */
    public void clearCache() {
        propsMap.clear();
    }
}
