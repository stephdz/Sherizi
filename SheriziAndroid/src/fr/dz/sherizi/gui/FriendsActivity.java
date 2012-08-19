package fr.dz.sherizi.gui;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import fr.dz.sherizi.R;
import fr.dz.sherizi.app.SheriziApplication;
import fr.dz.sherizi.gui.PullToRefreshListView.OnRefreshListener;
import fr.dz.sherizi.gui.adapter.ContactAdapter;
import fr.dz.sherizi.service.contact.Contact;

public class FriendsActivity extends Activity {

	// Application
	private SheriziApplication application;

	// Contacts list
	private PullToRefreshListView contactsList;
	private Handler contactsLoadedHandler = new Handler() {
			@Override
			@SuppressWarnings("unchecked")
	        public void handleMessage(Message msg) {
	        	List<Contact> friends = (List<Contact>) msg.obj;
	        	contactsList.setAdapter(new ContactAdapter(FriendsActivity.this, friends));
	        	if ( friends == null ) {
	        		contactsList.setRefreshing();
	        		application.updateFriends();
	        	} else {
		            contactsList.onRefreshComplete();
	        	}
	        }
	};

	@Override
	@SuppressLint("HandlerLeak")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Sets the layout
        setContentView(R.layout.friends);

        // Fetch used components
        contactsList = (PullToRefreshListView) findViewById(R.id.contactsList);

        // Sets the listener for friends refresh
        application = (SheriziApplication) getApplication();
        contactsList.setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
               application.updateFriends();
            }
        });

        // Register handler for filling the friends list
        application.setFriendsLoadedHandler(contactsLoadedHandler);
    }
}
