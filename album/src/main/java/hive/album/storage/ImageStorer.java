package hive.album.storage;

import hive.album.exception.ImageAlreadyExistException;
import hive.album.exception.ImageProfileException;
import hive.album.exception.InvalidPathException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageStorer {
  @Value("${hive.mugshot.image-directory-path}")
  private String rootDir;
  private Path resolvePathFullUrlWith(String filename) {
    return Paths.get(rootDir).resolve(filename);
  }
  public String StoreImageProfile(MultipartFile insertedImage){
    this.createDirectoryIfNotExist();
    try {
      Files.copy( insertedImage.getInputStream(),
                  Paths.get(rootDir).resolve(insertedImage.getOriginalFilename()));
    }catch(FileAlreadyExistsException e){
      throw new ImageAlreadyExistException();
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
    return "Image Stored in Success";
  }
  private void createDirectoryIfNotExist(){
    Path parentDir = Paths.get(rootDir);
    if (!Files.exists(parentDir)) {
      try {
        Files.createDirectories(parentDir);
      } catch (IOException e) {
        throw new RuntimeException("Not capable to create the directory.\n"+e);
      }
    }
  }
  public Resource loadImage(String ImageName) {
    try {
      Path file = resolvePathFullUrlWith(ImageName);
      Resource resource = new UrlResource(file.toUri());
      if (resource.exists() || resource.isReadable()) {
        return resource;
      }
    } catch (MalformedURLException e) {
      e.printStackTrace();
      throw new InvalidPathException();
    }
    return null;
  }
}
