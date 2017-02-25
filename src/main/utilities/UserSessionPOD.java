package main.utilities;

import main.dto.*;
import main.utilities.*;

public class UserSessionPOD {
  public boolean successfulLogin = false;
  public User user = null;

  public UserSessionPOD (boolean successfulLogin, User user){
    this.successfulLogin = successfulLogin;
    this.user = user;
  }
}
