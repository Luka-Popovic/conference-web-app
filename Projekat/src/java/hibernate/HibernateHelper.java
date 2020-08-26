package hibernate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import beans.*;
import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

public class HibernateHelper {
    
    Session session = null;

    public HibernateHelper() {
        this.session = HibernateUtil.getSessionFactory().openSession();
    }

    public User LoginHB(String username, String password){
    
        List<User> userList;
        User user = null;
        try {
        org.hibernate.Transaction tx = session.beginTransaction();
        Query q = session.createQuery ("from User as user where user.username = '"+ username+"'");
        userList = (List<User>) q.list();
        
        if(userList.isEmpty()) { user = null; session.getTransaction().commit(); return user; }
        //Trebalo bi da postoji bar jedan pa njega vracam
        else { user = userList.get(0); session.getTransaction().commit(); return user;}
        } catch (Exception e) {
        e.printStackTrace();
        session.getTransaction().rollback();
        }
        return user;

    }

 public void ChangePassword(User user){
    
       /* try {
        org.hibernate.Transaction tx = session.beginTransaction();
        Query q = session.createQuery ("update User set password = :pass where username = :usern ");
        q.setParameter("pass", newpassword);
        q.setParameter("usern",username);
        int result = q.executeUpdate();
        } catch (Exception e) {
        e.printStackTrace();
    }*/
    try{
        org.hibernate.Transaction tx = session.beginTransaction();
        session.update(user); session.getTransaction().commit();
        
    }catch(RuntimeException e){              
        session.getTransaction().rollback();
}
 }
 
 public String SingInHB(User user){
 
     String message = "";
     List<User> userlist = null;
     
     try{
            session.getTransaction().begin();
            Query q = session.createQuery("from User as user where user.username = '"+user.getUsername()+"' ");
            userlist = (List<User>) q.list();
            session.getTransaction().commit();
            //Ako ovaj user vec postoji vrati poruku o gresci
            if(!(userlist.isEmpty())) { message = "Username \""+user.getUsername()+"\" already exists. Please try again."; }
            else {
               
            session.getTransaction().begin();
            q = session.createQuery("from User as user where user.email = '"+user.getEmail()+"' ");
            userlist = (List<User>) q.list();
            session.getTransaction().commit();
            if(!(userlist.isEmpty())) { message = "Email \""+user.getEmail()+"\" has already been used for creating profile. Please try again."; }    
            else {
               
        org.hibernate.Transaction tx = session.beginTransaction();
        session.save(user); session.getTransaction().commit();
            message = "OK";
            }
          }
    }catch(RuntimeException e){              
        session.getTransaction().rollback();
}
     return message;
 }
 
 public List<GuestModeBean> SearchGuestModeHB(String searchName, Date searchDateBegin, Date searchDateEnd){
 
        List<GuestModeBean> guestlist = new ArrayList<GuestModeBean>();
        List<Conference> list = null;
        List<Location> locationlist = null;
        String begin = "";
        String end = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(searchDateBegin != null){ begin = sdf.format(searchDateBegin);}
        if(searchDateEnd != null){ end = sdf.format(searchDateEnd);}    
        
        try{
         org.hibernate.Transaction tx = session.beginTransaction();
         Query q;
         if(searchName.equals("") && searchDateBegin != null && searchDateEnd != null){
           
             q = session.createQuery ("from Conference as conference where conference.beginDate >= '"+begin+"' and conference.endDate <= '"+end+"'");
            list = (List<Conference>) q.list();
             session.getTransaction().commit();
         }
         else if((!searchName.equals("")) && searchDateBegin != null && searchDateEnd != null){
            
             q = session.createQuery ("from Conference as conference where conference.name = '"+ searchName+"'"
                    + " and conference.beginDate >= '"+begin+"' and conference.endDate <= '"+end+"'");
            list = (List<Conference>) q.list();
             session.getTransaction().commit();
         }
         else if((!searchName.equals("")) && searchDateBegin == null && searchDateEnd == null){
         
             q = session.createQuery ("from Conference as conference where conference.name = '"+ searchName+"'");
            list = (List<Conference>) q.list();
             session.getTransaction().commit();
         }
         else if (searchName.equals("") && searchDateBegin != null && searchDateEnd == null){
            q = session.createQuery ("from Conference as conference where conference.beginDate >= '"+begin+"'");
            list = (List<Conference>) q.list();
             session.getTransaction().commit();
         }
         else if (searchName.equals("") && searchDateBegin == null && searchDateEnd != null){
            q = session.createQuery ("from Conference as conference where conference.endDate <= '"+end+"'");
            list = (List<Conference>) q.list();
             session.getTransaction().commit();
         }
         else if ((!searchName.equals("")) && searchDateBegin != null && searchDateEnd == null){
            q = session.createQuery ("from Conference as conference where conference.name = '"+ searchName+"'"
                    + " and conference.beginDate >= '"+begin+"'");
            list = (List<Conference>) q.list();
             session.getTransaction().commit();
         }
         else if ((!searchName.equals("")) && searchDateBegin == null && searchDateEnd != null){
            q = session.createQuery ("from Conference as conference where conference.name = '"+ searchName+"' "
                    + " and conference.endDate <= '"+end+"'");
            list = (List<Conference>) q.list();
             session.getTransaction().commit();
         }
         else if ((searchName.equals("")) && searchDateBegin == null && searchDateEnd == null){
            q = session.createQuery ("from Conference");
            list = (List<Conference>) q.list();
             session.getTransaction().commit();
         }
         
         
         if(list != null){
             tx = session.beginTransaction();
             q = session.createQuery("from Location");
             locationlist = q.list();
             session.getTransaction().commit();
         
             for(Conference c:list){
                 for(Location l:locationlist){
                 if(l.getId() == c.getPlace()){
                 GuestModeBean gmb = new GuestModeBean(c.getId(), c.getName(), c.getField(), c.getBeginDate(),c.getEndDate(), l.getCity(), l.getCountry(), l.getPlace(), l.getStreet());
                 guestlist.add(gmb);
                        }
                    }
             }
         }
         
        }catch(Exception e){
            e.printStackTrace();
        }
     
        
        
        
        return guestlist;
 }
 
  public List<GuestModeBean> SearchGuestModeTodayConferencesHB(){
        
        List<GuestModeBean> guestlist = new ArrayList<GuestModeBean>();
        List<Conference> list = null;
        List<Location> locationlist = null;
        String begin = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        begin = sdf.format(date);
       
        try{
         org.hibernate.Transaction tx = session.beginTransaction();
         Query q;
         q = session.createQuery ("from Conference as conference where conference.beginDate = '"+begin+"'");
           list = (List<Conference>) q.list();
           session.getTransaction().commit();
         
           if(list != null){
             tx = session.beginTransaction();
             q = session.createQuery("from Location");
             locationlist = q.list();
             session.getTransaction().commit();
         
             for(Conference c:list){
                 for(Location l:locationlist){
                 if(l.getId() == c.getPlace()){
                 GuestModeBean gmb = new GuestModeBean(c.getId(), c.getName(), c.getField(), c.getBeginDate(),c.getEndDate(), l.getCity(), l.getCountry(), l.getPlace(), l.getStreet());
                 guestlist.add(gmb);
                        }
                    }
             }
         }
         
         
         
        }catch(Exception e){
            e.printStackTrace();
        }
 
    return guestlist;
 
}
  
  public List<GuestModeBean> SearchGuestModeMonthsConferencesHB(){
        
        List<GuestModeBean> guestlist = new ArrayList<GuestModeBean>();
        List<Conference> list = null;
        List<Location> locationlist = null;
        String begin = "";
        String end = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        begin = sdf.format(date);
       //
        Date dateend = new Date();
        Calendar calendar = Calendar.getInstance();
        int lastDate = calendar.getActualMaximum(Calendar.DATE);
        dateend.setDate(lastDate);
        end = sdf.format(dateend);
        
        try{
         org.hibernate.Transaction tx = session.beginTransaction();
         Query q;
         q = session.createQuery ("from Conference as conference where conference.beginDate >= '"+begin+"' and conference.endDate <= '"+end+"'");
           list = (List<Conference>) q.list();
           session.getTransaction().commit();
         
           if(list != null){
             tx = session.beginTransaction();
             q = session.createQuery("from Location");
             locationlist = q.list();
             session.getTransaction().commit();
                      
             for(Conference c:list){
                 for(Location l:locationlist){
                 if(l.getId() == c.getPlace()){
                 GuestModeBean gmb = new GuestModeBean(c.getId(), c.getName(), c.getField(), c.getBeginDate(),c.getEndDate(), l.getCity(), l.getCountry(), l.getPlace(), l.getStreet());
                 guestlist.add(gmb);
                        }
                    }
             }
         }

        }catch(Exception e){
            e.printStackTrace();
        }
 
    return guestlist;
}
  
  public List<RegularUserSearch> SearchRegularUser(String searchName, String searchLocationdregularuser, Date searchDateBegin, Date searchDateEnd, String searchFieldregularuser){
        
        List<RegularUserSearch> guestlist = new ArrayList<RegularUserSearch>();
        List<Conference> list = null;
        List<Location> locationlist = null;
  
        String begin = "";
        String end = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(searchDateBegin != null){ begin = sdf.format(searchDateBegin);}
        if(searchDateEnd != null){ end = sdf.format(searchDateEnd);}
        
        try{
        // org.hibernate.Transaction tx = session.beginTransaction();
         Query q;
         
          if(searchName == null && searchDateBegin != null && searchDateEnd != null){
             session.getTransaction().begin();
             q = session.createQuery ("from Conference as conference where conference.beginDate >= '"+begin+"' and conference.endDate <= '"+end+"'");
            list = (List<Conference>) q.list();
             session.getTransaction().commit();
         }
         
         else if( searchName != null && searchDateBegin != null && searchDateEnd != null){
             session.getTransaction().begin();
             q = session.createQuery ("from Conference as conference where conference.name = '"+ searchName+"'"
                    + " and conference.beginDate >= '"+begin+"' and conference.endDate <= '"+end+"'");
            list = (List<Conference>) q.list();
             session.getTransaction().commit();
         }
         else if(searchName != null && searchDateBegin == null && searchDateEnd == null){
             session.getTransaction().begin();
             q = session.createQuery ("from Conference as conference where conference.name = '"+ searchName+"'");
            list = (List<Conference>) q.list();
             session.getTransaction().commit();
         }
         else if (searchName == null && searchDateBegin != null && searchDateEnd == null){
            session.getTransaction().begin();
            q = session.createQuery ("from Conference as conference where conference.beginDate >= '"+begin+"'");
            list = (List<Conference>) q.list();
            session.getTransaction().commit();
         }
         else if (searchName == null && searchDateBegin == null && searchDateEnd != null){
             session.getTransaction().begin();
             q = session.createQuery ("from Conference as conference where conference.endDate <= '"+end+"'");
            list = (List<Conference>) q.list();
            session.getTransaction().commit();
         }
         else if (searchName != null && searchDateBegin != null && searchDateEnd == null){
             session.getTransaction().begin();
             q = session.createQuery ("from Conference as conference where conference.name = '"+ searchName+"'"
                    + " and conference.beginDate >= '"+begin+"'");
            list = (List<Conference>) q.list();
             session.getTransaction().commit();
         }
         else if (searchName != null && searchDateBegin == null && searchDateEnd != null){
             session.getTransaction().begin();
             q = session.createQuery ("from Conference as conference where conference.name = '"+ searchName+"' "
                    + " and conference.endDate <= '"+end+"'");
            list = (List<Conference>) q.list();
             session.getTransaction().commit();
         }
         else if (searchName == null && searchDateBegin == null && searchDateEnd == null){
             session.getTransaction().begin();
             q = session.createQuery ("from Conference");
            list = (List<Conference>) q.list();
             session.getTransaction().commit();
         }
         else if (searchName != null){
             session.getTransaction().begin();
             q = session.createQuery ("from Conference as conference where conference.name = '"+ searchName+"' ");
            list = (List<Conference>) q.list();
            session.getTransaction().commit();
         }
         
         if(list != null){
             org.hibernate.Transaction tx = session.beginTransaction();
             //tx = session.beginTransaction();
             q = session.createQuery("from Location");
             locationlist = q.list();
             session.getTransaction().commit();
         
             List<Conference> listconf = list;
             
            //Ako je uneto polje Field
            if(searchFieldregularuser != null){
                 listconf = new ArrayList<Conference>();
                 for(Conference c:list){
                     if(c.getField().equals(searchFieldregularuser)){
                         listconf.add(c);
                     }
                 }             
             }
            
            //Ako je uneto polje Location
            if(searchLocationdregularuser != null){
                     List<Conference> listconfpom = new ArrayList<Conference>();
                 for(Conference c:listconf){
                      for(Location l:locationlist){
                     if((c.getPlace() == l.getId()) && l.getCity().equals(searchLocationdregularuser) ){
                            listconfpom.add(c);
                     }
                    }
                  
                   
                 }
                 listconf = listconfpom;
             }
             
             
                 for(Conference c:listconf){
                 for(Location l:locationlist){
                 if(l.getId() == c.getPlace()){
                 RegularUserSearch gmb = new RegularUserSearch(c.getId(), c.getName(), c.getField(), c.getBeginDate(),c.getEndDate(), l.getCity(), l.getCountry(), l.getPlace(), l.getStreet());
                 guestlist.add(gmb);
                        }
                    }
             }
         }
         
        }catch(Exception e){
            e.printStackTrace();
        }
        //Datum do kada mogu da se prijave
         Date currentDate = new Date();
        currentDate.setDate(currentDate.getDate() + 2);
        Date todayDate = new Date();
        try {
            String pomdate = sdf.format(currentDate);
            String pomtodaydate = sdf.format(todayDate);
            currentDate = sdf.parse(pomdate);
            todayDate = sdf.parse(pomtodaydate);
        } catch (ParseException ex) {
            Logger.getLogger(HibernateHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<RegularUserSearch> pomlist = new ArrayList<RegularUserSearch>();
        
        for(RegularUserSearch rs:guestlist){
            if(rs.getBeginDate().after(currentDate)){
                rs.setRendered(true);
            }
        }
        
        //Ostavljam samo one konferencije koje treba da se odrze, one koje su prosle izbacujem
        for(RegularUserSearch rss:guestlist){
            if(rss.getBeginDate().after(todayDate)){
                pomlist.add(rss);
            }
            guestlist = pomlist;
        }
            
        return guestlist;
  }
  
  public String AddToMyConference(RegularUserSearch d, String username){
 
      String message = "";
      List<Conference> conflist = null;
      Conference conf = null;
      List<Myconferences> myconferencelist = null;
      try{
        
        org.hibernate.Transaction tx2 = session.beginTransaction();
        Query q = session.createQuery ("from Myconferences as myconferences where myconferences.conference.id = '"+ d.getConferenceid() +"' "
                + " and myconferences.username = '"+username+"' ");
        myconferencelist = (List<Myconferences>)q.list();
        session.getTransaction().commit();
        if(!(myconferencelist.isEmpty())) { message = "You have already added this conference in your conference list."; }  
        else{
        org.hibernate.Transaction tx1 = session.beginTransaction();
        q = session.createQuery ("from Conference as conference where conference.id = '"+ d.getConferenceid() +"' ");
        conflist = (List<Conference>)q.list();
        conf = conflist.get(0);
        session.getTransaction().commit();
         
         Myconferences myconf = new Myconferences(conf, username);
         org.hibernate.Transaction tx = session.beginTransaction();
        session.save(myconf); session.getTransaction().commit();
        message = "You have successfully added conference \""+d.getName()+"\" in your conference list.";
        }
    }catch(RuntimeException e){              
        session.getTransaction().rollback();
}
    return message;
  }
  
  public Map<String,String> AdminAddConferenceGetLocations(){
  
      List<String> locations = null;
      Map<String,String> locationsmap = new HashMap<String, String>();
      try {
        org.hibernate.Transaction tx = session.beginTransaction();
        Criteria cr = session.createCriteria(Location.class);
        cr.setProjection(Projections.property("place"));
       
        locations = (List<String>) cr.list();
        session.getTransaction().commit();
        
        for(String l:locations){
            locationsmap.put(l, l);
        }
      
        } catch (Exception e) {
        e.printStackTrace();
        session.getTransaction().rollback();
        }
      return locationsmap;
  }
  
  public Map<String,String> AdminAddConferenceGetCity(){
  
      List<String> cities = null;
      Map<String,String> citiesmap = new HashMap<String, String>();
      try {
        org.hibernate.Transaction tx = session.beginTransaction();
        Criteria cr = session.createCriteria(Location.class);
        cr.setProjection(Projections.property("city"));
       
        cities = (List<String>) cr.list();
        session.getTransaction().commit();
        
        for(String l:cities){
            citiesmap.put(l, l);
        }
      
        } catch (Exception e) {
        e.printStackTrace();
        session.getTransaction().rollback();
        }
      return citiesmap;
  }
  
  public Map<String,String> AdminAddConferenceGetUsernames(){
  
      List<String> cities = null;
      Map<String,String> citiesmap = new HashMap<String, String>();
      try {
        org.hibernate.Transaction tx = session.beginTransaction();
        Criteria cr = session.createCriteria(User.class);
        cr.setProjection(Projections.property("username"));
       
        cities = (List<String>) cr.list();
        session.getTransaction().commit();
        
        for(String l:cities){
            citiesmap.put(l, l);
        }
      
        } catch (Exception e) {
        e.printStackTrace();
        session.getTransaction().rollback();
        }
      return citiesmap;
  }
  
  public String AdminAddConference(String name, String field, String location, String city, Date dateBegin, Date dateEnd, String[] moderators){
        
        String message = "";
      
        List<Location> list = null;
        List<Conference> conflist = null;
        Location loc = null;
        Conference conf = null;
        int place = 0;
        String begin = "";
        String end = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Conference> helpconferencelist = null;
        boolean towork = true;
        
        try {
            begin = sdf.format(dateBegin);
            end = sdf.format(dateEnd);
            dateBegin = sdf.parse(begin);
            dateEnd = sdf.parse(end);
        } catch (ParseException ex) {
            Logger.getLogger(HibernateHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
      
      try {
          
        org.hibernate.Transaction tx7 = session.beginTransaction();
        Query q = session.createQuery ("from Conference as conference where conference.name = '"+ name +"' ");
        helpconferencelist = (List<Conference>)q.list();
        session.getTransaction().commit();  
        if(!(helpconferencelist.isEmpty())) { towork = false; message = "Conference with name \""+ name + "\" already exists. Please enter different conference name."; }
        
        else {  
        org.hibernate.Transaction tx = session.beginTransaction();
        q = session.createQuery ("from Location as location where location.city = '"+ city +"' and location.place = '"+location+"' ");
        list = (List<Location>)q.list();
        loc = list.get(0);
        place = loc.getId();
        session.getTransaction().commit();
        }
        } catch (Exception e) {
        e.printStackTrace();
        session.getTransaction().rollback();
        }
        if(towork == true){
        Conference newConference = new Conference(name, place, field, dateBegin, dateEnd);
        try{
        org.hibernate.Transaction tx = session.beginTransaction();
        session.save(newConference); session.getTransaction().commit();
        message = "You have successfully added new conference."; 
    }catch(RuntimeException e){              
        session.getTransaction().rollback();
    }
        if(towork == true){
        try {
        org.hibernate.Transaction tx = session.beginTransaction();
        Query q = session.createQuery ("from Conference as conference where conference.name = '"+ name +"' and conference.place = '"+place+"' "
                + " and conference.field = '"+field+"' and conference.beginDate = '"+begin+"' and conference.endDate = '"+end+"'");
        conflist = (List<Conference>)q.list();
        conf = conflist.get(0);
        session.getTransaction().commit();
        
        } catch (Exception e) {
        e.printStackTrace();
        session.getTransaction().rollback();
        }
       Conference newconference = new Conference(name, place, field, dateBegin, dateEnd);
       newconference.setId(place);
       int conferenceid = conf.getId();
       newconference.setId(conferenceid);
       //Treba uvesti tabelu moderatora u bazu
        for(String m:moderators){
            Conferencemoderators newmoderator = new Conferencemoderators(newconference, m);
            List<User> userlist = null;
                User user = null;
            org.hibernate.Transaction txx = session.beginTransaction();
                Query q = session.createQuery ("from User as user where user.username = '"+ m +"' ");
                userlist = q.list();
                session.getTransaction().commit();
            user = userlist.get(0);
            user.setType("m");
             
            try{
            
            org.hibernate.Transaction tx = session.beginTransaction();
            session.save(newmoderator); session.getTransaction().commit();    
                
            org.hibernate.Transaction ttx = session.beginTransaction();
            session.update(user); session.getTransaction().commit();
          }catch(RuntimeException e){              
        session.getTransaction().rollback();
    }
         }   
            try{  
            Agenda newAgenda = new Agenda(conf, dateBegin, dateEnd);  
            org.hibernate.Transaction tx4 = session.beginTransaction();
            session.save(newAgenda); session.getTransaction().commit();    
        
    }catch(RuntimeException e){              
        session.getTransaction().rollback();
    }
        
        }
      }
        return message;
  }
  
     public List<GuestModeBean> AdminDeleteAllConferenceList(){
     
         List<Conference> list = null;
         List<Location> locationlist = null;
         List<GuestModeBean> guestlist = new ArrayList<GuestModeBean>();
         
         try{
         org.hibernate.Transaction tx = session.beginTransaction();
         Query q;
            q = session.createQuery ("from Conference");
            list = (List<Conference>) q.list();
             session.getTransaction().commit();
        
         if(list != null){
             tx = session.beginTransaction();
             q = session.createQuery("from Location");
             locationlist = q.list();
             session.getTransaction().commit();
         
             for(Conference c:list){
                 for(Location l:locationlist){
                 if(l.getId() == c.getPlace()){
                 GuestModeBean gmb = new GuestModeBean(c.getId(), c.getName(), c.getField(), c.getBeginDate(),c.getEndDate(), l.getCity(), l.getCountry(), l.getPlace(), l.getStreet());
                 guestlist.add(gmb);
                        }
                    }
             }
         }
         
         }catch(Exception e){              
        session.getTransaction().rollback();
    }
    
         return guestlist;
     }
  
     public String adminModeCancelThisConferenceHB(GuestModeBean d, User user){
         
        String returnmessage = ""; 
        int id = d.getId();
        //Moram da izvucem sve korisnike iz baze i da proverim da li ih ima u moderatorlist, ako ih nema a imaju type m onda moram da im type promenum u r
        List<User> userlist = null; 
        List<Conferencemoderators> moderatorlist = null;
        List<Myconferences> myconferenceslist = null;
        
        try {
        org.hibernate.Transaction tx1 = session.beginTransaction();
        Query q;    
        q = session.createQuery("from Myconferences");
             myconferenceslist = (List<Myconferences>)q.list();
             session.getTransaction().commit();
            
        for(Myconferences m:myconferenceslist){
            if((m.getConference().getId()) == id){
            String user_to = m.getUsername();
            String user_from = user.getUsername();
            String content = "We are sorry to inform you that conference \"" + d.getName()+ "\" has been canceled. You will be informed about our next events.";
            org.hibernate.Transaction tx2 = session.beginTransaction();
            Messages message = new Messages(user_from, user_to, content);
            session.save(message); session.getTransaction().commit();
            }
        
        
        }     
             
        org.hibernate.Transaction tx = session.beginTransaction();
        
        Object o=session.load(Conference.class,id);
        Conference confdel = (Conference)o;
        
        session.delete(confdel);
        session.getTransaction().commit();
        //Query q = session.createQuery ("delete from Conference where Conference.id = 14");
        //q.executeUpdate();
        
        org.hibernate.Transaction tx3 = session.beginTransaction();
        q = session.createQuery("from User");
             userlist = (List<User>)q.list();
             session.getTransaction().commit();
        
        org.hibernate.Transaction tx4 = session.beginTransaction();
             q = session.createQuery("from Conferencemoderators");
             moderatorlist = (List<Conferencemoderators>)q.list();
             session.getTransaction().commit();      
             
             for(User u:userlist){
                 boolean moderator = false;
                 for(Conferencemoderators c:moderatorlist){
                     if(u.getUsername().equals(c.getModeratorUsername())){
                         moderator = true;
                         break;
                     }
                 }                 
                 if((moderator == false) && (u.getType().equals("m"))){
                     u.setType("r");
                      org.hibernate.Transaction tx5 = session.beginTransaction();
                    session.update(u); session.getTransaction().commit();                    
             }
             
             
             }
             returnmessage = "You have successfully canceled conference \""+d.getName()+"\".";
        
        } catch (Exception e) {
        e.printStackTrace();
        session.getTransaction().rollback();
        }
         return returnmessage;
     }
     
    public List<ModeratorBean> GetModeratorConferences(User user){
     
        List<ModeratorBean> mblist = new ArrayList<ModeratorBean>();
        List<Conferencemoderators> list = null;
        List<Location> locationlist = null;
        List<Agenda> agendalist = null;
        
        org.hibernate.Transaction tx = session.beginTransaction();
         Query q;  
             q = session.createQuery ("from Conferencemoderators");
             list = (List<Conferencemoderators>) q.list();
             session.getTransaction().commit();
         
         if(list != null){
             tx = session.beginTransaction();
             q = session.createQuery("from Location");
             locationlist = (List<Location>) q.list();
             session.getTransaction().commit();
         
             tx = session.beginTransaction();
             q = session.createQuery("from Agenda");
             agendalist = (List<Agenda>)q.list();
             session.getTransaction().commit();
         
             for(Conferencemoderators c:list){
                 if(c.getModeratorUsername().equals(user.getUsername())){
                 for(Location l:locationlist){
                 if(l.getId() == c.getConference().getPlace()){
                 ModeratorBean mb = new ModeratorBean(c.getConference().getId(), 0, c.getConference().getName(), c.getConference().getField(), c.getConference().getBeginDate(),c.getConference().getEndDate(), l.getCity(), l.getCountry(), l.getPlace(), l.getStreet());
                 mblist.add(mb);
                        }
                    }
             }
            }
             for(ModeratorBean m:mblist){
                 for(Agenda a:agendalist){
                     if((m.getConferenceid()) == (a.getConference().getId())){
                         m.setAgenda(a);
                         m.setAgendaid(a.getId());
                     }
                 } 
             }
         
                
         
         
         }
        return mblist;
     }
     
    public Map<String, String> RefreshLocationsInAddConferenceHB(String adminAddConferenceCity){
    
        
        List<Location> locationlist = null;
        Map<String,String> locationsmap = new HashMap<String, String>();
      try {
        org.hibernate.Transaction tx = session.beginTransaction();
        Query q = session.createQuery("from Location as location where location.city = '"+adminAddConferenceCity+"' ");
             locationlist = (List<Location>)q.list();
             session.getTransaction().commit();
       
        for(Location l:locationlist){
        locationsmap.put(l.getPlace(), l.getPlace());
        }
      
        } catch (Exception e) {
        e.printStackTrace();
        session.getTransaction().rollback();
        }
      return locationsmap;
        
    }
 

   public ModeratorBeanConferenceContent ModeratorPageViewConferenceProgramHB(ModeratorBean d){
   
   ModeratorBeanConferenceContent mbcc = new ModeratorBeanConferenceContent();
   int agendaid = d.getAgendaid();
   List<Sessions> sessionlist = null;
   List<SessionHallBean> sessionhalllist = new ArrayList<SessionHallBean>();   
   List<Hall> halllist = null;
   //List<Lecture> lecturelist = null;
   try {
        org.hibernate.Transaction tx = session.beginTransaction();
        Query q = session.createQuery("from Workshop as workshop where workshop.agenda = '"+agendaid+"' ");
             mbcc.setWorkshoplist((List<Workshop>)q.list());
             session.getTransaction().commit();
   
        org.hibernate.Transaction tx1 = session.beginTransaction();
         q = session.createQuery("from Sessionpause as sessionpause where sessionpause.agenda = '"+agendaid+"' ");
             mbcc.setSessionpauselist((List<Sessionpause>)q.list());
             session.getTransaction().commit();     
             
        org.hibernate.Transaction tx2 = session.beginTransaction();
         q = session.createQuery("from Openceremony as openceremony where openceremony.agenda = '"+agendaid+"' ");
             mbcc.setOpenceremonylist((List<Openceremony>)q.list());
             session.getTransaction().commit();    
             
        org.hibernate.Transaction tx3 = session.beginTransaction();
         q = session.createQuery("from Closeceremony as closeceremony where closeceremony.agenda = '"+agendaid+"' ");
             mbcc.setCloseceremonylist((List<Closeceremony>)q.list());
             session.getTransaction().commit();    
         
       org.hibernate.Transaction tx4 = session.beginTransaction();
         q = session.createQuery("from Sessions as session where session.agenda = '"+agendaid+"' ");
             sessionlist = (List<Sessions>) q.list();
             session.getTransaction().commit();   
             
             org.hibernate.Transaction tx5 = session.beginTransaction();
         q = session.createQuery("from Hall");
             halllist = (List<Hall>) q.list();
             session.getTransaction().commit();
             
             for(Sessions s:sessionlist){
                 for(Hall h:halllist){
                 if(h.getId() == s.getHall()){
                 SessionHallBean shb = new SessionHallBean(h.getConferenceHall(), s);
                 sessionhalllist.add(shb);
                 }
                 }
             }

             mbcc.setSessionhalllist(sessionhalllist);
             
           /*  org.hibernate.Transaction tx5 = session.beginTransaction();
         q = session.createQuery("from Lecture");
             lecturelist = (List<Lecture>) q.list();
             session.getTransaction().commit();   
             */
     
           /*
             for(Sessions s:sessionlist){
                 SessionLectureBean sl = new SessionLectureBean();
                 List<Lecture> sllist = new ArrayList<Lecture>(); 
                 sl.setSession(s.getId());
                 for(Lecture l:lecturelist){
                     if(l.getSessions().getId() == s.getId())
                     {
                         sllist.add(l);
                     }
                 }
             slbl.add(sl);
             }
             */
             
             
   } catch (Exception e) {
        e.printStackTrace();
        session.getTransaction().rollback();
        }
   
   return mbcc;
   }

    
   public Map<String, Integer> ModeratorpPageAddSessionHallList(String country, String city, String place, String street){
       
       Map<String, Integer> halllist = new HashMap<String, Integer>();
       List<Location> locationList = null;
       int locationid;
       List<Hall> halls = null;
       
       try {
        org.hibernate.Transaction tx = session.beginTransaction();
        Query q = session.createQuery("from Location as location where location.country = '"+country+"' "
                + " and location.city = '"+city+"' and location.place = '"+place+"' ");
             locationList = (List<Location>) q.list();
             locationid = locationList.get(0).getId();  
             session.getTransaction().commit();
       
        org.hibernate.Transaction tx1 = session.beginTransaction();
        q = session.createQuery("from Hall as hall where hall.location = '"+locationid+"' ");
             halls = (List<Hall>) q.list();
             session.getTransaction().commit(); 
             
             for(Hall h:halls){
             halllist.put(h.getConferenceHall(), h.getId());
             }

       } catch (Exception e) {
        e.printStackTrace();
        session.getTransaction().rollback();
        }
       return halllist;
   }
   
   public String ModeratorPageAddSessionToDBHP(Sessions moderatorpageSessionToAdd, String sessiondate){
       
       String message = "";
       List<Sessions> sessionlist = null;
       int hallid = moderatorpageSessionToAdd.getHall();
       try {
        org.hibernate.Transaction tx = session.beginTransaction();
        Query q = session.createQuery("from Sessions as session where session.date = '"+sessiondate+"' and session.hall = '"+hallid+"' ");
             sessionlist = (List<Sessions>) q.list();
             session.getTransaction().commit();
           //Ako nije prazna lista onda imamo preklapanja i javljamo gresku
             if(!(sessionlist.isEmpty())) { message = "You can not have two sessions on the same day in the same hall!";}
             else{
             
             org.hibernate.Transaction tx1 = session.beginTransaction();
             session.save(moderatorpageSessionToAdd); session.getTransaction().commit();
             message = "You have successfully add new session";
             }
       } catch (Exception e) {
        e.printStackTrace();
        session.getTransaction().rollback();
        }
       return message;
   }
   
   /*
   Prva verzija kada vracam autore a ne user-e iz baze
   public Map<String, String> ModeratoraddLectureAuthorList(){
   
    Map<String, String> authorlist = new HashMap<String, String>(); 
   List<Author> list = null;
    
   try {
        org.hibernate.Transaction tx = session.beginTransaction();
        Query q = session.createQuery("from Author as author where author.inDb = 1");
             list = (List<Author>) q.list();
             session.getTransaction().commit();
     } catch (Exception e) {
        e.printStackTrace();
        session.getTransaction().rollback();
        }
   
        for(Author a:list){
            authorlist.put(a.getUsername(), a.getUsername());
        }
    return authorlist;
   }
   */
   
   public Map<String, String> ModeratoraddLectureAuthorList(String username){
   
    Map<String, String> authorlist = new HashMap<String, String>(); 
   List<User> list = null;
    
   try {
        org.hibernate.Transaction tx = session.beginTransaction();
        Query q = session.createQuery("from User as user where user.username != '"+username+"' ");
             list = (List<User>) q.list();
             session.getTransaction().commit();
     } catch (Exception e) {
        e.printStackTrace();
        session.getTransaction().rollback();
        }
   
        for(User a:list){
            authorlist.put(a.getUsername(), a.getUsername());
        }
    return authorlist;
   }
   
   
   public String ModeratorPageAddSessionLectureToDBHP(Lecture newLecture ,String[] newLectureAuthors, String externalAuthor1, String externalAuthor2, String externalAuthor3, String externalAuthor4, String begintime, String endtime){
    
       String message = "";
       List<Lecture> lecturelist = null;
       int lectureid = 0;
       
       try{
        //Prvo proveravam da li postoji neko predavanju u okviru ove konferencije sa istim imenom
        org.hibernate.Transaction txx = session.beginTransaction();
        Query q = session.createQuery("from Lecture as lecture where lecture.sessions.agenda.conference.id = '"+newLecture.getSessions().getAgenda().getConference().getId()+"' and lecture.name = '"+newLecture.getName()+"' ");
        lecturelist = (List<Lecture>)q.list();
        session.getTransaction().commit();
        if(!(lecturelist.isEmpty())) { message = "Error! You already have lecture with name \""+ newLecture.getName() +"\" in this conference!";}  
        else { 
        
        org.hibernate.Transaction tx7 = session.beginTransaction();
        q = session.createQuery("from Lecture as lecture where lecture.sessions.id = '"+newLecture.getSessions().getId()+"' and lecture.startTime <= '"+begintime+"' "
                + " and lecture.endTime >= '"+begintime+"' ");
        lecturelist = (List<Lecture>)q.list();
        session.getTransaction().commit();
            if(!(lecturelist.isEmpty())) { message = "Error! You have overlap in lectures schedule!";} 
            else {
            
         org.hibernate.Transaction tx8 = session.beginTransaction();
        q = session.createQuery("from Lecture as lecture where lecture.sessions.id = '"+newLecture.getSessions().getId()+"' and lecture.startTime <= '"+endtime+"' "
                + " and lecture.endTime >= '"+endtime+"' ");
        lecturelist = (List<Lecture>)q.list();
        session.getTransaction().commit();
            if(!(lecturelist.isEmpty())) { message = "Error! You have overlap in lectures schedule!";}    
            else {       
            
         org.hibernate.Transaction tx = session.beginTransaction();
        session.save(newLecture); session.getTransaction().commit();
        
        String lecturename = newLecture.getName();
        int sessionlectureid = newLecture.getSessions().getId();
        
        org.hibernate.Transaction tx1 = session.beginTransaction();
        q = session.createQuery("from Lecture as lecture where lecture.sessions.id = '"+sessionlectureid+"' and lecture.name = '"+lecturename+"' ");
        lecturelist = (List<Lecture>)q.list();
        session.getTransaction().commit();
        lectureid = lecturelist.get(0).getId();
          
        for(String a:newLectureAuthors){
            Author newauth = new Author(newLecture, null, a, 1, 0);
            org.hibernate.Transaction tx2 = session.beginTransaction();
            session.save(newauth); session.getTransaction().commit();
        
            Myagenda newmyagenda = new Myagenda(newLecture.getSessions().getAgenda(), a, lectureid, "s", false, "");
            org.hibernate.Transaction txx1 = session.beginTransaction();
            session.save(newmyagenda); session.getTransaction().commit();
        }
        
        if(!(externalAuthor1.equals(""))) {
            Author newauth = new Author(newLecture, null, externalAuthor1 , 0, 0);
            org.hibernate.Transaction tx3 = session.beginTransaction();
            session.save(newauth); session.getTransaction().commit();
        }
        if(!(externalAuthor2.equals(""))) {
            Author newauth = new Author(newLecture, null, externalAuthor2 , 0, 0);
            org.hibernate.Transaction tx4 = session.beginTransaction();
            session.save(newauth); session.getTransaction().commit();
        }
        if(!(externalAuthor3.equals(""))) {
            Author newauth = new Author(newLecture, null, externalAuthor3 , 0, 0);
            org.hibernate.Transaction tx5 = session.beginTransaction();
            session.save(newauth); session.getTransaction().commit();
        }
        if(!(externalAuthor4.equals(""))) {
            Author newauth = new Author(newLecture, null, externalAuthor4 , 0, 0);
            org.hibernate.Transaction tx6 = session.beginTransaction();
            session.save(newauth); session.getTransaction().commit();
            }
        
            message = "You have successfully added new lecture";
            
            //Saljem poruke svima kojima sam ja favourite da imam predavanje
            if(newLectureAuthors != null){
            String conferenceName = lecturelist.get(0).getSessions().getAgenda().getConference().getName();
            String lectureName = lecturelist.get(0).getName();
            List<Favourites> favouritelist = null;
            String messageContent = "Hey, I am having lecture \""+lectureName+"\" on conference \""+conferenceName+"\". I will be very happy if I see you there.";
            
            session.getTransaction().begin();
            q = session.createQuery("from Favourites");
            favouritelist = (List<Favourites>) q.list();
            session.getTransaction().commit();
            
            for(String auth: newLectureAuthors){
                for(Favourites f:favouritelist){
                    if(auth.equals(f.getFavouriteUsername())){
                        Messages msg = new Messages(auth, f.getUsername(), messageContent);
                        session.getTransaction().begin();
                        session.save(msg); session.getTransaction().commit();
                    }
                }
            }
            
            
            }
            
            }
     }
   }   
    }catch(RuntimeException e){              
        session.getTransaction().rollback();
}
       
       return message;
   }
   
   
   public String ModeratorPageAddWorkshopToDBHP(Workshop newWorkshop ,String[] newLectureAuthors, String externalAuthor1, String externalAuthor2, String externalAuthor3, String externalAuthor4, String begintime, String endtime){
   
    
       String message = "";
       List<Workshop> workshoplist = null;
       int workshopid = 0;
       
       try{
        Query q; 
              
         org.hibernate.Transaction tx = session.beginTransaction();
        session.save(newWorkshop); session.getTransaction().commit();
        
        String topic = newWorkshop.getTopic();
        int agendaid = newWorkshop.getAgenda().getId();
        
        org.hibernate.Transaction tx1 = session.beginTransaction();
        q = session.createQuery("from Workshop as workshop where workshop.agenda.id = '"+agendaid+"' and workshop.topic = '"+topic+"' "
                + " and workshop.beginTime = '"+begintime+"' and workshop.endTime = '"+endtime+"'");
        workshoplist = (List<Workshop>)q.list();
        session.getTransaction().commit();
        workshopid = workshoplist.get(0).getId();
        
        for(String a:newLectureAuthors){
            Author newauth = new Author(null, newWorkshop, a, 1, 0);
            org.hibernate.Transaction tx2 = session.beginTransaction();
            session.save(newauth); session.getTransaction().commit();
        }
        
        if(!(externalAuthor1.equals(""))) {
            Author newauth = new Author(null, newWorkshop, externalAuthor1 , 0, 0);
            org.hibernate.Transaction tx3 = session.beginTransaction();
            session.save(newauth); session.getTransaction().commit();
        }
        if(!(externalAuthor2.equals(""))) {
            Author newauth = new Author(null, newWorkshop, externalAuthor2 , 0, 0);
            org.hibernate.Transaction tx4 = session.beginTransaction();
            session.save(newauth); session.getTransaction().commit();
        }
        if(!(externalAuthor3.equals(""))) {
            Author newauth = new Author(null, newWorkshop, externalAuthor3 , 0, 0);
            org.hibernate.Transaction tx5 = session.beginTransaction();
            session.save(newauth); session.getTransaction().commit();
        }
        if(!(externalAuthor4.equals(""))) {
            Author newauth = new Author(null, newWorkshop, externalAuthor4 , 0, 0);
            org.hibernate.Transaction tx6 = session.beginTransaction();
            session.save(newauth); session.getTransaction().commit();
            }
        
            message = "You have successfully added new workshop";
            
     
                
    }catch(RuntimeException e){              
        session.getTransaction().rollback();
}
       return message;
   }
   
 public String ModeratorPageAddOpenCeremonyToDBHP(Openceremony newOpenCeremony){
 
     String message = "";
     
     try{
        org.hibernate.Transaction tx = session.beginTransaction();
        session.save(newOpenCeremony); session.getTransaction().commit();
        message = "You have successfully added new open ceremony";
    }catch(RuntimeException e){              
        session.getTransaction().rollback();
}      
     return message;
 }  
   
   
 public String ModeratorPageAddCloseCeremonyToDBHP(Closeceremony newCloseCeremony){
 
     String message = "";
     
     try{
        org.hibernate.Transaction tx = session.beginTransaction();
        session.save(newCloseCeremony); session.getTransaction().commit();
        message = "You have successfully added new close ceremony";
    }catch(RuntimeException e){              
        session.getTransaction().rollback();
} 
     return message;
 }  
 
 public String ModeratorPageAddConferenceImagesToDBHP(Gallery newImage){
 
     String message = "";
     
     try{
        org.hibernate.Transaction tx = session.beginTransaction();
        session.save(newImage); session.getTransaction().commit();
        message = "You have successfully added new image to gallery";
    }catch(RuntimeException e){              
        session.getTransaction().rollback();
} 
     return message;
 }
 
 
 public String ModeratorPageAddSessionImageToDBHP(Gallery newImage){
 
     String message = "";
     
     try{
        org.hibernate.Transaction tx = session.beginTransaction();
        session.save(newImage); session.getTransaction().commit();
        message = "You have successfully added new image to gallery";
    }catch(RuntimeException e){              
        session.getTransaction().rollback();
} 
     return message;
 }
 
 
 public List<ModeratorBean>GetRegularUserMyConferenceList(String username){
 
     List<ModeratorBean> list = new ArrayList<ModeratorBean>();
     List<Agenda> agendalist = null;
     List<Myconferences> myConferenceslist = null;
     List<Location> locationlist = null;
     
     
       try {
        org.hibernate.Transaction tx = session.beginTransaction();
        Query q = session.createQuery("from Myconferences as myconferences where myconferences.username = '"+username+"' ");
             myConferenceslist = (List<Myconferences>) q.list();
             session.getTransaction().commit();
     
        org.hibernate.Transaction tx1 = session.beginTransaction();     
             q = session.createQuery("from Location");
             locationlist = (List<Location>) q.list();
             session.getTransaction().commit();
             
             
            tx = session.beginTransaction();
             q = session.createQuery("from Agenda");
             agendalist = (List<Agenda>)q.list();
             session.getTransaction().commit();     
             
             for(Myconferences c:myConferenceslist){
                 for(Location l:locationlist){
                 if(l.getId() == c.getConference().getPlace()){
                 ModeratorBean gmb = new ModeratorBean(c.getConference().getId(), 0, c.getConference().getName(), c.getConference().getField(), c.getConference().getBeginDate(), c.getConference().getEndDate(), l.getCity(), l.getCountry(), l.getPlace(), l.getStreet());
                 list.add(gmb);
                        }
                    }
             }
             
             for(ModeratorBean m:list){
                 for(Agenda a:agendalist){
                     if(m.getConferenceid() == a.getConference().getId()){
                         m.setAgendaid(a.getId());
                         m.setAgenda(a);
                     }
                 } 
             }
             
     }catch(RuntimeException e){              
        session.getTransaction().rollback();
}
     return list;
 }
 
 
 public String AddSessionToMyAgendaHB(int idevent, int conferenceid, String username, String type){
 
    String message = "";
    List<Myagenda> myagendalist = null;
    List<Agenda> agendaList = null;
    Agenda agenda = null;
   // int conferenceid = d.getSession().getAgenda().getConference().getId();
    try {
        session.getTransaction().begin();
        Query q = session.createQuery("from Myagenda as myagenda where myagenda.idevent = '"+idevent+"' "
                + " and myagenda.agenda.conference.id = '"+conferenceid+"' and myagenda.username = '"+username+"' and myagenda.type = '"+type+"' ");
             myagendalist = (List<Myagenda>) q.list();
             session.getTransaction().commit();
         if(!(myagendalist.isEmpty())) { message = "You have already added this event in your agenda."; } 
            
         else{
        session.getTransaction().begin();
         q = session.createQuery("from Agenda as agenda where agenda.conference.id = '"+conferenceid+"' ");
             agendaList = (List<Agenda>) q.list();
             session.getTransaction().commit();
    
         agenda = agendaList.get(0);
    
        Myagenda newMyAgenda = new Myagenda(agenda, username, idevent, type, false, "");
    
        session.getTransaction().begin();
        session.save(newMyAgenda); 
       session.getTransaction().commit();
        message = "You have successfully added this event in your agenda";
         }
    }catch(RuntimeException e){              
        session.getTransaction().rollback();
}

    return message;
 }
 
 public RegularUserMyAgenda GetRegularUserMyAgendaList(String username, ModeratorBean d){
 
     RegularUserMyAgenda ruma = new RegularUserMyAgenda();
     List<Myagenda> myagendalist = null; 
     List<Sessions> sessionlist = null;
     List<Hall> halllist = null;
     List<SessionHallBean> sessionhalllist = new ArrayList<SessionHallBean>();
     List<Lecture> lecturelist = new ArrayList<Lecture>();
     List<Lecture> helplecturelist = null;
     List<Workshop> workshoplist = new ArrayList<Workshop>();
     List<Workshop> helpworkshoplist = null;
     List<Openceremony> openceremonylist = new ArrayList<Openceremony>();
     List<Openceremony> helpopenceremonylist = null;
     List<Closeceremony> closeceremonylist = new ArrayList<Closeceremony>();
     List<Closeceremony> helpcloseceremonylist = null;
     List<LectureAuthorBean> lecb =  new ArrayList<LectureAuthorBean>();
     
     String types = "s";
     String typew = "w";
     String typeoc = "oc";
     String typecc = "cc";
     try {
        session.getTransaction().begin();
        Query q = session.createQuery("from Myagenda as myagenda where myagenda.username = '"+username+"'"
                + " and  myagenda.type = '"+types+"' and myagenda.agenda.id = '"+d.getAgenda().getId()+"' ");
             myagendalist = (List<Myagenda>) q.list();
             session.getTransaction().commit();
     
           
             org.hibernate.Transaction tx4 = session.beginTransaction();
         q = session.createQuery("from Sessions");
             sessionlist = (List<Sessions>) q.list();
             session.getTransaction().commit();   
             
             org.hibernate.Transaction tx5 = session.beginTransaction();
         q = session.createQuery("from Hall");
             halllist = (List<Hall>) q.list();
             session.getTransaction().commit();
             
             for(Myagenda ml:myagendalist){
             for(Sessions s:sessionlist){
                 if(ml.getIdevent() == s.getId()){
                 for(Hall h:halllist){
                 if(h.getId() == s.getHall()){
                 SessionHallBean shb = new SessionHallBean(h.getConferenceHall(), s);
                 sessionhalllist.add(shb);
                 }
                 }
             }
             }
             }
             ruma.setSessionhalllist(sessionhalllist);
             
             
             org.hibernate.Transaction tx6 = session.beginTransaction();
         q = session.createQuery("from Lecture");
             helplecturelist = (List<Lecture>) q.list();
             session.getTransaction().commit();
             
             for(SessionHallBean shbl:sessionhalllist){
                 for(Lecture lect:helplecturelist){
                     if((lect.getSessions().getId()) == (shbl.getSession().getId())){
                         lecturelist.add(lect);
                        //
                         LectureAuthorBean lcc = new LectureAuthorBean(lect);
                         lecb.add(lcc);
                        //
                     }
                 }
             }
             ruma.setLecturelist(lecturelist);
             
             
             session.getTransaction().begin();
             q = session.createQuery("from Myagenda as myagenda where myagenda.username = '"+username+"'"
                + " and  myagenda.type = '"+typew+"' and myagenda.agenda.id = '"+d.getAgenda().getId()+"' ");
             myagendalist = (List<Myagenda>) q.list();
             session.getTransaction().commit();
             
             session.getTransaction().begin();
             q = session.createQuery("from Workshop");
             helpworkshoplist = (List<Workshop>)q.list();
             session.getTransaction().commit();
             
           for(Myagenda ml:myagendalist){
                 for(Workshop ws:helpworkshoplist){
                     if(ml.getIdevent() == ws.getId()){
                         workshoplist.add(ws);
                     }
                 }
             }
             ruma.setWorkshoplist(workshoplist);
             
             
             session.getTransaction().begin();
             q = session.createQuery("from Myagenda as myagenda where myagenda.username = '"+username+"'"
                + " and  myagenda.type = '"+typeoc+"' and myagenda.agenda.id = '"+d.getAgenda().getId()+"' ");
             myagendalist = (List<Myagenda>) q.list();
             session.getTransaction().commit();
             
             session.getTransaction().begin();
             q = session.createQuery("from Openceremony");
             helpopenceremonylist = (List<Openceremony>)q.list();
             session.getTransaction().commit();
             
           for(Myagenda ml:myagendalist){
                 for(Openceremony ocl:helpopenceremonylist){
                     if(ml.getIdevent() == ocl.getId()){
                         openceremonylist.add(ocl);
                     }
                 }
             }
             ruma.setOpenceremonylist(openceremonylist);
             
             
             session.getTransaction().begin();
             q = session.createQuery("from Myagenda as myagenda where myagenda.username = '"+username+"'"
                + " and  myagenda.type = '"+typecc+"' and myagenda.agenda.id = '"+d.getAgenda().getId()+"' ");
             myagendalist = (List<Myagenda>) q.list();
             session.getTransaction().commit();
             
             session.getTransaction().begin();
             q = session.createQuery("from Closeceremony");
             helpcloseceremonylist = (List<Closeceremony>)q.list();
             session.getTransaction().commit();
             
           for(Myagenda ml:myagendalist){
                 for(Closeceremony occ:helpcloseceremonylist){
                     if(ml.getIdevent() == occ.getId()){
                         closeceremonylist.add(occ);
                     }
                 }
             }
            ruma.setCloseceremonylist(closeceremonylist);
            //
            List<Author> authorslist = null;
            session.getTransaction().begin();
            q = session.createQuery("from Author");
            authorslist = (List<Author>)q.list();
            session.getTransaction().commit();
           
            for(LectureAuthorBean lc:lecb){
                List<String> names = new ArrayList<String>();
                List<Author> anames = new ArrayList<Author>();
                boolean flag = false;
                for(Author at:authorslist){
                    if(at.getLecture() != null){
                    if(lc.getLecture().getId() == at.getLecture().getId()){
                    names.add(at.getUsername());
                    anames.add(at);
                    if(at.getUsername().equals(username)){ flag = true; }
                    }
                    }
                }
                lc.setAuthors(names);
                lc.setAuthorlist(anames);
                lc.setMeauthor(flag);
            }
            ruma.setLab(lecb);
           //
           
             
     }catch(RuntimeException e){              
        session.getTransaction().rollback();
}
     
     return ruma;
     
 }
 
 public String LikeEvent(int eventid, int agendaid, String type, String username){
 
     String message = "";
     List<Myagenda> myagendalist = null;
     Myagenda agenda = null;
     
     try {
        session.getTransaction().begin();
        Query q = session.createQuery("from Myagenda as myagenda where myagenda.username = '"+username+"'"
                + " and  myagenda.type = '"+type+"' and myagenda.idevent = '"+eventid+"' and myagenda.agenda.id = '"+agendaid+"' ");
             myagendalist = (List<Myagenda>) q.list();
             agenda = myagendalist.get(0);
             session.getTransaction().commit();
      
             agenda.setLikeOption(true);
            session.getTransaction().begin();
            session.update(agenda); session.getTransaction().commit();
            message = "You have successfully liked this event";
     }catch(RuntimeException e){              
        session.getTransaction().rollback();
}
     return message;    
 }
 
public String CommentEvent(int eventid, int agendaid, String type, String username, String comment){

    String message = "";
    List<Myagenda> myagendalist = null;
    Myagenda agenda = null;
     
     try {
        session.getTransaction().begin();
        Query q = session.createQuery("from Myagenda as myagenda where myagenda.username = '"+username+"'"
                + " and  myagenda.type = '"+type+"' and myagenda.idevent = '"+eventid+"' and myagenda.agenda.id = '"+agendaid+"' ");
             myagendalist = (List<Myagenda>) q.list();
             agenda = myagendalist.get(0);
             session.getTransaction().commit();
      
             agenda.setComment(comment);
            session.getTransaction().begin();
            session.update(agenda); session.getTransaction().commit();
            message = "You have successfully left comment";
     }catch(RuntimeException e){              
        session.getTransaction().rollback();
}
     return message;   
}

public String GradeEvent(int lectureid, String username, int grade){

    String message = "";
    List<Lecture> lecturelist = null;
    Lecture lecture = null;
     
     try {
        session.getTransaction().begin();
        Query q = session.createQuery("from Lecture as lecture where lecture.id = '"+lectureid+"' ");
             lecturelist = (List<Lecture>) q.list();
             lecture = lecturelist.get(0);
             session.getTransaction().commit();
      
            Lecturegrade newLG = new Lecturegrade(lecture, grade, username);
            session.getTransaction().begin();
            session.save(newLG); session.getTransaction().commit();
            message = "You have successfully left grade for this lecture";
     }catch(RuntimeException e){              
        session.getTransaction().rollback();
}
     return message; 
    
}

public List<User> GetConferenceVisitorsList(int conferenceid, String username){

    List<User> userlist = new ArrayList<User>();
    List<User> helpuserlist = null;
    List<Myconferences> myconferenceslist = null;
    
    session.getTransaction().begin();
        Query q = session.createQuery("from Myconferences as myconferences where myconferences.conference.id = '"+conferenceid+"' ");
        myconferenceslist = (List<Myconferences>) q.list();
        session.getTransaction().commit();
    
        session.getTransaction().begin();
        q = session.createQuery("from User");
        helpuserlist = (List<User>) q.list();
        session.getTransaction().commit();

        for(User u:helpuserlist){
            if(!(u.getUsername().equals(username))){
            for(Myconferences mycl: myconferenceslist){
            if(u.getUsername().equals(mycl.getUsername())){
                userlist.add(u);
                   }
                }
            }
        }
       
    return userlist;
}

public String SendConferenceMessage(Messages newMessage){

    String message = "";
     
     try {
            session.getTransaction().begin();
            session.save(newMessage); session.getTransaction().commit();
            message = "You have successfully sent message to user: "+newMessage.getUserFor()+"";
     }catch(RuntimeException e){              
        session.getTransaction().rollback();
}
     return message; 
      
}

public String AddToFavouritesUserDB(Favourites newFavourite){

    String message = "";
     
     try {
            session.getTransaction().begin();
            session.save(newFavourite); session.getTransaction().commit();
            message = "You have successfully add user: "+newFavourite.getFavouriteUsername()+" in your favourites";
     }catch(RuntimeException e){              
        session.getTransaction().rollback();
}
     return message; 
      
}
        

public List<Messages> GetMyMailbox(String username){
    
    List<Messages> messagelist = null;

    try {
    session.getTransaction().begin();
    Query q = session.createQuery("from Messages as messages where messages.userFor = '"+username+"' ");
    messagelist = (List<Messages>) q.list();
    session.getTransaction().commit();
        }catch(RuntimeException e){              
        session.getTransaction().rollback();
    }
    return messagelist;
}


public List<Gallery> GetGalleryForThisConference(int conferenceid){

    List<Gallery> gallerylist = null;

     try {
    session.getTransaction().begin();
    Query q = session.createQuery("from Gallery as gallery where gallery.conferenceid = '"+conferenceid+"' ");
    gallerylist = (List<Gallery>) q.list();
    session.getTransaction().commit();
        }catch(RuntimeException e){              
        session.getTransaction().rollback();
    }
    return gallerylist;
}

public List<Sessions> returnAllSessionsList(){

    List<Sessions> list = null;
    
    try {
    session.getTransaction().begin();
    Query q = session.createQuery("from Sessions");
    list = (List<Sessions>) q.list();
    session.getTransaction().commit();
        }catch(RuntimeException e){              
        session.getTransaction().rollback();
    }
    return list;
}

public boolean findMeinAuthor(User u){

    List<Author> list = null;
    boolean value = false;
    
    try {
    session.getTransaction().begin();
    Query q = session.createQuery("from Author as author where author.username = '"+u.getUsername()+"'"
            + " and author.inDb = 1 ");
    list = (List<Author>) q.list();
    session.getTransaction().commit();
    if(list.isEmpty()) { value = false; }     
    else { value = true; }
    }catch(RuntimeException e){              
        session.getTransaction().rollback();
    }
    return value;
}

public String HoldPresentationHB(int lectureid, String username){
    
    String message = "";
    List<Author> authorlist = null;
    Author author = null;
    
     try {
            session.getTransaction().begin();
            Query q = session.createQuery("from Author as author where author.username = '"+username+"'"
            + " and author.lecture.id = '"+lectureid+"' ");
            authorlist = (List<Author>) q.list();
            session.getTransaction().commit();
            author = authorlist.get(0);
            
            if(author.getPresenting() == 1) { message = "You have already announced that you are holding presentation."; }
            else {
            author.setPresenting(1);
            session.getTransaction().begin();
            session.update(author); session.getTransaction().commit();
            message = "You have successfully announced that you are holding presentation.";
            }
      }catch(RuntimeException e){              
       session.getTransaction().rollback();
}
     return message; 
}


public String LectureAddFileToDBDH(int lectureid, int conferenceid, String username, String types, byte[] content, String documentname){
    
    String message = "";
    List<Lecturedocument> lecturedocumentlist = null;
    Lecturedocument ld =  null;
    
    try {
            session.getTransaction().begin();
            Query q = session.createQuery("from Lecturedocument as lecturedocument where lecturedocument.username = '"+username+"'"
            + " and lecturedocument.lectureid = '"+lectureid+"' and lecturedocument.type = '"+types+"' ");
            lecturedocumentlist = (List<Lecturedocument>) q.list();
            session.getTransaction().commit();
            if(!(lecturedocumentlist.isEmpty())){
                ld = lecturedocumentlist.get(0);
                ld.setDocument(content);
            
            session.getTransaction().begin();
            session.update(ld); session.getTransaction().commit();
            message = "You have successfully uploaded document for your lecture."; 
            }
            else{
            ld = new Lecturedocument(lectureid, conferenceid, content, username, types, documentname);
            session.getTransaction().begin();
            session.save(ld); session.getTransaction().commit();
            message = "You have successfully uploaded document \""+ documentname +"\" for your lecture."; 
            }
    }catch(RuntimeException e){              
       session.getTransaction().rollback();
}
    return message;
}

public List<LectureFileDownloadBean> GetDownloadLectureFile(int conferenceid){

List<LectureFileDownloadBean> lfdb = new ArrayList<LectureFileDownloadBean>();
List<Lecture> lecturelist = null; 
List<Lecturedocument>lecturedocumentlist = null;

try {
            session.getTransaction().begin();
            Query q = session.createQuery("from Lecture");
            lecturelist = (List<Lecture>) q.list();
            session.getTransaction().commit();

            session.getTransaction().begin();
            q = session.createQuery("from Lecturedocument as lecturedocument where lecturedocument.conferenceid = '"+conferenceid+"'");
            lecturedocumentlist = (List<Lecturedocument>) q.list();
            session.getTransaction().commit();

            for(Lecture l:lecturelist){
                List<Lecturedocument> helplecturedocumentlist = new ArrayList<Lecturedocument>();
                for(Lecturedocument ld:lecturedocumentlist){
                    if(l.getId() == ld.getLectureid()){
                        StreamedContent file = new DefaultStreamedContent(new ByteArrayInputStream(ld.getDocument()), ld.getType(), ld.getDocumentname()+"."+ld.getType()); 
                        
                        Lecturedocument newld = new Lecturedocument(ld.getLectureid(), ld.getConferenceid(), ld.getDocument(), ld.getUsername(), ld.getType(), ld.getDocumentname());
                        newld.setFile(file);
                        helplecturedocumentlist.add(newld);
                    }
                }
            //Ako za to predavanje nema dokumenata onda nema potreba da ga prikazujem na stranici za skidanje dokumenata
             if(!(helplecturedocumentlist.isEmpty())){
                 LectureFileDownloadBean newlfdb = new LectureFileDownloadBean(l);
                 newlfdb.setLecturedocumentlist(helplecturedocumentlist);
                 lfdb.add(newlfdb);
             }
            }
            
}catch(RuntimeException e){              
       session.getTransaction().rollback();
}
        return lfdb;
}

public List<String> completeTextHB(String w){
    
    List<Conference> conferencelist = null;
    List<String> resultlist = new ArrayList<String>();
  try {
            session.getTransaction().begin();
            Query q = session.createQuery("from Conference");
            conferencelist = (List<Conference>) q.list();
            session.getTransaction().commit();
            
            for(Conference conf:conferencelist){
                if(conf.getName().contains(w)){
                resultlist.add(conf.getName());
                }
             }
  }catch(RuntimeException e){              
       session.getTransaction().rollback();
}
            return resultlist;
}
    
}