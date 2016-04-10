package geolab.playoutside.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import geolab.playoutside.R;
import geolab.playoutside.model.ExpMenuItem;
import geolab.playoutside.model.SubMenu;

/**
 * Created by GeoLab on 1/19/16.
 */
public class CustomExpAdapter extends BaseExpandableListAdapter {

    private ArrayList<ExpMenuItem> menus;
    private Context context;
    private LayoutInflater inflater;

    public CustomExpAdapter(Context context, ArrayList<ExpMenuItem> menus){
        this.menus = menus;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return menus.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return menus.get(groupPosition).getSubMenus().size() ;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return menus.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return menus.get(groupPosition).getSubMenus().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.exp_list_item,parent,false);

        ImageView parentMenuIcon = (ImageView) convertView.findViewById(R.id.parent_menu_icon_id);
        TextView parentMenuText = (TextView) convertView.findViewById(R.id.parent_menu_text_id);

        ExpMenuItem menuItem = (ExpMenuItem) getGroup(groupPosition);

        parentMenuIcon.setBackgroundResource(menuItem.getMenuIconId());
        parentMenuText.setText(menuItem.getParentMenu());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.exp_list_child_item,parent,false);

        ImageView subMenuIcon = (ImageView) convertView.findViewById(R.id.sub_menu_icon_id);
        TextView subMenuText = (TextView) convertView.findViewById(R.id.sub_menu_text_id);

        SubMenu childMenu = (SubMenu) getChild(groupPosition,childPosition);

        subMenuIcon.setBackgroundResource(childMenu.getIconId());
        subMenuText.setText(childMenu.getMenuName());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}