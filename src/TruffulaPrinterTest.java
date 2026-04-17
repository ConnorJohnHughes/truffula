import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TruffulaPrinterTest {

    /**
     * Checks if the current operating system is Windows.
     *
     * This method reads the "os.name" system property and checks whether it
     * contains the substring "win", which indicates a Windows-based OS.
     * 
     * You do not need to modify this method.
     *
     * @return true if the OS is Windows, false otherwise
     */
    private static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("win");
    }

    /**
     * Creates a hidden file in the specified parent folder.
     * 
     * The filename MUST start with a dot (.).
     *
     * On Unix-like systems, files prefixed with a dot (.) are treated as hidden.
     * On Windows, this method also sets the DOS "hidden" file attribute.
     * 
     * You do not need to modify this method, but you SHOULD use it when creating hidden files
     * for your tests. This will make sure that your tests work on both Windows and UNIX-like systems.
     *
     * @param parentFolder the directory in which to create the hidden file
     * @param filename the name of the hidden file; must start with a dot (.)
     * @return a File object representing the created hidden file
     * @throws IOException if an I/O error occurs during file creation or attribute setting
     * @throws IllegalArgumentException if the filename does not start with a dot (.)
     */
    private static File createHiddenFile(File parentFolder, String filename) throws IOException {
        if(!filename.startsWith(".")) {
            throw new IllegalArgumentException("Hidden files/folders must start with a '.'");
        }
        File hidden = new File(parentFolder, filename);
        hidden.createNewFile();
        if(isWindows()) {
            Path path = Paths.get(hidden.toURI());
            Files.setAttribute(path, "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
        }
        return hidden;
    }

    @Test
    public void testPrintTree_ExactOutput_WithCustomPrintStream(@TempDir File tempDir) throws IOException {
        // Build the example directory structure:
        // myFolder/
        //    .hidden.txt
        //    Apple.txt
        //    banana.txt
        //    Documents/
        //       images/
        //          Cat.png
        //          cat.png
        //          Dog.png
        //       notes.txt
        //       README.md
        //    zebra.txt

        // Create "myFolder"
        File myFolder = new File(tempDir, "myFolder");
        assertTrue(myFolder.mkdir(), "myFolder should be created");

        // Create visible files in myFolder
        File apple = new File(myFolder, "Apple.txt");
        File banana = new File(myFolder, "banana.txt");
        File zebra = new File(myFolder, "zebra.txt");
        apple.createNewFile();
        banana.createNewFile();
        zebra.createNewFile();

        // Create a hidden file in myFolder
        createHiddenFile(myFolder, ".hidden.txt");

        // Create subdirectory "Documents" in myFolder
        File documents = new File(myFolder, "Documents");
        assertTrue(documents.mkdir(), "Documents directory should be created");

        // Create files in Documents
        File readme = new File(documents, "README.md");
        File notes = new File(documents, "notes.txt");
        readme.createNewFile();
        notes.createNewFile();

        // Create subdirectory "images" in Documents
        File images = new File(documents, "images");
        assertTrue(images.mkdir(), "images directory should be created");

        // Create files in images
        File cat = new File(images, "cat.png");
        File dog = new File(images, "Dog.png");
        cat.createNewFile();
        dog.createNewFile();

        // Set up TruffulaOptions with showHidden = false and useColor = true
        TruffulaOptions options = new TruffulaOptions(myFolder, false, true);

        // Capture output using a custom PrintStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);

        // Instantiate TruffulaPrinter with custom PrintStream
        TruffulaPrinter printer = new TruffulaPrinter(options, printStream);

        // Call printTree (output goes to printStream)
        printer.printTree();

        // Retrieve printed output
        String output = baos.toString();
        String nl = System.lineSeparator();

        // Build expected output with exact colors and indentation
        ConsoleColor reset = ConsoleColor.RESET;
        ConsoleColor white = ConsoleColor.WHITE;
        ConsoleColor purple = ConsoleColor.PURPLE;
        ConsoleColor yellow = ConsoleColor.YELLOW;

        StringBuilder expected = new StringBuilder();
        expected.append(white).append("myFolder/").append(nl).append(reset);
        expected.append(purple).append("   Apple.txt").append(nl).append(reset);
        expected.append(purple).append("   banana.txt").append(nl).append(reset);
        expected.append(purple).append("   Documents/").append(nl).append(reset);
        expected.append(yellow).append("      images/").append(nl).append(reset);
        expected.append(white).append("         cat.png").append(nl).append(reset);
        expected.append(white).append("         Dog.png").append(nl).append(reset);
        expected.append(yellow).append("      notes.txt").append(nl).append(reset);
        expected.append(yellow).append("      README.md").append(nl).append(reset);
        expected.append(purple).append("   zebra.txt").append(nl).append(reset);

        // Assert that the output matches the expected output exactly
        assertEquals(expected.toString(), output);
    }

@Test
public void testPrintTreeNoColorNoOrder(@TempDir File tempDir) throws IOException {
    // Build the example directory structure:
    // myFolder/
    //    .hidden.txt
    //    Apple.txt
    //    banana.txt
    //    Documents/
    //       images/
    //          cat.png
    //          Dog.png
    //       notes.txt
    //       README.md
    //    zebra.txt

    File myFolder = new File(tempDir, "myFolder");
    assertTrue(myFolder.mkdir(), "myFolder should be created");

    File apple = new File(myFolder, "Apple.txt");
    File banana = new File(myFolder, "banana.txt");
    File zebra = new File(myFolder, "zebra.txt");
    assertTrue(apple.createNewFile());
    assertTrue(banana.createNewFile());
    assertTrue(zebra.createNewFile());



    File documents = new File(myFolder, "Documents");
    assertTrue(documents.mkdir(), "Documents directory should be created");

    File readme = new File(documents, "README.md");
    File notes = new File(documents, "notes.txt");
    assertTrue(readme.createNewFile());
    assertTrue(notes.createNewFile());

    File images = new File(documents, "images");
    assertTrue(images.mkdir(), "images directory should be created");

    File cat = new File(images, "cat.png");
    File dog = new File(images, "Dog.png");
    assertTrue(cat.createNewFile());
    assertTrue(dog.createNewFile());

    // showHidden = false, useColor = false
    TruffulaOptions options = new TruffulaOptions(myFolder, false, false);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(baos);

    TruffulaPrinter printer = new TruffulaPrinter(options, printStream);
    printer.printTree();

    String output = baos.toString();
    String nl = System.lineSeparator();

    assertTrue(output.contains("myFolder/" + nl));
    assertTrue(output.contains("   Apple.txt" + nl));
    assertTrue(output.contains("   banana.txt" + nl));
    assertTrue(output.contains("   Documents/" + nl));
    assertTrue(output.contains("      images/" + nl));
    assertTrue(output.contains("         cat.png" + nl));
    assertTrue(output.contains("         Dog.png" + nl));
    assertTrue(output.contains("      notes.txt" + nl));
    assertTrue(output.contains("      README.md" + nl));
    assertTrue(output.contains("   zebra.txt" + nl));

}

@Test
public void testPrintTreeNoColorNoOrder_Fords(@TempDir File tempDir) throws IOException {

    // Build the example directory structure:
    // Ford/
    //    Fusion.txt
    //    Escape.txt
    //    F150.txt
    //    Mustang/
    //       Models/
    //          GT.txt
    //          EcoBoost.txt
    //       Mach1.txt
    //       Shelby.txt

    File ford = new File(tempDir, "Ford");
    assertTrue(ford.mkdir(), "Ford should be created");

    File fusion = new File(ford, "Fusion.txt");
    File escape = new File(ford, "Escape.txt");
    File f150 = new File(ford, "F150.txt");
    assertTrue(fusion.createNewFile());
    assertTrue(escape.createNewFile());
    assertTrue(f150.createNewFile());

    File mustang = new File(ford, "Mustang");
    assertTrue(mustang.mkdir(), "Mustang directory should be created");

    File mach1 = new File(mustang, "Mach1.txt");
    File shelby = new File(mustang, "Shelby.txt");
    assertTrue(mach1.createNewFile());
    assertTrue(shelby.createNewFile());

    File models = new File(mustang, "Models");
    assertTrue(models.mkdir(), "Models directory should be created");

    File gt = new File(models, "GT.txt");
    File ecoboost = new File(models, "EcoBoost.txt");
    assertTrue(gt.createNewFile());
    assertTrue(ecoboost.createNewFile());

    // showHidden = false, useColor = false
    TruffulaOptions options = new TruffulaOptions(ford, false, false);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(baos);

    TruffulaPrinter printer = new TruffulaPrinter(options, printStream);
    printer.printTree();

    String output = baos.toString();
    String nl = System.lineSeparator();

    assertTrue(output.contains("Ford/" + nl));
    assertTrue(output.contains("   Fusion.txt" + nl));
    assertTrue(output.contains("   Escape.txt" + nl));
    assertTrue(output.contains("   Mustang/" + nl));
    assertTrue(output.contains("      Models/" + nl));
    assertTrue(output.contains("         GT.txt" + nl));
    assertTrue(output.contains("         EcoBoost.txt" + nl));
    assertTrue(output.contains("      Mach1.txt" + nl));
    assertTrue(output.contains("      Shelby.txt" + nl));
    assertTrue(output.contains("   F150.txt" + nl));

}

@Test
public void testPrintTreeNoColorNoOrder_AmericanBully(@TempDir File tempDir) throws IOException {

    // Build the example directory structure:
    // AmericanBully/
    //    Blue/
    //       Zeus.txt
    //       Luna.txt
    //    Brindle/
    //       Max.txt
    //       Bella.txt
    //    Fawn/
    //       Rocky.txt
    //       Daisy.txt
    //    Black/
    //       King.txt
    //       Shadow.txt

    File bully = new File(tempDir, "AmericanBully");
    assertTrue(bully.mkdir(), "AmericanBully should be created");

    File blue = new File(bully, "Blue");
    File brindle = new File(bully, "Brindle");
    File fawn = new File(bully, "Fawn");
    File black = new File(bully, "Black");

    assertTrue(blue.mkdir());
    assertTrue(brindle.mkdir());
    assertTrue(fawn.mkdir());
    assertTrue(black.mkdir());

    File zeus = new File(blue, "Zeus.txt");
    File luna = new File(blue, "Luna.txt");
    assertTrue(zeus.createNewFile());
    assertTrue(luna.createNewFile());

    File max = new File(brindle, "Max.txt");
    File bella = new File(brindle, "Bella.txt");
    assertTrue(max.createNewFile());
    assertTrue(bella.createNewFile());

    File rocky = new File(fawn, "Rocky.txt");
    File daisy = new File(fawn, "Daisy.txt");
    assertTrue(rocky.createNewFile());
    assertTrue(daisy.createNewFile());

    File king = new File(black, "King.txt");
    File shadow = new File(black, "Shadow.txt");
    assertTrue(king.createNewFile());
    assertTrue(shadow.createNewFile());

    // showHidden = false, useColor = false
    TruffulaOptions options = new TruffulaOptions(bully, false, false);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(baos);

    TruffulaPrinter printer = new TruffulaPrinter(options, printStream);
    printer.printTree();

    String output = baos.toString();
    String nl = System.lineSeparator();

    assertTrue(output.contains("AmericanBully/" + nl));
    assertTrue(output.contains("   Blue/" + nl));
    assertTrue(output.contains("      Zeus.txt" + nl));
    assertTrue(output.contains("      Luna.txt" + nl));
    assertTrue(output.contains("   Brindle/" + nl));
    assertTrue(output.contains("      Max.txt" + nl));
    assertTrue(output.contains("      Bella.txt" + nl));
    assertTrue(output.contains("   Fawn/" + nl));
    assertTrue(output.contains("      Rocky.txt" + nl));
    assertTrue(output.contains("      Daisy.txt" + nl));
    assertTrue(output.contains("   Black/" + nl));
    assertTrue(output.contains("      King.txt" + nl));
    assertTrue(output.contains("      Shadow.txt" + nl));

    // Hidden file should NOT appear
    assertFalse(output.contains(".hidden.txt"));
}

@Test
public void testPrintTreeShowHiddenSingle(@TempDir File tempDir) throws IOException {

    File root = new File(tempDir, "root");
    assertTrue(root.mkdir());

    // Create hidden file
    File hidden = new File(root, ".secret.txt");
    assertTrue(hidden.createNewFile());

    // showHidden = true
    TruffulaOptions options = new TruffulaOptions(root, true, false);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(baos);

    TruffulaPrinter printer = new TruffulaPrinter(options, printStream);
    printer.printTree();

    String output = baos.toString();
    String nl = System.lineSeparator();

    assertTrue(output.contains("root/" + nl));
    assertTrue(output.contains("   .secret.txt" + nl)); // show hidden file
}

@Test
public void testPrintTreeShowHiddenFiles(@TempDir File tempDir) throws IOException {

    File root = new File(tempDir, "root");
    assertTrue(root.mkdir());

   
    File hidden = new File(root, ".hidden.txt");
    File visible = new File(root, "visible.txt");
    assertTrue(hidden.createNewFile());
    assertTrue(visible.createNewFile());

    // showHidden = true
    TruffulaOptions options = new TruffulaOptions(root, true, false);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(baos);

    TruffulaPrinter printer = new TruffulaPrinter(options, printStream);
    printer.printTree();

    String output = baos.toString();
    String nl = System.lineSeparator();

    assertTrue(output.contains("root/" + nl));
    assertTrue(output.contains("   .hidden.txt" + nl));  // show hidden file and normal file
    assertTrue(output.contains("   visible.txt" + nl));  
}

@Test
public void testPrintTreeHiddenDisabled(@TempDir File tempDir) throws IOException {

    File root = new File(tempDir, "root");
    assertTrue(root.mkdir());

    File hidden = new File(root, ".hidden.txt");
    assertTrue(hidden.createNewFile());

    // showHidden = false
    TruffulaOptions options = new TruffulaOptions(root, false, false);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(baos);

    TruffulaPrinter printer = new TruffulaPrinter(options, printStream);
    printer.printTree();

    String output = baos.toString();

    assertFalse(output.contains(".hidden.txt"));  // does not show file
}
@Test
public void testPrintTreeShowHiddenSingleColor(@TempDir File tempDir) throws IOException {

    File root = new File(tempDir, "root");
    assertTrue(root.mkdir());

    // Create hidden file
    File hidden = new File(root, ".secret.txt");
    assertTrue(hidden.createNewFile());

    // showHidden = true
    TruffulaOptions options = new TruffulaOptions(root, true, true);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(baos);

    TruffulaPrinter printer = new TruffulaPrinter(options, printStream);
    printer.printTree();

    String output = baos.toString();
    String nl = System.lineSeparator();

    ConsoleColor reset = ConsoleColor.RESET;
    ConsoleColor purple = ConsoleColor.PURPLE;
    ConsoleColor white = ConsoleColor.WHITE;

    assertTrue(output.contains("root/" + nl));
    assertTrue(output.contains("   .secret.txt" + nl)); // show hidden file

    StringBuilder expected = new StringBuilder();
    expected.append(white).append("root/").append(nl).append(reset);
    expected.append(purple).append("   .secret.txt").append(nl).append(reset);

    assertEquals(expected.toString(), output);


}

@Test
public void testPrintTreeShowHiddenFilesColor(@TempDir File tempDir) throws IOException {

    File root = new File(tempDir, "root");
    assertTrue(root.mkdir());

   
    File hidden = new File(root, ".hidden.txt");
    File visible = new File(root, "visible.txt");
    assertTrue(hidden.createNewFile());
    assertTrue(visible.createNewFile());

    // showHidden = true
    TruffulaOptions options = new TruffulaOptions(root, true, true);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(baos);

    TruffulaPrinter printer = new TruffulaPrinter(options, printStream);
    printer.printTree();

    String output = baos.toString();
    String nl = System.lineSeparator();

    ConsoleColor reset = ConsoleColor.RESET;
    ConsoleColor purple = ConsoleColor.PURPLE;
    ConsoleColor white = ConsoleColor.WHITE;

    assertTrue(output.contains("root/" + nl));
    assertTrue(output.contains("   .hidden.txt" + nl));  // show hidden file and normal file
    assertTrue(output.contains("   visible.txt" + nl));  

    StringBuilder expected = new StringBuilder();
    expected.append(white).append("root/").append(nl).append(reset);
    expected.append(purple).append("   .hidden.txt").append(nl).append(reset);
    expected.append(purple).append("   visible.txt").append(nl).append(reset);

    assertEquals(expected.toString(), output);
}

@Test
public void testPrintTreeHiddenDisabledColor(@TempDir File tempDir) throws IOException {

    File root = new File(tempDir, "root");
    assertTrue(root.mkdir());

    File hidden = new File(root, ".hidden.txt");
    File visible = new File(root, "visible.txt");
    assertTrue(hidden.createNewFile());
    assertTrue(visible.createNewFile());

    // showHidden = false
    TruffulaOptions options = new TruffulaOptions(root, false, true);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(baos);

    TruffulaPrinter printer = new TruffulaPrinter(options, printStream);
    printer.printTree();

    String output = baos.toString();
    String nl = System.lineSeparator();

    ConsoleColor reset = ConsoleColor.RESET;
    ConsoleColor purple = ConsoleColor.PURPLE;
    ConsoleColor white = ConsoleColor.WHITE;

    assertFalse(output.contains(".hidden.txt"));  // does not show file

    StringBuilder expected = new StringBuilder();
    expected.append(white).append("root/").append(nl).append(reset);
    // expected.append(purple).append("    .hidden.txt").append(nl).append(reset);
    expected.append(purple).append("   visible.txt").append(nl).append(reset);

    assertEquals(expected.toString(), output);
}
@Test
public void testPrintTreeColorNoOrder(@TempDir File tempDir) throws IOException {

    // Build structure:
    // root/
    //    file1.txt
    //    folder/
    //       file2.txt

    File root = new File(tempDir, "root");
    assertTrue(root.mkdir());

    File file1 = new File(root, "file1.txt");
    assertTrue(file1.createNewFile());

    File folder = new File(root, "folder");
    assertTrue(folder.mkdir());

    File file2 = new File(folder, "file2.txt");
    assertTrue(file2.createNewFile());
    
    TruffulaOptions options = new TruffulaOptions(root, false, true);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(baos);

    TruffulaPrinter printer = new TruffulaPrinter(options, printStream);
    printer.printTree();

    String output = baos.toString();
    String nl = System.lineSeparator();

   
    ConsoleColor reset = ConsoleColor.RESET;
    ConsoleColor white = ConsoleColor.WHITE;
    ConsoleColor purple = ConsoleColor.PURPLE;
    ConsoleColor yellow = ConsoleColor.YELLOW;

    
    assertTrue(output.contains(white + "root/" + nl + reset));
    assertTrue(output.contains(purple + "   file1.txt" + nl + reset));
    assertTrue(output.contains(purple + "   folder/" + nl + reset));
    assertTrue(output.contains(yellow + "      file2.txt" + nl + reset));
}
@Test
public void testPrintTreeAlphabeticalSorting(@TempDir File tempDir) throws IOException {

    // Build structure:
    // root/
    //    zebra.txt
    //    Apple.txt
    //    banana.txt

    File root = new File(tempDir, "root");
    assertTrue(root.mkdir());

    File zebra = new File(root, "zebra.txt");
    File apple = new File(root, "Apple.txt");
    File banana = new File(root, "banana.txt");

    assertTrue(zebra.createNewFile());
    assertTrue(apple.createNewFile());
    assertTrue(banana.createNewFile());

    TruffulaOptions options = new TruffulaOptions(root, false, false);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(baos);

    TruffulaPrinter printer = new TruffulaPrinter(options, printStream);
    printer.printTree();

    String output = baos.toString();
    String nl = System.lineSeparator();

    int appleIndex = output.indexOf("   Apple.txt" + nl);
    int bananaIndex = output.indexOf("   banana.txt" + nl);
    int zebraIndex = output.indexOf("   zebra.txt" + nl);

    assertTrue(appleIndex != -1);
    assertTrue(bananaIndex != -1);
    assertTrue(zebraIndex != -1);

    assertTrue(appleIndex < bananaIndex);
    assertTrue(bananaIndex < zebraIndex);
}
}
