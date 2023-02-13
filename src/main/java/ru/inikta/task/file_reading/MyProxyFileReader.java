package ru.inikta.task.file_reading;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MyProxyFileReader {
    private final FileReader fileReader;
    private Long position;
    private final File file;

    public MyProxyFileReader(File file) throws FileNotFoundException {
        this.file = file;
        this.fileReader = new FileReader(file);
        this.position = 0L;
    }

    public int read() throws IOException {
        position++;
        return fileReader.read();
    }

    public File getFile() {
        return file;
    }

    public Long getPosition() {
        return position;
    }
}
