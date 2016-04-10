package geolab.playoutside.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import geolab.playoutside.R;
import geolab.playoutside.db.Category_db;
import geolab.playoutside.model.MyEvent;
import geolab.playoutside.view.EventDetailActivity;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by GeoLab on 1/11/16.
 */
public class AdminAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private ArrayList<MyEvent> eventsList;
    private LayoutInflater inflater;
    private Context context;
    private String URL = "http://geolab.club/geolabwork/ika/delete.php";
    private AdminAdapter myAdapter;
    private String day;
    private String month;
    private String year;
    private String everything;

    public AdminAdapter(Context context, ArrayList<MyEvent> list) {
        inflater = LayoutInflater.from(context);
        eventsList = list;
        myAdapter = this;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            convertView = inflater.inflate(R.layout.news_category_item1, parent, false);

            holder.timeHolder = (TextView) convertView.findViewById(R.id.time_text_id1);
            holder.titleHolder = (TextView) convertView.findViewById(R.id.title_text_id1);
            holder.descriptionHolder = (TextView) convertView.findViewById(R.id.description_text_id1);
            holder.placeHolder = (TextView) convertView.findViewById(R.id.place_text_id1);
            holder.playerHolder = (TextView) convertView.findViewById(R.id.player_count_id1);
            holder.setting = (LinearLayout) convertView.findViewById(R.id.setting_id);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.timeHolder.setText(eventsList.get(position).getTime());
        holder.timeHolder.setBackgroundResource(Category_db.category_icons[eventsList.get(position).getCategory_id()]);

        holder.titleHolder.setText(eventsList.get(position).getTitle());
        holder.descriptionHolder.setText(eventsList.get(position).getDescription()+"...");
        holder.placeHolder.setText(eventsList.get(position).getPlace());
        holder.playerHolder.setText(String.valueOf(eventsList.get(position).getEvents().size()) );
        holder.setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Won't be able to recover this file!")
                        .setCancelText("No,cancel plz!")
                        .setConfirmText("Yes,delete it!")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                // reuse previous dialog instance, keep widget user state, reset them if you need
//                            sDialog.setTitleText("Cancelled!")
//                                    .setContentText("Your imaginary file is safe :)")
//                                    .setConfirmText("OK")
//                                    .showCancelButton(false)
//                                    .setCancelClickListener(null)
//                                    .setConfirmClickListener(null)
//                                    .changeAlertType(SweetAlertDialog.ERROR_TYPE);

                                // or you can new a SweetAlertDialog to show
                                sDialog.dismiss();
                                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Cancelled!")
                                        .setContentText("Your imaginary file is safe :)")
                                        .setConfirmText("OK")
                                        .show();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                delete(URL+"?eventId="+eventsList.get(position).getEventId());
                                sDialog.setTitleText("Deleted!")
                                        .setContentText("Your imaginary file has been deleted!")
                                        .setConfirmText("OK")
                                        .showCancelButton(false)
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                                eventsList.remove(position);
                                myAdapter.notifyDataSetChanged();

                            }

                        })
                        .show();

            }
        });

        return convertView;
    }


    @Override
    public View getHeaderView(final int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.header, parent, false);
            holder.headerDateHolder = (TextView) convertView.findViewById(R.id.header_text_id);
            holder.daysleft = (TextView) convertView.findViewById(R.id.days_left_id);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        String headerText = "" + eventsList.get(position).getDate();
        String year = headerText.split("-")[0]; // "Before"
        String month = headerText.split("-")[1];
        final String day = headerText.split("-")[2]; // "After"
        everything = day+"/"+month+"/"+year;



        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
        Date d = null;
        try {
            d = formatter.parse(everything);//catch exception
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        Calendar thatDay = Calendar.getInstance();
        thatDay.setTime(d);



        Calendar today = Calendar.getInstance();

        long diff =thatDay.getTimeInMillis()- today.getTimeInMillis();

        final long days = Math.round(diff * 1f / TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)+0.5);


        holder.daysleft.setText("(" + String.valueOf(days) + ")  days left");


        holder.headerDateHolder.setText(day+"/"+month+"/"+year);
        return convertView;
    }


    public AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            MyEvent event = (MyEvent) parent.getAdapter().getItem(position);

            Intent detailIntent = new Intent(context, EventDetailActivity.class);
            Bundle bundle = new Bundle();
            detailIntent.putExtra("check",true);
            bundle.putSerializable("event",event);
            detailIntent.putExtra("fromadapter",bundle);

            context.startActivity(detailIntent);

        }

    };

    public AdapterView.OnItemLongClickListener longClickListener = new AdapterView.OnItemLongClickListener() {

        @Override
        public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
            MyEvent event = (MyEvent) parent.getAdapter().getItem(position);

            new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Are you sure?")
                    .setContentText("Won't be able to recover this file!")
                    .setCancelText("No,cancel plz!")
                    .setConfirmText("Yes,delete it!")
                    .showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            // reuse previous dialog instance, keep widget user state, reset them if you need
//                            sDialog.setTitleText("Cancelled!")
//                                    .setContentText("Your imaginary file is safe :)")
//                                    .setConfirmText("OK")
//                                    .showCancelButton(false)
//                                    .setCancelClickListener(null)
//                                    .setConfirmClickListener(null)
//                                    .changeAlertType(SweetAlertDialog.ERROR_TYPE);

                            // or you can new a SweetAlertDialog to show
                            sDialog.dismiss();
                            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Cancelled!")
                                    .setContentText("Your imaginary file is safe :)")
                                    .setConfirmText("OK")
                                    .show();
                        }
                    })
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            delete(URL+"?eventId="+eventsList.get(position).getEventId());
                            sDialog.setTitleText("Deleted!")
                                    .setContentText("Your imaginary file has been deleted!")
                                    .setConfirmText("OK")
                                    .showCancelButton(false)
                                    .setCancelClickListener(null)
                                    .setConfirmClickListener(null)
                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                            eventsList.remove(position);
                            myAdapter.notifyDataSetChanged();

                        }

                    })
                    .show();


            return true;
        }

    };


    @Override
    public long getHeaderId(int position) {
        //return the first character of the country as ID because this is what headers are based upon
        if(eventsList.get(position).getDate() == null || eventsList.get(position).getDate().isEmpty())
            return 1;
        else {
            String headerText = "" + eventsList.get(position).getDate();
            year = headerText.split("-")[0]; // "Before"
            month = headerText.split("-")[1];
            day = headerText.split("-")[2]; // "After"
            everything = day+"/"+month+"/"+year;
            Date dat = new Date(everything);
            Calendar c = Calendar.getInstance();
            c.setTime(dat);

            return c.getTimeInMillis();
        }
    }

    class HeaderViewHolder {
        TextView headerDateHolder, daysleft;
    }

    class ViewHolder {
        TextView timeHolder, titleHolder, descriptionHolder, placeHolder, playerHolder;
        LinearLayout setting;

    }
    private void delete(String URL){


        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();

                params.toString();
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
