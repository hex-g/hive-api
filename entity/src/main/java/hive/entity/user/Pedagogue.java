package hive.entity.user;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@DynamicUpdate
@Entity
@Table(name = "tb_pedagogue")
public class Pedagogue {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "rm", unique = true)
  private String rm;

  @ManyToOne(cascade = CascadeType.ALL, optional = false)
  @JoinColumn(name = "person_id", unique = true)
  private Person person;

  public Pedagogue() {
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
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
