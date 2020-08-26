
package beans;

import hibernate.*;
import java.util.List;

public class RegularUserMyAgenda {

    private List<SessionHallBean> sessionhalllist = null;
    private List<Sessionpause> sessionpauselist = null;
    private List<Workshop> workshoplist = null;
    private List<Openceremony> openceremonylist = null;
    private List<Closeceremony> closeceremonylist = null;
    private List<Lecture> lecturelist = null;    
    private boolean render = false;
    private String comment = "";
    private List<LectureAuthorBean> lab = null;
    
    public RegularUserMyAgenda() {
    }

    public List<SessionHallBean> getSessionhalllist() {
        return sessionhalllist;
    }

    public void setSessionhalllist(List<SessionHallBean> sessionhalllist) {
        this.sessionhalllist = sessionhalllist;
    }

    public List<Sessionpause> getSessionpauselist() {
        return sessionpauselist;
    }

    public void setSessionpauselist(List<Sessionpause> sessionpauselist) {
        this.sessionpauselist = sessionpauselist;
    }

    public List<Workshop> getWorkshoplist() {
        return workshoplist;
    }

    public void setWorkshoplist(List<Workshop> workshoplist) {
        this.workshoplist = workshoplist;
    }

    public List<Openceremony> getOpenceremonylist() {
        return openceremonylist;
    }

    public void setOpenceremonylist(List<Openceremony> openceremonylist) {
        this.openceremonylist = openceremonylist;
    }

    public List<Closeceremony> getCloseceremonylist() {
        return closeceremonylist;
    }

    public void setCloseceremonylist(List<Closeceremony> closeceremonylist) {
        this.closeceremonylist = closeceremonylist;
    }

    public List<Lecture> getLecturelist() {
        return lecturelist;
    }

    public void setLecturelist(List<Lecture> lecturelist) {
        this.lecturelist = lecturelist;
    }

    public boolean isRender() {
        return render;
    }

    public void setRender(boolean render) {
        this.render = render;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<LectureAuthorBean> getLab() {
        return lab;
    }

    public void setLab(List<LectureAuthorBean> lab) {
        this.lab = lab;
    }
        
}
