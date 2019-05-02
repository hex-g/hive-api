package hive.mugshot.controller;

import hive.mugshot.storage.ImageStorer;
import hive.mugshot.storage.ImageUtils;
import hive.common.security.HiveHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/utils")
public class UtilsController {

  private final ImageStorer imageStorer;
  @Value("${hive.mugshot.profile-image-name}")
  private String imageName;

  @Autowired
  public UtilsController(ImageStorer imageStorer){
    this.imageStorer=imageStorer;
  }

  @ResponseStatus(code = HttpStatus.OK, reason = "Random image generated and successfully stored")
  @PostMapping("/generateRandomImage")
  public void generateRandomImage(@RequestHeader(name = HiveHeaders.AUTHENTICATED_USER_ID) final String userId) {
    var generatedImage=ImageUtils.generateRandomImage();
    imageStorer.storeImageProfile(userId,generatedImage,imageName);
  }

}
