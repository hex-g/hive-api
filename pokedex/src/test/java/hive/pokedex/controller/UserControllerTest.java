package hive.pokedex.controller;

import hive.entity.user.User;
import hive.pokedex.repository.UserRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

  @Mock
  private UserRepository userRepository;

  private MockMvc mockMvc;

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
  public void givenUserDoesNotExists_whenUserInfoUpdatedIsRetrieved_then404IsReceived() throws Exception {

    mockMvc.perform(
        post("/admin/user")
            .param("id", "3123")
    ).andExpect(status().isNotFound())
        .andExpect(status().reason("Entity not found"));

  }

  @Test
  public void givenUserExists_whenUserInfoUpdatedIsRetrieved_then200IsReceived() throws Exception {
    when(userRepository.existsById(1)).thenReturn(true);

    var user = new User("test", "123", "STUDENT");
    user.setId(1);

    when(userRepository.getOne(1)).thenReturn(user);

    mockMvc.perform(
        post("/admin/user")
            .param("id", "1")
            .param("username", "username-updated")
    ).andExpect(status().isOk());

  }

  @Test
  public void givenUserTriedSave_whenNotUserInfoRetrieved_then406IsReceived() throws Exception {

    mockMvc.perform(
        post("/admin/user")
    ).andExpect(status().isNotAcceptable())
        .andExpect(status().reason("Null value not allowed"));

  }

  @Test
  public void givenUserTriedSave_whenUserInfoIsEmptyIsRetrieved_then406IsReceived() throws Exception {

    mockMvc.perform(
        post("/admin/user")
            .param("username", "")
            .param("password", "")
    ).andExpect(status().isNotAcceptable())
        .andExpect(status().reason("Null value not allowed"));

  }

  @Test
  public void givenUserTriedSave_whenUserInfoIsBlankOrWithSpacesIsRetrieved_then406IsReceived() throws Exception {

    mockMvc.perform(
        post("/admin/user")
            .param("username", "  ")
            .param("password", "  ")
    ).andExpect(status().isNotAcceptable())
        .andExpect(status().reason("Null value not allowed"));

  }

  @Test
  public void givenUserTriedSave_whenUsernameExistsIsRetrieved_then409IsReceived() throws Exception {
    when(userRepository.existsByUsername("test")).thenReturn(true);

    mockMvc.perform(
        post("/admin/user")
            .param("username", "test")
            .param("password", "123")
    ).andExpect(status().isConflict())
        .andExpect(status().reason("Username already registered, try again"));

  }

  @Test
  public void givenUserDoesNotExists_whenDeleteUserIdIsRetrieved_then404IsReceived() throws Exception {

    mockMvc.perform(
        delete("/admin/user")
            .param("id", "1")
    ).andExpect(status().isNotFound())
        .andExpect(status().reason("Entity not found"));

  }
}
