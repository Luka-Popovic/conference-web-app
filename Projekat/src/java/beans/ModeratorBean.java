
package beans;

import hibernate.*;
import java.util.Date;
import java.util.List;

public class ModeratorBean {
    
    private int conferenceid;
    private int agendaid;
    private Agenda agenda;
    private String name;
    private String field;
    private Date beginDate;
    private Date endDate;
    private String city;
    private String country;
    private String place;
    private String street;
    private boolean rendered = false;
   
    
    public ModeratorBean(int conferenceid, int agendaid, String name, String field, Date beginDate, Date endDate, String city, String country, String place, String street) {
        this.conferenceid = conferenceid;
        this.agendaid = agendaid;
        this.name = name;
        this.field = field;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.city = city;
        this.country = country;
        this.place = place;
        this.street = street;
    }

    public Agenda getAgenda() {
        return agenda;
    }

    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;
    }

    public int getConferenceid() {
        return conferenceid;
    }

    public void setConferenceid(int conferenceid) {
        this.conferenceid = conferenceid;
    }

    public int getAgendaid() {
        return agendaid;
    }

    public void setAgendaid(int agendaid) {
        this.agendaid = agendaid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public boolean isRendered() {
        return rendered;
    }

    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }
    
    
    
}
