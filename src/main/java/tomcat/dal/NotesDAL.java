package tomcat.dal;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import java.sql.SQLException;
import java.sql.ResultSet;

import tomcat.utils.H2JDBC;
import tomcat.utils.H2JDBCInstance;
import tomcat.dto.NoteDTO;

import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Qualifier;

@Service
public class NotesDAL {
  private NoteDTO parseNoteFromResultSet(ResultSet resultSet) throws SQLException{
    return new NoteDTO(resultSet.getInt("id"), resultSet.getString("text"));
  }

  private Integer getNextNoteID() throws SQLException{
    Connection conn = H2JDBC.getInstance().getConnection();

    Statement idStmt = conn.createStatement();
    ResultSet idResuilt = idStmt.executeQuery("select notes_id_seq.nextval");

    Integer id = null;

    if (idResuilt.next()) {
      id = idResuilt.getInt(1);
    }

    idStmt.close();
    return id;
  }

  public NoteDTO getNote(Integer id) throws SQLException{
    Connection connection = H2JDBC.getInstance().getConnection();
    PreparedStatement statement = connection.prepareStatement("select * from notes where id = ?");
    statement.setInt(1, id);
    ResultSet resultSet = statement.executeQuery();

    NoteDTO note = new NoteDTO();

    if (resultSet.next()) {
      note = this.parseNoteFromResultSet(resultSet);
    }

    statement.close();
    return note;
  }
  
  public NoteDTO createNote(String text) throws SQLException {
    Connection connection = H2JDBC.getInstance().getConnection();
    Integer nextNoteID = this.getNextNoteID();

    PreparedStatement statement = connection.prepareStatement("insert into notes values (?, ?)");
    statement.setInt(1, nextNoteID);
    statement.setString(2, text);
    statement.execute();
    statement.close();

    return this.getNote(nextNoteID);
  }

  public ArrayList<NoteDTO> getNotes() throws SQLException {
    Connection connection = H2JDBC.getInstance().getConnection();
    Statement statement = connection.createStatement();
    ResultSet resultSet = statement.executeQuery("select * from notes");
    ArrayList<NoteDTO> notes = new ArrayList();
    
    while (resultSet.next()) {
      notes.add(this.parseNoteFromResultSet(resultSet));
    }

    statement.close();
    return notes;
  }

  public boolean deleteNote(Integer id) throws SQLException {
    Connection connection = H2JDBC.getInstance().getConnection();
    PreparedStatement statement = connection.prepareStatement("delete from notes where id = ?");
    statement.setInt(1, id);
    statement.execute();
    statement.close();
    return true;
  }

  public NoteDTO editNote(NoteDTO editedNote) throws SQLException {
    Connection connection = H2JDBC.getInstance().getConnection();
    PreparedStatement statement = connection.prepareStatement("update notes set text = ? where id = ?");

    statement.setString(1, editedNote.text);
    statement.setInt(2, editedNote.id);
    statement.execute();
    statement.close();

    return this.getNote(editedNote.id);
  }
}