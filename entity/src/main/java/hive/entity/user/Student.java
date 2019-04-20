package hive.entity.user;

import javax.persistence.*;

@Entity
@Table(name = "tb_student")
public class Student {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "ra", unique = true)
  private String ra;

  @ManyToOne(cascade = CascadeType.ALL, optional = false)
  @JoinColumn(name = "person_id", unique = true)
  private Person person;

  public Student() {
  }

  public Student(String ra) {
    this.ra = ra;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }

  public String getRa() {
    return ra;
  }

  public void setRa(String ra) {
    this.ra = ra;
  }
}
