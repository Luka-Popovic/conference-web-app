
package beans;

import hibernate.*;
import java.util.List;

public class LectureFileDownloadBean {

private Lecture lecture;
private List<Lecturedocument> lecturedocumentlist;

    public LectureFileDownloadBean(Lecture lecture) {
        this.lecture = lecture;
    }

    public Lecture getLecture() {
        return lecture;
    }

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }

    public List<Lecturedocument> getLecturedocumentlist() {
        return lecturedocumentlist;
    }

    public void setLecturedocumentlist(List<Lecturedocument> lecturedocumentlist) {
        this.lecturedocumentlist = lecturedocumentlist;
    }

    
}
