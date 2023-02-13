package ru.inikta.task.file_reading;

import com.sun.jdi.InvalidTypeException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilesChunkReader {
    //chunk sizes in bytes
    public static final long CHUNK_1KB = 1024;
    public static final long CHUNK_1MB = 1024 * CHUNK_1KB;
    public static final long CHUNK_512MB = 512 * CHUNK_1MB;

    //file readers with current position counter
    private final List<MyProxyFileReader> fileReaders;

    //marks readers, which files ended as completed
    private final Map<MyProxyFileReader, Boolean> completedFileReaders;
    private final long chunkSize;
    private long counter = 0;
    private final Type dataType;

    /**
     * Reads data from files in separate file readers and adds it to list of files lines as strings.<br>
     * Amount of data read from files is restricted by automatically calculated chunk size, which is less or equal to current free memory size.
     *
     * @param files files to read data from
     * @throws FileNotFoundException
     */
    public FilesChunkReader(List<File> files, Type dataType) throws FileNotFoundException {
        //compute size of chunk to read from files
        this.chunkSize = computeChunkSize(Runtime.getRuntime().freeMemory(), CHUNK_512MB);
        this.fileReaders = new ArrayList<>();
        this.dataType = dataType;

        for (File file : files) {
            this.fileReaders.add(new MyProxyFileReader(file));
        }

        //mark readers as not completed their reading process (file has not ended yet)
        this.completedFileReaders = new HashMap<>();
        for (MyProxyFileReader myProxyFileReader : fileReaders) {
            completedFileReaders.put(myProxyFileReader, false);
        }
    }

    /**
     * Read chunk of data from files line by line from each file, skipping those, which have whitespaces or tabs in them.<br>
     *
     * @return list of mixed lines from files
     * @throws IOException
     */
    public List<String> readChunkLines() throws IOException, InvalidTypeException {
        List<String> chunkLines = new ArrayList<>();

        //read while chunk is not fulfilled or all files were not read out
        while (counter < chunkSize & completedFileReaders.containsValue(false)) {
            for (MyProxyFileReader myProxyFileReader : fileReaders) {
                //skip file if it is already fully read
                if (completedFileReaders.get(myProxyFileReader)) {
                    continue;
                }

                StringBuilder stringBuilder = new StringBuilder();
                boolean stringIsCorrect = true;

                //read character by character and check if file has ended
                //mark file accordingly
                int characterInt = myProxyFileReader.read();
                if (characterInt == -1) {
                    if (!completedFileReaders.get(myProxyFileReader)) {
                        completedFileReaders.put(myProxyFileReader, true);
                    }
                    continue;
                }

                //read line from file skipping the lines with whitespaces and tabs in them
                if (!completedFileReaders.get(myProxyFileReader)) {
                    while (characterInt != '\n' & characterInt != -1) {
                        if (characterInt == ' ' || characterInt == '\t') {
                            stringIsCorrect = false;
                        }

                        if (dataType.equals(Long.class)
                                & !Character.isDigit(characterInt)
                                & characterInt != '-'
                                & characterInt != '+'
                                & characterInt != '\r') {
                            throw new InvalidTypeException(
                                    "Wrong type of data in file.\n" +
                                            "File: " + myProxyFileReader.getFile().getName() + "\n" +
                                            "Character position: " + myProxyFileReader.getPosition() + "\n" +
                                            "Character: " + (char) characterInt);
                        }

                        stringBuilder.append((char) characterInt);
                        characterInt = myProxyFileReader.read();

                        counter++;
                        if (characterInt == -1) {
                            completedFileReaders.put(myProxyFileReader, true);
                        }
                    }

                    //trim line from '\r' and other unwanted characters
                    if (stringIsCorrect) {
                        chunkLines.add(stringBuilder.toString().trim());
                    }
                }
            }
        }

        return chunkLines;
    }

    /**
     * Compute data chunk size, which would be read from files.<br>
     * Divides chunk by 2 until it fits in current free memory.
     *
     * @param freeMemoryByteSize  current free memory size in bytes
     * @param predefinedChunkSize initial chunk size in bytes
     * @return chunk size
     */
    private long computeChunkSize(long freeMemoryByteSize, long predefinedChunkSize) {
        if (freeMemoryByteSize / predefinedChunkSize <= 1) {
            return computeChunkSize(freeMemoryByteSize, predefinedChunkSize / 2);
        } else {
            return predefinedChunkSize;
        }
    }
}
