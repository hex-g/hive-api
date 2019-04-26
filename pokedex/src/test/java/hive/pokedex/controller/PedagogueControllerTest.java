package hive.pokedex.controller;

import hive.entity.user.Pedagogue;
import hive.entity.user.Person;
import hive.entity.user.User;
import hive.pokedex.repository.PedagogueRepository;
import hive.pokedex.repository.UserRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PedagogueControllerTest {

  @Mock
  private PedagogueRepository pedagogueRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private List<Pedagogue> pedagogues;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    var pedagogueController = new PedagogueController(pedagogueRepository, userRepository, pedagogues);

    mockMvc = MockMvcBuilders.standaloneSetup(pedagogueController).build();
  }

  @Test
  public void givenPedagogueDoesNotExists_whenPedagogueInfoIsRetrieved_then404IsReceived() throws Exception {

    mockMvc.perform(
        get("/admin/pedagogue")
            .param("username", "test")
    ).andExpect(status().isNotFound())
        .andExpect(status().reason("Entity not found"));

  }

  @Test
  public void givenPedagogueExists_whenPedagogueInfoIsRetrieved_then200IsReceived() throws Exception {
    pedagogues = mock(List.class);
    when(pedagogues.isEmpty()).thenReturn(false);

    mockMvc.perform(
            get("/admin/pedagogue")
                    .param("rm", "test")
    ).andExpect(status().isOk());

  }

  @Test
  public void givenPedagogueDoesNotExists_whenPedagogueUpdatedInfoIsRetrieved_then404IsReceived() throws Exception {

    mockMvc.perform(
        post("/admin/pedagogue")
            .param("id", "3123")
    ).andExpect(status().isNotFound())
        .andExpect(status().reason("Entity not found"));

  }

  @Test
  public void givenPedagogueExists_whenPedagogueUpdatedInfoIsRetrieved_then200IsReceived() throws Exception {
    when(pedagogueRepository.existsById(1)).thenReturn(true);

    var pedagogue = new Pedagogue("rm");
    pedagogue.setId(1);

    var person = new Person("test-updated");
    person.setUser(new User("test", "123", "PEDAGOGUE"));
    pedagogue.setPerson(person);

    when(pedagogueRepository.getOne(1)).thenReturn(pedagogue);

    mockMvc.perform(
        post("/admin/pedagogue")
            .param("id", "1")
            .param("name", "name-updated")
            .param("rm", "rm-updated")
            .param("username", "username-updated")
            .param("password", "password-updated")
    ).andExpect(status().isOk());

    when(pedagogueRepository.save(pedagogue)).thenReturn(pedagogue);

  }

  @Test
  public void givenTriedToSavePedagogue_whenNoPedagogueInfoRetrieved_then406IsReceived() throws Exception {

    mockMvc.perform(
        post("/admin/pedagogue")
    ).andExpect(status().isNotAcceptable())
        .andExpect(status().reason("Null value not allowed"));

  }

  @Test
  public void givenTriedToSavePedagogue_whenEmptyPedagogueInfoIsRetrieved_then406IsReceived() throws Exception {

    mockMvc.perform(
        post("/admin/pedagogue")
            .param("name", "")
            .param("rm", "")
            .param("username", "")
            .param("password", "")
    ).andExpect(status().isNotAcceptable())
        .andExpect(status().reason("Null value not allowed"));

  }

  @Test
  public void givenTriedToSavePedagogue_whenPedagogueInfoOnlyWhiteSpacesIsRetrieved_then406IsReceived() throws Exception {

    mockMvc.perform(
        post("/admin/pedagogue")
            .param("name", " ")
            .param("rm", " ")
            .param("username", "  ")
            .param("password", "  ")
    ).andExpect(status().isNotAcceptable())
        .andExpect(status().reason("Null value not allowed"));

  }

  @Test
  public void givenTriedToSavePedagogue_whenExistentPedagogueRmIsRetrieved_then409IsReceived() throws Exception {
    when(pedagogueRepository.existsByRm("rm-test")).thenReturn(true);

    mockMvc.perform(
        post("/admin/pedagogue")
            .param("name", "test")
            .param("rm", "rm-test")
            .param("username", "test")
            .param("password", "test")
    ).andExpect(status().isConflict())
        .andExpect(status().reason("Entity already registered, try again"));

  }

  @Test
  public void givenTriedToSavePedagogue_whenExistentPedagogueUsernameIsRetrieved_then409IsReceived() throws Exception {
    when(userRepository.existsByUsername("test")).thenReturn(true);

    mockMvc.perform(
        post("/admin/pedagogue")
            .param("name", "test")
            .param("rm", "rm-test")
            .param("username", "test")
            .param("password", "test")
    ).andExpect(status().isConflict())
        .andExpect(status().reason("Username already registered, try again"));

  }

  @Test
  public void givenPedagogueDoesNotExists_whenDeletePedagogueByIdIsRetrieved_then404IsReceived() throws Exception {

    mockMvc.perform(
        delete("/admin/pedagogue")
            .param("id", "1")
    ).andExpect(status().isNotFound())
        .andExpect(status().reason("Entity not found"));

  }

  @Test
  public void givenPedagogueExists_whenDeletePedagogueByIdIsRetrieved_then200IsReceived() throws Exception {
    when(pedagogueRepository.existsById(1)).thenReturn(true);

    mockMvc.perform(
        delete("/admin/pedagogue")
            .param("id", "1")
    ).andExpect(status().isOk());

  }

}
