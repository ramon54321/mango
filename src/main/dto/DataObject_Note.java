package main.dto;

import javax.persistence.*;
import main.dto.connections.*;
import javax.xml.bind.annotation.*;

@XmlRootElement
@Entity
public class DataObject_Note {

	@Id
	@GeneratedValue
	private int noteId;
	private String creatorUsername;
	private String title;
	private String note;

	public DataObject_Note(){

	}

	public DataObject_Note(String creatorUsername, String title, String note){
		this.creatorUsername = creatorUsername;
		this.title = title;
		this.note = note;
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
