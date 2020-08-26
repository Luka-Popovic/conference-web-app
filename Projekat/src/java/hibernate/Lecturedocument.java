package hibernate;
// Generated Jul 6, 2017 9:30:01 PM by Hibernate Tools 4.3.1

import org.primefaces.model.StreamedContent;




/**
 * Lecturedocument generated by hbm2java
 */
public class Lecturedocument  implements java.io.Serializable {


     private Integer id;
     private int lectureid;
     private int conferenceid;
     private byte[] document;
     private String username;
     private String type;
     private String documentname;
     private StreamedContent file;
     
    public Lecturedocument() {
    }

    public Lecturedocument(int lectureid, int conferenceid, byte[] document, String username, String type, String documentname) {
       this.lectureid = lectureid;
       this.conferenceid = conferenceid;
       this.document = document;
       this.username = username;
       this.type = type;
       this.documentname = documentname;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public int getLectureid() {
        return this.lectureid;
    }
    
    public void setLectureid(int lectureid) {
        this.lectureid = lectureid;
    }
    public int getConferenceid() {
        return this.conferenceid;
    }
    
    public void setConferenceid(int conferenceid) {
        this.conferenceid = conferenceid;
    }
    public byte[] getDocument() {
        return this.document;
    }
    
    public void setDocument(byte[] document) {
        this.document = document;
    }
    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    public String getType() {
        return this.type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    public String getDocumentname() {
        return this.documentname;
    }
    
    public void setDocumentname(String documentname) {
        this.documentname = documentname;
    }

    public StreamedContent getFile() {
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    }

}

