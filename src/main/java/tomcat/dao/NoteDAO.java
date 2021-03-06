package tomcat.dao;

import javax.persistence.Entity;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.search.annotations.*;

@Entity
@EntityScan
@Indexed
@Table(name="notes")
public class NoteDAO {
  @Id
  private Integer id;

  @Field
  private String text;

  @Field
  private Integer owner_id;

	public Integer getOwner_id() {
		return this.owner_id;
	}

	public void setOwner_id(Integer owner_id) {
		this.owner_id = owner_id;
	}


  public Integer getId() {
    return id;
  }

  public void setId(int id) {
      this.id = id;
  }

  public String getText() {
    return this.text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public NoteDAO(){}
  
  public NoteDAO(Integer id, String text, Integer owner_id) {
    this.id = id;
    this.text = text;
    this.owner_id = owner_id;
  }
}