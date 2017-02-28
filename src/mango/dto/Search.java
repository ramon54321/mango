package mango.dto;

import javax.persistence.*;
import javax.xml.bind.annotation.*;

@XmlRootElement
public class Search {

  @XmlElement
  public String search;

}
