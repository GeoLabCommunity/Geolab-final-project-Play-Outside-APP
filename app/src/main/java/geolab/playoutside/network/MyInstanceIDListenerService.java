package geolab.playoutside.network;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

import geolab.playoutside.Add_Event_Activity;

/**
 * Created by dalkh on 28-Jan-16.
 */
public class MyInstanceIDListenerService extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify of changes
        Intent intent = new Intent(this, Add_Event_Activity.class);
        startService(intent);
    }
}
