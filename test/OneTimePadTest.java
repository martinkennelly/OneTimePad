import org.onetimepad.OneTimePad;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.io.File;
import java.io.IOException;

class OneTimePadTest {
    public static File textFile = new File("./testfile" + System.currentTimeMillis() + ".txt");
    public static File textFileNoExt = new File("./testfile" + System.currentTimeMillis());

    @Test
    @DisplayName("Setting up environment to test method checkFile")
    @BeforeAll
    public static void createTextFile() throws IOException {
        textFile.createNewFile();
        if (!textFile.exists()) {
            throw new RuntimeException("Failed to create file");
        }
    }

    @Test
    @DisplayName("Check if text file exists when it does infact exist")
    public void givenTextFileWhenItExistsThenTrue() {
        OneTimePad otp = new OneTimePad();
        assertTrue(otp.checkFile(textFile.getName()));
    }

    @Test
    @DisplayName("Check null parameter to checkFile")
    public void givenStringParameterWhenNullThenFalse() {
        OneTimePad otp = new OneTimePad();
        assertFalse(otp.checkFile(null));
    }

    @Test
    @DisplayName("Check file path that doesnt exist")
    public void givenStringPathWhenPathFileDoesntExistThenFalse() {
        OneTimePad otp = new OneTimePad();
        String fileThatDoesntExist = new String(System.currentTimeMillis() + ".txt");
        File file = new File(fileThatDoesntExist);
        if (file.exists()) {
            throw new RuntimeException();
        }
        assertFalse(otp.checkFile(fileThatDoesntExist));
    }

    @Test
    @DisplayName("Check file with no dot txt extention")
    public void givenFileWhenNoTxtExtThenFalse() {
        OneTimePad otp = new OneTimePad();
        assertFalse(otp.checkFile(textFileNoExt.getName()));
    }

    @AfterAll
    public static void cleanUp() {
        textFile.delete();
        textFileNoExt.delete();
    }
}