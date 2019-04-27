package hive.mugshot.controller;

import hive.mugshot.repository.UserRepository;
import hive.mugshot.storage.ImageStorer;
import hive.mugshot.storage.ImageUtils;
import hive.common.security.HiveHeaders;
import hive.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
  @Value("${hive.mugshot.profile-image-name}")
  private String imageName;


  @ResponseStatus(code = HttpStatus.OK, reason = "The server is working")
  @GetMapping("/hey") public void hey(){}
  @Autowired
  public UtilsController(ImageStorer imageStorer,UserRepository userRepository){
    this.userRepository=userRepository;
    this.imageStorer=imageStorer;
    userRepository.save(new User("Test_User","123",0,0));
  }

  @GetMapping(value = "searchImage/{profileImageName:.+}", produces = MediaType.IMAGE_JPEG_VALUE)
  public ResponseEntity<Resource> searchSomeImage(@RequestHeader(name = HiveHeaders.AUTHENTICATED_USER_NAME_HEADER) final String username, @PathVariable String profileImageName) {
    var userID=userRepository.findByUsername(username).getId().toString();
    Resource file = imageStorer.loadImage(username,profileImageName);
    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }

  @ResponseStatus(code = HttpStatus.OK, reason = "Random image generated and successfully stored")
  @PostMapping("/generateRandomImage")
  public void generateRandomImage(@RequestHeader(name = HiveHeaders.AUTHENTICATED_USER_NAME_HEADER) final String username) {
    var generatedImage=ImageUtils.generateRandomImage();
    var userID=userRepository.findByUsername(username).getId();
    imageStorer.storeImageProfile(userID.toString(),generatedImage,imageName);
  }

}
