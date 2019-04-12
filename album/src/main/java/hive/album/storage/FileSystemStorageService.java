package hive.album.storage;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageService implements iStorageService{
  @Value("${hive.mugshot.image-directory-path}")
  private String imageDirectoryPathAsString;
  private Path imageDirectoryPath;
  //@Autowired
  public FileSystemStorageService() {
    this.imageDirectoryPath = Paths.get(imageDirectoryPathAsString);
  }

  @Override
  public void store(MultipartFile file) {
    String filename = StringUtils.cleanPath(file.getOriginalFilename());
    try {
      if (file.isEmpty()) {
        //throw new StorageException("Failed to store empty file " + filename);
        throw new RuntimeException();
      }
      if (filename.contains("..")) {
        // This is a security check
        /*throw new StorageException(
            "Cannot store file with relative path outside current directory "
                + filename);*/
        throw new RuntimeException();
      }
      try (InputStream inputStream = file.getInputStream()) {
        Files.copy(inputStream, this.imageDirectoryPath.resolve(filename),
            StandardCopyOption.REPLACE_EXISTING);
      }
    }
    catch (IOException e) {
      //throw new StorageException("Failed to store file " + filename, e);
      throw new RuntimeException("Failed to store file " + filename, e);
    }
  }

  @Override
  public Stream<Path> loadAll() {
    try {
      return Files.walk(this.imageDirectoryPath, 1)
          .filter(path -> !path.equals(this.imageDirectoryPath))
          .map(this.imageDirectoryPath::relativize);
    }
    catch (IOException e) {
      //throw new StorageException("Failed to read stored files", e);
      throw new RuntimeException("Failed to read stored files", e);
    }

  }

  @Override
  public Path load(String filename) {
    return imageDirectoryPath.resolve(filename);
  }

  @Override
  public Resource loadAsResource(String filename) {
    try {
      Path file = load(filename);
      Resource resource = new UrlResource(file.toUri());
      if (resource.exists() || resource.isReadable()) {
        return resource;
      }
      else {
        throw new RuntimeException("Could not read file: " + filename);

      }
    }
    catch (MalformedURLException e) {
      throw new RuntimeException("Could not read file: " + filename, e);
    }
  }

  @Override
  public void deleteAll() {
    FileSystemUtils.deleteRecursively(imageDirectoryPath.toFile());
  }

  @Override
  public void init() {
    try {
      Files.createDirectories(imageDirectoryPath);
    }
    catch (IOException e) {
      throw new RuntimeException("Could not initialize storage", e);
    }
  }
}