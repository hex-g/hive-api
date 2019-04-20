package hive.album.controller;

import hive.album.repository.UserRepository;
import hive.album.storage.ImageStorer;
import hive.common.security.HiveHeaders;
import hive.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/utils")
public class UtilsController {
  private final UserRepository userRepository;
  private final ImageStorer imageStorer;
  @ResponseStatus(code = HttpStatus.OK, reason = "The server is working and have an user")
  @GetMapping("/hey") public void hey(){}
  @GetMapping("/dir") public String getRootDirectory() {
    return "nothing";
  }
  @Autowired public UtilsController(UserRepository userRepository,ImageStorer imageStorer){
    this.userRepository=userRepository;
    this.imageStorer=imageStorer;
    userRepository.save(new User("User","123",0,0));
    userRepository.save(new User("Germano","123",0,0));
    userRepository.save(new User("Java","123",0,0));
    userRepository.save(new User("com espaço","123",0,0));
    userRepository.save(new User("com/barra","123",0,0));
    userRepository.save(new User("愛","123",0,0));
  }
  @GetMapping("/user") public User getUser
      (@RequestHeader(name = HiveHeaders.AUTHENTICATED_USER_NAME_HEADER) final String username)
  {
    return userRepository.findByUsername(username);
  }
  @GetMapping("/users") public Iterable<User> getAllUser()
  {
    return userRepository.findAll();
  }
  @GetMapping(value = "searchImage/{profileImageName:.+}", produces = MediaType.IMAGE_JPEG_VALUE)
  public ResponseEntity<Resource> searchSomeImage(@RequestHeader(name = HiveHeaders.AUTHENTICATED_USER_NAME_HEADER) final String username, @PathVariable String profileImageName) {
    var userID=userRepository.findByUsername(username).getId().toString();
    Resource file = imageStorer.loadImage(username,profileImageName);
    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }
}
