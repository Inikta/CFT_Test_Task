import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestingGenerators {

    private static final Random randomGenerator = new Random();

    public static final String PATH_PREFIX = "testing/";
    public static final String PATH_PREFIX_INT = PATH_PREFIX + "test_int_files/";
    public static final String PATH_PREFIX_STR = PATH_PREFIX + "test_str_files/";

    public static List<File> createIntegerLongFiles(int amount, long maxLines) throws IOException {
        List<File> files = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            File file = new File(PATH_PREFIX_INT + "int_test_file" + i + ".txt");
            files.add(file);
            file.createNewFile();
            if (maxLines > 0) {
                fillFileLongInt(file, randomGenerator.nextLong(), randomGenerator.nextLong(maxLines));
            } else {
                fillFileLongInt(file, randomGenerator.nextLong(), 0);
            }
        }
        return files;
    }

    public static void deleteIntegerLongFiles(int amount) throws IOException {
        for (int i = 0; i < amount; i++) {
            File file = new File(PATH_PREFIX_INT + "int_test_file" + i + ".txt");
            file.delete();
        }
    }

    private static void fillFileLongInt(File file, long lowerBound, long amount) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        long currentLowerBound = lowerBound;
        for (long i = 0; i < amount; i++) {
            long num = currentLowerBound + randomGenerator.nextLong(500);
            fileWriter.write(Long.toString(num) + "\n");
            currentLowerBound = num;
        }
        fileWriter.close();
    }

    public static List<File> createStringFiles(int amount, long maxLines) throws IOException {
        List<File> files = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            File file = new File(PATH_PREFIX_STR + "str_test_file" + i + ".txt");
            files.add(file);
            file.createNewFile();
            fillFileString(file, randomGenerator.nextLong(maxLines));
        }
        return files;
    }

    private static void fillFileString(File file, long amount) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        for (long i = 0; i < amount; i++) {
            fileWriter.write(stringGenerator((int) i % 94, randomGenerator.nextInt(50)) + "\n");
        }
        fileWriter.close();
    }

    private static String stringGenerator(int charOffset, long length) {
        StringBuilder stringBuilder = new StringBuilder();
        for (long i = 0; i < length; i++) {
            stringBuilder.append(32 + ((charOffset + i) % 94 + randomGenerator.nextInt(94 - charOffset)) % 94);
        }
        return stringBuilder.toString();
    }

    public static String[] filesArgumentsCreator(String order, String type, File outFile, List<File> inFiles) {
        List<String> args = new ArrayList<>();
        args.add(order);
        args.add(type);
        args.add(outFile.getPath());
        args.addAll(inFiles.stream().map(File::getPath).toList());

        return args.toArray(new String[args.size()]);
    }
}
