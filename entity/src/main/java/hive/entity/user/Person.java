package hive.entity.user;

import javax.persistence.*;

@Entity
@Table(name = "tb_person")
public class Person {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "name")
  private String name;

  @ManyToOne(cascade = CascadeType.ALL, optional = false)
  @JoinColumn(name="user_id", unique=true)
  private User user;

  public Person() {
  }

  public Person(String name) {
    this.name = name;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
