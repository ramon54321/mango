package mango.dto;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.util.Date;

@XmlRootElement
@Entity
public class Note {

	@Id
	@GeneratedValue
	private int noteId;
	@OneToOne
	private User user;
	private String title;
	private String note;
	private boolean isTask = false;
	private boolean pinned = false;
	private boolean completed = false;
	private Date dateCreated;
	private Date dateCompleted;
	private User userCompleted;
	@XmlElement
	@Transient
	public int sortOrder;

	public Note(){

	}

	public Note(User user, String title, String note, boolean completed, boolean isTask, boolean pinned){
		this.user = user;
		this.title = title;
		this.note = note;
		this.completed = completed;
		this.isTask = isTask;
		this.pinned = pinned;
	}

	public void setUserCompleted(User userCompleted){
		this.userCompleted = userCompleted;
	}

	@XmlElement
	public User getUserCompleted(){
		return this.userCompleted;
	}

	public void setDateCompleted(Date dateCompleted){
		this.dateCompleted = dateCompleted;
	}

	public void setDateCompletedCurrent(){
		this.dateCompleted = new Date();
	}

	@XmlElement
	public Date getDateCompleted(){
		return this.dateCompleted;
	}

	public void setDateCreated(Date dateCreated){
		this.dateCreated = dateCreated;
	}

	public void setDateCurrent(){
		this.dateCreated = new Date();
	}

	@XmlElement
	public Date getDateCreated(){
		return this.dateCreated;
	}

	public void setCompleted(boolean completed){
		this.completed = completed;
	}

	@XmlElement
	public boolean getCompleted(){
		return this.completed;
	}

	public void setPinned(boolean pinned){
		this.pinned = pinned;
	}

	@XmlElement
	public boolean getPinned(){
		return this.pinned;
	}

	public void setIsTask(boolean isTask){
		this.isTask = isTask;
	}

	@XmlElement
	public boolean getIsTask(){
		return this.isTask;
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
