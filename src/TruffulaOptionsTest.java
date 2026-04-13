import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class TruffulaOptionsTest {

  @TempDir
  Path tempDir;

  @Test
  void testValidDirectoryIsSet(@TempDir File tempDir) throws FileNotFoundException {
    // Arrange: Prepare the arguments with the temp directory
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String directoryPath = directory.getAbsolutePath();
    String[] args = {"-nc", "-h", directoryPath};

    // Act: Create TruffulaOptions instance
    TruffulaOptions options = new TruffulaOptions(args);

    // Assert: Check that the root directory is set correctly
    assertEquals(directory.getAbsolutePath(), options.getRoot().getAbsolutePath());
    assertTrue(options.isShowHidden());
    assertFalse(options.isUseColor());
  }
      @Test
    public void testHiddenFlag() throws Exception {
      //act
      TruffulaOptions options = new TruffulaOptions(new String[]{"-h", tempDir.toString()});
      //assert
      assertEquals(new File(tempDir.toString()), options.getRoot());
      assertTrue(options.isShowHidden());
      assertTrue(options.isUseColor());
    }

    @Test
    public void testNoColorFlag() throws Exception {
      // act
      TruffulaOptions options = new TruffulaOptions(new String[]{"-nc", tempDir.toString()});
      // assert
      assertEquals(new File(tempDir.toString()), options.getRoot());
      assertFalse(options.isShowHidden());
      assertFalse(options.isUseColor());
    }

    @Test
    public void testBothFlags() throws Exception {
      //act
      TruffulaOptions options = new TruffulaOptions(new String[]{"-h", "-nc", tempDir.toString()});
      //assert
      assertEquals(new File(tempDir.toString()), options.getRoot());
      assertTrue(options.isShowHidden());
      assertFalse(options.isUseColor());
    }

    @Test
    public void testBothFlagsReverse() throws Exception {
      //act
      TruffulaOptions options = new TruffulaOptions(new String[]{"-nc", "-h", tempDir.toString()});
      //assert
      assertEquals(new File(tempDir.toString()), options.getRoot());
      assertTrue(options.isShowHidden());
      assertFalse(options.isUseColor());
    }

    @Test
    public void testUnknownFlag() {
      //act/assert
      assertThrows(IllegalArgumentException.class, () -> {
          new TruffulaOptions(new String[]{"-x", tempDir.toString()});
      });
    }

    @Test
    public void testMissingPath() {
      //act/assert
      assertThrows(IllegalArgumentException.class, () -> {
          new TruffulaOptions(new String[]{});
      });
    }

    @Test
    public void testOnlyFlagNoPath() {
      //act/aseert
      assertThrows(Exception.class, () -> {
          new TruffulaOptions(new String[]{"-h"});
      });
    }

    @Test
    public void testPathDoesNotExist() {
      //act
      String badPath = tempDir.resolve("does_not_exist").toString();
      //assert
      assertThrows(FileNotFoundException.class, () -> {
          new TruffulaOptions(new String[]{badPath});
      });
    }

    @Test
    public void testPathIsAFile() throws IOException {
      //act
      Path tempFile = Files.createTempFile(tempDir, "sample", ".txt");
      //asert
      assertThrows(FileNotFoundException.class, () -> {
          new TruffulaOptions(new String[]{tempFile.toString()});
      });
    }

    @Test
    public void testUnknownFlagInvalidText() {
      //act/assert
      assertThrows(IllegalArgumentException.class, () -> {
          new TruffulaOptions(new String[]{"-h", "-bad", tempDir.toString()});
      });
    }
}
