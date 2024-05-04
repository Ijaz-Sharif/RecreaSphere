package com.example.recreasphere.Model;

public class Event {
    String eventTitle,eventVenu,eventDate,eventStartTime,eventId
            ,teamAMember,teamBMember,eventStatus;

    public Event(String eventTitle, String eventDate, String eventStartTime,
                 String eventVenu,
                 String eventId
    ,String teamAMember,String teamBMember) {
        this.eventTitle = eventTitle;
        this.eventDate = eventDate;
        this.eventStartTime = eventStartTime;
        this.eventId = eventId;
        this.eventVenu=eventVenu;
        this.teamAMember=teamAMember;
        this.teamBMember=teamBMember;
    }
    public Event(String eventTitle, String eventDate, String eventStartTime,
                 String eventVenu,
                 String eventId
    ,String eventStatus) {
        this.eventTitle = eventTitle;
        this.eventDate = eventDate;
        this.eventStartTime = eventStartTime;
        this.eventId = eventId;
        this.eventVenu=eventVenu;
        this.eventStatus=eventStatus;
    }
    public Event(String eventTitle, String eventDate, String eventStartTime,
                 String eventVenu,
                 String eventId) {
        this.eventTitle = eventTitle;
        this.eventDate = eventDate;
        this.eventStartTime = eventStartTime;
        this.eventId = eventId;
        this.eventVenu=eventVenu;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public String getTeamAMember() {
        return teamAMember;
    }

    public String getTeamBMember() {
        return teamBMember;
    }

    public String getEventVenu() {
        return eventVenu;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventStartTime() {
        return eventStartTime;
    }



    public String getEventId() {
        return eventId;
    }
}
