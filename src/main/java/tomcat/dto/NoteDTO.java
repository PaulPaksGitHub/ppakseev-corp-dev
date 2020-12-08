package tomcat.dto;

public class NoteDTO {
  public Integer id;
  public String text;

  public NoteDTO(){};

  public NoteDTO(Integer id, String text) {
    this.id = id;
    this.text = text;
  };
}