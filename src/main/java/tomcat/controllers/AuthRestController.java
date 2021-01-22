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

import tomcat.dal.UsersModel;
import tomcat.dao.TokensDAO;
import tomcat.dao.UserDAO;
import tomcat.dto.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api")
public class AuthRestController {
  @Autowired
  private UsersModel usersModel;

  @PostMapping("/register")
  public TokensDAO register(@RequestBody RegisterForm registerForm) throws SQLException {
    return this.usersModel.createUser(registerForm);
  }

  @PostMapping("/login")
  public TokensDAO login(@RequestBody LoginForm loginForm) throws SQLException {
    return this.usersModel.login(loginForm);
  }

  @PostMapping("/refresh-token")
  public TokensDAO refreshToken(@RequestBody RefreshTokensForm refreshTokenForm) throws SQLException {
    return this.usersModel.refreshToken(refreshTokenForm);
  }
}

