package tomcat.controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;

import tomcat.dal.NotesDAL;
import tomcat.dto.NoteDTO;

class CreateBody {
  public String text;
}

@RestController
@RequestMapping("/api")
public class NotesRestController {
  @Autowired
  private NotesDAL notesModel;

	@GetMapping("/notes")
	public ArrayList<NoteDTO> getNotes() throws SQLException {
		return this.notesModel.getNotes();
	}

  @GetMapping("/note")
  public NoteDTO getNote(@RequestParam(value = "id") Integer id) throws SQLException {
    return this.notesModel.getNote(id);
  }
  
  @PostMapping("/note")
  public NoteDTO createNote(@RequestBody CreateBody body) throws SQLException {
    return this.notesModel.createNote(body.text);
  }
  
  
  @DeleteMapping("/note/{id}")
  public boolean deleteNote(@PathVariable(value = "id") Integer id) throws SQLException {
    return this.notesModel.deleteNote(id);
  }

  @PutMapping("/note/{id}")
  public NoteDTO editNote(@PathVariable(value = "id") Integer id, @RequestBody CreateBody body) throws SQLException {
    return this.notesModel.editNote(new NoteDTO(id, body.text));
  }
}

