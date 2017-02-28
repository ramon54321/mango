package mango.dto;

import javax.persistence.*;
import javax.xml.bind.annotation.*;

@XmlRootElement
public class SignIn {
  private String username;
  private String password;

  public SignIn(){}

  public SignIn(String username, String password){
    this.username = username;
    this.password = password;
  }

  @XmlElement
  public String getUsername(){
    return this.username;
  }

  public void setUsername(String username){
    this.username = username;
  }

  @XmlElement
  public String getPassword(){
    return this.password;
  }

  public void setPassword(String password){
    this.password = password;
  }
}
