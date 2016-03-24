package geolab.playoutside.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import geolab.playoutside.R;
import geolab.playoutside.ViewProfile;
import geolab.playoutside.db.Category_db;
import geolab.playoutside.model.AllPlayersModel;
import geolab.playoutside.model.MyEvent;
import geolab.playoutside.view.EventDetailActivity;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by GeoLab on 1/11/16.
 */
public class AllPlayerAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private ArrayList<AllPlayersModel> allPlayersModelArrayList;
    private LayoutInflater inflater;
    private Context context;


    public AllPlayerAdapter(Context context, ArrayList<AllPlayersModel> list) {
        inflater = LayoutInflater.from(context);
        allPlayersModelArrayList = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return allPlayersModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return allPlayersModelArrayList.get(position);
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

            convertView = inflater.inflate(R.layout.player_items, parent, false);

            holder.nameItem = (TextView) convertView.findViewById(R.id.item_name);
            holder.ageItem = (TextView) convertView.findViewById(R.id.item_age);
            holder.mailItem = (TextView) convertView.findViewById(R.id.item_mail);
            holder.reitingItem = (TextView) convertView.findViewById(R.id.item_reiting);
            holder.profileItem = (CircleImageView) convertView.findViewById(R.id.item_profile);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nameItem.setText(allPlayersModelArrayList.get(position).getName());
        holder.ageItem.setText(allPlayersModelArrayList.get(position).getBirthday());
        holder.mailItem.setText(allPlayersModelArrayList.get(position).getMail());
        holder.reitingItem.setText(allPlayersModelArrayList.get(position).getReiting());

        String imgUrl = "https://graph.facebook.com/" + (allPlayersModelArrayList.get(position).getFb_id()) + "/picture?height=400";
        Picasso.with(context)
                .load(imgUrl)
                .resize(62, 62)
                .centerCrop()
                .into(holder.profileItem);

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
        String headerText = "" + allPlayersModelArrayList.get(position).getName();
        holder.headerDateHolder.setText(headerText);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return position;
    }


    public AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            AllPlayersModel allPlayersModel = (AllPlayersModel) parent.getAdapter().getItem(position);

            Intent viewprofile = new Intent(context, ViewProfile.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("playerinfo",allPlayersModel);
            viewprofile.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            viewprofile.putExtra("fromadapter",bundle);

            context.startActivity(viewprofile);

        }

    };



//    @Override
//    public long getHeaderId(int position) {
//        //return the first character of the country as ID because this is what headers are based upon
//
//        if(allPlayersModelArrayList.get(position).getDate() == null || allPlayersModelArrayList.get(position).getDate().isEmpty())
//            return 1;
//        else {
//            Date dat = new Date(allPlayersModelArrayList.get(position).getDate());
//            Calendar c = Calendar.getInstance();
//            c.setTime(dat);
//
//            return c.getTimeInMillis();
//        }
//    }

    class HeaderViewHolder {
        TextView headerDateHolder;
    }

    class ViewHolder {
        TextView nameItem, ageItem, mailItem, reitingItem;
        CircleImageView profileItem;

    }

}