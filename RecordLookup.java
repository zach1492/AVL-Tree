import java.io.*;
import java.util.Scanner;
/**
 * Manages recordBST and allows the user to interact with it
 * Reads records from a CSV file 
 * provdes an interactive menu for 
 * searching, 
 * adding, 
 * removing 
 * and filtering records by genre and year range
 */
public class RecordLookup {

    private static RecordBST bst = new RecordBST();//Bst that hold loaded records

    private static Scanner scanner = new Scanner(System.in);//scans user input from terminal

    /**
     * Entry point to program
     * 
     * loads records from the csv file, assumes that the csv file is in the same folder as this script 
     * runs interactive menu
     * 
     * @param args is not used
     */
    public static void main(String[] args) {
        System.out.println("\nStarting record storage program\n");

        String filePath = "records.csv";
        
        int loaded = loadRecords(filePath);
        if (loaded < 0) {
            System.out.println("Error - Could not read the file \nShuting down");
            return;
        }
        System.out.println("Loaded " + loaded + " records from " + filePath + ".\n");

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": searchRecord();         break;
                case "2": addRecord();             break;
                case "3": removeRecord();          break;
                case "4": printByGenre();          break;
                case "5": printByGenreYearRange(); break;
                case "6": running = false;         break;
                default:
                    System.out.println("Error, please input a value from 1 to 6\n");
            }
        }
        System.out.println("Shuting down program");
        scanner.close();
    }

    /**
     * Reads file and puts them in bst
     * assumes file follows the formant genre, year, artist, title
     * Skips empty or broken lines with a warning
     * 
     * @param filePath is the path to the file record
     * @return number of succesfully loaded records or -1 if file not found error
     */
    private static int loadRecords(String filePath) {
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNum = 0;
            while ((line = br.readLine()) != null) {
                lineNum++;
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length != 4) {
                    System.out.println("Error, empty or broken line on " + lineNum + ": " + line);
                    continue;
                }

                try {
                    String genre  = parts[0].trim();
                    int    year   = Integer.parseInt(parts[1].trim());
                    String artist = parts[2].trim();
                    String title  = parts[3].trim();

                    bst.insert(new Record(genre, year, artist, title));
                    count++;
                } catch (NumberFormatException e) {
                    System.out.println("Error, skipping line " + lineNum + " (broken year): " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: file not found – " + filePath);
            return -1;
        } catch (IOException e) {
            System.out.println("Error in reading file: " + e.getMessage());
            return -1;
        }
        return count;
    }
    /**
     * Prints menu option to console
     */
    private static void printMenu() {
        System.out.println("\nSelect an option:");
        System.out.println("  1. Search for a record");
        System.out.println("  2. Add a new record");
        System.out.println("  3. Remove a record");
        System.out.println("  4. Print all records of a given genre");
        System.out.println("  5. Print records within a genre and year range");
        System.out.println("  6. Exit");
        System.out.print("Type input: ");
    }

    /**
     * promts user for record details
     * and searches bst for it
     */
    private static void searchRecord() {
        System.out.println("\nSearch for a Record");
        Record r = promptRecord();
        if (r == null) return;

        boolean found = bst.search(r);
        System.out.println(found
                ? "Record found: " + r
                : "Record not found: " + r);
        System.out.println();
    }

    /**
     * promts user for record details
     * and add it to the bst 
     */
    private static void addRecord() {
        System.out.println("\nAdd a New Record");
        Record r = promptRecord();
        if (r == null) return;

        bst.insert(r);
        System.out.println("\nRecord added: " + r);
        System.out.println();
    }

    /**
     * promts user for record details
     * and remose it from the bst 
     */
    private static void removeRecord() {
        System.out.println("\nRemove a Record");
        Record r = promptRecord();
        if (r == null) return;

        if (bst.search(r)) {
            bst.remove(r);
            System.out.println("\nRecord removed: " + r);
        } else {
            System.out.println("\nRecord not found");
        }
        System.out.println();
    }

    /**
     * promts user for genre details
     * and prints all records by genre in the bst
     */
    private static void printByGenre() {
        System.out.println("\nPrint Records by Genre");
        System.out.print("Enter genre: ");
        String genre = scanner.nextLine().trim();
        if (genre.isEmpty()) {
            System.out.println("Genre cannot be empty\n");
            return;
        }
        System.out.println("Records in genre \"" + genre + "\":");
        bst.printGenre(genre);
        System.out.println();
    }

    /**
     * promts user for genre details and year range
     * prints all records in genre  and year range
     */
    private static void printByGenreYearRange() {
        System.out.println("\nPrint Records by Genre and Year Range");
        System.out.print("Enter genre: ");
        String genre = scanner.nextLine().trim();
        if (genre.isEmpty()) {
            System.out.println("Genre cannot be empty.\n");
            return;
        }

        Integer earliest = promptYear("Enter earliest year: ");
        if (earliest == null) return;

        Integer latest = promptYear("Enter latest year: ");
        if (latest == null) return;

        if (earliest >= latest) {
            System.out.println("Earliest year must be less than latest year\n");
            return;
        }

        System.out.println("Records in genre \"" + genre + "\" with " + earliest + " < year < " + latest + ":");
        bst.printGenreWithYearRange(genre, earliest, latest);
        System.out.println();
    }

    /**
     * Prompt user for all 4 record details and returns a record
     * 
     * @param return the record
     */
    private static Record promptRecord() {
        System.out.print("Enter genre: ");
        String genre = scanner.nextLine().trim();

        Integer year = promptYear("Enter year: ");
        if (year == null) return null;

        System.out.print("Enter artist: ");
        String artist = scanner.nextLine().trim();

        System.out.print("Enter title: ");
        String title = scanner.nextLine().trim();

        if (genre.isEmpty() || artist.isEmpty() || title.isEmpty()) {
            System.out.println("Error, genre, artist, and title must not be empty\n");
            return null;
        }

        return new Record(genre, year, artist, title);
    }

    /**
     * Prompts user for the year
     * 
     * @param prompt text to print out before request
     * @return year entered or null if invalid
     */
    private static Integer promptYear(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Error: \"" + input + "\" is not a valid year. Please enter an integer\n");
            return null;
        }
    }
}
