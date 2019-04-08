package hive.kirby.controller;

import hive.common.security.HiveHeaders;
import hive.kirby.entity.Note;
import hive.kirby.exception.InvalidPathException;
import hive.kirby.exception.NoteNotFoundException;
import hive.kirby.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;

@RestController
@RequestMapping("/note")
public class NoteController {
  @Value("${hive.kirby.storage-directory}")
  private String rootDir;

  private final UserRepository userRepository;

  @Autowired
  public NoteController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @GetMapping
  public Note serveNote(
      @RequestHeader(name = HiveHeaders.AUTHENTICATED_USER_NAME_HEADER) final String username,
      @RequestParam final String path
  ) {
    if (path.contains("..") || path.endsWith("/")) {
      throw new InvalidPathException();
    }

    var user = userRepository.findByUsername(username);

    try (var stream = new FileInputStream(Paths.get(rootDir, user.getId().toString(), path).toFile())) {
      return new Note(path, new String(stream.readAllBytes()));
    } catch (FileNotFoundException e) {
      throw new NoteNotFoundException();
    } catch (IOException e) {
      throw new RuntimeException();
    }
  }

  // TODO
  @PostMapping
  public void saveNote(@RequestBody final Note note) {
    final var path = note.getPath();

    if (path.contains("..") || path.endsWith("/")) {
      throw new InvalidPathException();
    }
  }
}
