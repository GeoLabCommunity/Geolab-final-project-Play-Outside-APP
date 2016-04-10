package geolab.playoutside.model;

import java.io.Serializable;

/**
 * Created by GeoLab on 3/29/16.
 */
public class CommentsModel implements Serializable{
    private String profileImage;
    private String dateTime;
    private String comment;

    public CommentsModel(String profileImage, String dateTime, String comment) {
        this.profileImage = profileImage;
        this.dateTime = dateTime;
        this.comment = comment;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
