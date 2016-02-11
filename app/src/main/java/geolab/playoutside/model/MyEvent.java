package geolab.playoutside.model;

import java.io.Serializable;

/**
 * Created by GeoLab on 1/11/16.
 */
public class MyEvent implements Serializable {
    private String time,date,title, description, place, playerCount, longitude, latitude;
    private long id;
    private int category_id;

    public MyEvent(String time,String date, String title, String description, String place, String player,String longitude, String latitude, long id, int category_id) {
        this.time = time;
        this.title = title;
        this.description = description;
        this.place = place;
        this.playerCount = player;
        this.date = date;
        this.id = id;
        this.category_id=category_id;
        this.longitude=longitude;
        this.latitude=latitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
