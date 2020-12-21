package tomcat.dal;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import java.sql.SQLException;
import java.sql.ResultSet;

import tomcat.dao.NoteDAO;
import tomcat.dto.NoteDTO;

import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;

import org.hibernate.*;
import java.math.BigInteger;
import java.lang.System;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

@Service
public class NotesDAL {
  @Autowired
  private SessionFactory sessionFactory;

  private NoteDTO parseNoteFromResultSet(ResultSet resultSet) throws SQLException{
    return new NoteDTO(resultSet.getInt("id"), resultSet.getString("text"));
  }

  private Integer getNextNoteID() throws SQLException{
    return ((BigInteger) this.sessionFactory.getCurrentSession()
    .createSQLQuery("select notes_id_seq.nextval").uniqueResult())
    .intValue();
  }

  public NoteDAO getNote(Integer id) throws SQLException{
    return this.sessionFactory.getCurrentSession().get(NoteDAO.class, id);
  }
  
  public NoteDAO createNote(String text) throws SQLException {
    Integer nextNoteID = this.getNextNoteID();
    Session session = this.sessionFactory.openSession();
    Transaction tx = null;

    try {
        tx = session.beginTransaction();
        session.save(new NoteDAO(nextNoteID, text));
        tx.commit();
    }
    catch (RuntimeException e) {
        tx.rollback();
    }
    finally {
        session.close();
    }

    return this.getNote(nextNoteID);
  }

  public List<NoteDAO> getNotes() throws SQLException {
    CriteriaBuilder criteriaBuilder = this.sessionFactory.getCriteriaBuilder();
    CriteriaQuery<NoteDAO> criteriaQuery = criteriaBuilder.createQuery(NoteDAO.class);
    criteriaQuery.from(NoteDAO.class);
    return this.sessionFactory.getCurrentSession().createQuery(criteriaQuery).list();
  }

  public boolean deleteNote(Integer id) throws SQLException {
    Session session = this.sessionFactory.openSession();
    Transaction tx = null;
    try {
        tx = session.beginTransaction();
        session.delete(this.getNote(id));
        tx.commit();
    }
    catch (RuntimeException e) {
        tx.rollback();
    }
    finally {
        session.close();
    }
    return true;
  }

  public NoteDAO editNote(NoteDAO editedNote) throws SQLException {
    Session session = this.sessionFactory.openSession();
    Transaction tx = null;
    try {
        tx = session.beginTransaction();
        session.merge(new NoteDAO(editedNote.getId(), editedNote.getText()));
        tx.commit();
    }
    catch (RuntimeException e) {
        tx.rollback();
    }
    finally {
        session.close();
    }

    return new NoteDAO(editedNote.getId(), editedNote.getText());
  }
}