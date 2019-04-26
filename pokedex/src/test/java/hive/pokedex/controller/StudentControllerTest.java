package hive.pokedex.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import hive.entity.user.Person;
import hive.entity.user.Student;
import hive.entity.user.User;
import hive.pokedex.repository.StudentRepository;
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
public class StudentControllerTest {

  @Mock
  private StudentRepository studentRepository;
  @Mock
  private UserRepository userRepository;

  private MockMvc mockMvc;

  private final Type type = new TypeToken<List<Student>>() {}.getType();

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    var studentController = new StudentController(studentRepository, userRepository);

    mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
  }

  @Test
  public void givenStudentDoesNotExists_whenStudentInfoIsRetrieved_then404IsReceived() throws Exception {

    mockMvc.perform(
        get("/admin/student")
            .param("username", "test")
    ).andExpect(status().isNotFound())
        .andExpect(status().reason("Entity not found"));

  }

  @Test
  @SuppressWarnings("unchecked")
  public void givenStudentExists_whenStudentInfoIsRetrieved_then200IsReceived() throws Exception {

    List<Student> studentList = new ArrayList<>();
    studentList.add(new Student("ra-select-test"));

    when(studentRepository.findAll((Example<Student>) any())).thenReturn(studentList);

    MvcResult result = mockMvc.perform(
        get("/admin/student")
    ).andExpect(status().isOk()).andReturn();

    List<Student> resultList = new Gson().fromJson(result.getResponse().getContentAsString(), type);

    assertEquals(resultList.get(0).getRa(), studentList.get(0).getRa());

  }

  @Test
  public void givenStudentDoesNotExists_whenStudentUpdatedInfoIsRetrieved_then404IsReceived() throws Exception {

    mockMvc.perform(
        post("/admin/student")
            .param("id", "3123")
    ).andExpect(status().isNotFound())
        .andExpect(status().reason("Entity not found"));

  }

  @Test
  public void givenStudentExists_whenStudentUpdatedInfoIsRetrieved_then200IsReceived() throws Exception {
    when(studentRepository.existsById(1)).thenReturn(true);

    var student = new Student("ra");
    student.setId(1);

    var person = new Person("test-updated");
    person.setUser(new User("test", "123", "STUDENT"));
    student.setPerson(person);

    when(studentRepository.getOne(1)).thenReturn(student);

    mockMvc.perform(
        post("/admin/student")
            .param("id", "1")
            .param("name", "name-updated")
            .param("rm", "rm-updated")
            .param("username", "username-updated")
            .param("password", "password-updated")
    ).andExpect(status().isOk());

  }

  @Test
  public void givenTriedToSaveStudent_whenNoStudentInfoRetrieved_then406IsReceived() throws Exception {

    mockMvc.perform(
        post("/admin/student")
    ).andExpect(status().isNotAcceptable())
        .andExpect(status().reason("Null value not allowed"));

  }

  @Test
  public void givenTriedToSaveStudent_whenEmptyStudentInfoIsRetrieved_then406IsReceived() throws Exception {

    mockMvc.perform(
        post("/admin/student")
            .param("name", "")
            .param("ra", "")
            .param("username", "")
            .param("password", "")
    ).andExpect(status().isNotAcceptable())
        .andExpect(status().reason("Null value not allowed"));

  }

  @Test
  public void givenTriedToSaveStudent_whenStudentInfoOnlyWhiteSpacesIsRetrieved_then406IsReceived() throws Exception {

    mockMvc.perform(
        post("/admin/student")
            .param("name", " ")
            .param("ra", " ")
            .param("username", "  ")
            .param("password", "  ")
    ).andExpect(status().isNotAcceptable())
        .andExpect(status().reason("Null value not allowed"));

  }

  @Test
  public void givenTriedToSaveStudent_whenExistentStudentRaIsRetrieved_then409IsReceived() throws Exception {
    when(studentRepository.existsByRa("ra-test")).thenReturn(true);

    mockMvc.perform(
        post("/admin/student")
            .param("name", "test")
            .param("ra", "ra-test")
            .param("username", "test")
            .param("password", "test")
    ).andExpect(status().isConflict())
        .andExpect(status().reason("Entity already registered, try again"));

  }

  @Test
  public void givenTriedToSaveStudent_whenExistentStudentUsernameIsRetrieved_then409IsReceived() throws Exception {
    when(userRepository.existsByUsername("test")).thenReturn(true);

    mockMvc.perform(
        post("/admin/student")
            .param("name", "test")
            .param("ra", "ra-test")
            .param("username", "test")
            .param("password", "test")
    ).andExpect(status().isConflict())
        .andExpect(status().reason("Username already registered, try again"));

  }

  @Test
  public void givenStudentDoesNotExists_whenDeleteStudentByIdIsRetrieved_then404IsReceived() throws Exception {

    mockMvc.perform(
        delete("/admin/student")
            .param("id", "1")
    ).andExpect(status().isNotFound())
        .andExpect(status().reason("Entity not found"));

  }

  @Test
  public void givenStudentExists_whenDeleteStudentByIdIsRetrieved_then200IsReceived() throws Exception {
    when(studentRepository.existsById(1)).thenReturn(true);

    mockMvc.perform(
        delete("/admin/student")
            .param("id", "1")
    ).andExpect(status().isOk());

  }
}
