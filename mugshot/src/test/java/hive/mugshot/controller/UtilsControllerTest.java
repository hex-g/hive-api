package hive.mugshot.controller;

import hive.mugshot.repository.UserRepository;
import hive.mugshot.storage.ImageStorer;
import hive.common.security.HiveHeaders;
import hive.entity.user.User;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.concurrent.ThreadLocalRandom;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UtilsControllerTest {

  private MockMvc mockMvc;
  private String username = RandomStringUtils.randomAlphabetic(8);
  private Integer userId = ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
  @Value("${hive.mugshot.profile-image-name}")
  private String imageName;
  @Mock
  private UserRepository userRepository;
  @Mock
  private ImageStorer imageStorer;
  @Mock
  private User user;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    when(user.getId()).thenReturn(userId);
    when(userRepository.findByUsername(username)).thenReturn(user);
    var utilsController = new UtilsController(imageStorer,userRepository);
    ReflectionTestUtils.setField(utilsController, "imageName", imageName);
    mockMvc = MockMvcBuilders.standaloneSetup(utilsController).build();
  }

  @Test
  public void givenUserName_whenRequestGeneratedImage_then200isReturned() throws Exception{
    mockMvc
        .perform(
            post("/utils/generateRandomImage")
                .header(HiveHeaders.AUTHENTICATED_USER_NAME_HEADER, username)
        )
        .andExpect(status().isOk())
        .andDo(print());
  }

}
