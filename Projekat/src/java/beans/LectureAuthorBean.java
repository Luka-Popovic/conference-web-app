package beans;

import hibernate.*;
import java.util.List;

public class LectureAuthorBean {

private Lecture lecture;
private List<String> authors;
private List<Author> authorlist;
private boolean meauthor = false;    
private boolean uploadrender = false;

public LectureAuthorBean(Lecture lecture, List<String> authors) {
        this.lecture = lecture;
        this.authors = authors;
    }

    public LectureAuthorBean(Lecture lecture) {
        this.lecture = lecture;
        this.authors = null;
        this.authorlist = null;
    }

    public Lecture getLecture() {
        return lecture;
    }

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public List<Author> getAuthorlist() {
        return authorlist;
    }

    public void setAuthorlist(List<Author> authorlist) {
        this.authorlist = authorlist;
    }

    public boolean isMeauthor() {
        return meauthor;
    }

    public void setMeauthor(boolean meauthor) {
        this.meauthor = meauthor;
    }

    public boolean isUploadrender() {
        return uploadrender;
    }

    public void setUploadrender(boolean uploadrender) {
        this.uploadrender = uploadrender;
    }
    
}
