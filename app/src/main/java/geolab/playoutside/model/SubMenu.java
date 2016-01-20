package geolab.playoutside.model;

/**
 * Created by GeoLab on 1/19/16.
 */
public class SubMenu {
    private String menuName;
    private int iconId;

    public SubMenu(String menuName, int iconId) {
        this.menuName = menuName;
        this.iconId = iconId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }
}
