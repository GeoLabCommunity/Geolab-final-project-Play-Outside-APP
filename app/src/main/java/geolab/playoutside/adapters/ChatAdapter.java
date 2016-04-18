package geolab.playoutside.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import geolab.playoutside.Chat;
import geolab.playoutside.R;
import geolab.playoutside.ViewProfile;
import geolab.playoutside.model.CommentsModel;



/**
 * Created by GeoLab on 3/29/16.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.Holder> {
    private ArrayList<CommentsModel> commentsModelArrayList;
    private Context context;
    private String time;
    private String date;
    private String timeDate;
    private String day;
    private String month;
    private String year;
    private String everything;

    public ChatAdapter(Chat context, ArrayList<CommentsModel> commentsModelArrayList) {
        this.commentsModelArrayList = commentsModelArrayList;
        this.context = context;
    }
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comments_item, parent, false);
        Holder viewholder = new Holder(view);
        return viewholder;
    }
    @Override
    public void onBindViewHolder(Holder holder, final int position) {

        timeDate=commentsModelArrayList.get(position).getDateTime();
        time = timeDate.split(" ")[1];
        date = timeDate.split(" ")[0];

        year = date.split("-")[0];
        month = date.split("-")[1];
        day = date.split("-")[2];
        everything = day+"/"+month+"/"+year;

        holder.dateView.setText(everything);
        holder.timeView.setText(time);
        holder.commentView.setText(commentsModelArrayList.get(position).getComment());

        String imgUrl = "https://graph.facebook.com/" + commentsModelArrayList.get(position).getProfileImage() + "/picture?height=400";
        Picasso.with(context)
                .load(imgUrl)
                .resize(300, 300)
                .centerCrop()
                .into(holder.profileImageView);
        holder.profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fb_id = commentsModelArrayList.get(position).getProfileImage();
                Intent transport = new Intent(context, ViewProfile.class);
                Bundle bundle = new Bundle();
                bundle.putString("fb_id", fb_id);
                transport.putExtra("Extra", bundle);
                context.startActivity(transport);
                ((Activity)context).finish();

            }
        });
    }
    @Override
    public int getItemCount() {
        return commentsModelArrayList == null ? 0 : commentsModelArrayList.size();
    }
    class Holder extends RecyclerView.ViewHolder {
        TextView dateView, timeView, commentView;
        CircleImageView profileImageView;
        public Holder(View itemView) {
            super(itemView);
            dateView = (TextView) itemView.findViewById(R.id.comment_date);
            timeView = (TextView) itemView.findViewById(R.id.comment_time);
            commentView = (TextView) itemView.findViewById(R.id.comment_comment);
            profileImageView = (CircleImageView) itemView.findViewById(R.id.comment_profile);
        }
    }
}