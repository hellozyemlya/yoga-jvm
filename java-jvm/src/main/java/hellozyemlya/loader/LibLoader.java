package hellozyemlya.loader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

public class LibLoader {
  private static final String  cacheRoot = String.format("%s/.yoga-jvm", System.getProperty("user.home"));

  public static void load(String libName) {
    String libraryFilename = System.mapLibraryName(libName);
    String hash = readResourceHash("/" + libraryFilename);
    Path cachedLibrary = Paths.get(cacheRoot, hash, libraryFilename);
    if(!cachedLibrary.toFile().isFile()) {
      writeResourceToPath(cachedLibrary, "/" + libraryFilename);
    }
    System.load(cachedLibrary.toString());
  }

  private static String readResourceHash(String resourcePath) {
    return readResourceAsString(resourcePath + ".sha256").strip();
  }

  private static InputStream openResourceStream(String resourcePath) {
    return Objects.requireNonNull(LibLoader.class.getResourceAsStream(resourcePath));
  }

  private static String readResourceAsString(String resourcePath) {
    try {
      try(InputStream stream = openResourceStream(resourcePath)) {
        return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed to read resource as string", e);
    }
  }

  private static void writeResourceToPath(Path destination, String resourcePath) {
    destination.getParent().toFile().mkdirs();

    try(InputStream resourceStream = openResourceStream(resourcePath)) {
      try(OutputStream outputStream = Files.newOutputStream(destination, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
        resourceStream.transferTo(outputStream);
        outputStream.flush();
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
