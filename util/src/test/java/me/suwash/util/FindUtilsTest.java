package me.suwash.util;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import me.suwash.util.FindUtils.FileType;

@lombok.extern.slf4j.Slf4j
public class FindUtilsTest {

   private static final String DIR_BASE = "src/test/resources";

   @Rule
   public TestName name = new TestName();

   @BeforeClass
   public static void setUpBeforeClass() throws Exception {
       log.debug("■■■ " + FindUtilsTest.class.getName() + " ■■■");
   }

   @AfterClass
   public static void tearDownAfterClass() throws Exception {
   }

   @Before
   public void setUp() throws Exception {
       log.debug("■ " + name.getMethodName() + " - START");
       log.debug(RuntimeUtils.getMemoryInfo());
   }

   @After
   public void tearDown() throws Exception {
       log.debug(RuntimeUtils.getMemoryInfo());
       log.debug("■ " + name.getMethodName() + " - END");
   }

   @Test
   public void testFind() {
       List<File> finded = null;

       String dirPath = DIR_BASE;
       finded = FindUtils.find(dirPath);
       log.debug("-- find");
       log.debug("---- dirPath: " + dirPath);
       printFinded(finded);

       int minDepth = 2;
       finded = FindUtils.find(dirPath, minDepth);
       log.debug("-- find");
       log.debug("---- dirPath: " + dirPath);
       log.debug("---- minDepth: " + minDepth);
       printFinded(finded);

       int maxDepth = 2;
       finded = FindUtils.find(dirPath, minDepth, maxDepth);
       log.debug("-- find");
       log.debug("---- dirPath: " + dirPath);
       log.debug("---- minDepth: " + minDepth);
       log.debug("---- maxDepth: " + maxDepth);
       printFinded(finded);

       minDepth = 3;
       maxDepth = 3;
       FileType fileType = FileType.File;
       finded = FindUtils.find(dirPath, minDepth, maxDepth, fileType);
       log.debug("-- find");
       log.debug("---- dirPath: " + dirPath);
       log.debug("---- minDepth: " + minDepth);
       log.debug("---- maxDepth: " + maxDepth);
       log.debug("---- fileType: " + fileType);
       printFinded(finded);

       minDepth = 2;
       maxDepth = 2;
       fileType = FileType.Directory;
       String namePattern = ".*ut.*";
       finded = FindUtils.find(dirPath, minDepth, maxDepth, fileType, namePattern);
       log.debug("-- find");
       log.debug("---- dirPath: " + dirPath);
       log.debug("---- minDepth: " + minDepth);
       log.debug("---- maxDepth: " + maxDepth);
       log.debug("---- fileType: " + fileType);
       log.debug("---- namePattern: " + namePattern);
       printFinded(finded);

       finded = FindUtils.find(dirPath, fileType);
       log.debug("-- find");
       log.debug("---- dirPath: " + dirPath);
       log.debug("---- fileType: " + fileType);
       printFinded(finded);

       finded = FindUtils.find(dirPath, fileType, namePattern);
       log.debug("-- find");
       log.debug("---- dirPath: " + dirPath);
       log.debug("---- fileType: " + fileType);
       log.debug("---- namePattern: " + namePattern);
       printFinded(finded);
   }

   private void printFinded(List<File> finded) {
       log.debug("-- finded");
       for (File curFile : finded) {
           String fileType = "file: ";
           if (curFile.isDirectory()) {
               fileType = "dir : ";
           }
           log.debug("---- " + fileType + curFile.getAbsolutePath());
       }
   }

}
