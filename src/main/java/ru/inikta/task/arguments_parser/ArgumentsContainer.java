package ru.inikta.task.arguments_parser;

import ru.inikta.task.sort.SortingOrder;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;

public class ArgumentsContainer {
    private SortingOrder order;
    private Type dataType;
    private File outputFile;
    private List<File> inputFiles;

    public SortingOrder getOrder() {
        return order;
    }

    public void setOrder(SortingOrder order) {
        this.order = order;
    }

    public Type getDataType() {
        return dataType;
    }

    public void setDataType(Type dataType) {
        this.dataType = dataType;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    public List<File> getInputFiles() {
        return inputFiles;
    }

    public void setInputFiles(List<File> inputFiles) {
        this.inputFiles = inputFiles;
    }
}
