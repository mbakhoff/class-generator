package generator;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class ClassGenerator {

  private static final Random rng = new Random();

  public static void main(String[] args) throws Exception {
    String[] classNames = { "Külmkapp", "Säilik", "Toiduaine", "Roog" };
    String[] fieldTypes = { "List<Roog>", "String", "Säilik", "int" };
    String[] fieldNames = { "nimi", "säilivus", "sisu", "koostisained", "vajabTähelepanu" };

    String source = readTemplate();
    source = source.replace("${className}", pickRandom(classNames));
    source = source.replace("${field1Type}", pickRandom(fieldTypes));
    source = source.replace("${field1Name}", pickRandom(fieldNames));
    source = source.replace("${field2Type}", pickRandom(fieldTypes));
    source = source.replace("${field2Name}", pickRandom(fieldNames));
    System.out.println(source);
  }

  private static String pickRandom(String[] choices) {
    return choices[rng.nextInt(choices.length)];
  }

  private static String readTemplate() throws IOException {
    try (InputStream is = getStream()) {
      if (is == null) {
        // classloader didn't find the file. look inside the jar
        // and make sure the path in getStream() is correct.
        throw new RuntimeException("stream is null");
      }
      return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }
  }

  private static InputStream getStream() {
    // TODO: return a stream to template.txt
    // do not use java.io.FileInputStream or java.nio.file.Files
    // use getResourceAsStream from the ClassLoader that loaded ClassGenerator
    return null;
  }
}
