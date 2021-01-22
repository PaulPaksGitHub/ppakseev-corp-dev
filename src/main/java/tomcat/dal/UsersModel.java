package tomcat.dal;

import java.math.*;

import java.sql.*;
import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

import javax.management.Query;

import java.sql.SQLException;
import java.sql.ResultSet;

import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;

import org.hibernate.*;
import org.hibernate.criterion.Restrictions;
import java.lang.System;
import tomcat.dto.*;
import tomcat.dao.*;

import org.springframework.stereotype.Service;

@Service
public class UsersModel {
  @Autowired
  private SessionFactory sessionFactory;

  private Integer getNextUserID() throws SQLException{
    return ((BigInteger) this.sessionFactory.getCurrentSession()
    .createSQLQuery("select users_id_seq.nextval").uniqueResult())
    .intValue();
  }

  public TokensDAO getTokensByUserID(Session session, Integer user_id) throws SQLException{
    return (TokensDAO)session.get(TokensDAO.class, user_id);
  }

  public TokensDAO getTokensByParam(Session session, String paramName, String param) throws SQLException{
    try {
      CriteriaBuilder cb = session.getCriteriaBuilder();

      CriteriaQuery<TokensDAO> cr = cb.createQuery(TokensDAO.class);
      Root<TokensDAO> root = cr.from(TokensDAO.class);
      cr.select(root).where(cb.equal(root.get(paramName), param));
  
      TypedQuery<TokensDAO> query = session.createQuery(cr);
      return (TokensDAO)query.getSingleResult();
    } catch (NoResultException e){
      return null;
    }
  }

  public UserDAO getUserByLogin(Session session, String login) throws SQLException{
    CriteriaBuilder cb = session.getCriteriaBuilder();

    CriteriaQuery<UserDAO> cr = cb.createQuery(UserDAO.class);
    Root<UserDAO> root = cr.from(UserDAO.class);
    cr.select(root).where(cb.equal(root.get("login"), login));

    TypedQuery<UserDAO> query = session.createQuery(cr);
    UserDAO user = query.getSingleResult();

    return user;
  }

  public TokensDAO getTokensByAccess(String accessToken) throws SQLException {
    Session session = this.sessionFactory.openSession();
    TokensDAO tokens = this.getTokensByParam(session, "accessToken", accessToken);
    session.close();
    return tokens;
  }

  public TokensDAO createUser(RegisterForm registerForm) throws SQLException {
    Integer nextUserID = this.getNextUserID();
    Session session = this.sessionFactory.openSession();
    Transaction tx = null;
    
    TokensDAO tokens = new TokensDAO(nextUserID);

    try {
      tx = session.beginTransaction();
      session.save(tokens);
      session.save(new UserDAO(nextUserID, registerForm.login, registerForm.password));
      tx.commit();
    }
    catch (RuntimeException e) {
        tx.rollback();
    }
    finally {
        session.close();
    }

    return tokens;
  }

  public TokensDAO login(LoginForm loginForm) throws SQLException {
    Session session = this.sessionFactory.openSession();
    Transaction transaction = null;

    TokensDAO tokens = null;

    try {
      transaction = session.beginTransaction();
      UserDAO userWithSameLogin = this.getUserByLogin(session, loginForm.login);
      if (userWithSameLogin.getPassword().equals(loginForm.password)) {
        tokens = this.getTokensByUserID(session, userWithSameLogin.getId());
        tokens.refreshExpiredTokens();
        session.save(tokens);
      }
      transaction.commit();
    } 
    catch (RuntimeException e) {
      transaction.rollback();
    }
    finally {
      session.close();
    }

    return tokens;
  }

  public TokensDAO refreshToken(RefreshTokensForm refreshTokenForm) throws SQLException {
    Session session = this.sessionFactory.openSession();
    TokensDAO tokens = this.getTokensByParam(session, "refreshToken", refreshTokenForm.refreshToken);
    if (tokens != null) {
      Boolean isRefreshed = tokens.refreshAccessToken();
      if (isRefreshed) {
        session.save(tokens);
        session.close();
        return tokens;
      }
    }
    session.close();
    return null;
  }
}