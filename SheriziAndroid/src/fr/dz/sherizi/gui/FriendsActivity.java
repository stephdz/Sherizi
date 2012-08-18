package fr.dz.sherizi.gui;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import fr.dz.sherizi.R;
import fr.dz.sherizi.gui.adapter.ContactAdapter;
import fr.dz.sherizi.gui.task.SheriziFriendsTask;
import fr.dz.sherizi.push.PushService;
import fr.dz.sherizi.service.contact.Contact;

public class FriendsActivity extends Activity {

	private Handler handler = new Handler() {
			@Override
			@SuppressWarnings("unchecked")
			@SuppressLint("HandlerLeak")
	        public void handleMessage(Message msg) {
	        	List<Contact> friends = (List<Contact>) msg.obj;
	            ListView contactsList = (ListView) findViewById(R.id.contactsList);
	            contactsList.setAdapter(new ContactAdapter(FriendsActivity.this, friends));
	        }
	};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends);
        PushService.register(this);

        // Fills the list
        new SheriziFriendsTask().execute(this);
    }

    /**
     * Sets the friends list
     * @param friends
     */
    public void setFriendsList(List<Contact> friends) {
    	Message msg = new Message();
    	msg.obj = friends;
    	handler.sendMessage(msg);
    }
}
