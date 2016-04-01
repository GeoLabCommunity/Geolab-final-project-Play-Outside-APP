package geolab.playoutside.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import geolab.playoutside.R;
import geolab.playoutside.ViewProfile;
import geolab.playoutside.model.CommentsModel;



/**
 * Created by GeoLab on 3/29/16.
 */
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.Holder> {
    private ArrayList<CommentsModel> commentsModelArrayList;
    private Context context;
    private String time;
    private String date;
    private String timeDate;
    private String day;
    private String month;
    private String year;
    private String everything;

    public CommentsAdapter(ViewProfile context, ArrayList<CommentsModel> commentsModelArrayList) {
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
        year = date.split("-")[0]; // "Before"
        month = date.split("-")[1];
        day = date.split("-")[2]; // "After"
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
               String ika = commentsModelArrayList.get(position).getProfileImage();
                Toast.makeText(context, "daechira"+ika, Toast.LENGTH_LONG).show();
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