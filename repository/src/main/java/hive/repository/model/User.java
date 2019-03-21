package hive.repository.model;

// import java.util.HashMap;
// import java.util.Map;
//
// import com.fasterxml.jackson.annotation.JsonAnyGetter;
// import com.fasterxml.jackson.annotation.JsonAnySetter;
// import com.fasterxml.jackson.annotation.JsonIgnore;
// import com.fasterxml.jackson.annotation.JsonInclude;
// import com.fasterxml.jackson.annotation.JsonProperty;
// import com.fasterxml.jackson.annotation.JsonPropertyOrder;
//
// @JsonInclude(JsonInclude.Include.NON_NULL)
// @JsonPropertyOrder({
//     "id",
//     "username",
//     "password",
//     "name",
//     "role"
// })
// public class User {
//
//   @JsonProperty("id")
//   private Integer id;
//   @JsonProperty("username")
//   private String username;
//   @JsonProperty("password")
//   private String password;
//   @JsonProperty("name")
//   private String name;
//   @JsonProperty("role")
//   private String role;
//   @JsonIgnore
//   private Map<String, Object> additionalProperties = new HashMap<String, Object>();
//
//   public User() {
//   }
//
//   public User(Integer id, String username, String password, String name, String role) {
//     this.id = id;
//     this.username = username;
//     this.password = password;
//     this.name = name;
//     this.role = role;
//   }
//
//   @JsonProperty("id")
//   public Integer getId() {
//     return id;
//   }
//
//   @JsonProperty("id")
//   public void setId(Integer id) {
//     this.id = id;
//   }
//
//   @JsonProperty("username")
//   public String getUsername() {
//     return username;
//   }
//
//   @JsonProperty("username")
//   public void setUsername(String username) {
//     this.username = username;
//   }
//
//   @JsonProperty("password")
//   public String getPassword() {
//     return password;
//   }
//
//   @JsonProperty("password")
//   public void setPassword(String password) {
//     this.password = password;
//   }
//
//   @JsonProperty("name")
//   public String getName() {
//     return name;
//   }
//
//   @JsonProperty("name")
//   public void setName(String name) {
//     this.name = name;
//   }
//
//   @JsonProperty("role")
//   public String getRole() {
//     return role;
//   }
//
//   @JsonProperty("role")
//   public void setRole(String role) {
//     this.role = role;
//   }
//
//   @JsonAnyGetter
//   public Map<String, Object> getAdditionalProperties() {
//     return this.additionalProperties;
//   }
//
//   @JsonAnySetter
//   public void setAdditionalProperty(String name, Object value) {
//     this.additionalProperties.put(name, value);
//   }
//
// }

public class User {
  private int id;
  private String username;
  private String password;
  private String name;
  private String role;

  public User() {
  }

  public User(int id, String username, String password, String name, String role) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.name = name;
    this.role = role;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }
}
