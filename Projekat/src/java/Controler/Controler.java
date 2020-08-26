
package Controler;

import beans.*;
import hibernate.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

@ManagedBean
@SessionScoped
public class Controler implements Serializable{
  
   boolean admindeleteconferencerendered = false;  
   boolean adminaddconferencerendered = false; 
   boolean regularusersearchrendered = false;
   boolean registrationfromguestmode=false; 
   boolean guestmodesearchrendered = false;
   private UploadedFile file = null;
   private String username;
   private String password;
   private String newpassword;
   private String confirmpassword;
   private String poruka = "";
   private String porukachangepassword = "";
   private String signinporuka = "";
   private User user = null;
   private User newUser = new User();
   private boolean amiauthor = false;
    //Za searhc u Guest mode
   private String searchName = "";
   private Date searchDateBegin = null;
   private Date searchDateEnd = null;
   List<GuestModeBean> guestsearchlist = null;   
    //Za search u Regular User mode
   private String searchNameregularuser = "";
   private String searchFieldregularuser = "";
   private String searchLocationdregularuser = "";
   private String regularuserAddConferenceToMyAgendaMessage = "";
   private Date searchDateBeginregularuser = null;
   private Date searchDateEndregularuser = null; 
   List<RegularUserSearch> regularusersearchlist = null;   
   private boolean showCodeinputtextfield = false;
   private String EntryCode;
   private Map<String, String> regularuserCityMap = null;
    //Za addConference u Admin mode
   private String adminAddConferenceName = "";
   private String adminAddConferenceField = "";
   private String adminAddConferenceLocation = "";
   private String adminAddConferenceCity = "";
   private Date adminAddConferenceDateBegin = null;
   private Date adminAddConferenceDateEnd = null;
   Map<String,String> adminAddConferenceLocationlist = null;
   Map<String,String> adminAddConferenceCitylist = null;
   String[] moderators = null;
   Map<String,String> adminAddConferenceUsernamelist = null;
   private String adminmodemessage = "";
    //Za deleteConference u Admin mode 
   List<GuestModeBean> adminDeleteConferenceAlllist = null;
   String testmessage = "";
  //Za Moderator page
        //Za prikaz eventa za neku konferenciju
    private ModeratorBean conferencetoshow = null;
        //Za dodavanje nove sesije
    List<ModeratorBean> moderatorbeanlist = null;
    ModeratorBeanConferenceContent moderatorbeanconferencecontent = null;
    boolean moderatorpageviewconferenceprogramrender = false;
    boolean moderatorpageaddsessionrender = false;
    private Agenda moderatopageAddEventAgenda = null;
    private Sessions moderatorpageSessionToAdd = null;
    private Date newsessionDate = null;
    private Date newsessionBeginTime = null;
    private Date newsessionEndTime = null;
    private String newsessionMessage = "";
    private int newsessionHall;
    Map<String, Integer> newSessionHalllist = null;
    private String newSessionAddName = "";    
    private ModeratorBean moderatorpageModeratorBeanSessionAdd = null;    
        //Za dodavanje novog predavanja
    boolean moderatorpageAddSessionLecturerender = false;
    private SessionHallBean newsessionLecture = null;
    private Map<String, String> addLectureAuthorList = null;
    private Date addLectureBeginTime = null;
    private Date addLectureEndTime = null;
    private String newLectureName = "";
    private String newlectureMessage = "";
    private String[] newLectureAuthors = null;    
    private String externalAuthor1 = "";
    private String externalAuthor2 = "";
    private String externalAuthor3 = "";
    private String externalAuthor4 = "";
    Lecture newLecture = null;
    private boolean newLectureCheckBox = false;
        //Za dodavanje radionice
    boolean moderatorpageAddWorkshoprender = false;
    private Agenda moderatornewWorkshopAgenda = null;
    private String newWorkshopMessage = "";
    private Date addWorkshopBeginTime = null;
    private Date addWorkshopEndTime = null;
    private Workshop newWorkshop = null;
    private String newWorkshopTopic = "";
    private Date newWorkshopDate = null;
    //Za dodavanje open ceremony
    boolean moderatorpageOpenCeremonyrender = false;
    private Agenda moderatornewOpenCeremonyAgenda = null;
    private Date addOpenCeremonyBeginTime = null;
    private Date addOpenCeremonyEndTime = null;
    private Openceremony newOpenCeremony = null;
    private String newOpenCeremonyMessage = "";
    private String openCeremonyType = "";
    private Date newOpenCeremonyDate = null;
    //Za dodavanje close ceremony
    boolean moderatorpageCloseCeremonyrender = false;
    private Agenda moderatornewCloseCeremonyAgenda = null;
    private Date addCloseCeremonyBeginTime = null;
    private Date addCloseCeremonyEndTime = null;
    private Closeceremony newCloseCeremony = null;
    private String newCloseCeremonyMessage = "";
    private String closeCeremonyType = "";
    private Date newCloseCeremonyDate = null;
    //Za dodavanje slika u ceremoniju i dan ceremonije
    boolean moderatorpageAddConferenceImagesrender = false;
    private Agenda moderatorAddConferenceImagesAgenda = null;
    private String moderatorAddConferenceImagesMessage = "";
    Map<Integer,Integer> moderatorAddConferenceImagesDayList = null;
    private int moderatorAddConferenceImagesDay = -1;
    private boolean addConferenceImageCheckBox = false;
    private UploadedFile addConferenceImageFile = null;
    //Za dodavanje slika u sesiju
    private SessionHallBean ImageSession = null;
    private boolean sessionaddimagemod = false;
    private UploadedFile addSessionImageFile = null;
    private String showSessionsMessage = "";
    //Za Regular User - My Conferences
    private List<ModeratorBean> regularUserMyConferencesList;
    private RegularUserSearch myConferencesAgenda = null;
    private ModeratorBeanConferenceContent myConferencesAgendaView = null;
    private boolean regularUserMyConferencesrender = false;
    private String regularUserMyConferencesmessage = "";
    //Za Regular User - Conference Agenda
    private ModeratorBean conferenceAgendaBean = null;
    //Za Regular User - My Agenda
    private RegularUserMyAgenda  regularUserMyAgendaList = null;
    private String regularUserMyAgendamessage = "";
    private boolean leavecommentrender = false;
    private String myagendacomment = "";
    private String leavegradefield = "";
    private UploadedFile addOpenCeremonyImageFile = null;
    private UploadedFile addCloseCeremonyImageFile = null;
    private UploadedFile addWorkshopImageFile = null;
    //Za Regular User - Conferece Visitors
    private List<User> conferencevisitorslist = null;
    private boolean conferencevisitorprofilerender = false;
    private User conferencevisitorprofile = null;
    private boolean conferencevisitorSendMessagerender = false;
    private User conferencevisitorMessageReceiver = null;
    private String conferencevisitorMessageContent = "";
    private String conferencevisitorsString = "";
    //Za Regular User - Mailbox
    private List<Messages> mymailbox = null;
    //Za Regular User - Gallery
    private List<Integer> gallerydays = new ArrayList<Integer>();
    private List<Gallery> gallerylist = null;
    private List<Gallery> wholeConferenceGallerylist = null;
    private int openGalleryconferenceid;
    private List<Gallery> sessionDayGalleryImages = null;
    private List<Sessions> sessionDaySessions = null;
    private int conferencedaynum;
    private List<Gallery> sessionGalleryImages = null;
    private Sessions choosenSession = null;
    //Za Regular User - My Agenda - Lecture - UploadFile
    private UploadedFile LectureUploadFile = null;
    //Za Regular User - Download files
    private List<LectureFileDownloadBean> lecturefileDownloadlist = null;
//  Za testiranje
    private String emptydatetestmessage = "";
//

    public ModeratorBean getConferenceAgendaBean() {
        return conferenceAgendaBean;
    }

    public void setConferenceAgendaBean(ModeratorBean conferenceAgendaBean) {
        this.conferenceAgendaBean = conferenceAgendaBean;
    }

    public ModeratorBean getModeratorpageModeratorBeanSessionAdd() {
        return moderatorpageModeratorBeanSessionAdd;
    }

    public void setModeratorpageModeratorBeanSessionAdd(ModeratorBean moderatorpageModeratorBeanSessionAdd) {
        this.moderatorpageModeratorBeanSessionAdd = moderatorpageModeratorBeanSessionAdd;
    }

    public ModeratorBean getConferencetoshow() {
        return conferencetoshow;
    }

    public void setConferencetoshow(ModeratorBean conferencetoshow) {
        this.conferencetoshow = conferencetoshow;
    }

    public String getAdminmodemessage() {
        return adminmodemessage;
    }

    public void setAdminmodemessage(String adminmodemessage) {
        this.adminmodemessage = adminmodemessage;
    }

    public Map<String, String> getRegularuserCityMap() {
        return regularuserCityMap;
    }

    public void setRegularuserCityMap(Map<String, String> regularuserCityMap) {
        this.regularuserCityMap = regularuserCityMap;
    }

    public String getEmptydatetestmessage() {
        return emptydatetestmessage;
    }

    public void setEmptydatetestmessage(String emptydatetestmessage) {
        this.emptydatetestmessage = emptydatetestmessage;
    }

    public List<LectureFileDownloadBean> getLecturefileDownloadlist() {
        return lecturefileDownloadlist;
    }

    public void setLecturefileDownloadlist(List<LectureFileDownloadBean> lecturefileDownloadlist) {
        this.lecturefileDownloadlist = lecturefileDownloadlist;
    }

    public UploadedFile getLectureUploadFile() {
        return LectureUploadFile;
    }

    public void setLectureUploadFile(UploadedFile LectureUploadFile) {
        this.LectureUploadFile = LectureUploadFile;
    }

    public boolean isAmiauthor() {
        return amiauthor;
    }

    public void setAmiauthor(boolean amiauthor) {
        this.amiauthor = amiauthor;
    }

    public UploadedFile getAddOpenCeremonyImageFile() {
        return addOpenCeremonyImageFile;
    }

    public void setAddOpenCeremonyImageFile(UploadedFile addOpenCeremonyImageFile) {
        this.addOpenCeremonyImageFile = addOpenCeremonyImageFile;
    }

    public UploadedFile getAddCloseCeremonyImageFile() {
        return addCloseCeremonyImageFile;
    }

    public void setAddCloseCeremonyImageFile(UploadedFile addCloseCeremonyImageFile) {
        this.addCloseCeremonyImageFile = addCloseCeremonyImageFile;
    }

    public UploadedFile getAddWorkshopImageFile() {
        return addWorkshopImageFile;
    }

    public void setAddWorkshopImageFile(UploadedFile addWorkshopImageFile) {
        this.addWorkshopImageFile = addWorkshopImageFile;
    }

    public Sessions getChoosenSession() {
        return choosenSession;
    }

    public void setChoosenSession(Sessions choosenSession) {
        this.choosenSession = choosenSession;
    }

    public List<Gallery> getSessionGalleryImages() {
        return sessionGalleryImages;
    }

    public void setSessionGalleryImages(List<Gallery> sessionGalleryImages) {
        this.sessionGalleryImages = sessionGalleryImages;
    }

    public int getConferencedaynum() {
        return conferencedaynum;
    }

    public void setConferencedaynum(int conferencedaynum) {
        this.conferencedaynum = conferencedaynum;
    }

    public List<Sessions> getSessionDaySessions() {
        return sessionDaySessions;
    }

    public void setSessionDaySessions(List<Sessions> sessionDaySessions) {
        this.sessionDaySessions = sessionDaySessions;
    }

    public String getNewSessionAddName() {
        return newSessionAddName;
    }

    public void setNewSessionAddName(String newSessionAddName) {
        this.newSessionAddName = newSessionAddName;
    }

    public List<Gallery> getSessionDayGalleryImages() {
        return sessionDayGalleryImages;
    }

    public void setSessionDayGalleryImages(List<Gallery> sessionDayGalleryImages) {
        this.sessionDayGalleryImages = sessionDayGalleryImages;
    }

    public Date getNewWorkshopDate() {
        return newWorkshopDate;
    }

    public void setNewWorkshopDate(Date newWorkshopDate) {
        this.newWorkshopDate = newWorkshopDate;
    }

    public Date getNewOpenCeremonyDate() {
        return newOpenCeremonyDate;
    }

    public void setNewOpenCeremonyDate(Date newOpenCeremonyDate) {
        this.newOpenCeremonyDate = newOpenCeremonyDate;
    }

    public Date getNewCloseCeremonyDate() {
        return newCloseCeremonyDate;
    }

    public void setNewCloseCeremonyDate(Date newCloseCeremonyDate) {
        this.newCloseCeremonyDate = newCloseCeremonyDate;
    }

    public List<Integer> getGallerydays() {
        return gallerydays;
    }

    public void setGallerydays(List<Integer> gallerydays) {
        this.gallerydays = gallerydays;
    }

    public int getOpenGalleryconferenceid() {
        return openGalleryconferenceid;
    }

    public void setOpenGalleryconferenceid(int openGalleryconferenceid) {
        this.openGalleryconferenceid = openGalleryconferenceid;
    }

    public List<Gallery> getGallerylist() {
        return gallerylist;
    }

    public void setGallerylist(List<Gallery> gallerylist) {
        this.gallerylist = gallerylist;
    }

    public List<Gallery> getWholeConferenceGallerylist() {
        return wholeConferenceGallerylist;
    }

    public void setWholeConferenceGallerylist(List<Gallery> wholeConferenceGallerylist) {
        this.wholeConferenceGallerylist = wholeConferenceGallerylist;
    }

    public List<Messages> getMymailbox() {
        return mymailbox;
    }

    public void setMymailbox(List<Messages> mymailbox) {
        this.mymailbox = mymailbox;
    }

    public String getConferencevisitorsString() {
        return conferencevisitorsString;
    }

    public void setConferencevisitorsString(String conferencevisitorsString) {
        this.conferencevisitorsString = conferencevisitorsString;
    }

    public String getConferencevisitorMessageContent() {
        return conferencevisitorMessageContent;
    }

    public void setConferencevisitorMessageContent(String conferencevisitorMessageContent) {
        this.conferencevisitorMessageContent = conferencevisitorMessageContent;
    }

    public boolean isConferencevisitorSendMessagerender() {
        return conferencevisitorSendMessagerender;
    }

    public void setConferencevisitorSendMessagerender(boolean conferencevisitorSendMessagerender) {
        this.conferencevisitorSendMessagerender = conferencevisitorSendMessagerender;
    }

    public User getConferencevisitorMessageReceiver() {
        return conferencevisitorMessageReceiver;
    }

    public void setConferencevisitorMessageReceiver(User conferencevisitorMessageReceiver) {
        this.conferencevisitorMessageReceiver = conferencevisitorMessageReceiver;
    }
    
    public boolean isConferencevisitorprofilerender() {
        return conferencevisitorprofilerender;
    }

    public void setConferencevisitorprofilerender(boolean conferencevisitorprofilerender) {
        this.conferencevisitorprofilerender = conferencevisitorprofilerender;
    }

    public User getConferencevisitorprofile() {
        return conferencevisitorprofile;
    }

    public void setConferencevisitorprofile(User conferencevisitorprofile) {
        this.conferencevisitorprofile = conferencevisitorprofile;
    }

    public List<User> getConferencevisitorslist() {
        return conferencevisitorslist;
    }

    public void setConferencevisitorslist(List<User> conferencevisitorslist) {
        this.conferencevisitorslist = conferencevisitorslist;
    }

    public String getLeavegradefield() {
        return leavegradefield;
    }

    public void setLeavegradefield(String leavegradefield) {
        this.leavegradefield = leavegradefield;
    }

    public String getEntryCode() {
        return EntryCode;
    }

    public void setEntryCode(String EntryCode) {
        this.EntryCode = EntryCode;
    }

    public String getMyagendacomment() {
        return myagendacomment;
    }

    public void setMyagendacomment(String myagendacomment) {
        this.myagendacomment = myagendacomment;
    }

    public boolean isLeavecommentrender() {
        return leavecommentrender;
    }

    public void setLeavecommentrender(boolean leavecommentrender) {
        this.leavecommentrender = leavecommentrender;
    }

    public String getRegularUserMyAgendamessage() {
        return regularUserMyAgendamessage;
    }

    public void setRegularUserMyAgendamessage(String regularUserMyAgendamessage) {
        this.regularUserMyAgendamessage = regularUserMyAgendamessage;
    }

    public RegularUserMyAgenda getRegularUserMyAgendaList() {
        return regularUserMyAgendaList;
    }

    public void setRegularUserMyAgendaList(RegularUserMyAgenda regularUserMyAgendaList) {
        this.regularUserMyAgendaList = regularUserMyAgendaList;
    }

    public boolean isShowCodeinputtextfield() {
        return showCodeinputtextfield;
    }

    public void setShowCodeinputtextfield(boolean showCodeinputtextfield) {
        this.showCodeinputtextfield = showCodeinputtextfield;
    }

    public String getRegularUserMyConferencesmessage() {
        return regularUserMyConferencesmessage;
    }

    public void setRegularUserMyConferencesmessage(String regularUserMyConferencesmessage) {
        this.regularUserMyConferencesmessage = regularUserMyConferencesmessage;
    }

    public boolean isRegularUserMyConferencesrender() {
        return regularUserMyConferencesrender;
    }

    public void setRegularUserMyConferencesrender(boolean regularUserMyConferencesrender) {
        this.regularUserMyConferencesrender = regularUserMyConferencesrender;
    }

    public List<ModeratorBean> getRegularUserMyConferencesList() {
        return regularUserMyConferencesList;
    }

    public void setRegularUserMyConferencesList(List<ModeratorBean> regularUserMyConferencesList) {
        this.regularUserMyConferencesList = regularUserMyConferencesList;
    }

    public ModeratorBeanConferenceContent getMyConferencesAgendaView() {
        return myConferencesAgendaView;
    }

    public void setMyConferencesAgendaView(ModeratorBeanConferenceContent myConferencesAgendaView) {
        this.myConferencesAgendaView = myConferencesAgendaView;
    }

    public RegularUserSearch getMyConferencesAgenda() {
        return myConferencesAgenda;
    }

    public void setMyConferencesAgenda(RegularUserSearch myConferencesAgenda) {
        this.myConferencesAgenda = myConferencesAgenda;
    }

    public String getRegularuserAddConferenceToMyAgendaMessage() {
        return regularuserAddConferenceToMyAgendaMessage;
    }

    public void setRegularuserAddConferenceToMyAgendaMessage(String regularuserAddConferenceToMyAgendaMessage) {
        this.regularuserAddConferenceToMyAgendaMessage = regularuserAddConferenceToMyAgendaMessage;
    }

    public String getShowSessionsMessage() {
        return showSessionsMessage;
    }

    public void setShowSessionsMessage(String showSessionsMessage) {
        this.showSessionsMessage = showSessionsMessage;
    }

    public UploadedFile getAddSessionImageFile() {
        return addSessionImageFile;
    }

    public void setAddSessionImageFile(UploadedFile addSessionImageFile) {
        this.addSessionImageFile = addSessionImageFile;
    }

    public boolean isSessionaddimagemod() {
        return sessionaddimagemod;
    }

    public void setSessionaddimagemod(boolean sessionaddimagemod) {
        this.sessionaddimagemod = sessionaddimagemod;
    }

    public SessionHallBean getImageSession() {
        return ImageSession;
    }

    public void setImageSession(SessionHallBean ImageSession) {
        this.ImageSession = ImageSession;
    }

    public UploadedFile getAddConferenceImageFile() {
        return addConferenceImageFile;
    }

    public void setAddConferenceImageFile(UploadedFile addConferenceImageFile) {
        this.addConferenceImageFile = addConferenceImageFile;
    }

    public boolean isAddConferenceImageCheckBox() {
        return addConferenceImageCheckBox;
    }

    public void setAddConferenceImageCheckBox(boolean addConferenceImageCheckBox) {
        this.addConferenceImageCheckBox = addConferenceImageCheckBox;
    }

    public Map<Integer, Integer> getModeratorAddConferenceImagesDayList() {
        return moderatorAddConferenceImagesDayList;
    }

    public void setModeratorAddConferenceImagesDayList(Map<Integer, Integer> moderatorAddConferenceImagesDayList) {
        this.moderatorAddConferenceImagesDayList = moderatorAddConferenceImagesDayList;
    }

    public int getModeratorAddConferenceImagesDay() {
        return moderatorAddConferenceImagesDay;
    }

    public void setModeratorAddConferenceImagesDay(int moderatorAddConferenceImagesDay) {
        this.moderatorAddConferenceImagesDay = moderatorAddConferenceImagesDay;
    }

    public String getModeratorAddConferenceImagesMessage() {
        return moderatorAddConferenceImagesMessage;
    }

    public void setModeratorAddConferenceImagesMessage(String moderatorAddConferenceImagesMessage) {
        this.moderatorAddConferenceImagesMessage = moderatorAddConferenceImagesMessage;
    }

    public Agenda getModeratorAddConferenceImagesAgenda() {
        return moderatorAddConferenceImagesAgenda;
    }

    public void setModeratorAddConferenceImagesAgenda(Agenda moderatorAddConferenceImagesAgenda) {
        this.moderatorAddConferenceImagesAgenda = moderatorAddConferenceImagesAgenda;
    }

    public boolean isModeratorpageAddConferenceImagesrender() {
        return moderatorpageAddConferenceImagesrender;
    }

    public void setModeratorpageAddConferenceImagesrender(boolean moderatorpageAddConferenceImagesrender) {
        this.moderatorpageAddConferenceImagesrender = moderatorpageAddConferenceImagesrender;
    }

    public boolean isModeratorpageCloseCeremonyrender() {
        return moderatorpageCloseCeremonyrender;
    }

    public void setModeratorpageCloseCeremonyrender(boolean moderatorpageCloseCeremonyrender) {
        this.moderatorpageCloseCeremonyrender = moderatorpageCloseCeremonyrender;
    }

    public Agenda getModeratornewCloseCeremonyAgenda() {
        return moderatornewCloseCeremonyAgenda;
    }

    public void setModeratornewCloseCeremonyAgenda(Agenda moderatornewCloseCeremonyAgenda) {
        this.moderatornewCloseCeremonyAgenda = moderatornewCloseCeremonyAgenda;
    }

    public Date getAddCloseCeremonyBeginTime() {
        return addCloseCeremonyBeginTime;
    }

    public void setAddCloseCeremonyBeginTime(Date addCloseCeremonyBeginTime) {
        this.addCloseCeremonyBeginTime = addCloseCeremonyBeginTime;
    }

    public Date getAddCloseCeremonyEndTime() {
        return addCloseCeremonyEndTime;
    }

    public void setAddCloseCeremonyEndTime(Date addCloseCeremonyEndTime) {
        this.addCloseCeremonyEndTime = addCloseCeremonyEndTime;
    }

    public Closeceremony getNewCloseCeremony() {
        return newCloseCeremony;
    }

    public void setNewCloseCeremony(Closeceremony newCloseCeremony) {
        this.newCloseCeremony = newCloseCeremony;
    }

    public String getNewCloseCeremonyMessage() {
        return newCloseCeremonyMessage;
    }

    public void setNewCloseCeremonyMessage(String newCloseCeremonyMessage) {
        this.newCloseCeremonyMessage = newCloseCeremonyMessage;
    }

    public String getCloseCeremonyType() {
        return closeCeremonyType;
    }

    public void setCloseCeremonyType(String closeCeremonyType) {
        this.closeCeremonyType = closeCeremonyType;
    }

    public String getNewOpenCeremonyMessage() {
        return newOpenCeremonyMessage;
    }

    public void setNewOpenCeremonyMessage(String newOpenCeremonyMessage) {
        this.newOpenCeremonyMessage = newOpenCeremonyMessage;
    }

    public boolean isModeratorpageOpenCeremonyrender() {
        return moderatorpageOpenCeremonyrender;
    }

    public void setModeratorpageOpenCeremonyrender(boolean moderatorpageOpenCeremonyrender) {
        this.moderatorpageOpenCeremonyrender = moderatorpageOpenCeremonyrender;
    }

    public Agenda getModeratornewOpenCeremonyAgenda() {
        return moderatornewOpenCeremonyAgenda;
    }

    public void setModeratornewOpenCeremonyAgenda(Agenda moderatornewOpenCeremonyAgenda) {
        this.moderatornewOpenCeremonyAgenda = moderatornewOpenCeremonyAgenda;
    }

    public Date getAddOpenCeremonyBeginTime() {
        return addOpenCeremonyBeginTime;
    }

    public void setAddOpenCeremonyBeginTime(Date addOpenCeremonyBeginTime) {
        this.addOpenCeremonyBeginTime = addOpenCeremonyBeginTime;
    }

    public Date getAddOpenCeremonyEndTime() {
        return addOpenCeremonyEndTime;
    }

    public void setAddOpenCeremonyEndTime(Date addOpenCeremonyEndTime) {
        this.addOpenCeremonyEndTime = addOpenCeremonyEndTime;
    }

    public Openceremony getNewOpenCeremony() {
        return newOpenCeremony;
    }

    public void setNewOpenCeremony(Openceremony newOpenCeremony) {
        this.newOpenCeremony = newOpenCeremony;
    }

    public String getOpenCeremonyType() {
        return openCeremonyType;
    }

    public void setOpenCeremonyType(String openCeremonyType) {
        this.openCeremonyType = openCeremonyType;
    }

    public boolean isModeratorpageAddWorkshoprender() {
        return moderatorpageAddWorkshoprender;
    }

    public void setModeratorpageAddWorkshoprender(boolean moderatorpageAddWorkshoprender) {
        this.moderatorpageAddWorkshoprender = moderatorpageAddWorkshoprender;
    }

    public Date getAddWorkshopBeginTime() {
        return addWorkshopBeginTime;
    }

    public void setAddWorkshopBeginTime(Date addWorkshopBeginTime) {
        this.addWorkshopBeginTime = addWorkshopBeginTime;
    }

    public Date getAddWorkshopEndTime() {
        return addWorkshopEndTime;
    }

    public void setAddWorkshopEndTime(Date addWorkshopEndTime) {
        this.addWorkshopEndTime = addWorkshopEndTime;
    }

    public Workshop getNewWorkshop() {
        return newWorkshop;
    }

    public void setNewWorkshop(Workshop newWorkshop) {
        this.newWorkshop = newWorkshop;
    }

    public String getNewWorkshopTopic() {
        return newWorkshopTopic;
    }

    public void setNewWorkshopTopic(String newWorkshopTopic) {
        this.newWorkshopTopic = newWorkshopTopic;
    }

    public String getNewWorkshopMessage() {
        return newWorkshopMessage;
    }

    public void setNewWorkshopMessage(String newWorkshopMessage) {
        this.newWorkshopMessage = newWorkshopMessage;
    }

    public Agenda getModeratornewWorkshopAgenda() {
        return moderatornewWorkshopAgenda;
    }

    public void setModeratornewWorkshopAgenda(Agenda moderatornewWorkshopAgenda) {
        this.moderatornewWorkshopAgenda = moderatornewWorkshopAgenda;
    }

    public boolean isNewLectureCheckBox() {
        return newLectureCheckBox;
    }

    public void setNewLectureCheckBox(boolean newLectureCheckBox) {
        this.newLectureCheckBox = newLectureCheckBox;
    }

    public Lecture getNewLecture() {
        return newLecture;
    }

    public void setNewLecture(Lecture newLecture) {
        this.newLecture = newLecture;
    }

    public String getNewLectureName() {
        return newLectureName;
    }

    public void setNewLectureName(String newLectureName) {
        this.newLectureName = newLectureName;
    }

    public String getExternalAuthor1() {
        return externalAuthor1;
    }

    public void setExternalAuthor1(String externalAuthor1) {
        this.externalAuthor1 = externalAuthor1;
    }

    public String getExternalAuthor2() {
        return externalAuthor2;
    }

    public void setExternalAuthor2(String externalAuthor2) {
        this.externalAuthor2 = externalAuthor2;
    }

    public String getExternalAuthor3() {
        return externalAuthor3;
    }

    public void setExternalAuthor3(String externalAuthor3) {
        this.externalAuthor3 = externalAuthor3;
    }

    public String getExternalAuthor4() {
        return externalAuthor4;
    }

    public void setExternalAuthor4(String externalAuthor4) {
        this.externalAuthor4 = externalAuthor4;
    }

    public String[] getNewLectureAuthors() {
        return newLectureAuthors;
    }

    public void setNewLectureAuthors(String[] newLectureAuthors) {
        this.newLectureAuthors = newLectureAuthors;
    }

    public String getNewlectureMessage() {
        return newlectureMessage;
    }

    public void setNewlectureMessage(String newlectureMessage) {
        this.newlectureMessage = newlectureMessage;
    }

    public Map<String, String> getAddLectureAuthorList() {
        return addLectureAuthorList;
    }

    public void setAddLectureAuthorList(Map<String, String> addLectureAuthorList) {
        this.addLectureAuthorList = addLectureAuthorList;
    }

    public Date getAddLectureBeginTime() {
        return addLectureBeginTime;
    }

    public void setAddLectureBeginTime(Date addLectureBeginTime) {
        this.addLectureBeginTime = addLectureBeginTime;
    }

    public Date getAddLectureEndTime() {
        return addLectureEndTime;
    }

    public void setAddLectureEndTime(Date addLectureEndTime) {
        this.addLectureEndTime = addLectureEndTime;
    }

    public SessionHallBean getNewsessionLecture() {
        return newsessionLecture;
    }

    public void setNewsessionLecture(SessionHallBean newsessionLecture) {
        this.newsessionLecture = newsessionLecture;
    }

    public boolean isModeratorpageAddSessionLecturerender() {
        return moderatorpageAddSessionLecturerender;
    }

    public void setModeratorpageAddSessionLecturerender(boolean moderatorpageAddSessionLecturerender) {
        this.moderatorpageAddSessionLecturerender = moderatorpageAddSessionLecturerender;
    }

    public int getNewsessionHall() {
        return newsessionHall;
    }

    public void setNewsessionHall(int newsessionHall) {
        this.newsessionHall = newsessionHall;
    }

    public String getNewsessionMessage() {
        return newsessionMessage;
    }

    public void setNewsessionMessage(String newsessionMessage) {
        this.newsessionMessage = newsessionMessage;
    }

    public Date getNewsessionBeginTime() {
        return newsessionBeginTime;
    }

    public void setNewsessionBeginTime(Date newsessionBeginTime) {
        this.newsessionBeginTime = newsessionBeginTime;
    }

    public Date getNewsessionEndTime() {
        return newsessionEndTime;
    }

    public void setNewsessionEndTime(Date newsessionEndTime) {
        this.newsessionEndTime = newsessionEndTime;
    }

    public Map<String, Integer> getNewSessionHalllist() {
        return newSessionHalllist;
    }

    public void setNewSessionHalllist(Map<String, Integer> newSessionHalllist) {
        this.newSessionHalllist = newSessionHalllist;
    }

    public Date getNewsessionDate() {
        return newsessionDate;
    }

    public void setNewsessionDate(Date newsessionDate) {
        this.newsessionDate = newsessionDate;
    }

    public Sessions getModeratorpageSessionToAdd() {
        return moderatorpageSessionToAdd;
    }

    public void setModeratorpageSessionToAdd(Sessions moderatorpageSessionToAdd) {
        this.moderatorpageSessionToAdd = moderatorpageSessionToAdd;
    }

    public Agenda getModeratopageAddEventAgenda() {
        return moderatopageAddEventAgenda;
    }

    public void setModeratopageAddEventAgenda(Agenda moderatopageAddEventAgenda) {
        this.moderatopageAddEventAgenda = moderatopageAddEventAgenda;
    }

    public boolean isModeratorpageaddsessionrender() {
        return moderatorpageaddsessionrender;
    }

    public void setModeratorpageaddsessionrender(boolean moderatorpageaddsessionrender) {
        this.moderatorpageaddsessionrender = moderatorpageaddsessionrender;
    }

    public boolean isModeratorpageviewconferenceprogramrender() {
        return moderatorpageviewconferenceprogramrender;
    }

    public void setModeratorpageviewconferenceprogramrender(boolean moderatorpageviewconferenceprogramrender) {
        this.moderatorpageviewconferenceprogramrender = moderatorpageviewconferenceprogramrender;
    }

    public ModeratorBeanConferenceContent getModeratorbeanconferencecontent() {
        return moderatorbeanconferencecontent;
    }

    public void setModeratorbeanconferencecontent(ModeratorBeanConferenceContent moderatorbeanconferencecontent) {
        this.moderatorbeanconferencecontent = moderatorbeanconferencecontent;
    }

    public List<ModeratorBean> getModeratorbeanlist() {
        return moderatorbeanlist;
    }

    public void setModeratorbeanlist(List<ModeratorBean> moderatorbeanlist) {
        this.moderatorbeanlist = moderatorbeanlist;
    }

    public String getTestmessage() {
        return testmessage;
    }

    public void setTestmessage(String testmessage) {
        this.testmessage = testmessage;
    }
   
    public boolean isAdmindeleteconferencerendered() {
        return admindeleteconferencerendered;
    }

    public void setAdmindeleteconferencerendered(boolean admindeleteconferencerendered) {
        this.admindeleteconferencerendered = admindeleteconferencerendered;
    }

    public List<GuestModeBean> getAdminDeleteConferenceAlllist() {
        return adminDeleteConferenceAlllist;
    }

    public void setAdminDeleteConferenceAlllist(List<GuestModeBean> adminDeleteConferenceAlllist) {
        this.adminDeleteConferenceAlllist = adminDeleteConferenceAlllist;
    }
   
    public String[] getModerators() {
        return moderators;
    }

    public void setModerators(String[] moderators) {
        this.moderators = moderators;
    }

    public Map<String, String> getAdminAddConferenceUsernamelist() {
        return adminAddConferenceUsernamelist;
    }

    public void setAdminAddConferenceUsernamelist(Map<String, String> adminAddConferenceUsernamelist) {
        this.adminAddConferenceUsernamelist = adminAddConferenceUsernamelist;
    }

    public Map<String, String> getAdminAddConferenceCitylist() {
        return adminAddConferenceCitylist;
    }

    public void setAdminAddConferenceCitylist(Map<String, String> adminAddConferenceCitylist) {
        this.adminAddConferenceCitylist = adminAddConferenceCitylist;
    }

    public Map<String, String> getAdminAddConferenceLocationlist() {
        return adminAddConferenceLocationlist;
    }

    public void setAdminAddConferenceLocationlist(Map<String, String> adminAddConferenceLocationlist) {
        this.adminAddConferenceLocationlist = adminAddConferenceLocationlist;
    }

   

    public String getAdminAddConferenceCity() {
        return adminAddConferenceCity;
    }

    public void setAdminAddConferenceCity(String adminAddConferenceCity) {
        this.adminAddConferenceCity = adminAddConferenceCity;
    }

    public String getAdminAddConferenceName() {
        return adminAddConferenceName;
    }

    public void setAdminAddConferenceName(String adminAddConferenceName) {
        this.adminAddConferenceName = adminAddConferenceName;
    }

    public String getAdminAddConferenceField() {
        return adminAddConferenceField;
    }

    public void setAdminAddConferenceField(String adminAddConferenceField) {
        this.adminAddConferenceField = adminAddConferenceField;
    }

    public String getAdminAddConferenceLocation() {
        return adminAddConferenceLocation;
    }

    public void setAdminAddConferenceLocation(String adminAddConferenceLocation) {
        this.adminAddConferenceLocation = adminAddConferenceLocation;
    }

    public Date getAdminAddConferenceDateBegin() {
        return adminAddConferenceDateBegin;
    }

    public void setAdminAddConferenceDateBegin(Date adminAddConferenceDateBegin) {
        this.adminAddConferenceDateBegin = adminAddConferenceDateBegin;
    }

    public Date getAdminAddConferenceDateEnd() {
        return adminAddConferenceDateEnd;
    }

    public void setAdminAddConferenceDateEnd(Date adminAddConferenceDateEnd) {
        this.adminAddConferenceDateEnd = adminAddConferenceDateEnd;
    }
   
    public boolean isAdminaddconferencerendered() {
        return adminaddconferencerendered;
    }

    public void setAdminaddconferencerendered(boolean adminaddconferencerendered) {
        this.adminaddconferencerendered = adminaddconferencerendered;
    }

    public boolean isRegularusersearchrendered() {
        return regularusersearchrendered;
    }

    public void setRegularusersearchrendered(boolean regularusersearchrendered) {
        this.regularusersearchrendered = regularusersearchrendered;
    }

    public List<RegularUserSearch> getRegularusersearchlist() {
        return regularusersearchlist;
    }

    public void setRegularusersearchlist(List<RegularUserSearch> regularusersearchlist) {
        this.regularusersearchlist = regularusersearchlist;
    }
   

    public String getSearchFieldregularuser() {
        return searchFieldregularuser;
    }

    public void setSearchFieldregularuser(String searchFieldregularuser) {
        this.searchFieldregularuser = searchFieldregularuser;
    }

    public String getSearchLocationdregularuser() {
        return searchLocationdregularuser;
    }

    public void setSearchLocationdregularuser(String searchLocationdregularuser) {
        this.searchLocationdregularuser = searchLocationdregularuser;
    }

    public String getSearchNameregularuser() {
        return searchNameregularuser;
    }

    public void setSearchNameregularuser(String searchNameregularuser) {
        this.searchNameregularuser = searchNameregularuser;
    }

    public Date getSearchDateBeginregularuser() {
        return searchDateBeginregularuser;
    }

    public void setSearchDateBeginregularuser(Date searchDateBeginregularuser) {
        this.searchDateBeginregularuser = searchDateBeginregularuser;
    }

    public Date getSearchDateEndregularuser() {
        return searchDateEndregularuser;
    }

    public void setSearchDateEndregularuser(Date searchDateEndregularuser) {
        this.searchDateEndregularuser = searchDateEndregularuser;
    }

    public boolean isRegistrationfromguestmode() {
        return registrationfromguestmode;
    }

    public void setRegistrationfromguestmode(boolean registrationfromguestmode) {
        this.registrationfromguestmode = registrationfromguestmode;
    }

    public boolean isGuestmodesearchrendered() {
        return guestmodesearchrendered;
    }

    public void setGuestmodesearchrendered(boolean guestmodesearchrendered) {
        this.guestmodesearchrendered = guestmodesearchrendered;
    }

    public List<GuestModeBean> getGuestsearchlist() {
        return guestsearchlist;
    }

    public void setGuestsearchlist(List<GuestModeBean> guestsearchlist) {
        this.guestsearchlist = guestsearchlist;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public Date getSearchDateBegin() {
        return searchDateBegin;
    }

    public void setSearchDateBegin(Date searchDateBegin) {
        this.searchDateBegin = searchDateBegin;
    }

    public Date getSearchDateEnd() {
        return searchDateEnd;
    }

    public void setSearchDateEnd(Date searchDateEnd) {
        this.searchDateEnd = searchDateEnd;
    }

    
   
//Za sada samo ovde radim helper = new HibernateHelper()
   private HibernateHelper helper = new HibernateHelper();

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String getSigninporuka() {
        return signinporuka;
    }

    public void setSigninporuka(String signinporuka) {
        this.signinporuka = signinporuka;
    }

   
    public String getConfirmpassword() {
        return confirmpassword;
    }

    public void setConfirmpassword(String confirmpassword) {
        this.confirmpassword = confirmpassword;
    }

    public User getNewUser() {
        return newUser;
    }

    public void setNewUser(User newUser) {
        this.newUser = newUser;
    }

    public String getPorukachangepassword() {
        return porukachangepassword;
    }

    public void setPorukachangepassword(String porukachangepassword) {
        this.porukachangepassword = porukachangepassword;
    }

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public HibernateHelper getHelper() {
        return helper;
    }

    public void setHelper(HibernateHelper helper) {
        this.helper = helper;
    }
    
   
    public String getUsername() {
        return username;
    }

    public void setUsername(String user) {
        this.username = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }
    
     public String LogIn() {
        
         //adresa stranice na koju cu ici ako logovanje uspe 
         String address;
         //helper =  new HibernateHelper();
         user = helper.LoginHB(username, password);
         amiauthor = helper.findMeinAuthor(user);
         if(user == null) { poruka="Incorrect username, please try again."; }
         else { 
             if(user.getPassword().equals(password)){
                 poruka = "";
                 if(user.getType().equals("m")) { moderatorbeanlist = helper.GetModeratorConferences(user); return "moderatorwelcomepage"; }
                 else if(user.getType().equals("a")) { return "adminpage"; }
                 else if(user.getType().equals("r")) { regularuserCityMap = helper.AdminAddConferenceGetCity();  return "regularuserpage"; }
                 
             }else { poruka = "Incorrect password, please try again."; }
                 }
         
         return "";
    }
    
     
    public String LogChangePassword(){
    
        //adresa stranice na koju cu ici ako logovanje uspe 
         String address;
         
         //helper = new HibernateHelper();
         user = helper.LoginHB(username, password);
         if(user == null) { porukachangepassword="Incorrect username, please try again."; }
         else { 
             if(!(user.getPassword().equals(password))){
                 porukachangepassword = "Incorrect password, please try again."; 
                 
             }else{
                 porukachangepassword = "";
             user.setPassword(newpassword);
                 helper.ChangePassword(user);
                 return "login";
             }
                }
         
         return "";
    
    }
     
    public String SignIn(){
    
        if(!(newUser.getPassword().equals(confirmpassword))){
            
            signinporuka = "Confirm password is not same as password.\n Try again";
        }
        else if(newUser.getUsername().equals("")) { signinporuka = "Username can not be empty.";}
        else if(newUser.getEmail().equals("")) { signinporuka = "Email can not be empty.";}
        else{
        signinporuka = "";
        newUser.setType("r");
        String fileName = file.getFileName(); 
        //String contentType = uploadedFile.getContentType();
        byte[] contents = file.getContents();


//byte[] image = new byte[(int)file.getSize()];
      /* try {
        FileInputStream fileInputStream = new FileInputStream(file.getFileName());
        fileInputStream.read(image);
        fileInputStream.close();
        }catch (Exception e) {
    e.printStackTrace();
}      */
        newUser.setProfilePicture(contents);
        signinporuka = helper.SingInHB(newUser);
        if(!(signinporuka.equals("OK"))) { return null;}
        else{
        user = newUser;
        newUser = new User();
        regularuserCityMap = helper.AdminAddConferenceGetCity();
        return "regularuserpage";
        }
        }
        return null;
    }
 
    public void SearchGuestMode(){
    
     guestsearchlist = null;
     guestsearchlist = helper.SearchGuestModeHB(searchName, searchDateBegin, searchDateEnd);
     guestmodesearchrendered = true;
     searchName = "";
     searchDateBegin = null;
     searchDateEnd = null;
             }

    public void SearchGuestModeTodayConferences(){
     
     guestsearchlist = null;
     guestsearchlist = helper.SearchGuestModeTodayConferencesHB();
     guestmodesearchrendered = true;
    
    }
    
    public void SearchGuestModeMonthsConferences(){
     
     guestsearchlist = null;
     guestsearchlist = helper.SearchGuestModeMonthsConferencesHB();
     guestmodesearchrendered = true;
    
    }
    
    public String GuestModeRegistrationLink() {
        
        registrationfromguestmode = true;
        return "signinpage";
    }

    public String LogOut() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        session.invalidate();
        return "login";
    }
    
    public String RegularUserMyConferences(){
        
        regularUserMyConferencesList = helper.GetRegularUserMyConferenceList(user.getUsername());
        //searchFieldregularuser = "";
        //searchLocationdregularuser = "";
        return "regularusermyconferences";
    }
    
   public String RegularUserMailbox(){
       
       mymailbox = helper.GetMyMailbox(user.getUsername());
       return "regularusermailbox";
   }
    
   public void RegularUserSearchPage(){
      if(searchDateBeginregularuser != null && searchDateEndregularuser != null){
      if(searchDateBeginregularuser.after(searchDateEndregularuser)) { emptydatetestmessage = "Search begin date can not be after search end date. Please try again." ;}
      else { 
     regularusersearchlist = null;
     regularusersearchlist = helper.SearchRegularUser(searchNameregularuser, searchLocationdregularuser, searchDateBeginregularuser, searchDateEndregularuser, searchFieldregularuser);
     regularusersearchrendered = true;
     searchNameregularuser = "";
    // searchFieldregularuser = "";
     //searchLocationdregularuser = "";
     searchDateBeginregularuser = null;
     searchDateEndregularuser = null;  
     regularuserAddConferenceToMyAgendaMessage = "";
     regularuserCityMap = helper.AdminAddConferenceGetCity();
      }  
      }else { 
     regularusersearchlist = null;
     regularusersearchlist = helper.SearchRegularUser(searchNameregularuser, searchLocationdregularuser, searchDateBeginregularuser, searchDateEndregularuser, searchFieldregularuser);
     regularusersearchrendered = true;
     searchNameregularuser = "";
     //searchFieldregularuser = "";
     //searchLocationdregularuser = "";
     searchDateBeginregularuser = null;
     searchDateEndregularuser = null;  
     regularuserAddConferenceToMyAgendaMessage = "";
     regularuserCityMap = helper.AdminAddConferenceGetCity();
      }
}

   public void RegularUserCloseTable(){
    regularusersearchrendered = false;
    regularuserAddConferenceToMyAgendaMessage = "";
    showCodeinputtextfield = false;
    EntryCode = "";
   }

   public void RegularUserAddToMyConferences(RegularUserSearch d){
   
       if(EntryCode.equals("")) { regularuserAddConferenceToMyAgendaMessage = "You have not entered entry code. Please try again."; }
       else 
       { 
         int ec = Integer.parseInt(EntryCode);
       if(ec != d.getConferenceid()) { regularuserAddConferenceToMyAgendaMessage = "You have entered wrong conference entry key. Please try again.";}
      //Myconferences myconference = new Myconferences(user.getUsername(),d.getConferenceid());
       else{  regularuserAddConferenceToMyAgendaMessage = helper.AddToMyConference(d, user.getUsername());
      //regularuserAddConferenceToMyAgendaMessage = "You have sucessfully added conference to your conference list";
      showCodeinputtextfield = false;
      d.setRenderedforentrycode(false);
      EntryCode = "";
       }
        }
  }

   public void AdminAddConferenceSetRendered(){
       adminaddconferencerendered = true;
       adminAddConferenceLocationlist = helper.AdminAddConferenceGetLocations();
       adminAddConferenceCitylist = helper.AdminAddConferenceGetCity();
       adminAddConferenceUsernamelist = helper.AdminAddConferenceGetUsernames();
       adminAddConferenceUsernamelist.remove(user.getUsername());
   }
   
   public void AdminModeAddConference(){
   
       if(adminAddConferenceDateBegin.after(adminAddConferenceDateEnd)) { adminmodemessage = "Error! Conference begin date can not be after conference end date."; }
       else {
           adminmodemessage = helper.AdminAddConference(adminAddConferenceName, adminAddConferenceField, adminAddConferenceLocation, adminAddConferenceCity, adminAddConferenceDateBegin, adminAddConferenceDateEnd, moderators);
       }
   
   }
   
   public void AdminModeCloseAddConferenceTable(){
    adminaddconferencerendered = false;
    adminAddConferenceName = "";
    adminAddConferenceField = "";
    adminAddConferenceLocation = "";
    adminAddConferenceCity = "";
    adminAddConferenceDateBegin = null;
    adminAddConferenceDateEnd = null;
    adminAddConferenceLocationlist = null;
    adminAddConferenceCitylist = null;
    moderators = null;
    adminAddConferenceUsernamelist = null;
    adminmodemessage = "";
   }
   
   public void AdminDeleteConferenceSetRendered(){
       admindeleteconferencerendered = true;
       adminDeleteConferenceAlllist = helper.AdminDeleteAllConferenceList();
   }
 
   public void AdminModeCloseDeleteAllConferenceTable(){
       admindeleteconferencerendered = false;
       adminmodemessage = "";
   }
   
   public void adminModeCancelThisConference(GuestModeBean d){
   
       adminmodemessage = helper.adminModeCancelThisConferenceHB(d, user);
       adminDeleteConferenceAlllist = helper.AdminDeleteAllConferenceList();
       
   }
   
   public void RefreshLocationsInAddConference(){
   
      adminAddConferenceLocationlist = helper.RefreshLocationsInAddConferenceHB(adminAddConferenceCity);
       
   }
 
   public String ModeratorPageViewConferenceProgram(ModeratorBean d){
       showSessionsMessage = "";
       newlectureMessage = "";
       moderatorpageAddSessionLecturerender = false;
       conferencetoshow = d;
       moderatorbeanconferencecontent = helper.ModeratorPageViewConferenceProgramHB(d);
       moderatorpageviewconferenceprogramrender = true;
       return "moderatorpageviewconferenceprogram";
   }
   
   public void ModeratorPageViewConferenceProgramCloseView(){
   
         moderatorpageviewconferenceprogramrender = false;
         showSessionsMessage = "";
   }
   
  public String ModeratorPageAddSession(Agenda a, String country, String city, String place, String street, ModeratorBean mb){
  
      moderatorpageModeratorBeanSessionAdd = mb;
      newsessionMessage = "";
      newsessionDate = null;
      newsessionBeginTime = null;
      newsessionEndTime = null;
      moderatopageAddEventAgenda = a;
      moderatorpageSessionToAdd = new Sessions();
      moderatorpageSessionToAdd.setAgenda(a);
      newSessionHalllist = helper.ModeratorpPageAddSessionHallList(country, city, place, street);
      moderatorpageaddsessionrender = true;
      return "moderatoraddeventpage";
  }
 
  public String ModeratorPageAddSessionClosePanelGrid(){
       moderatorpageaddsessionrender = false;
       moderatorpageSessionToAdd = null;
       moderatopageAddEventAgenda = null;
       moderatorpageModeratorBeanSessionAdd = null;
       newsessionMessage = "";
       newSessionAddName = "";
       moderatorpageAddWorkshoprender = false;
       externalAuthor1 = "";
       externalAuthor2 = "";
       externalAuthor3 = "";
       externalAuthor4 = "";
       newWorkshopMessage = "";
       newLectureAuthors = null;
       newWorkshopDate = null;
       addOpenCeremonyBeginTime = null;
       addOpenCeremonyEndTime = null;
       moderatorpageOpenCeremonyrender = false;
       moderatornewOpenCeremonyAgenda = null;
       newOpenCeremonyMessage = "";
       openCeremonyType = "";
       newOpenCeremonyDate = null;
       addCloseCeremonyBeginTime = null;
       addCloseCeremonyEndTime = null;
       moderatorpageCloseCeremonyrender = false;
       moderatornewCloseCeremonyAgenda = null;
       newCloseCeremonyMessage = "";
       closeCeremonyType = "";
       newCloseCeremonyDate = null;
       return "moderatorpage";
  }    
  
  public void ModeratorPageAddSessionToDB(){
  
        String sessiondate = "";
        String begintime = "";
        String endtime = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdft = new SimpleDateFormat("HH:mm:ss");
        try {
            sessiondate = sdf.format(newsessionDate);
            newsessionDate = sdf.parse(sessiondate); //sdf.parse(sessiondate);
           
            endtime = sdft.format(newsessionEndTime);
            newsessionEndTime = sdft.parse(endtime);
            
            begintime = sdft.format(newsessionBeginTime);
            newsessionBeginTime = sdft.parse(begintime);
          } catch (Exception ex) {
            Logger.getLogger(HibernateHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(newsessionBeginTime.after(newsessionEndTime)){ newsessionMessage = "Error! Session begin time is after session end time!"; }
        else if(newsessionDate.before(moderatopageAddEventAgenda.getBeginDate())) { newsessionMessage = "Error! Session date can not be before conference begin date!"; } 
        else if(newsessionDate.after(moderatopageAddEventAgenda.getEndDate())) { newsessionMessage = "Error! Session date can not be after conference end date!"; } 
        else{
         int lenght = (int) (newsessionDate.getTime() - moderatopageAddEventAgenda.getBeginDate().getTime())/(1000*60*60*24);
         lenght++; // Radim lenght++ jer ako mi je sesija isti dan kada i pocetak konferencije onda za rezultat dobijam 0 a to je 1 dan konferencije
         moderatorpageSessionToAdd = new Sessions(moderatopageAddEventAgenda, newSessionAddName, newsessionHall, newsessionDate, newsessionBeginTime, newsessionEndTime, lenght);
         newsessionMessage = helper.ModeratorPageAddSessionToDBHP(moderatorpageSessionToAdd, sessiondate);  
         newSessionAddName = "";
        }
  }
  
  public void ModeratorPageAddSessionLectureRender(SessionHallBean d){
      
      newLectureName = "";
      newLectureCheckBox = false;
      externalAuthor1 = "";
      externalAuthor2 = "";
      externalAuthor3 = "";
      externalAuthor4 = "";
      newlectureMessage = "";
      newLectureAuthors = null;
      moderatorpageAddSessionLecturerender = true;
      newsessionLecture = d;//Ovo je sesija za koji cemo dodati predavanje
      addLectureAuthorList = helper.ModeratoraddLectureAuthorList(user.getUsername());
      newLecture = null;
  }

 public void  ModeratorPageAddSessionLectureCloseRender(){
     
      newLectureName = "";
      newLectureCheckBox = false;
      externalAuthor1 = "";
      externalAuthor2 = "";
      externalAuthor3 = "";
      externalAuthor4 = "";
      newlectureMessage = "";
      newLectureAuthors = null;
      moderatorpageAddSessionLecturerender = false;
      newsessionLecture = null;//Ovo je sesija za koji cemo dodati predavanje
      addLectureAuthorList = null;
      newLecture = null;
 }
  
 
 public void ModeratorPageAddSessionLectureToDB(){
       
        if(addLectureBeginTime.before(newsessionLecture.getSession().getBeginTime()) || addLectureEndTime.after(newsessionLecture.getSession().getEndTime())){
                newlectureMessage = "Error! Lecture time exceeds session time schedule.";
        }
        else {
        String begintime = "";
        String endtime = "";
        SimpleDateFormat sdft = new SimpleDateFormat("HH:mm:ss");
        int num = 0;
        long lenght = (addLectureEndTime.getTime() - addLectureBeginTime.getTime())/(1000*60);
        if(newLectureAuthors != null){
        num += newLectureAuthors.length;
        }
        
        if(!(externalAuthor1.equals(""))) { num++; }
        if(!(externalAuthor2.equals(""))) { num++; }
        if(!(externalAuthor3.equals(""))) { num++; }
        if(!(externalAuthor4.equals(""))) { num++; }
        
        try {
            endtime = sdft.format(addLectureEndTime);
            addLectureEndTime = sdft.parse(endtime);
            
            begintime = sdft.format(addLectureBeginTime);
            addLectureBeginTime = sdft.parse(begintime);
          } catch (Exception ex) {
            Logger.getLogger(HibernateHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(addLectureBeginTime.after(addLectureEndTime)){ newlectureMessage = "Error! Lecture begin time is after lecture end time!"; }
        else if(num > 4) { newlectureMessage = "Error! You have entered more than 4 authors!"; }
        else if(lenght > 30 && newLectureCheckBox == true) { newlectureMessage = "Error! Entry lecture can not be longer that 30 minutes!";}
        else if(lenght > 20 && newLectureCheckBox == false) { newlectureMessage = "Error! Lecture can not be longer that 20 minutes!";}
        else{
         newlectureMessage = "";
         newLecture = new Lecture(newsessionLecture.getSession(), newLectureName, addLectureBeginTime, addLectureEndTime);
         newlectureMessage = helper.ModeratorPageAddSessionLectureToDBHP(newLecture ,newLectureAuthors, externalAuthor1, externalAuthor2, externalAuthor3, externalAuthor4, begintime, endtime);
        }
        }     
 }
 
 public String ModeratorPageAddWorkshopRender(Agenda d, ModeratorBean mb){
 
     moderatorpageModeratorBeanSessionAdd = mb;
     moderatorpageAddWorkshoprender = true;
     moderatornewWorkshopAgenda = d;
     addLectureAuthorList = helper.ModeratoraddLectureAuthorList(user.getUsername());
     newWorkshopMessage = "";
     addWorkshopBeginTime = null;
     addWorkshopEndTime = null;
     newWorkshop = null;
     newWorkshopTopic = "";
     externalAuthor1 = "";
     externalAuthor2 = "";
     externalAuthor3 = "";
     externalAuthor4 = "";
     newLectureAuthors = null;
     newWorkshopDate = null;
     return "moderatoraddeventpage";
 }

 public void ModeratorPageAddWorkshopCloseRender(){
     
     moderatorpageAddWorkshoprender = false;
     externalAuthor1 = "";
     externalAuthor2 = "";
     externalAuthor3 = "";
     externalAuthor4 = "";
     newWorkshopMessage = "";
     newLectureAuthors = null;
     newWorkshopDate = null;
 }

 
 public void ModeratorPageAddWorkshopToDB(){
 
     /*
     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdft = new SimpleDateFormat("HH:mm:ss");
        try {
            sessiondate = sdf.format(newsessionDate);
            newsessionDate = sdf.parse(sessiondate); //sdf.parse(sessiondate);
     */
     
     
        String workshopdate = "";
        String begintime = "";
        String endtime = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdft = new SimpleDateFormat("HH:mm:ss");
        int num = 0;
        long lenght = (addWorkshopEndTime.getTime() - addWorkshopBeginTime.getTime())/(1000*60);
        if(newLectureAuthors != null){
        num += newLectureAuthors.length;
        }
        
        if(!(externalAuthor1.equals(""))) { num++; }
        if(!(externalAuthor2.equals(""))) { num++; }
        if(!(externalAuthor3.equals(""))) { num++; }
        if(!(externalAuthor4.equals(""))) { num++; }
        
        try {
            workshopdate = sdf.format(newWorkshopDate);
            newWorkshopDate = sdf.parse(workshopdate);
            
            endtime = sdft.format(addWorkshopEndTime);
            addWorkshopEndTime = sdft.parse(endtime);
            
            begintime = sdft.format(addWorkshopBeginTime);
            addWorkshopBeginTime = sdft.parse(begintime);
          } catch (Exception ex) {
            Logger.getLogger(HibernateHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(newWorkshopDate.before(moderatornewWorkshopAgenda.getBeginDate())) { newWorkshopMessage = "Error! Workshop date can not be before conference begin date!"; }
        else if(newWorkshopDate.after(moderatornewWorkshopAgenda.getEndDate())) { newWorkshopMessage = "Error! Workshop date can not be after conference end date!"; }
        else if(addWorkshopBeginTime.after(addWorkshopEndTime)){ newWorkshopMessage = "Error! Workshop begin time is after workshop end time!"; }
        else if(num > 4) { newWorkshopMessage = "Error! You have entered more than 4 authors!"; }
        else if(lenght > 180) { newWorkshopMessage = "Error! Workshop can not be longer that 180 minutes!";}
        else{
         newWorkshopMessage = "";
         Workshop newWorkshop = new Workshop(moderatornewWorkshopAgenda, newWorkshopTopic, newWorkshopDate, addWorkshopBeginTime, addWorkshopEndTime);
         newWorkshopMessage = helper.ModeratorPageAddWorkshopToDBHP(newWorkshop ,newLectureAuthors, externalAuthor1, externalAuthor2, externalAuthor3, externalAuthor4, begintime, endtime);
        }    
 }
 
    public String ModeratorPageAddOpenCeremonyRender(Agenda d, ModeratorBean mb){
        moderatorpageModeratorBeanSessionAdd = mb;
        addOpenCeremonyBeginTime = null;
        addOpenCeremonyEndTime = null;
        moderatorpageOpenCeremonyrender = true;
        moderatornewOpenCeremonyAgenda = d;
        newOpenCeremonyMessage = "";
        openCeremonyType = "";
        newOpenCeremonyDate = null;
        return "moderatoraddeventpage";
    }
 
    public void ModeratorPageAddOpenCeremonyCloseRender(){
        addOpenCeremonyBeginTime = null;
        addOpenCeremonyEndTime = null;
        moderatorpageOpenCeremonyrender = false;
        moderatornewOpenCeremonyAgenda = null;
        newOpenCeremonyMessage = "";
        openCeremonyType = "";
        newOpenCeremonyDate = null;
        
    }
    
    public void ModeratorPageAddOpenCeremonyToDB(){
    
        String openceremonydate = "";
        String begintime = "";
        String endtime = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdft = new SimpleDateFormat("HH:mm:ss");
        long lenght = (addOpenCeremonyEndTime.getTime() - addOpenCeremonyBeginTime.getTime())/(1000*60);
     
         try {
            openceremonydate = sdf.format(newOpenCeremonyDate);
            newOpenCeremonyDate = sdf.parse(openceremonydate);
             
            endtime = sdft.format(addOpenCeremonyEndTime);
            addOpenCeremonyEndTime = sdft.parse(endtime);
            
            begintime = sdft.format(addOpenCeremonyBeginTime);
            addOpenCeremonyBeginTime = sdft.parse(begintime);
          } catch (Exception ex) {
            Logger.getLogger(HibernateHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(newOpenCeremonyDate.before(moderatornewOpenCeremonyAgenda.getBeginDate())) { newOpenCeremonyMessage = "Error! Open ceremony date can not be before conference begin date!"; }
        else if(newOpenCeremonyDate.after(moderatornewOpenCeremonyAgenda.getEndDate())) { newOpenCeremonyMessage = "Error! Open ceremony date can not be after conference end date!"; }         
        else if(addOpenCeremonyBeginTime.after(addOpenCeremonyEndTime)){ newOpenCeremonyMessage = "Error! Open ceremony begin time is after open ceremony end time!"; }
        else if(lenght > 60) { newOpenCeremonyMessage = "Error! Open ceremony can not be longer that 60 minutes!";}
        else{
         newOpenCeremonyMessage = "";
         newOpenCeremony = new Openceremony(moderatornewOpenCeremonyAgenda, newOpenCeremonyDate, addOpenCeremonyBeginTime, addOpenCeremonyEndTime, openCeremonyType);
         newOpenCeremonyMessage = helper.ModeratorPageAddOpenCeremonyToDBHP(newOpenCeremony);        
        }
    }
    
    
    public String ModeratorPageAddCloseCeremonyRender(Agenda d, ModeratorBean mb){
        moderatorpageModeratorBeanSessionAdd = mb;
        addCloseCeremonyBeginTime = null;
        addCloseCeremonyEndTime = null;
        moderatorpageCloseCeremonyrender = true;
        moderatornewCloseCeremonyAgenda = d;
        newCloseCeremonyMessage = "";
        closeCeremonyType = "";
        newCloseCeremonyDate = null;
        return "moderatoraddeventpage";
    }
 
    public void ModeratorPageAddCloseCeremonyCloseRender(){
        addCloseCeremonyBeginTime = null;
        addCloseCeremonyEndTime = null;
        moderatorpageCloseCeremonyrender = false;
        moderatornewCloseCeremonyAgenda = null;
        newCloseCeremonyMessage = "";
        closeCeremonyType = "";
        newCloseCeremonyDate = null;
    }
    
    public void ModeratorPageAddCloseCeremonyToDB(){
    
        String closeceremonydate = "";
        String begintime = "";
        String endtime = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdft = new SimpleDateFormat("HH:mm:ss");
        long lenght = (addCloseCeremonyEndTime.getTime() - addCloseCeremonyBeginTime.getTime())/(1000*60);
     
         try {
            closeceremonydate = sdf.format(newCloseCeremonyDate);
            newCloseCeremonyDate = sdf.parse(closeceremonydate);
             
            endtime = sdft.format(addCloseCeremonyEndTime);
            addCloseCeremonyEndTime = sdft.parse(endtime);
            
            begintime = sdft.format(addCloseCeremonyBeginTime);
            addCloseCeremonyBeginTime = sdft.parse(begintime);
          } catch (Exception ex) {
            Logger.getLogger(HibernateHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(newCloseCeremonyDate.before(moderatornewCloseCeremonyAgenda.getBeginDate())) { newCloseCeremonyMessage = "Error! Close ceremony date can not be before conference begin date!"; }
        else if(newCloseCeremonyDate.after(moderatornewCloseCeremonyAgenda.getEndDate())) { newCloseCeremonyMessage = "Error! Close ceremony date can not be after conference end date!"; } 
        else if(addCloseCeremonyBeginTime.after(addCloseCeremonyEndTime)){ newCloseCeremonyMessage = "Error! Close ceremony begin time is after close ceremony end time!"; }
        else if(lenght > 60) { newCloseCeremonyMessage = "Error! Close ceremony can not be longer that 60 minutes!";}
        else{
         newCloseCeremonyMessage = "";
         newCloseCeremony = new Closeceremony(moderatornewCloseCeremonyAgenda, newCloseCeremonyDate, addCloseCeremonyBeginTime, addCloseCeremonyEndTime, closeCeremonyType);
         newCloseCeremonyMessage = helper.ModeratorPageAddCloseCeremonyToDBHP(newCloseCeremony);
        }
    }
    
    //Galeriju cu da pravim kao jednu tabelu u koju cu da stavim id koji ce mi biti povezan sa id-je konferencije
    //zatim ce imati polje za slike konferencije, poslje za slike dana konferencije i polje za slike sesije u okviru nekog dana
    //ako su mi popunjena polja za konferencije i dan onda taj red predstavlja slike za taj dan konferencije
    //a ako imam slike neke sesije onda su mi sva tri polja popunjena


   public void ModeratorPageAddConferenceImagesRender(Agenda a){
   
        moderatorpageAddConferenceImagesrender = true;
        addConferenceImageCheckBox = false;
        moderatorAddConferenceImagesAgenda = a;
        moderatorAddConferenceImagesMessage = "";
        moderatorAddConferenceImagesDayList = new HashMap<Integer, Integer>();
        moderatorAddConferenceImagesDay = -1;
        addConferenceImageFile = null;
        int lenght = (int) (a.getEndDate().getTime() - a.getBeginDate().getTime())/(1000*60*60*24);
        lenght++;
        for(int i=1; i<=lenght; i++){
            moderatorAddConferenceImagesDayList.put(i, i);
        }
        
   }


   public void ModeratorPageAddConferenceImagesCloseRender(){
   
        moderatorpageAddConferenceImagesrender = false;
        addConferenceImageCheckBox = false;
        moderatorAddConferenceImagesAgenda = null;
        moderatorAddConferenceImagesMessage = "";
        moderatorAddConferenceImagesDayList = null;
        moderatorAddConferenceImagesDay = -1;
        addConferenceImageFile = null;
   }
   
   
    public void ModeratorPageAddConferenceImagesToDB(String type){

        if((addConferenceImageCheckBox == true) && (moderatorAddConferenceImagesDay != -1)){
            moderatorAddConferenceImagesMessage = "Error! You have selected conference folder and conference day folder at the same time!";
       }
        else if((addConferenceImageCheckBox == false) && (moderatorAddConferenceImagesDay == -1)) { 
         moderatorAddConferenceImagesMessage = "Error! You have to selected conference folder or conference day folder!";
        }
        else {
            moderatorAddConferenceImagesMessage = "";
            Gallery newImage = null;

           if(addConferenceImageFile.getSize() == 0) { moderatorAddConferenceImagesMessage = "You have not selected image. Please try again"; }
           else {      
        String fileName = addConferenceImageFile.getFileName(); 
        byte[] contents = addConferenceImageFile.getContents();

        int conferenceid = -1;
        int daynum = -1;
        int sessionid = -1;
        if(addConferenceImageCheckBox == true) { 
            conferenceid =  moderatorAddConferenceImagesAgenda.getConference().getId();
            newImage = new Gallery(conferenceid, daynum, sessionid, type, contents);
        }
        else if(moderatorAddConferenceImagesDay != -1) {
            conferenceid =  moderatorAddConferenceImagesAgenda.getConference().getId();
            daynum =  moderatorAddConferenceImagesDay;
            newImage = new Gallery(conferenceid, daynum, sessionid, type, contents);
        }
       moderatorAddConferenceImagesMessage = helper.ModeratorPageAddConferenceImagesToDBHP(newImage);
           }
        }
    }

    
    public void ModeratorPageAddSessionImage(SessionHallBean d){
    
        d.setAddimagerendered(true);
        showSessionsMessage = "";
        ImageSession = d;
        sessionaddimagemod = true;
        addSessionImageFile = null;
    }
    
    public void ModeratorPageAddSessionImageSave(String type, SessionHallBean d){
    
     Gallery newImage = null;   
     if(addSessionImageFile.getSize() == 0) { showSessionsMessage = "You have not selected image. Please try again";}  
     else{
     byte[] contents = addSessionImageFile.getContents();

     int conferenceid = ImageSession.getSession().getAgenda().getConference().getId();
     int daynum = ImageSession.getSession().getConferenceDay();
     int sessionid = ImageSession.getSession().getId();
        
     newImage = new Gallery(conferenceid, daynum, sessionid, type, contents);
     
    showSessionsMessage = helper.ModeratorPageAddSessionImageToDBHP(newImage);
    
    ImageSession = null;
    sessionaddimagemod = false;
    addSessionImageFile = null;
    d.setAddimagerendered(false);
     }
}
    
    public String RegularUserMyConferencesAgendaRender (ModeratorBean d){
    
        conferenceAgendaBean = d;
       //myConferencesAgenda = d;
       regularUserMyConferencesmessage = "";
       regularUserMyConferencesrender = true;
       myConferencesAgendaView =  helper.ModeratorPageViewConferenceProgramHB(d);
       return "regularuserconfereceagendapage";
    }
    
    public String RegularUserMyConferencesAgendaRenderClose(){
    
       conferenceAgendaBean = null;
       regularUserMyConferencesmessage = "";
       regularUserMyConferencesrender = false;
       myConferencesAgendaView = null; 
       return "regularusermyconferences";
    }
    
    public String RegularUserMyConferencesAgendaMainPageRenderClose(){
    
       conferenceAgendaBean = null;
       regularUserMyConferencesmessage = "";
       regularUserMyConferencesrender = false;
       myConferencesAgendaView = null; 
       return "regularuserpage";
    }
    
    
    public void regularUserEntryCodeRender(RegularUserSearch d){
        
         d.setRenderedforentrycode(true);
        //showCodeinputtextfield = true;
    }
    
    
    public void AddSessionToMyAgenda(int eventid, int conferenceid, String type){
    
       String username = user.getUsername();
       regularUserMyConferencesmessage = helper.AddSessionToMyAgendaHB(eventid, conferenceid, username, type);
    }
    
    ////Sutra uraditi dodavanje radionica i ceremonija u moju agendu, korisiti istu metodu samo promeniti slovo koje
    ////prosledjujes, umesto s prosledi slovo koje odgovara tom eventu


  public String RegularUserMyAgendaUserView(ModeratorBean d){
        
      regularUserMyAgendaList = helper.GetRegularUserMyAgendaList(user.getUsername(), d);
      regularUserMyAgendamessage = "";
      return "regularusermyagenda";
  }

  public String gotoPreviousPageFromMyAgenda(){
  
      regularUserMyAgendaList = null;
      regularUserMyAgendamessage = "";
      leavecommentrender = false;
      myagendacomment = "";
      leavegradefield = "";
      return "regularusermyconferences";  
  }
  
  public String gotoMainPageFromMyAgenda(){
  
      regularUserMyAgendaList = null;
      regularUserMyAgendamessage = "";
      leavecommentrender = false;
      myagendacomment = "";
      leavegradefield = "";
      return "regularuserpage";
  }
  
  public void LikeThisEvent(int eventid, int agendaid, String type){
  
          String username = user.getUsername();
          regularUserMyAgendamessage = helper.LikeEvent(eventid, agendaid, type, username);
  }
  
  public void LeaveCommentRender(SessionHallBean d, Workshop w){
      if(d != null) { d.setRendered(true); }
      else if(w != null) { w.setRendered(true); }
      
      regularUserMyAgendamessage = "";
      myagendacomment = "";
  }
  
  public void LeaveCommentToEvent(int eventid, int agendaid, String type, SessionHallBean d, Workshop w){
  
      if(myagendacomment.equals("")) { regularUserMyAgendamessage = "You have not entered any comment. Please try again."; }
      else{
      String username = user.getUsername();
      regularUserMyAgendamessage = regularUserMyAgendamessage = helper.CommentEvent(eventid, agendaid, type, username, myagendacomment);
      leavecommentrender = false;
      if(d != null) { d.setRendered(false); }
      else if(w != null) { w.setRendered(false); }
      }
}
  
  
  // NE RADE MI KOMENTARI LEPO, JER MI SE SVA POLJA ZA UNOS STVORE ODJEDNOM, ILI VIDETI DA SE SVAKI RENDERUJE POSEBNO ILI 
  // NEKA SVAKI IMA SVOJE POLJE ZA KOMENTAR, ISTI PROBLEM I KOD UNOSENJA SIFRE ZA PRIJAVU NA KONFERENCIJU
  // MOZES RESITI TO TAKO STO CES PRIKAZATI POSEBAN PANEL GRID ZA UNOSE KOMENTARA I ULAZNE SIFRE AKO VIDIS DA KRECES DA SE MUCIS SA OVIM
  
  
  public void LeaveGradeRender(Lecture d){
     
      d.setRendered(true);
      leavegradefield = "";
      regularUserMyAgendamessage = "";
  }
  
  
  public void LeaveGradeForEvent(int lectureid, Lecture d){
      
      if(leavegradefield.equals("")) { regularUserMyAgendamessage = "You have not entered grade. Please try again."; }
      else{
       int grade = Integer.parseInt(leavegradefield);
       if((grade < 1) || (grade > 5)) { regularUserMyAgendamessage = "You have entered invalid grade. Please try again.";}
       else{
      
      String username = user.getUsername();
      regularUserMyAgendamessage = regularUserMyAgendamessage = helper.GradeEvent(lectureid, username, grade);
      if(d != null) { d.setRendered(false); }
      }
    }
  }
  
  
  public String RegularUserConferenceVisitorsView(int conferenceid){
  
      String username = user.getUsername();
      conferencevisitorslist = helper.GetConferenceVisitorsList(conferenceid, username);
      conferencevisitorsString = "";
      return "regularuserconferencevisitorspage";
  }
  
  public void ViewConferenceVisitorProfile(User d){
      
    conferencevisitorprofilerender = true;
    conferencevisitorprofile = d;
    conferencevisitorsString = "";
  }
  
  
  public void ViewConferenceVisitorProfileClose(){
    
    conferencevisitorsString = "";
    conferencevisitorprofilerender = false;
    conferencevisitorprofile = null;    
  }
  
  
  public void SendMessageRender(User d){
      
    conferencevisitorSendMessagerender = true;
    conferencevisitorMessageReceiver = d;
    conferencevisitorMessageContent = "";  
    conferencevisitorsString = "";
    conferencevisitorprofilerender = false;
  }
  
  
  public void SendMessageCloseRender(){
      
    conferencevisitorSendMessagerender = false;
    conferencevisitorMessageReceiver = null;
    conferencevisitorMessageContent = "";  
    conferencevisitorsString = "";
  }
  
  public void ConferenceVisitorSendMessage(){
  
    Messages newMessage = new Messages(user.getUsername(), conferencevisitorMessageReceiver.getUsername(), conferencevisitorMessageContent);
    conferencevisitorsString = helper.SendConferenceMessage(newMessage);
  }
  
  public void AddToFavouritesUser(String d){
  
    Favourites newFavourite = new Favourites(user.getUsername(), d);
    conferencevisitorsString = helper.AddToFavouritesUserDB(newFavourite);
  }
 
  public String ConferenceGallery(int conferenceid, Date beginDate, Date endDate){
  
      openGalleryconferenceid = conferenceid;
      int lenght = (int) (endDate.getTime() - beginDate.getTime())/(1000*60*60*24);
      lenght++;
      gallerydays = new ArrayList<Integer>();
     for(int i=1; i<=lenght; i++){
            gallerydays.add(i);
        }
      
     gallerylist = helper.GetGalleryForThisConference(conferenceid);
     wholeConferenceGallerylist = new ArrayList<Gallery>();
     for(Gallery g:gallerylist){
         if((g.getDaynum() == -1) && (g.getEventid() == -1)){
             wholeConferenceGallerylist.add(g);
         }
     }
  
     return "conferencegallerypage";
  }
 
  public String openGallerySessionDay(int daynum){
      
      sessionDayGalleryImages = new ArrayList<Gallery>();
      conferencedaynum = daynum;
              
      for(Gallery g:gallerylist){
         if((g.getConferenceid() == openGalleryconferenceid) && (g.getDaynum() == daynum) && (g.getEventid() == -1)){
             sessionDayGalleryImages.add(g);
         }
      }
       
     List<Sessions> sessionlist = helper.returnAllSessionsList();
     sessionDaySessions = new ArrayList<Sessions>();
  
     Map<Integer,Integer> helpmap = new HashMap<Integer, Integer>();
  
     for(Gallery gg:gallerylist){
         if((gg.getConferenceid() == openGalleryconferenceid) && (gg.getDaynum() == daynum) && (gg.getEventid() != -1)){
             if(!(helpmap.containsKey(gg.getEventid()))){
                 helpmap.put(gg.getEventid(), gg.getEventid());
             for(Sessions s: sessionlist){
                 if(s.getId() == gg.getEventid()){
                     sessionDaySessions.add(s);
                   }
                }
             }
         }
     }
     return "regularusergalleryconferenceday";
  }
 
  //SADA TREBA DA IMPLEMENTIRAM DA MI SE PRIKAZUJU SLIKE ZA ODREDJENE DANE KONFERENCIJE I ZA SESIJE U OKVIRU DANA,
  //NE ZABORAVI DA VEC IMAS DOVUCENE SLIKE IZ BAZE U gallerylist I DA ODATLE UZIMAS SLIKE
  
 
  public String openGalleryForSession(Sessions s){
  
      sessionGalleryImages = new ArrayList<Gallery>();
      choosenSession = s;
      
       for(Gallery g:gallerylist){
         if((g.getConferenceid() == openGalleryconferenceid) && (g.getDaynum() == conferencedaynum) && (g.getEventid() == s.getId())){
             sessionGalleryImages.add(g);
         }
      }
  
       return "regularusersessiongallery";
  
  }
  
  public void RegularUserAddEventImageOCRender(Openceremony d){
      d.setRendered(true);
      regularUserMyAgendamessage = "";
  }
  
  public void RegularUserAddOpenCeremonyImage(String type, Openceremony d){
  
      Gallery newImage = null;   
        
      if(addOpenCeremonyImageFile.getSize() == 0) { regularUserMyAgendamessage = "You have not selected image. Please try again"; }
      else{ 
          
     byte[] contents = addOpenCeremonyImageFile.getContents();

     int conferenceid = d.getAgenda().getConference().getId();
     
     int lenght = (int) (d.getDate().getTime() - d.getAgenda().getBeginDate().getTime())/(1000*60*60*24);
     lenght++;
     
     int daynum = lenght;
     int sessionid = -1;
        
     newImage = new Gallery(conferenceid, daynum, sessionid, type, contents);
     
    regularUserMyAgendamessage = helper.ModeratorPageAddSessionImageToDBHP(newImage);
    d.setRendered(false);
    addOpenCeremonyImageFile = null;  
      }
  }
  
  
  public void RegularUserAddEventImageCCRender(Closeceremony d){
      d.setRendered(true);
      regularUserMyAgendamessage = "";
  }
  
 public void RegularUserAddCloseCeremonyImage(String type, Closeceremony d){
  
      Gallery newImage = null;   
        
      if(addCloseCeremonyImageFile.getSize() == 0) { regularUserMyAgendamessage = "You have not selected image. Please try again"; }
      else{ 
          
     byte[] contents = addCloseCeremonyImageFile.getContents();

     int conferenceid = d.getAgenda().getConference().getId();
     
     int lenght = (int) (d.getDate().getTime() - d.getAgenda().getBeginDate().getTime())/(1000*60*60*24);
     lenght++;
     
     int daynum = lenght;
     int sessionid = -1;
        
     newImage = new Gallery(conferenceid, daynum, sessionid, type, contents);
     
    regularUserMyAgendamessage = helper.ModeratorPageAddSessionImageToDBHP(newImage);
    d.setRendered(false);
    addCloseCeremonyImageFile = null;  
      }
  }
  
  
 public void RegularUserAddEventImageWRender(Workshop d){
     d.setImgrendered(true);
     regularUserMyAgendamessage = "";
 }
 
 
 public void RegularUserAddWorkshopImage(String type, Workshop d){
  
      Gallery newImage = null;   
        
      if(addWorkshopImageFile.getSize() == 0) { regularUserMyAgendamessage = "You have not selected image. Please try again"; }
      else{ 
          
     byte[] contents = addWorkshopImageFile.getContents();

     int conferenceid = d.getAgenda().getConference().getId();
     
     int lenght = (int) (d.getDate().getTime() - d.getAgenda().getBeginDate().getTime())/(1000*60*60*24);
     lenght++;
     
     int daynum = lenght;
     int sessionid = -1;
        
     newImage = new Gallery(conferenceid, daynum, sessionid, type, contents);
     
    regularUserMyAgendamessage = helper.ModeratorPageAddSessionImageToDBHP(newImage);
    d.setImgrendered(false);
    addWorkshopImageFile = null;  
      }
  }
 
 
 public void RegularUserAddSessionImage(SessionHallBean d){
        d.setImgsessionrendered(true);
        regularUserMyAgendamessage = "";
        ImageSession = d;
        addSessionImageFile = null;
 }
 
 
 public void RegularUserAddSessionImageSave(String type, SessionHallBean d){
 
     Gallery newImage = null;   
     if(addSessionImageFile.getSize() == 0) { regularUserMyAgendamessage = "You have not selected image. Please try again";}  
     else{
     byte[] contents = addSessionImageFile.getContents();

     int conferenceid = ImageSession.getSession().getAgenda().getConference().getId();
     int daynum = ImageSession.getSession().getConferenceDay();
     int sessionid = ImageSession.getSession().getId();
        
     newImage = new Gallery(conferenceid, daynum, sessionid, type, contents);
     
    regularUserMyAgendamessage = helper.ModeratorPageAddSessionImageToDBHP(newImage);
    
    ImageSession = null;
    addSessionImageFile = null;
    d.setImgsessionrendered(false);
     }
 }
 
 
 //Dodao sam da se ispisuju autori predavanja i da ih bojim ako drze predavanje, takodje sam dodao fleg amiauthor koji mi govori da li
 //je ulogovani korisnik autor nekog rada, sada to mogu iskoristiti da mi sluzi za render da li da mi prikazuje opcije prezentujem rad
 //Jedino da imam neku listu koji izlacim pri logovanju gde sam ja sve author i da onda radim eq u rendered pa da tu prikazujem dugme za dodatne opcije
 
 
 public void HoldPresentation(Lecture d){
   
       int lectureid = d.getId();
       String username = user.getUsername();
       regularUserMyAgendamessage = helper.HoldPresentationHB(lectureid, username);
   }
 
 public void UploadDocumentRender(LectureAuthorBean d){
     
     d.setUploadrender(true);
     LectureUploadFile = null;
     regularUserMyAgendamessage = "";
 }
 
 public void LectureAddFileToDB(LectureAuthorBean d){
     
     String type = LectureUploadFile.getContentType();
     if(LectureUploadFile.getSize() == 0) { regularUserMyAgendamessage = "You have not selected document. Please try again.";}
     else if((!type.contains("pdf")) && (!type.contains("powerpoint"))) { regularUserMyAgendamessage = "Documents that are not pdf or ppt format can not be added."; }
     else {
     String types = "";
     if(type.contains("pdf")) { types = "pdf"; }
     else if(type.contains("powerpoint")) { types = "ppt"; }
     int lectureid = d.getLecture().getId();
     int conferenceid = d.getLecture().getSessions().getAgenda().getConference().getId();
     byte[] contents = LectureUploadFile.getContents();
     String documentname = LectureUploadFile.getFileName();
     documentname = documentname.substring(documentname.lastIndexOf("\\")+1, documentname.lastIndexOf("."));
     String username = user.getUsername();
     regularUserMyAgendamessage = helper.LectureAddFileToDBDH(lectureid, conferenceid ,username, types, contents, documentname);
     d.setUploadrender(false);
     }
 }
 
 public String DownloadConferenceFiles(int conferenceid){
     
     lecturefileDownloadlist = helper.GetDownloadLectureFile(conferenceid);
    
     
 return "conferencedocuments";
 }
 
 public List<String> completeText(String query){
      List<String> results = helper.completeTextHB(query);
      regularuserAddConferenceToMyAgendaMessage = query;
      return results;     
 }
 
 public void TwitterEvent(ModeratorBean d){
 HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
 String url = request.getRequestURL().toString();
 String page1 = "https://twitter.com/intent/tweet?text=Conference%20\""+d.getName()+"\".%20Stay%20tuned.%20:)%20"+url;
 String page = "https://twitter.com/intent/tweet?text="+url;    
       try {
           FacesContext.getCurrentInstance().getExternalContext().redirect(page1);
       } catch (IOException ex) {
           Logger.getLogger(Controler.class.getName()).log(Level.SEVERE, null, ex);
       }
 }
 
}
