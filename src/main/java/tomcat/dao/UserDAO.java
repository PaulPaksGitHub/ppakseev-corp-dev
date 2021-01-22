package tomcat.dao;

import javax.persistence.Entity;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import javax.persistence.*;

import org.hibernate.search.annotations.*;

@Entity
@EntityScan
@Indexed
@Table(name="users", uniqueConstraints=@UniqueConstraint(columnNames = {"id", "login"}))
public class UserDAO {
  @Id
  private Integer id;

  @Field
  private String login;

  @Field
  private String password;

  public Integer getId() {
    return id;
  }

  public void setId(int id) {
      this.id = id;
  }

  public String getLogin(){
    return this.login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword(){
    return this.password;
  }

  public void setPassword(String password){
    this.password = password;
  }

  public UserDAO(Integer id, String login, String password) {
    this.id = id;
    this.login = login;
    this.password = password;
  }

  public UserDAO(){}
}