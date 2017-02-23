package main.dto;

import javax.persistence.*;
import main.dto.connections.*;
import main.utilities.*;
import javax.xml.bind.annotation.*;

@XmlRootElement
@Entity
public class DataObject_User {

	@Id
	@GeneratedValue
	private int userId;
	private String username;
	private String password;
	private int level; // 0:Normal    1:Manager    3:Admin
	private String email;
	private String firstname;
	private String lastname;

	public DataObject_User(){

	}

	public DataObject_User(String username, String password, int level, String email, String firstname, String lastname){
		this.username = username;
		this.password = DataServices.getHash(password);
		this.level = level;
		this.email = email;
	}

	public void setUserId(int userId){
		this.userId = userId;
	}

	@XmlElement
	public int getUserId(){
		return this.userId;
	}

	public void setUsername(String username){
		this.username = username;
	}

	@XmlElement
	public String getUsername(){
		return this.username;
	}

	public void setPassword(String password){
		this.password = password;
	}

	@XmlElement
	public String getPassword(){
		return this.password;
	}

	public void setLevel(int level){
		this.level = level;
	}

	@XmlElement
	public int getLevel(){
		return this.level;
	}

	public void setEmail(String email){
		this.email = email;
	}

	@XmlElement
	public String getEmail(){
		return this.email;
	}

	public void setFirstname(String firstname){
		this.firstname = firstname;
	}

	@XmlElement
	public String getFirstname(){
		return this.firstname;
	}

	public void setLastname(String lastname){
		this.lastname = lastname;
	}

	@XmlElement
	public String getLastname(){
		return this.lastname;
	}
}
