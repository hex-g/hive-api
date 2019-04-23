package hive.album.controller;

import hive.album.exception.ImageNotFound;
import hive.album.repository.UserRepository;
import hive.album.storage.ImageStorer;
import hive.entity.user.User;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MugshotControllerTest {
  @Value("${hive.mugshot.image-directory-path}")
  private String rootDir;
  private MockMvc mockMvc;
  private String imageName="ProfileImage.jpg";
  private String validDirectoryName;
  @Mock
  private UserRepository userRepository;
  @Autowired
  private ImageStorer imageStorer;
  @Mock
  private User user;
  private String username = RandomStringUtils.randomAlphabetic(8);
  private Integer userId = ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
  private MockMultipartFile multipartFile;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    when(user.getId()).thenReturn(userId);
    when(userRepository.findByUsername(username)).thenReturn(user);
    var mugshotController = new MugshotController(imageStorer,userRepository);
    ReflectionTestUtils.setField(mugshotController, "rootDir", rootDir);
    mockMvc = MockMvcBuilders.standaloneSetup(mugshotController).build();
    validDirectoryName = (rootDir + "/" + user.getId() + "/");
  }

  private void createImageForTest(String directoryName) throws Exception{
    var file=new File(directoryName + "/ProfileImage.jpg");
    ImageIO.write(new BufferedImage(512,512,BufferedImage.TYPE_INT_RGB),"jpg",file);
  }
  private void createDirectoryForTest(String directoryName) throws Exception{
      Files.createDirectories(Paths.get(directoryName));
  }
  private void deleteTestDirectory(String directoryName) throws Exception{
      Files.delete(Paths.get(directoryName));
  }
  private void deleteTestImage(String directoryName) throws Exception{
    Files.delete(Paths.get(directoryName+"/ProfileImage.jpg"));
  }
  //SUCCESS:
  @Test
  public void givenValidImage_WhenImageRetrieved_then200andJpegImageTypeIsReturned() throws Exception{
    createDirectoryForTest(validDirectoryName);
    createImageForTest(validDirectoryName);
    try {
      mockMvc.perform(
          get("/mugshot")
          .header("authenticated-user-name", username))
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.IMAGE_JPEG))
          .andDo(print());
    }finally {
      deleteTestImage(validDirectoryName);
      deleteTestDirectory(validDirectoryName);
    }
  }
  @Test
  public void givenValidImage_WhenImageUploaded_then200isReturned() throws Exception{
    multipartFile = new MockMultipartFile("image", "Profile Image.jpg", MediaType.IMAGE_JPEG_VALUE, "Spring Framework".getBytes());
    mockMvc.perform(
        multipart("/mugshot").file(multipartFile).header("authenticated-user-name",username))
        .andExpect(status().isOk())
        .andDo(print());
    deleteTestImage(validDirectoryName);
    deleteTestDirectory(validDirectoryName);
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
    createDirectoryForTest(validDirectoryName);
    try {
      mockMvc.perform(
          get("/mugshot")
              .header("authenticated-user-name", username))
          .andExpect(status().isNotFound())
          .andDo(print());
    }finally {
      deleteTestDirectory(validDirectoryName);
    }
  }
  @Test
  public void givenUnsupportedMediaType_WhenImageUploaded_then415isReturned()throws Exception{
      BufferedImage originalImage = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ImageIO.write(originalImage, "jpg", baos);
      multipartFile = new MockMultipartFile("image", "ProfileImage.wmv", MediaType.APPLICATION_PDF_VALUE, baos.toByteArray());
      mockMvc.perform(multipart("/mugshot")
          .file(multipartFile)
          .header("authenticated-user-name", username))
          .andExpect(status().isUnsupportedMediaType())
          .andDo(print());
  }
  //falar com vitor sobre a gambiarra
  @Test
  public void givenFileTooLarge_WhenImageUploaded_then413isReturned() throws Exception{
      byte[] moreThan1MB = new byte[1024 * 1024 * 2];
      multipartFile = new MockMultipartFile("image", "ProfileImage.jpeg", MediaType.IMAGE_JPEG_VALUE, moreThan1MB);
      mockMvc.perform(multipart("/mugshot")
          .file(multipartFile).header("authenticated-user-name", username))
          .andExpect(status().isPayloadTooLarge())
          .andDo(print());
  }
  @Test
  public void givenWrongBodyRequestKey_WhenImageUploaded_then400isReturned() throws Exception{
      byte[] moreThan1MB = new byte[0];
      multipartFile = new MockMultipartFile("wrongBodyKey", "ProfileImage.jpeg", MediaType.IMAGE_JPEG_VALUE, moreThan1MB);
      mockMvc.perform(multipart("/mugshot")
          .file(multipartFile).header("authenticated-user-name", username))
          .andExpect(status().isBadRequest())
          .andDo(print());
  }
}
