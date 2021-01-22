package tomcat.controllers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;

import tomcat.dal.*;
import tomcat.dao.*;
import tomcat.dto.*;

class CreateBody {
  public String text;
}

@RestController
@RequestMapping("/api")
public class NotesRestController {
  @Autowired
  private NotesDAL notesModel;
  @Autowired
  private UsersModel usersModel;

	@GetMapping("/notes")
	public ResponseEntity getNotes(@RequestHeader("access-token") String accessToken) throws SQLException {
    TokensDAO tokens = this.usersModel.getTokensByAccess(accessToken);
    if (tokens != null && tokens.isAccessTokenAlive()) {
      return new ResponseEntity<List<NoteDAO>>(this.notesModel.getNotes(tokens.getUser_id()), HttpStatus.OK);
    } else {
      return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
    }
  }
  
	@GetMapping("/shared-notes")
	public ResponseEntity getSharedNotes(@RequestHeader("access-token") String accessToken) throws SQLException {
    TokensDAO tokens = this.usersModel.getTokensByAccess(accessToken);
    if (tokens != null && tokens.isAccessTokenAlive()) {
      return new ResponseEntity<List<NoteDAO>>(this.notesModel.getMySharedNotes(tokens.getUser_id()), HttpStatus.OK);
    } else {
      return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
    }
	}
  
  @PostMapping("/note")
  public ResponseEntity createNote(@RequestHeader("access-token") String accessToken,@RequestBody CreateBody body) throws SQLException {
    TokensDAO tokens = this.usersModel.getTokensByAccess(accessToken);
    if (tokens != null && tokens.isAccessTokenAlive()) {
      return new ResponseEntity<NoteDAO>(this.notesModel.createNote(tokens.getUser_id(), body.text), HttpStatus.OK);
    } else {
      return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
    }
  }
  
  
  @DeleteMapping("/note/{id}")
  public ResponseEntity deleteNote(@RequestHeader("access-token") String accessToken,@PathVariable(value = "id") Integer id) throws SQLException {
    TokensDAO tokens = this.usersModel.getTokensByAccess(accessToken);
    if (tokens != null && tokens.isAccessTokenAlive()) {
      return new ResponseEntity<Boolean>(this.notesModel.deleteNote(tokens.getUser_id(), id), HttpStatus.OK);
    } else {
      return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
    }
  }

  @PutMapping("/note/{id}")
  public ResponseEntity editNote(@RequestHeader("access-token") String accessToken,@PathVariable(value = "id") Integer id, @RequestBody CreateBody body) throws SQLException {
    TokensDAO tokens = this.usersModel.getTokensByAccess(accessToken);
    if (tokens != null && tokens.isAccessTokenAlive()) {
      return new ResponseEntity<NoteDAO>(this.notesModel.editNote(tokens.getUser_id(), new NoteDAO(id, body.text, tokens.getUser_id())), HttpStatus.OK);
    } else {
      return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
    }
  }

  @PostMapping("note-share")
  public ResponseEntity shareNote(@RequestHeader("access-token") String accessToken,@RequestBody NoteShareForm body) throws SQLException {
    TokensDAO tokens = this.usersModel.getTokensByAccess(accessToken);
    if (tokens != null && tokens.isAccessTokenAlive()) {
      return new ResponseEntity<SharedNoteDAO>(this.notesModel.shareNote(tokens.getUser_id(), body), HttpStatus.OK);
    } else {
      return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
    }
  }
}

