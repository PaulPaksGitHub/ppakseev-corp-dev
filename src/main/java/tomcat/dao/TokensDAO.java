package tomcat.dao;

import java.security.*;
import java.math.*;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Random;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import javax.persistence.*;

import org.hibernate.search.annotations.*;

@Entity
@EntityScan
@Indexed
@Table(name="tokens")
public class TokensDAO {
  @Transient
  private Integer accessTokenTTLSeconds = 60;
  @Transient
  private Integer refreshTokenTTLSeconds = 600;

  @Id
  private Integer user_id;

  @Field 
  private String accessToken;

  @Field
  private String refreshToken;

  @Field
  private Integer accessExpiredAt;

  @Field
  private Integer refreshExpiredAt;

	public Integer getRefreshExpiredAt() {
		return this.refreshExpiredAt;
	}

	public void setRefreshExpiredAt(Integer refreshExpiredAt) {
		this.refreshExpiredAt = refreshExpiredAt;
	}

	public Integer getUser_id() {
		return this.user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public String getAccessToken() {
		return this.accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return this.refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
  }
  
	public Integer getAccessExpiredAt() {
		return this.accessExpiredAt;
	}

	public void setAccessExpiredAt(Integer accessExpiredAt) {
		this.accessExpiredAt = accessExpiredAt;
	}
  
  private String getRandomHash(){
    try {
      byte[] byteArray = new byte[7];
      new Random().nextBytes(byteArray);
      String generatedString = new String(byteArray, Charset.forName("UTF-8"));
      MessageDigest m = MessageDigest.getInstance("md5");
      m.update(generatedString.getBytes(),0,generatedString.length());
      return new BigInteger(1,m.digest()).toString(16);
    } catch (NoSuchAlgorithmException e) {
      return "";
    }
  }

  private Integer getCurrentUnixTime(){
    return (int)(System.currentTimeMillis() / 1000L);
  }
  
  private void configureAccessToken(){
    Integer currentUnixTime = this.getCurrentUnixTime();
    this.accessToken = this.getRandomHash();
    this.accessExpiredAt = currentUnixTime + this.accessTokenTTLSeconds;
  }

  private void configureRefreshToken(){
    Integer currentUnixTime = this.getCurrentUnixTime();
    this.refreshExpiredAt = currentUnixTime + this.refreshTokenTTLSeconds;
    this.refreshToken = this.getRandomHash();
  }

  public Boolean refreshAccessToken(){
    if (this.accessToken.length() > 0 && this.refreshToken.length() > 0) {
      Integer currentUnixTime = this.getCurrentUnixTime();
      if (this.refreshExpiredAt > currentUnixTime) {
       this.configureAccessToken();
       return true; 
      }
    }
    return false;
  }

  public void refreshAllTokens(){
    this.configureAccessToken();
    this.configureRefreshToken();
  }

  public Boolean isAccessTokenAlive(){
    return this.getCurrentUnixTime() < this.accessExpiredAt;
  }

  public void refreshExpiredTokens(){
    Integer currentUnixTime = this.getCurrentUnixTime();
    if (this.accessExpiredAt < currentUnixTime) {
      this.configureAccessToken();
    }
    if (this.refreshExpiredAt < currentUnixTime) {
      this.configureRefreshToken();
    }
  }

  public TokensDAO(Integer user_id){
    this.user_id = user_id;
    this.refreshAllTokens();
  }

  public TokensDAO(){}
}