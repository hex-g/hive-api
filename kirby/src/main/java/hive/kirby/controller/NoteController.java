package hive.kirby.controller;

import hive.common.security.HiveHeaders;
import hive.kirby.entity.Note;
import hive.kirby.exception.IAmADirectoryException;
import hive.kirby.exception.InvalidPathException;
import hive.kirby.exception.NoteNotFoundException;
import hive.kirby.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

@RestController
@RequestMapping("/note")
public class NoteController {
  @Value("${hive.kirby.storage-directory}")
  private String rootDir;
  private String fileExtension = ".kirby";

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
    if (path.contains("..")) {
      throw new InvalidPathException();
    }

    var user = userRepository.findByUsername(username);
    var pathObj = Paths.get(rootDir, user.getId().toString(), path);

    if (Files.isDirectory(pathObj)) {
      throw new IAmADirectoryException();
    }

    try {
      return new Note(path, new String(Files.readAllBytes(pathObj)));
    } catch (NoSuchFileException e) {
      throw new NoteNotFoundException();
    } catch (IOException e) {
      throw new RuntimeException();
    }
  }

  @PostMapping(consumes = {"application/json"})
  public void saveNote(
      @RequestHeader(name = HiveHeaders.AUTHENTICATED_USER_NAME_HEADER) final String username,
      @RequestBody final Note note
  ) {
    final var path = note.getPath();

    if (path.contains("..")) {
      throw new InvalidPathException();
    }

    var user = userRepository.findByUsername(username);
    var pathObj = Paths.get(rootDir, user.getId().toString(), path);

    try {
      if (Files.exists(pathObj)) {
        if (Files.isDirectory(pathObj)) {
          throw new IAmADirectoryException();
        }

        Files.write(pathObj, note.getContent().getBytes());
      } else {
        Files.createDirectories(pathObj.getParent());
        Files.write(pathObj, note.getContent().getBytes());
      }
    } catch (IOException e) {
      throw new RuntimeException();
    }
  }
}
