package hive.album.entity;

public class ProfileImage {
  private String path;
  private String content;

  public ProfileImage() {
  }
  public ProfileImage(String path, String content) {
    this.path = path;
    this.content = content;
  }
  public String getPath() {
    return path;
  }
  public void setPath(String path) {
    this.path = path;
  }
  public String getContent() {
    return content;
  }
  public void setContent(String content) {
    this.content = content;
  }
}
