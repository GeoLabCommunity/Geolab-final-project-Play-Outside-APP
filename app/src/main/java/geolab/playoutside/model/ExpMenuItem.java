package geolab.playoutside.model;

import java.util.ArrayList;

/**
 * Created by GeoLab on 1/19/16.
 */
public class ExpMenuItem {
    private String parentMenu;
    private ArrayList<SubMenu> subMenus;
    private int menuIconId;

    public ExpMenuItem(String parentMenu, ArrayList<SubMenu> subMenus, int menuIconId) {
        this.parentMenu = parentMenu;
        this.subMenus = subMenus;
        this.menuIconId = menuIconId;
    }

    public String getParentMenu() {
        return parentMenu;
    }

    public void setParentMenu(String parentMenu) {
        this.parentMenu = parentMenu;
    }

    public ArrayList<SubMenu> getSubMenus() {
        return subMenus;
    }

    public void setSubMenus(ArrayList<SubMenu> subMenus) {
        this.subMenus = subMenus;
    }

    public int getMenuIconId() {
        return menuIconId;
    }

    public void setMenuIconId(int menuIconId) {
        this.menuIconId = menuIconId;
    }
}
