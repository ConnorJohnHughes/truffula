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

## TruffulaPrinter.java / TruffulaPrinterTest.java

## AlphabeticalFileSorter.java