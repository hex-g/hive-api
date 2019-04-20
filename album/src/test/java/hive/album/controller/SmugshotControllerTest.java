package hive.album.controller;

import hive.album.repository.UserRepository;
import hive.album.storage.ImageStorer;
import hive.common.security.HiveHeaders;
import hive.entity.user.User;
import org.apache.commons.lang.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.UrlResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.mockito.BDDMockito.given;
@RunWith(SpringRunner.class)
@SpringBootTest
public class SmugshotControllerTest {
  @Value("${hive.mugshot.image-directory-path}")
  private String rootDir;
  private MockMvc mockMvc;
  @Mock
  private UserRepository userRepository;
  @Mock
  private ImageStorer imageStorer;
  @Mock
  private User user;
  private String username = RandomStringUtils.randomAlphabetic(8);
  private Integer userId = 3;//ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
  private MockMultipartFile multipartFile;
  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    when(user.getId()).thenReturn(userId);
    when(userRepository.findByUsername(username)).thenReturn(user);
    var mugshotController = new MugshotController(imageStorer,userRepository);
    ReflectionTestUtils.setField(mugshotController, "rootDir", rootDir);
    mockMvc = MockMvcBuilders.standaloneSetup(mugshotController).build();
    multipartFile = new MockMultipartFile("image", "ProfileImage.jpg", "image/jpeg", "Spring Framework".getBytes());
  }
  //SUCCESS:
  @Test
  public void givenValidImage_WhenImageRetrieved_then200isReturned() throws Exception{
    mockMvc.perform(
        get("/mugshot").header("authenticated-user-name",username)
    ).andExpect(status().isOk())
        .andExpect(model().attribute("files",
            Matchers.contains("http://localhost/mugshot_images_profiles/ProfileImage.jpg")));
  }
  @Test
  public void givenValidImage_WhenImageUploaded_then200isReturned() throws Exception{
    mockMvc.perform(
        multipart("/mugshot").file(multipartFile).header("authenticated-user-name",username)
    ).andExpect(status().isOk());
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
  }
  @Test
  public void givenUnsupportedMediaType_WhenImageUploaded_then415isReturned(){

  }
  @Test
  public void givenFileTooLarge_WhenImageUploaded_then413isReturned(){

  }
  @Test
  public void givenMultipleFile_WhenImageUploaded_then413isReturned(){

  }
}
