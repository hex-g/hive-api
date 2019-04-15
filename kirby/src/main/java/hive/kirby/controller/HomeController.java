package hive.kirby.controller;

import hive.common.security.HiveHeaders;
import hive.kirby.entity.PathNode;
import hive.kirby.repository.UserRepository;
import hive.kirby.util.DirectoryTreeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Paths;

@RestController
@RequestMapping("/")
public class HomeController {
  @Value("${hive.kirby.storage-directory}")
  private String rootDir;

  private final UserRepository userRepository;

  @Autowired
  public HomeController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @GetMapping("/tree")
  public PathNode userDirectoryTree(
      @RequestHeader(name = HiveHeaders.AUTHENTICATED_USER_NAME_HEADER) final String username
  ) {
    final var user = userRepository.findByUsername(username);

    return new DirectoryTreeBuilder(Paths.get(rootDir, user.getId().toString()).toString()).getRoot();
  }
}
