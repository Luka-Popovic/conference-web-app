
package beans;

import hibernate.*;
import java.util.List;

public class SessionHallBean {
    
    private String hall;
    private Sessions  session;
    private boolean rendered = false;
    private boolean addimagerendered = false;//Ovo sluzi za dodavanje slike u Moderator Mode
    private boolean imgsessionrendered = false;//Ovo sluzi za dodavanje slike u Regular User Mode - MyAgenda
    
    public SessionHallBean(String hall, Sessions session) {
        this.hall = hall;
        this.session = session;
    }

    public SessionHallBean() {
       
    }

    public String getHall() {
        return hall;
    }

    public void setHall(String hall) {
        this.hall = hall;
    }

    public Sessions getSession() {
        return session;
    }

    public void setSession(Sessions session) {
        this.session = session;
    }

    public boolean isRendered() {
        return rendered;
    }

    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }

    public boolean isAddimagerendered() {
        return addimagerendered;
    }

    public void setAddimagerendered(boolean addimagerendered) {
        this.addimagerendered = addimagerendered;
    }

    public boolean isImgsessionrendered() {
        return imgsessionrendered;
    }

    public void setImgsessionrendered(boolean imgsessionrendered) {
        this.imgsessionrendered = imgsessionrendered;
    }

}
