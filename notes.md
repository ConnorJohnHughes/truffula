# Truffula Notes
As part of Wave 0, please fill out notes for each of the below files. They are in the order I recommend you go through them. A few bullet points for each file is enough. You don't need to have a perfect understanding of everything, but you should work to gain an idea of how the project is structured and what you'll need to implement. Note that there are programming techniques used here that we have not covered in class! You will need to do some light research around things like enums and and `java.io.File`.

PLEASE MAKE FREQUENT COMMITS AS YOU FILL OUT THIS FILE.

## App.java
- Application to print a directory tree
- uses colors to show hidden files, root of tree, and levels in the directory
- hidden files are shown with a '.' before them
- arguements used = -h to show hidden files and -nc to turn off color
- path arguement is mandatory to use truffula
- error messages for illegal arguments and no file found
- this is where the main method is at to use the app
- main = create truffula options -> pass to truffula printer -> then printTree

## ConsoleColor.java
- file for colors in terminal
- using ANSI escape codes for console colors
- supports a handleful of colors
- there is a reset code to go back to default color of console
- uses constructor for color code as string
- returns code as string
- returns code as string for print statements
- what does it mean when it says "escape" codes?
## ColorPrinter.java / ColorPrinterTest.java
- prints current color through an output stream
- terminal prints out color to confirm
- sets current color that will be used until changed again
- optionally the color can reset after printing 
- uses output stream for colors
- constructs a ColorPrinter using a printStream and color of choice
- construtor for colorPrinter
- tests for colorPrinter
- ColorPrinter test is to confirm color choice using red 

## TruffulaOptions.java / TruffulaOptionsTest.java
- used for the configuration options on how the directory tree is displayed
- options to configure color, show hidden files, and the root directory from where the tree begins
- if color is disabled then all text is white
- throws illegal argument exception for unknown flags(arguments) and path is missing
- throws file not found exception if the directory chosen does not exist or if the path points to a file instead of a directory
- getRoot() is used to return the root directory as a file object
- isShowHidden() uses a boolen to tell if the show hidden files option is on or not
- uses a toString to print out Truffula options that are chosen
- isUseColor() is a boolean to show if color is on or not
- TruffulaOptions() is a method used to show what options are chosen and handles errors.
- uses constructor to parse options together with explicit values
- Tests to show if options are set up correctly
- test covers options for directory, color, and hidden files

## TruffulaPrinter.java / TruffulaPrinterTest.java

## AlphabeticalFileSorter.java