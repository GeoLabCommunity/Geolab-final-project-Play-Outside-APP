package geolab.playoutside.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by GeoLab on 1/11/16.
 */
public class MyEvent implements Serializable {
    private int eventId;
    private String user_id, time,date,title, description, place, playerCount, longitude, latitude;
    private int category_id;
    private List<String> events;

    public MyEvent(int eventId, String user_id, String time, String date, String title, String description, String place, String player, String longitude, String latitude, int category_id, List<String> events) {
        this.eventId = eventId;
        this.user_id = user_id;
        this.time = time;
        this.date = date;
        this.title = title;
        this.description = description;
        this.place = place;
        this.playerCount = player;
        this.longitude=longitude;
        this.latitude=latitude;
        this.category_id=category_id;
        this.events = events;

    }


    public List<String> getEvents() {
        return events;
    }

    public void setEvents(List<String> events) {
        this.events = events;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return user_id;
    }

    public void setId(String user_id) {
        this.user_id = user_id;
    }

    public String getTime() {
        return time;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(String playerCount) {
        this.playerCount = playerCount;
    }
}
