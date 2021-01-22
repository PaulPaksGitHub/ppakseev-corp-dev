package tomcat.dao;


import javax.persistence.Entity;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.search.annotations.*;

@Entity
@EntityScan
@Indexed
@Table(name="shared_notes")
public class SharedNoteDAO {
  @Id
  private Integer id;

  @Field 
  private Integer receiver_id;

  @Field
  private Integer owner_id;

  @Field
  private Integer note_id;

	public Integer getNote_id() {
		return this.note_id;
	}

	public void setNote_id(Integer note_id) {
		this.note_id = note_id;
	}


	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOwner_id() {
		return this.owner_id;
	}

	public void setOwner_id(Integer owner_id) {
		this.owner_id = owner_id;
	}

	public Integer getReceiver_id() {
		return this.receiver_id;
	}

	public void setReceiver_id(Integer receiver_id) {
		this.receiver_id = receiver_id;
  }
  
  public SharedNoteDAO(){}

  public SharedNoteDAO(Integer id, Integer owner_id, Integer receiver_id, Integer note_id) {
    this.id = id;
    this.owner_id = owner_id;
    this.receiver_id = receiver_id;
    this.note_id = note_id;
  }

}