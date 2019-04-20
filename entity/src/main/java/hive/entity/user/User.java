package hive.entity.user;

import org.hibernate.annotations.Check;

import javax.persistence.*;

@Entity
@Table(name = "tb_user")
@Check(constraints = "role in ('STUDENT', 'PEDAGOGUE', 'ADMIN')")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "username", unique = true)
  private String username;

  @Column(name = "password")
  private String password;

  @Column(name = "role")
  private String role;

  public User() {
  }

  public User(String username, String password, String role) {
    this.username = username;
    this.password = password;
    this.role = role;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
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

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

}
