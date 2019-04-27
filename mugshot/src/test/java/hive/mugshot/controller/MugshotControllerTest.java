package hive.mugshot.controller;

import hive.mugshot.exception.ImageNotFound;
import hive.mugshot.repository.UserRepository;
import hive.mugshot.storage.ImageStorer;
import hive.entity.user.User;

import org.junit.*;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.*;
import java.nio.file.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MugshotControllerTest {

  private MockMvc mockMvc;
  private String validDirectoryName;
  private String username = RandomStringUtils.randomAlphabetic(8);
  private Integer userId = ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
  private MockMultipartFile multipartFile;
  @Value("${hive.mugshot.image-directory-path}")
  private String rootDir;
  @Value("${hive.mugshot.profile-image-name}")
  private String imageName;
  @Mock
  private UserRepository userRepository;
  @Mock
  private ImageStorer imageStorer;
  @Mock
  private User user;

  private Resource createImageForTest(String directoryName) throws Exception{
    var file=new File(directoryName + "/ProfileImage.jpg");
    ImageIO.write(new BufferedImage(512,512,BufferedImage.TYPE_INT_RGB),"jpg",file);
    return new UrlResource(file.toURI());
  }

  private void createDirectoryForTest(String directoryName) throws Exception{
    Files.createDirectories(Paths.get(directoryName));
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    when(user.getId()).thenReturn(userId);
    when(userRepository.findByUsername(username)).thenReturn(user);
    var mugshotController = new MugshotController(imageStorer,userRepository);
    ReflectionTestUtils.setField(mugshotController, "imageName", imageName);
    mockMvc = MockMvcBuilders.standaloneSetup(mugshotController).build();
    validDirectoryName = (rootDir + "/" + user.getId() + "/");
  }

  //SUCCESS:
  @Test
  public void givenValidImage_WhenImageRetrieved_then200andJpegImageTypeIsReturned() throws Exception{
    createDirectoryForTest(validDirectoryName);
    var resourceImage=createImageForTest(validDirectoryName);
    given(imageStorer.loadImage(userId.toString(),imageName))
        .willReturn(resourceImage);
      mockMvc.perform(
          get("/mugshot")
          .header("authenticated-user-name", username))
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.IMAGE_JPEG))
          .andDo(print());
  }

  @Test
  public void givenValidImage_WhenImageUploaded_then200isReturned() throws Exception{
    multipartFile = new MockMultipartFile("image", "Profile Image.jpg", MediaType.IMAGE_JPEG_VALUE, "Spring Framework".getBytes());
    mockMvc.perform(
        multipart("/mugshot").file(multipartFile).header("authenticated-user-name",username))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  public void givenValidImage_WhenImageDeleted_then204isReturned() throws Exception{
    mockMvc.perform(
        delete("/mugshot").header("authenticated-user-name",username)
    ).andExpect(status().isNoContent());
  }

  //ERRORS:
  @Test
  public void givenFileNotFound_WhenImageRetrieved_then404isReturned() throws Exception{
    given(imageStorer.loadImage(userId.toString(),imageName)).willThrow(new ImageNotFound());
    createDirectoryForTest(validDirectoryName);
    mockMvc.perform(
        get("/mugshot")
        .header("authenticated-user-name", username))
        .andExpect(status().isNotFound())
        .andDo(print());
  }

  @Test
  public void givenUnsupportedMediaType_WhenImageUploaded_then415isReturned()throws Exception{
      BufferedImage originalImage = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      ImageIO.write(originalImage, "jpg", byteArrayOutputStream);
      multipartFile = new MockMultipartFile("image", "Unsupported.Extension.wmv", MediaType.APPLICATION_PDF_VALUE, byteArrayOutputStream.toByteArray());
      mockMvc.perform(multipart("/mugshot")
          .file(multipartFile)
          .header("authenticated-user-name", username))
          .andExpect(status().isUnsupportedMediaType())
          .andDo(print());
  }

  @Test
  public void givenWrongBodyRequestKey_WhenImageUploaded_then400isReturned() throws Exception{
      byte[] EmptyFile = new byte[0];
      multipartFile = new MockMultipartFile("wrongBodyKey", "ProfileImage.jpeg", MediaType.IMAGE_JPEG_VALUE, EmptyFile);
      mockMvc.perform(multipart("/mugshot")
          .file(multipartFile).header("authenticated-user-name", username))
          .andExpect(status().isBadRequest())
          .andDo(print());
  }

  @After
  public void deleteCreatedDirectory() throws IOException {
    Path createdDirectoryPath=Paths.get(rootDir,userId.toString());
    Path createdImagePath=createdDirectoryPath.resolve(imageName);
    Files.deleteIfExists(createdImagePath);
    Files.deleteIfExists(createdDirectoryPath);
  }

}
