package hive.mugshot.controller;

import hive.mugshot.exception.NotAcceptedFileFormatException;
import hive.mugshot.repository.UserRepository;
import hive.mugshot.storage.ImageStorer;
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

import static hive.mugshot.storage.ImageUtils.validateIfHasAnImageAsExtension;

@RestController
@RequestMapping("/")
public class MugshotController {

  @Value("${hive.mugshot.profile-image-name}")
  private String imageName;
  private final ImageStorer imageStorer;
  private final UserRepository userRepository;

  @Autowired
  public MugshotController(ImageStorer imageStorer,UserRepository userRepository) {
    this.userRepository = userRepository;
    this.imageStorer = imageStorer;
  }

  @ResponseStatus(code = HttpStatus.OK, reason = "Profile image successfully stored")
  @PostMapping
  public void sendImageProfile(
      @RequestParam("image") MultipartFile insertedImage,
      @RequestHeader(name = HiveHeaders.AUTHENTICATED_USER_NAME_HEADER) final String username
  ){
    if(!validateIfHasAnImageAsExtension(insertedImage.getOriginalFilename())) {
      throw new NotAcceptedFileFormatException();
    }
    var userID=userRepository.findByUsername(username).getId().toString();
    imageStorer.storeImageProfile(userID,insertedImage,imageName);
  }

  @GetMapping(produces = MediaType.IMAGE_JPEG_VALUE)
  public ResponseEntity<Resource> searchProfileImage(
      @RequestHeader(name = HiveHeaders.AUTHENTICATED_USER_NAME_HEADER) final String username
  ){
    var userID=userRepository.findByUsername(username).getId().toString();
    Resource file = imageStorer.loadImage(userID,imageName);
    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }

  @ResponseStatus(code = HttpStatus.NO_CONTENT, reason = "Profile image successfully deleted")
  @DeleteMapping
  public void deleteProfileImage(
      @RequestHeader(name = HiveHeaders.AUTHENTICATED_USER_NAME_HEADER) final String username
  ){
    var userID=userRepository.findByUsername(username).getId().toString();
    imageStorer.deleteImage(userID,imageName);
  }

}
