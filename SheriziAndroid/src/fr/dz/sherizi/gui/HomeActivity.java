package fr.dz.sherizi.gui;

import android.app.Activity;
import android.os.Bundle;
import fr.dz.sherizi.R;
import fr.dz.sherizi.push.PushService;

public class HomeActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        PushService.register(this);
    }
}
