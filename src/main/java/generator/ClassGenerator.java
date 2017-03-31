package generator;

import org.apache.commons.io.IOUtils;

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
      if (is == null)
        throw new RuntimeException("template stream is null");
      return IOUtils.toString(is, StandardCharsets.UTF_8);
    }
  }

  private static InputStream getStream() {
    // TODO: return a stream to template.txt
    // do not use java.io.FileInputStream or java.nio.file.Files
    // create and test two solutions:
    // 1) using ClassLoader#getResourceAsStream
    // 2) using Class#getResourceAsStream
    return null;
  }
}
