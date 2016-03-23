package geolab.playoutside.model;

import java.io.Serializable;

/**
 * Created by GeoLab on 3/23/16.
 */
public class AllPlayersModel implements Serializable {
    private String fb_id, name, mail, birthday, reiting;

    public AllPlayersModel(String fb_id, String name, String mail, String birthday, String reiting) {
        this.fb_id = fb_id;
        this.name = name;
        this.mail = mail;
        this.birthday = birthday;
        this.reiting = reiting;
    }

    public String getFb_id() {
        return fb_id;
    }

    public void setFb_id(String fb_id) {
        this.fb_id = fb_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getReiting() {
        return reiting;
    }

    public void setReiting(String reiting) {
        this.reiting = reiting;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
