package hive.kirby.util;

import hive.kirby.entity.PathNode;

public class DirectoryTreeBuilder {
  private PathNode root;

  public DirectoryTreeBuilder(Integer userId) {
    root = new PathNode();
    root.setName(userId.toString());

    scan(root);
  }

  public PathNode getRoot() {
    return root;
  }

  private void scan(PathNode node) {
  }
}
