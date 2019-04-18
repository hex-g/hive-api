package hive.entity.user;

import javax.persistence.*;

@Entity
@Table(name = "tb_pedagogue")
public class Pedagogue {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "rm", unique = true)
  private String rm;

  @ManyToOne(cascade = CascadeType.ALL, optional = false)
  @JoinColumn(name="person_id", unique=true)
  private Person person;

  public Pedagogue() {
  }

  public Pedagogue(String rm) {
    this.rm = rm;
  }

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }

  public String getRm() {
    return rm;
  }

  public void setRm(String rm) {
    this.rm = rm;
  }
}
