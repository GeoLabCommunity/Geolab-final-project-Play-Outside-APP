package geolab.playoutside.adapters;

import android.app.Activity;
import android.app.Application;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import geolab.playoutside.AllPlayers;
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
        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date dat = sf.parse(allPlayersModelArrayList.get(position).getBirthday());
            Calendar birthDate = Calendar.getInstance();
            birthDate.setTimeInMillis(dat.getTime());
            holder.ageItem.setText("(" + String.valueOf(getAge(birthDate, Calendar.getInstance())) + ")");
        } catch (ParseException e) {
            e.printStackTrace();
        }
       // holder.ageItem.setText(allPlayersModelArrayList.get(position).getBirthday());
        holder.mailItem.setText(allPlayersModelArrayList.get(position).getMail());
        holder.reitingItem.setText(allPlayersModelArrayList.get(position).getReiting());

        String imgUrl = "https://graph.facebook.com/" + (allPlayersModelArrayList.get(position).getFb_id()) + "/picture?height=300";

        Picasso.with(context)
                .load(imgUrl)
                .resize(300, 300)
                .centerCrop()
                .into(holder.profileItem);

        return convertView;
    }


    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.header_for_player, parent, false);
            holder.headerDateHolder = (TextView) convertView.findViewById(R.id.header_text_id);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        //set header timeHolder as first char in name
        String headerText = "" + allPlayersModelArrayList.get(position).getName().subSequence(0, 1).charAt(0);
        holder.headerDateHolder.setText(headerText);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return allPlayersModelArrayList.get(position).getName().subSequence(0, 1).charAt(0);
    }


    public AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            AllPlayersModel allPlayersModel = (AllPlayersModel) parent.getAdapter().getItem(position);

            Intent viewprofile = new Intent(context, ViewProfile.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("playerinfo", allPlayersModel);
            bundle.putBoolean("online", true);
            viewprofile.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            viewprofile.putExtra("fromadapter", bundle);

            context.startActivity(viewprofile);
        }

    };



    class HeaderViewHolder {
        TextView headerDateHolder;
    }

    class ViewHolder {
        TextView nameItem, ageItem, mailItem, reitingItem;
        CircleImageView profileItem;

    }
    private int getAge(Calendar birthDate, Calendar currentDate) {
        int age = currentDate.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);

        if (currentDate.get(Calendar.MONTH) < birthDate.get(Calendar.MONTH)) {
            age--;
        } else if (currentDate.get(Calendar.MONTH) == birthDate.get(Calendar.MONTH)
                && currentDate.get(Calendar.DAY_OF_MONTH) < birthDate.get(Calendar.DAY_OF_MONTH)) {
            age--;
        }

        return age;
    }

}
