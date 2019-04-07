package hive.kirby.entity;

public class PathNode {
  private String name;
  private PathNode[] children;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public PathNode[] getChildren() {
    return children;
  }

  public void setChildren(PathNode[] children) {
    this.children = children;
  }
}
