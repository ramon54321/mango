package main.dto;

import javax.persistence.*;
import javax.xml.bind.annotation.*;

@XmlRootElement
public class Status {
  private boolean status;
  private String message;

  public Status(){}

  public Status(boolean status, String message){
    this.status = status;
    this.message = message;
  }

  @XmlElement
  public boolean getStatus(){
    return this.status;
  }

  public void setStatus(boolean status){
    this.status = status;
  }

  @XmlElement
  public String getMessage(){
    return this.message;
  }

  public void setMessage(String message){
    this.message = message;
  }
}
