package main.dto;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import main.dto.*;
import java.util.Date;

@XmlRootElement
@Entity
public class Note {

	@Id
	@GeneratedValue
	private int noteId;
	private User user;
	private String title;
	private String note;
	private boolean active;
	private Date dateCreated;

	public Note(){

	}

	public Note(User user, String title, String note, boolean active){
		this.user = user;
		this.title = title;
		this.note = note;
		this.active = active;
	}

	public void setDateCreated(Date dateCreated){
		this.dateCreated = dateCreated;
	}

	@XmlElement
	public Date getDateCreated(){
		return this.dateCreated;
	}

	public void setActive(boolean active){
		this.active = active;
	}

	@XmlElement
	public boolean getActive(){
		return this.active;
	}

	public void setNoteId(int noteId){
		this.noteId = noteId;
	}

	@XmlElement
	public int getNoteId(){
		return this.noteId;
	}

	public void setUser(User user){
		this.user = user;
	}

	@XmlElement
	public User getUser(){
		return this.user;
	}

	public void setTitle(String title){
		this.title = title;
	}

	@XmlElement
	public String getTitle(){
		return this.title;
	}

	public void setNote(String note){
		this.note = note;
	}

	@XmlElement
	public String getNote(){
		return this.note;
	}
}
