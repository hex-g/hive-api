package hive.mugshot.storage;

import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ThreadLocalRandom;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ImageStorerTest {
  private Integer userId;
  private MockMultipartFile multipartFile;
  @Autowired
  private ImageStorer imageStorer;
  @Value("${hive.mugshot.image-directory-path}")
  private String rootDir;
  @Value("${hive.mugshot.profile-image-name}")
  private String imageName;
  @Value("${hive.mugshot.profile-image-dimension}")
  private int imageSizeInPixels;


  @Before
  public void setUp(){
    userId= ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
    multipartFile=new MockMultipartFile(
        "image",
        "WhateverName.gif",
        MediaType.IMAGE_JPEG_VALUE,
        "Spring Framework".getBytes()
    );
  }

  @Test
  public void uploadImage_WhenImageNameAndUserDirectoryIsNotNull_expectExistenceOfFile(){
    imageStorer.storeImageProfile(userId.toString(),multipartFile,imageName);
    Path path=Paths.get(rootDir,userId.toString(),imageName);
    Assert.assertTrue(Files.exists(path));
  }

  @Test
  public void deleteImage_WhenImageNameAndUserDirectoryIsNotNull_expectNoExistenceOfFile(){
    imageStorer.deleteImage(userId.toString(),imageName);
    Path path=Paths.get(rootDir,userId.toString(),imageName);
    Assert.assertFalse(Files.exists(path));
  }

  @Test
  public void loadImage_WhenMultipartIsNotNull_expectConfiguredImageName() throws IOException {
    imageStorer.storeImageProfile(userId.toString(),multipartFile,imageName);
    var resource=imageStorer.loadImage(userId.toString(),imageName);
    Assert.assertEquals(imageName,resource.getFile().getName());
  }

  @Test
  public void loadImage_WhenMultipartIsNotNull_expectConfiguredImageSize() throws IOException {
    imageStorer.storeImageProfile(userId.toString(),multipartFile,imageName);
    var resource=imageStorer.loadImage(userId.toString(),imageName);
    var image=ImageIO.read(resource.getFile());
    Assert.assertEquals(imageSizeInPixels,image.getHeight());
    Assert.assertEquals(imageSizeInPixels,image.getWidth());
  }

  @After
  public void deleteCreatedDirectoryAndFile() throws IOException {
    Path createdDirectoryPath=Paths.get(rootDir,userId.toString());
    Path createdImagePath=createdDirectoryPath.resolve(imageName);
    Files.deleteIfExists(createdImagePath);
    Files.deleteIfExists(createdDirectoryPath);
  }

}


