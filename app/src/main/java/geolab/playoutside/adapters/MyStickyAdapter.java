package geolab.playoutside.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import geolab.playoutside.R;
import geolab.playoutside.db.Category_db;
import geolab.playoutside.model.MyEvent;
import geolab.playoutside.view.EventDetailActivity;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by GeoLab on 1/11/16.
 */
public class MyStickyAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private ArrayList<MyEvent> eventsList;
    private LayoutInflater inflater;
    private Context context;

    public MyStickyAdapter(Context context, ArrayList<MyEvent> list) {
        inflater = LayoutInflater.from(context);
        eventsList = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return eventsList.size();
    }

    @Override
    public Object getItem(int position) {
        return eventsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            convertView = inflater.inflate(R.layout.news_category_item, parent, false);

            holder.timeHolder = (TextView) convertView.findViewById(R.id.time_text_id);
            holder.titleHolder = (TextView) convertView.findViewById(R.id.title_text_id);
            holder.descriptionHolder = (TextView) convertView.findViewById(R.id.description_text_id);
            holder.placeHolder = (TextView) convertView.findViewById(R.id.place_text_id);
            holder.playerHolder = (TextView) convertView.findViewById(R.id.player_count_id);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.timeHolder.setText(eventsList.get(position).getTime());
        holder.timeHolder.setBackgroundResource(Category_db.category_icons[eventsList.get(position).getCategory_id()]);

        holder.titleHolder.setText(eventsList.get(position).getTitle());
        holder.descriptionHolder.setText(eventsList.get(position).getDescription()+"...");
        holder.placeHolder.setText(eventsList.get(position).getPlace());
        holder.playerHolder.setText(eventsList.get(position).getPlayerCount());

        return convertView;
    }


    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.header, parent, false);
            holder.headerDateHolder = (TextView) convertView.findViewById(R.id.header_text_id);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        //set header timeHolder as first char in name
        String headerText = "" + eventsList.get(position).getDate();
        holder.headerDateHolder.setText(headerText);
        return convertView;
    }


    public AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            MyEvent event = (MyEvent) parent.getAdapter().getItem(position);

            Intent detailIntent = new Intent(context, EventDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("event",event);
            detailIntent.putExtras(bundle);

            context.startActivity(detailIntent);

        }

    };

    @Override
    public long getHeaderId(int position) {
        //return the first character of the country as ID because this is what headers are based upon

        if(eventsList.get(position).getDate() == null || eventsList.get(position).getDate().isEmpty())
            return 1;
        else {
            Date dat = new Date(eventsList.get(position).getDate());
            Calendar c = Calendar.getInstance();
            c.setTime(dat);

            return c.getTimeInMillis();
        }
    }

    class HeaderViewHolder {
        TextView headerDateHolder;
    }

    class ViewHolder {
        TextView timeHolder, titleHolder, descriptionHolder, placeHolder, playerHolder;

    }
}
