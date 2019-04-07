package hive.kirby.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hive.common.security.HiveHeaders;
import hive.kirby.entity.Note;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(NoteController.class)
public class NoteControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Test
  public void givenPathContainsParentDirectoryReference_whenNoteIsRetrieved_then400IsReceived() throws Exception {
    mockMvc
        .perform(get("/note").param("path", "..").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(status().reason("Invalid path"));
  }

  @Test
  public void givenPathEndsWithSlash_whenNoteIsRetrieved_then400IsReceived() throws Exception {
    mockMvc
        .perform(get("/note").param("path", "/").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(status().reason("Invalid path"));
  }

  @Test
  public void givenPathIsValidAndNoteDoesNotExist_whenNoteIsRetrieved_then404IsReceived() throws Exception {
    mockMvc
        .perform(get("/note").param("path", "a/valid/path").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(status().reason("Note not found"));
  }

  @Test
  public void givenPathIsValidAndNoteExists_whenNoteIsRetrieved_then200IsReceived() throws Exception {
    var userId = RandomStringUtils.randomNumeric(4);
    var dirs = NoteController.rootDir + "/" + userId + "/a/valid/path/to/a";
    var path = dirs + "/file.md";

    try {
      Files.createDirectories(Paths.get(dirs));
      Files.createFile(Paths.get(path));

      mockMvc
          .perform(
              get("/note")
                  .header(HiveHeaders.AUTHENTICATED_USER_NAME_HEADER, userId)
                  .param("path", path)
                  .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk());
    } finally {
      try {
        Files.walk(Paths.get(NoteController.rootDir))
            .sorted(Comparator.reverseOrder())
            .map(Path::toFile)
            .forEach(File::delete);
      } catch (IOException e) {
        System.err.println("Created files could not be deleted. Root directory name: " + userId + ".");
      }
    }
  }

  @Test
  public void givenPathIsValidAndNoteExists_whenNoteIsRetrieved_thenRetrievedResourceIsCorrect() throws Exception {
    var userId = RandomStringUtils.randomNumeric(4);
    var dirs = NoteController.rootDir + "/" + userId + "/a/valid/path/to/a";
    var path = dirs + "/file.md";
    var fileContent = RandomStringUtils.randomAscii(1024);

    try {
      Files.createDirectories(Paths.get(dirs));
      Files.createFile(Paths.get(path));
      try (var fileOutputStream = new FileOutputStream(path)) {
        fileOutputStream.write(fileContent.getBytes());
      } catch (IOException e) {
        System.err.println("Could not write to created file. File: " + path);
      }

      var result = mockMvc
          .perform(
              get("/note")
                  .header(HiveHeaders.AUTHENTICATED_USER_NAME_HEADER, userId)
                  .param("path", path)
                  .contentType(MediaType.APPLICATION_JSON))
          .andReturn();

      var actual = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Note.class);

      assertEquals(path, actual.getPath());
      assertEquals(fileContent, actual.getContent());
    } finally {
      try {
        Files.walk(Paths.get(NoteController.rootDir))
            .sorted(Comparator.reverseOrder())
            .map(Path::toFile)
            .forEach(File::delete);
      } catch (IOException e) {
        System.err.println("Created files could not be deleted. Root directory name: " + userId + ".");
      }
    }
  }
}
