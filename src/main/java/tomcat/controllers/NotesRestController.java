package com.example.springboot;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

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

import tomcat.dal.NotesDAL;
import tomcat.dao.NoteDAO;

class CreateBody {
  public String text;
}

@CrossOrigin
@RestController
@RequestMapping("/api")
public class NotesRestController {
	@GetMapping("/notes")
	public ArrayList<NoteDAO> getNotes() throws SQLException {
		return new NotesDAL().getNotes();
	}

  @GetMapping("/note")
  public NoteDAO getNote(@RequestParam(value = "id") Integer id) throws SQLException {
    return new NotesDAL().getNote(id);
  }
  
  @PostMapping("/note-create")
  public NoteDAO createNote(@RequestBody CreateBody body) throws SQLException {
    return new NotesDAL().createNote(body.text);
  }
  
  
  @DeleteMapping("/note-delete")
  public boolean deleteNote(@RequestParam(value = "id") Integer id) throws SQLException {
    return new NotesDAL().deleteNote(id);
  }

  @PutMapping("/note-edit")
  public NoteDAO editNote(@RequestBody NoteDAO editedNote) throws SQLException {
    return new NotesDAL().editNote(editedNote);
  }
}

