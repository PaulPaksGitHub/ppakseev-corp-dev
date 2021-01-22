package tomcat.dal;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.sql.*;
import javax.persistence.*;
import javax.persistence.criteria.*;

import tomcat.dao.*;
import tomcat.dto.*;

import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;

import org.hibernate.*;
import java.math.BigInteger;
import java.lang.System;

@Service
public class NotesDAL {
  @Autowired
  private SessionFactory sessionFactory;

  private Integer getNextNoteID() throws SQLException{
    return ((BigInteger) this.sessionFactory.getCurrentSession()
    .createSQLQuery("select notes_id_seq.nextval").uniqueResult())
    .intValue();
  }
  
  private Integer getNextSharedNoteID() throws SQLException{
    return ((BigInteger) this.sessionFactory.getCurrentSession()
    .createSQLQuery("select shared_notes_id_seq.nextval").uniqueResult())
    .intValue();
  }

  public NoteDAO getNote(Integer id) throws SQLException{
    Session session = this.sessionFactory.openSession();
    NoteDAO note = session.get(NoteDAO.class, id);
    session.close();
    return note;
  }
  
  public NoteDAO createNote(Integer userID, String text) throws SQLException {
    Integer nextNoteID = this.getNextNoteID();
    Session session = this.sessionFactory.openSession();
    Transaction tx = null;

    try {
        tx = session.beginTransaction();
        session.save(new NoteDAO(nextNoteID, text, userID));
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

  public List<NoteDAO> getNotes(Integer userID) throws SQLException {
    Session session = this.sessionFactory.openSession();
    List<NoteDAO> notes;

    try {
      CriteriaBuilder criteriaBuilder = this.sessionFactory.getCriteriaBuilder();
      CriteriaQuery<NoteDAO> criteriaQuery = criteriaBuilder.createQuery(NoteDAO.class);
      Root<NoteDAO> root = criteriaQuery.from(NoteDAO.class);
      criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("owner_id"), userID));
  
      TypedQuery<NoteDAO> query = session.createQuery(criteriaQuery);
      notes = query.getResultList();

    } catch (NoResultException e) {
      notes = Collections.emptyList();
    } finally {
      session.close();
    }
    

    return notes;
  }
  
  public List<SharedNoteDAO> getSharedNotes(Integer userID) throws SQLException {
    Session session = this.sessionFactory.openSession();
    List<SharedNoteDAO> notes;

    try {
      CriteriaBuilder criteriaBuilder = this.sessionFactory.getCriteriaBuilder();
      CriteriaQuery<SharedNoteDAO> criteriaQuery = criteriaBuilder.createQuery(SharedNoteDAO.class);
      Root<SharedNoteDAO> root = criteriaQuery.from(SharedNoteDAO.class);
      criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("receiver_id"), userID));
  
      TypedQuery<SharedNoteDAO> query = session.createQuery(criteriaQuery);
      notes = query.getResultList();

    } catch (NoResultException e) {
      notes = Collections.emptyList();
    } finally {
      session.close();
    }
    

    return notes;
  }

  public boolean deleteNote(Integer userID, Integer id) throws SQLException {
    Session session = this.sessionFactory.openSession();

    NoteDAO note = this.getNote(id);

    if (note.getOwner_id() != userID) {
      session.close();
      return false;
    }

    Transaction tx = null;
    try {
        tx = session.beginTransaction();
        session.delete(note);
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

  public NoteDAO editNote(Integer userID, NoteDAO editedNote) throws SQLException {
    Session session = this.sessionFactory.openSession();
    Transaction tx = null;

    NoteDAO note = this.getNote(editedNote.getId());

    if (note.getOwner_id() != userID) {
      session.close();
      return null;
    }

    try {
        tx = session.beginTransaction();
        session.merge(new NoteDAO(editedNote.getId(), editedNote.getText(), editedNote.getOwner_id()));
        tx.commit();
    }
    catch (RuntimeException e) {
        tx.rollback();
    }
    finally {
        session.close();
    }

    return new NoteDAO(editedNote.getId(), editedNote.getText(), editedNote.getOwner_id());
  }

  public SharedNoteDAO shareNote(Integer userID, NoteShareForm noteShareForm) throws SQLException {
    Integer nextSharedNoteID = this.getNextSharedNoteID();
    Session session = this.sessionFactory.openSession();
    Transaction tx = null;

    System.out.println("rid " + noteShareForm.receiver_user_id);
    System.out.println("ni " + noteShareForm.note_id);

    NoteDAO note = this.getNote(noteShareForm.note_id);
    SharedNoteDAO shared_note = null;

    if (userID == note.getOwner_id()) {
      try {
        tx = session.beginTransaction();
        shared_note = new SharedNoteDAO(nextSharedNoteID, userID, noteShareForm.receiver_user_id, noteShareForm.note_id);
        session.save(shared_note);
        tx.commit();
      }
      catch (RuntimeException e) {
          tx.rollback();
      }
    }

    session.close();

    return shared_note;
  }

  public List<NoteDAO> getMySharedNotes(Integer userID) throws SQLException {
    Session session = this.sessionFactory.openSession();

    List<SharedNoteDAO> sharedList = this.getSharedNotes(userID);

    ArrayList<NoteDAO> notes = new ArrayList<NoteDAO>();

    for (SharedNoteDAO sn : sharedList) {
      notes.add((NoteDAO)this.getNote(sn.getNote_id()));
    }

    session.close();

    return notes;
  }
}