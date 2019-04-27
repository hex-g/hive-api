package hive.pokedex.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import hive.entity.user.User;
import hive.pokedex.repository.UserRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

  @Mock
  private UserRepository userRepository;

  private MockMvc mockMvc;

  private final Type type = new TypeToken<List<User>>() {}.getType();
  private final String ROLE = "ADMIN";

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    var userController = new UserController(userRepository);

    mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
  }

  @Test
  public void givenUserDoesNotExists_whenUserInfoIsRetrieved_then404IsReceived() throws Exception {

    mockMvc.perform(
        get("/admin/user")
            .param("username", "test")
    ).andExpect(status().isNotFound())
        .andExpect(status().reason("Entity not found"));

  }

  @Test
  @SuppressWarnings("unchecked")
  public void givenStudentExists_whenStudentInfoIsRetrieved_then200IsReceived() throws Exception {

    List<User> userList = new ArrayList<>();
    userList.add(new User("username-test-select","password",ROLE));

    when(userRepository.findAll((Example<User>) any())).thenReturn(userList);

    MvcResult result = mockMvc.perform(
        get("/admin/user")
    ).andExpect(status().isOk()).andReturn();

    List<User> resultList = new Gson().fromJson(result.getResponse().getContentAsString(), type);

    assertEquals(resultList.get(0).getUsername(), userList.get(0).getUsername());

  }

  @Test
  public void givenUserDoesNotExists_whenUserUpdatedInfoIsProvided_then404IsReceived() throws Exception {

    mockMvc.perform(
        post("/admin/user")
            .param("id", "3123")
    ).andExpect(status().isNotFound())
        .andExpect(status().reason("Entity not found"));

  }

  @Test
  public void givenUserExists_whenUserUpdatedInfoIsProvided_then200IsReceived() throws Exception {
    when(userRepository.existsById(1)).thenReturn(true);

    var user = new User("test", "123", ROLE);
    user.setId(1);

    when(userRepository.getOne(1)).thenReturn(user);

    mockMvc.perform(
        post("/admin/user")
            .param("id", "1")
            .param("username", "username-updated")
    ).andExpect(status().isOk());

  }

  @Test
  public void givenTriedToSaveUser_whenNoUserInfoProvided_then406IsReceived() throws Exception {

    mockMvc.perform(
        post("/admin/user")
    ).andExpect(status().isNotAcceptable())
        .andExpect(status().reason("Null value not allowed"));

  }

  @Test
  public void givenTriedToSaveUser_whenEmptyUserInfoIsProvided_then406IsReceived() throws Exception {

    mockMvc.perform(
        post("/admin/user")
            .param("username", "")
            .param("password", "")
    ).andExpect(status().isNotAcceptable())
        .andExpect(status().reason("Null value not allowed"));

  }

  @Test
  public void givenTriedToSaveUser_whenUserInfoOnlyWhiteSpacesIsProvided_then406IsReceived() throws Exception {

    mockMvc.perform(
        post("/admin/user")
            .param("username", "  ")
            .param("password", "  ")
    ).andExpect(status().isNotAcceptable())
        .andExpect(status().reason("Null value not allowed"));

  }

  @Test
  public void givenTriedToSaveUser_whenExistentUsernameIsProvided_then409IsReceived() throws Exception {
    when(userRepository.existsByUsername("test")).thenReturn(true);

    mockMvc.perform(
        post("/admin/user")
            .param("username", "test")
            .param("password", "123")
    ).andExpect(status().isConflict())
        .andExpect(status().reason("Username already registered"));

  }

  @Test
  public void givenUserDoesNotExists_whenDeleteUserById_then404IsReceived() throws Exception {

    mockMvc.perform(
        delete("/admin/user")
            .param("id", "1")
    ).andExpect(status().isNotFound())
        .andExpect(status().reason("Entity not found"));

  }

  @Test
  public void givenUserExists_whenDeleteUserById_then200IsReceived() throws Exception {
    when(userRepository.existsById(1)).thenReturn(true);

    mockMvc.perform(
        delete("/admin/user")
            .param("id", "1")
    ).andExpect(status().isOk());

  }

}
