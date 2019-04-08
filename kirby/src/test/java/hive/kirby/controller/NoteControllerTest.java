package hive.kirby.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hive.common.security.HiveHeaders;
import hive.entity.user.User;
import hive.kirby.entity.Note;
import hive.kirby.repository.UserRepository;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NoteControllerTest {
  @Value("${hive.kirby.storage-directory}")
  private String rootDir;

  private MockMvc mockMvc;

  @Mock
  private UserRepository userRepository;
  @Mock
  private User user;
  private String username = RandomStringUtils.randomAlphabetic(8);
  private Integer userId = ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    when(user.getId()).thenReturn(userId);
    when(userRepository.findByUsername(username)).thenReturn(user);

    var noteController = new NoteController(userRepository);

    ReflectionTestUtils.setField(noteController, "rootDir", rootDir);

    mockMvc = MockMvcBuilders.standaloneSetup(noteController).build();
  }

  @Test
  public void givenPathContainsParentDirectoryReference_whenNoteIsRetrieved_then400IsReceived() throws Exception {
    mockMvc
        .perform(
            get("/note")
                .header(HiveHeaders.AUTHENTICATED_USER_NAME_HEADER, username)
                .param("path", "..")
        )
        .andExpect(status().isBadRequest())
        .andExpect(status().reason("Invalid path"));
  }

  @Test
  public void givenPathEndsWithSlash_whenNoteIsRetrieved_then400IsReceived() throws Exception {
    mockMvc
        .perform(
            get("/note")
                .header(HiveHeaders.AUTHENTICATED_USER_NAME_HEADER, username)
                .param("path", "/")
        )
        .andExpect(status().isBadRequest())
        .andExpect(status().reason("Invalid path"));
  }

  @Test
  public void givenPathIsValidAndNoteDoesNotExist_whenNoteIsRetrieved_then404IsReceived() throws Exception {
    mockMvc
        .perform(
            get("/note")
                .header(HiveHeaders.AUTHENTICATED_USER_NAME_HEADER, username)
                .param("path", "a/valid/path")
        )
        .andExpect(status().isNotFound())
        .andExpect(status().reason("Note not found"));
  }

  @Test
  public void givenPathIsValidAndNoteExists_whenNoteIsRetrieved_then200IsReceived() throws Exception {
    var id = userId;
    var dirs = rootDir + "/" + id + "/a/valid/path/to/a";
    var fullPath = dirs + "/file.md";
    var path = "a/valid/path/to/a/file.md";

    try {
      Files.createDirectories(Paths.get(dirs));
      Files.createFile(Paths.get(fullPath));

      mockMvc
          .perform(
              get("/note")
                  .header(HiveHeaders.AUTHENTICATED_USER_NAME_HEADER, username)
                  .param("path", path)
          )
          .andExpect(status().isOk());
    } finally {
      try {
        Files.walk(Paths.get(rootDir, id.toString()))
            .sorted(Comparator.reverseOrder())
            .map(Path::toFile)
            .forEach(File::delete);
      } catch (IOException e) {
        System.err.println("Created files could not be deleted. Root directory name: " + id + ".");
      }
    }
  }

  @Test
  public void givenPathIsValidAndNoteExists_whenNoteIsRetrieved_thenRetrievedResourceIsCorrect() throws Exception {
    var id = userId;
    var dirs = rootDir + "/" + id + "/a/valid/path/to/a";
    var fullPath = dirs + "/file.md";
    var path = "a/valid/path/to/a/file.md";
    var fileContent = RandomStringUtils.randomAscii(2048);

    try {
      Files.createDirectories(Paths.get(dirs));
      Files.createFile(Paths.get(fullPath));
      try (var fileOutputStream = new FileOutputStream(fullPath)) {
        fileOutputStream.write(fileContent.getBytes());
      } catch (IOException e) {
        System.err.println("Could not write to created file. File: " + fullPath);
      }

      var result = mockMvc
          .perform(
              get("/note")
                  .header(HiveHeaders.AUTHENTICATED_USER_NAME_HEADER, username)
                  .param("path", path)
                  .contentType(MediaType.APPLICATION_JSON))
          .andReturn();

      var actual = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Note.class);

      assertEquals(path, actual.getPath());
      assertEquals(fileContent, actual.getContent());
    } finally {
      try {
        Files.walk(Paths.get(rootDir, id.toString()))
            .sorted(Comparator.reverseOrder())
            .map(Path::toFile)
            .forEach(File::delete);
      } catch (IOException e) {
        System.err.println("Created files could not be deleted. Root directory name: " + id + ".");
      }
    }
  }
}
