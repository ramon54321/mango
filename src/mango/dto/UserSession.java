package mango.dto;

import mango.dto.*;
import mango.utilities.*;

public class UserSession {

	public boolean successfulLogin = false;
	public User user = null;

	public UserSession(boolean successfulLogin, User user) {
		this.successfulLogin = successfulLogin;
		this.user = user;
	}
}
