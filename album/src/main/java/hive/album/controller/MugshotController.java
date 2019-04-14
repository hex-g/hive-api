package hive.album.controller;

import hive.album.entity.ProfileImage;
import hive.album.exception.ImageNotFound;
import hive.album.repository.UserRepository;
import hive.album.storage.ImageStorer;
import hive.common.security.HiveHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

@RestController
@RequestMapping("/")
public class MugshotController {
  private final UserRepository userRepository;
  @Value("${hive.mugshot.image-directory-path}")
  private String rootDir;
  @Autowired
  private ImageStorer imageStorer;
  @ResponseStatus(code = HttpStatus.I_AM_A_TEAPOT, reason = "The server is working")
  @GetMapping("/hey")
  public void hey() {}
  @GetMapping("/dir")
  public String getRootDirectory() {
    return rootDir;
  }
  @Autowired
  public MugshotController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }
  //tratar multiplos arquivos excecao
  //tratar replace de imagens
  //tratar replace de imagem não proprietaria
  //tratar renomeacao de imagem
  @PostMapping("smugshot")
  public String sendImageProfile
      ( @RequestParam("image") MultipartFile insertedImage,
        @RequestHeader(name = HiveHeaders.AUTHENTICATED_USER_NAME_HEADER) final String username) {
    return imageStorer.StoreImageProfile(insertedImage);
  }
  @GetMapping(value="smugshot/{profileImageName:.+}",produces = MediaType.IMAGE_JPEG_VALUE)
  public ResponseEntity<Resource> searchProfileImage
      (@RequestHeader(name = HiveHeaders.AUTHENTICATED_USER_NAME_HEADER) final String username,
       @PathVariable String profileImageName){
    var user = userRepository.findByUsername(username);
    Resource file = imageStorer.loadImage(profileImageName);
    return ResponseEntity.ok().header(
        HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }
}
