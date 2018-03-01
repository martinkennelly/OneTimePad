import org.onetimepad.OneTimePad;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;

class OneTimePadTest {
    public static File textFile = new File("./testfile" + System.currentTimeMillis() + ".txt");
    public static File textFileNoExt = new File("./testfile" + System.currentTimeMillis());
    public static File textFileWrongExt = new File("./testfile" + System.currentTimeMillis() + ".txtt");

    @Test
    @DisplayName("Setting up environment to test")
    @BeforeAll
    public static void createTextFile() throws IOException {
        textFile.createNewFile();
        textFileNoExt.createNewFile();
        textFileWrongExt.createNewFile();
        if (!textFile.exists() || !textFileWrongExt.exists() || !textFileNoExt.exists()) {
            throw new RuntimeException("Failed to create file");
        }
    }

    @Test
    @DisplayName("Check if text file exists when it does infact exist")
    public void givenTextFileWhenItExistsThenTrue() {
        assertTrue(OneTimePad.checkFile(textFile.getName()));
    }

    @Test
    @DisplayName("Check null parameter to checkFile")
    public void givenStringParameterWhenNullThenFalse() {
        assertFalse(OneTimePad.checkFile(null));
    }

    @Test
    @DisplayName("Check file path that doesnt exist")
    public void givenStringPathWhenPathFileDoesntExistThenFalse() {
        String fileThatDoesntExist = new String(System.currentTimeMillis() + ".txt");
        File file = new File(fileThatDoesntExist);
        if (file.exists()) {
            throw new RuntimeException();
        }
        assertFalse(OneTimePad.checkFile(fileThatDoesntExist));
    }

    @Test
    @DisplayName("Check file with no dot txt extention")
    public void givenFileWhenNoTxtExtThenFalse() {
        assertFalse(OneTimePad.checkFile(textFileNoExt.getName()));
    }

    @Test
    @DisplayName("Check file with incorrect extention (.txtt)")
    public void givenFileWhenExtIncorrectThenFalse() {
        assertFalse(OneTimePad.checkFile(textFileWrongExt.getName()));
    }

    @Test
    @DisplayName("Check generateKey output key length")
    public void givenInputLengthXWhenOutputLengthEqualsXThenTrue() {
        int keyLength = 5;
        assertTrue(keyLength == OneTimePad.generateKey(keyLength).length());
    }

    @Test
    @DisplayName("Check generateKey output key length when zero")
    public void givenInputLengthZeroWhenOuputLengthZeroThenTrue() {
        int keyLength = 0;
        assertTrue(keyLength == OneTimePad.generateKey(keyLength).length());
    }

    @Test
    @DisplayName("Check generateKey output key length when negative is zero")
    public void givenInputLengthNegativeWhenOuputLengthZeroThenTrue() {
        int keyLength = -1;
        assertTrue(0 == OneTimePad.generateKey(keyLength).length());
    }

    @Test
    @DisplayName("Check generateKey key generation uniqueness")
    public void givenMultpleCallsWhenLengthIsConstantThenKeysUnique() {
        //amountCharacters ^ length is the possible amount of unique strings. 26 unique characters
        //available in method generate key therefore 26 ^ length of possible unique keys.
        final int iterations = 5000000;
        final int keyLength = 10;        //~141167095653376 possible unique key strings.
        HashSet<String> keyBox = new HashSet<String>();

        for (int i = 0; i < iterations; i++) {
            keyBox.add(OneTimePad.generateKey(keyLength));
        }
        System.out.println(keyBox.size());
        assertTrue(iterations == keyBox.size());
    }



    @AfterAll
    @DisplayName("Clean up after test cases")
    public static void cleanUp() {
        textFile.delete();
        textFileNoExt.delete();
        textFileWrongExt.delete();
    }
}