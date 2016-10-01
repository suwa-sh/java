package me.suwash.util.i18n;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DdSourceTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {}

    @AfterClass
    public static void tearDownAfterClass() throws Exception {}

    @Before
    public void setUp() throws Exception {}

    @After
    public void tearDown() throws Exception {}

    @Test
    public void testGetName_存在しないIDの場合() {
        DdSource dd = DdSource.getInstance();
        String ddId = "NotExistId";

        assertEquals("指定IDをそのまま返すこと", ddId, dd.getName(ddId));
    }

    @Test
    public void testGetName_空文字の場合() {
        DdSource dd = DdSource.getInstance();
        String ddId = StringUtils.EMPTY;
        assertEquals("空文字を返すこと", StringUtils.EMPTY, dd.getName(ddId));

        ddId = null;
        assertEquals("空文字を返すこと", StringUtils.EMPTY, dd.getName(ddId));
}

    @Test
    public void testGetName_ロケール指定なしの場合() {
        DdSource dd = DdSource.getInstance();
        String ddId = "functionCategory.function.SampleKbn";

        assertEquals("マシンロケールの文言を返すこと", "サンプル区分", dd.getName(ddId));
    }

    @Test
    public void testGetName_ロケール指定ありの場合() {
        DdSource dd = DdSource.getInstance();
        String ddId = "functionCategory.function.SampleKbn";
        Locale locale = Locale.US;

        assertEquals("指定ロケールの文言を返すこと", "SampleClassification", dd.getName(ddId, locale));
    }

    @Test
    public void testClearCache() throws IOException {
        DdSource dd = DdSource.getInstance();
        String ddId = "functionCategory.function.SampleKbn";

        String expectBeforeReplace = "サンプル区分";
        String expectAfterReplace = expectBeforeReplace + "置換済";


        FileSystem fs = FileSystems.getDefault();
        Path mainPath = fs.getPath(this.getClass().getResource("/dd_source_ja.properties").getPath());
        Path defaultPath = fs.getPath(this.getClass().getResource("/DEFAULT_dd_source_ja.properties").getPath());
        Path testPath = fs.getPath(this.getClass().getResource("/TEST_dd_source_ja.properties").getPath());

        // ファイル入れ替え前
        assertEquals("置換されていないこと", expectBeforeReplace, dd.getName(ddId));
        // ファイル入れ替え
        Files.copy(testPath, mainPath, StandardCopyOption.REPLACE_EXISTING);

        // キャッシュクリア前
        assertEquals("置換されていないこと", expectBeforeReplace, dd.getName(ddId));
        // キャッシュクリア
        dd.clearCache();
        // キャッシュクリア後
        assertEquals("置換されていること", expectAfterReplace, dd.getName(ddId));

        // ファイルを戻す
        Files.copy(defaultPath, mainPath, StandardCopyOption.REPLACE_EXISTING);
        dd.clearCache();
        assertEquals("置換されていないこと", expectBeforeReplace, dd.getName(ddId));
    }
}
