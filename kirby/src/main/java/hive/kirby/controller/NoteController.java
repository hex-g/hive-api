package hive.kirby.controller;

import hive.kirby.entity.Note;
import hive.kirby.exception.InvalidPathException;
import hive.kirby.exception.NoteNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("/note")
public class NoteController {
  static String rootDir = "kirby-storage";

  @GetMapping()
  public Note serveNoteByPath(@RequestParam final String path) {
    if (path.contains("..") || path.endsWith("/")) {
      throw new InvalidPathException();
    }

    try (var stream = new FileInputStream(path)) {
      return new Note(path, new String(stream.readAllBytes()));
    } catch (FileNotFoundException e) {
      throw new NoteNotFoundException();
    } catch (IOException e) {
      throw new RuntimeException();
    }
  }
}
