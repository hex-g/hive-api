package hive.album.controller;

import hive.album.repository.UserRepository;
import hive.album.storage.ImageStorer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@RunWith(SpringRunner.class)
@SpringBootTest
public class UtilsControllerTest {
  private MockMvc mockMvc;
  @Mock
  private UserRepository userRepository;
  @Mock
  private ImageStorer imageStorer;
  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    var utilsController = new UtilsController(userRepository,imageStorer);
    mockMvc = MockMvcBuilders.standaloneSetup(utilsController).build();
  }

  @Test
  public void givenRequestWithOkResponseAsDefault_whenVerifyIfTheServeIsOk_then200isReturned() throws Exception{
    mockMvc
        .perform(
            get("/utils/hey")
        )
        .andExpect(status().isOk());
  }
}
