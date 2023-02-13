import com.sun.jdi.InvalidTypeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.inikta.task.Main;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class SortingTests {

    public static final String PATH_PREFIX_OUT = "testing/test_results/";

    private boolean ascendingSortingTester(File outfile) throws IOException {
        Scanner scanner = new Scanner(outfile);
        long prevNum = Long.MIN_VALUE;
        while (scanner.hasNextLong()) {
            long num = scanner.nextLong();
            if (num < prevNum) {
                return false;
            }
            prevNum = num;
        }
        return true;
    }

    @Test
    public void emptyFilesTest() throws IOException, InvalidTypeException {
        File outFile = new File(PATH_PREFIX_OUT + "emptyFilesTest_out.txt");
        outFile.createNewFile();
        Main.main(
                TestingGenerators.filesArgumentsCreator(
                        "-a",
                        "-i",
                        outFile,
                        TestingGenerators
                                .createIntegerLongFiles(3, 0)));
        Assertions.assertTrue(ascendingSortingTester(outFile));
        //outFile.delete();
        //TestingGenerators.deleteIntegerLongFiles(3);
    }

    @Test
    public void intFilesTest() throws IOException, InvalidTypeException {
        File outFile = new File(PATH_PREFIX_OUT + "intFilesTest_out.txt");
        outFile.createNewFile();
        Main.main(
                TestingGenerators.filesArgumentsCreator(
                        "-a",
                        "-i",
                        outFile,
                        TestingGenerators
                                .createIntegerLongFiles(5, 100)));
        Assertions.assertTrue(ascendingSortingTester(outFile));
        //outFile.delete();
        //TestingGenerators.deleteIntegerLongFiles(5);
    }

    @Test
    public void longIntFilesTest() throws IOException, InvalidTypeException {
        File outFile = new File(PATH_PREFIX_OUT + "longIntFilesTest_out.txt");
        outFile.createNewFile();
        Main.main(
                TestingGenerators.filesArgumentsCreator(
                        "-a",
                        "-i",
                        outFile,
                        TestingGenerators
                                .createIntegerLongFiles(3, 100000)));
        Assertions.assertTrue(ascendingSortingTester(outFile));
        //outFile.delete();
        //TestingGenerators.deleteIntegerLongFiles(3);
    }
}
