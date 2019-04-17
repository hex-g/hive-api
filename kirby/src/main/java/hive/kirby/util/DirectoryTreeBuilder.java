package hive.kirby.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hive.kirby.entity.PathNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class DirectoryTreeBuilder {
  private PathNode root;

  public DirectoryTreeBuilder(String rootPath) {
    root = new PathNode();
    root.setName("root");
    root.setChildren(walkPath(rootPath));
  }

  public PathNode getRoot() {
    return root;
  }

  private PathNode[] walkPath(String path) {
    if (!Files.exists(Paths.get(path))) {
      return new PathNode[0];
    }

    try {
      var children = new ArrayList<PathNode>();
      Files.list(Paths.get(path)).forEach(e -> {
        var name = e.getFileName().toString();
        var temp = new PathNode();
        children.add(temp);
        temp.setName(name);
        if (Files.isDirectory(e)) {
          temp.setChildren(walkPath(path + "/" + name));
        }
      });
      return children.toArray(PathNode[]::new);
    } catch (IOException e) {
      throw new RuntimeException();
    }
  }
}
