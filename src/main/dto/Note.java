package main.dto;

import javax.persistence.*;
import javax.xml.bind.annotation.*;

@XmlRootElement
@Entity
public class Note {

	@Id
	@GeneratedValue
	private int noteId;
	private String creatorUsername;
	private String title;
	private String note;
	private boolean active;

	public Note(){

	}

	public Note(String creatorUsername, String title, String note, boolean active){
		this.creatorUsername = creatorUsername;
		this.title = title;
		this.note = note;
		this.active = active;
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

	public void setCreatorUsername(String creatorUsername){
		this.creatorUsername = creatorUsername;
	}

	@XmlElement
	public String getCreatorUsername(){
		return this.creatorUsername;
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
