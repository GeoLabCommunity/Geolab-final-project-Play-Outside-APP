package geolab.playoutside.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import geolab.playoutside.R;
import geolab.playoutside.model.MyEvent;

public class EventDetailActivity extends AppCompatActivity {

    @Bind(R.id.detail_title_text_id) protected TextView title;
    @Bind(R.id.detail_date_text_id) protected TextView date;
    @Bind(R.id.detail_time_text_id) protected TextView time;
    @Bind(R.id.detail_description_text_id) protected TextView description;
    @Bind(R.id.detail_place_text_id) protected TextView place;
    @Bind(R.id.detail_player_count_text_id) protected TextView count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        MyEvent myEvent = (MyEvent) bundle.get("event");


        title.setText(myEvent.getTitle());
        time.setText(myEvent.getTime());
        description.setText(myEvent.getDescription());
        date.setText(myEvent.getDate());
        place.setText(myEvent.getPlace());
        count.setText(myEvent.getPlayerCount());


    }
}
