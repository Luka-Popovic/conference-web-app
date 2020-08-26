
package beans;

import hibernate.*;
import java.util.List;


public class ModeratorBeanConferenceContent {
    
    
    private List<SessionHallBean> sessionhalllist = null;
    private List<Sessionpause> sessionpauselist = null;
    private List<Workshop> workshoplist = null;
    private List<Openceremony> openceremonylist = null;
    private List<Closeceremony> closeceremonylist = null;

    public ModeratorBeanConferenceContent() {
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

}
