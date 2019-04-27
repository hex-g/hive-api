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

  @Autowired
  public UtilsController(ImageStorer imageStorer,UserRepository userRepository){
    this.userRepository=userRepository;
    this.imageStorer=imageStorer;
  }

  @ResponseStatus(code = HttpStatus.OK, reason = "Random image generated and successfully stored")
  @PostMapping("/generateRandomImage")
  public void generateRandomImage(@RequestHeader(name = HiveHeaders.AUTHENTICATED_USER_NAME_HEADER) final String username) {
    var generatedImage=ImageUtils.generateRandomImage();
    var userID=userRepository.findByUsername(username).getId();
    imageStorer.storeImageProfile(userID.toString(),generatedImage,imageName);
  }

}
