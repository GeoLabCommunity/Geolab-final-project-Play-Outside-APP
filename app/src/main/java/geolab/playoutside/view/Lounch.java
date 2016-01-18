package geolab.playoutside.view;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import geolab.playoutside.MainActivity;
import geolab.playoutside.R;

public class Lounch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_lounch );

        new Handler().postDelayed( new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(Lounch.this, MainActivity.class);
                startActivity(mainIntent);
                Lounch.this.finish();
            }
        }, 1500);

    }
}
