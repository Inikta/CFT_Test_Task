package ru.inikta.task.arguments_parser;

import ru.inikta.task.sort.SortingOrder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandLineArgsParser {

    private static final String[] ORDER_PARAMETERS = {"-a", "-d"};
    private static final String[] DATA_TYPE_PARAMETERS = {"-i", "-s"};

    /**
     * Parse command line arguments in order: <br>
     * <ol>
     *     <li>Sorting order (-a - ascending, -d - descending) (not required)</li>
     *     <li>Data type (-s - strings, -i - integers) (required)</li>
     *     <li>Output file (must have .txt in it) (required)</li>
     *     <li>Input files (no more than 50 at once, must have .txt) (required)</li>
     * </ol>
     *
     * @param arguments command line arguments
     * @return container of parameters for other program entities
     * @throws IOException output file not found
     */
    public static ArgumentsContainer parse(List<String> arguments) throws IOException {
        ArgumentsContainer container = new ArgumentsContainer();
        int counter = 0;

        if (Arrays.stream(ORDER_PARAMETERS).toList().contains(arguments.get(counter).toLowerCase())) {
            if (arguments.get(counter).equals("-a")) {
                container.setOrder(SortingOrder.ASCENDING);
            } else if (arguments.get(counter).equals("-d")) {
                container.setOrder(SortingOrder.DESCENDING);
            } else {
                throw new IllegalArgumentException("Wrong order argument " + arguments.get(counter) + ".");
            }
            counter++;
        } else {
            container.setOrder(SortingOrder.ASCENDING);
        }

        if (Arrays.stream(DATA_TYPE_PARAMETERS).toList().contains(arguments.get(counter).toLowerCase())) {
            if (arguments.get(counter).equals("-i")) {
                container.setDataType(Long.class);
            } else if (arguments.get(counter).equals("-s")) {
                container.setDataType(String.class);
            } else {
                throw new IllegalArgumentException("Wrong data type argument " + arguments.get(counter) + ".");
            }
            counter++;
        }

        if (arguments.get(counter).contains(".txt")) {
            File outputFile = new File(arguments.get(counter));
            if (outputFile.exists()) {
                if (outputFile.isFile() & outputFile.canWrite()) {
                    container.setOutputFile(outputFile);
                } else {
                    throw new FileNotFoundException(
                            outputFile.getName() + " does not exist, is not a file or cannot be edited. \n");
                }
            } else {
                outputFile.createNewFile();
                container.setOutputFile(outputFile);
            }
            counter++;
        } else {
            throw new IllegalArgumentException("Wrong output file type " + arguments.get(counter) + ".");
        }

        container.setInputFiles(new ArrayList<>());
        if (arguments.size() - counter > 50) {
            throw new IllegalArgumentException("Too many input files: " + (arguments.size() - counter) + " > 50.");
        }

        int correctInputFilesCounter = 0;
        for (int i = counter; i < arguments.size(); i++) {
            if (arguments.get(counter).contains(".txt")) {
                File inputFile = new File(arguments.get(i));
                if (inputFile.exists() & inputFile.isFile() & inputFile.canRead()) {
                    correctInputFilesCounter++;
                    container.getInputFiles().add(inputFile);
                }
            }
        }

        if ((arguments.size() - counter - correctInputFilesCounter) > 0) {
            System.out.println("List of incorrect files:");
            for (int i = counter; i < arguments.size(); i++) {
                File inputFile = new File(arguments.get(i));
                if (!arguments.get(counter).contains(".txt") || !inputFile.exists() || !inputFile.isFile() || !inputFile.canRead()) {
                    System.out.println("\t" + arguments.get(counter));
                }
            }
            System.out.println((arguments.size() - counter - correctInputFilesCounter) + " files in total.");
        } else if (arguments.size() - counter == 0) {
            throw new FileNotFoundException("Input files not found.");
        } else if (correctInputFilesCounter == 0) {
            throw new IOException("All files are unreadable.");
        }

        System.out.println(correctInputFilesCounter + " files were accepted to sort.");

        return container;
    }
}
