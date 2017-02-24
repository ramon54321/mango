package main.utilities;

import main.dto.*;
import main.utilities.*;

public class UserSessionPOD {
  public boolean successfulLogin = false;
  public DataObject_User user = null;

  public UserSessionPOD (boolean successfulLogin, DataObject_User user){
    this.successfulLogin = successfulLogin;
    this.user = user;
  }
}
