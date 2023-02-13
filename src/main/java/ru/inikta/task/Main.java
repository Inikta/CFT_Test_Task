package ru.inikta.task;

import com.sun.jdi.InvalidTypeException;
import ru.inikta.task.arguments_parser.ArgumentsContainer;
import ru.inikta.task.arguments_parser.CommandLineArgsParser;
import ru.inikta.task.file_reading.FilesChunkReader;
import ru.inikta.task.sort.MergeSorter;
import ru.inikta.task.sort.MergeSorterFabric;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.sql.Time;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class Main {
    /**
     * Entry point of the program. Performs arguments reading, program entities initialization, sorting calling and writing to file.
     * @param args command line arguments.
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, InvalidTypeException {
        ArgumentsContainer container;
        try {
            container = CommandLineArgsParser.parse(Arrays.stream(args).toList());
        } catch (IllegalArgumentException | IOException exception) {
            System.out.println("Sorting failed! Cause: " + exception.getMessage());
            return;
        }

        FileOutputStream fileWriter = new FileOutputStream(container.getOutputFile());

        MergeSorter mergeSorter;
        try {
            mergeSorter = new MergeSorterFabric().createMergeSorter(container.getDataType(), container.getOrder());
        } catch (InvalidClassException exception) {
            System.out.println("Sorting failed! Cause: " + exception.getMessage());
            return;
        }

        FilesChunkReader filesChunkReader = new FilesChunkReader(container.getInputFiles(), container.getDataType());

        //read chunks of lines from files and sort them one by one, while there data to read is left
        //assuming that data is sorted in each file, the final compilation of chunks will be sorted correctly too
        long startTime = System.currentTimeMillis();
        List<String> chunkLines;
        try {
            chunkLines = filesChunkReader.readChunkLines();
        } catch (IOException | InvalidTypeException exception) {
            System.out.println("Sorting failed! Cause: " + exception.getMessage());
            return;
        }
        while (!chunkLines.isEmpty()) {
            List<String> chunkResult = mergeSorter.sort(chunkLines);
            for (String line : chunkResult) {
                fileWriter.write((line + "\n").getBytes());
            }
            chunkLines = filesChunkReader.readChunkLines();
        }
        fileWriter.close();
        long endTime = System.currentTimeMillis();
        System.out.println("Sorting complete! Time passed (milliseconds): " + (endTime - startTime));
    }
}