package main.dto;

import main.dto.*;
import main.utilities.*;

public class UserSession {
  public boolean successfulLogin = false;
  public User user = null;

  public UserSession (boolean successfulLogin, User user){
    this.successfulLogin = successfulLogin;
    this.user = user;
  }
}
