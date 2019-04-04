package hive.entity.user;

import org.hibernate.annotations.Check;

import javax.persistence.*;

@Entity
@Table(name = "tb_user")
@Check(constraints = "role in (0, 1, 2) and status in (0, 1, 2, 3)")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "username", unique = true)
  private String username;

  @Column(name = "password")
  private String password;

  @Column(name = "role")
  private int role;

  @Column(name = "status")
  private int status;

  public User() {
  }

  public User(String username, String password, int role, int status) {
    this.username = username;
    this.password = password;
    this.role = role;
    this.status = status;
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

  public int getRole() {
    return role;
  }

  public void setRole(int role) {
    this.role = role;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

}