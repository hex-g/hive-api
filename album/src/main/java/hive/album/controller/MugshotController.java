package hive.album.controller;

import hive.album.exception.UserNotFoundException;
import hive.album.repository.UserRepository;
import hive.album.storage.ImageStorer;
import hive.common.security.HiveHeaders;
import hive.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

@RestController
@RequestMapping("/smugshot")
public class MugshotController {

  @Value("${hive.mugshot.image-directory-path}")
  private String rootDir;

  private final ImageStorer imageStorer;
  private final UserRepository userRepository;

  @Autowired public MugshotController(UserRepository userRepository,ImageStorer imageStorer) {
    this.imageStorer=imageStorer;
    this.userRepository = userRepository;
    userRepository.save(new User("TESTE","123",0,0));
    userRepository.save(new User("GERMANO","123",0,0));
    userRepository.save(new User("JAVA","123",0,0));
  }

  @ResponseStatus(code = HttpStatus.I_AM_A_TEAPOT, reason = "The server is working and have an user")
  @GetMapping("/hey") public void hey(){}

  @GetMapping("/dir") public String getRootDirectory() {
    return rootDir;
  }
  @GetMapping("/user") public String getUser
      (@RequestHeader(name = HiveHeaders.AUTHENTICATED_USER_NAME_HEADER) final String username)
  {
    var user=userRepository.findByUsername(username);
    return user.getUsername();
  }
  //tratar multiplos arquivos excecao
  //tratar replace de imagens
  //tratar replace de imagem não proprietaria
  //tratar renomeacao de imagem
  @PostMapping
  public String sendImageProfile(@RequestParam("image") MultipartFile insertedImage, @RequestHeader(name = HiveHeaders.AUTHENTICATED_USER_NAME_HEADER) final String username) {
    var user = userRepository.findByUsername(username);
    if(user==null){
      throw new UserNotFoundException();
    }
    return imageStorer.StoreImageProfile(username,insertedImage);
  }

  @GetMapping(value = "/{profileImageName:.+}", produces = MediaType.IMAGE_JPEG_VALUE)
  public ResponseEntity<Resource> searchProfileImage(@RequestHeader(name = HiveHeaders.AUTHENTICATED_USER_NAME_HEADER) final String username, @PathVariable String profileImageName) {
    var user = userRepository.findByUsername(username);
    if(user==null){
      throw new UserNotFoundException();
    }
    Resource file = imageStorer.loadImage(username,profileImageName);
    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }

  @DeleteMapping
  public void deleteProfileImage(@RequestHeader(name = HiveHeaders.AUTHENTICATED_USER_NAME_HEADER) final String username) {
    var user = userRepository.findByUsername(username);
    if(user==null){
      throw new UserNotFoundException();
    }
    imageStorer.deleteAllUserImages(username);
  }
}
