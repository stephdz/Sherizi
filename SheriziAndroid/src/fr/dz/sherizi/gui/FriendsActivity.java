package fr.dz.sherizi.gui;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import fr.dz.sherizi.R;
import fr.dz.sherizi.app.SheriziApplication;
import fr.dz.sherizi.gui.PullToRefreshListView.OnRefreshListener;
import fr.dz.sherizi.gui.adapter.ContactAdapter;
import fr.dz.sherizi.service.contact.Contact;
import fr.dz.sherizi.service.share.SharedData;

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

	// Datas describing what to share
	private SharedData shared;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Prepare UI
        prepareGUI();

        // Prepare sharing
        prepareSharing();
    }

	/**
	 * Inits the user interface
	 */
	protected void prepareGUI() {
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

	/**
	 * Prepares for sharing
	 */
	protected void prepareSharing() {
		Intent intent = getIntent();
	    Bundle extras = intent.getExtras();
	    if ( extras != null ) {
		    if ( extras.containsKey(Intent.EXTRA_STREAM) ) {
	            shared = new SharedData<Uri>((Uri) extras.getParcelable(Intent.EXTRA_STREAM));
	            Toast.makeText(this, "Sharing URI : "+shared.getData()+" !", Toast.LENGTH_LONG).show();
		    } else if ( extras.containsKey(Intent.EXTRA_TEXT) ) {
		    	String text = intent.getStringExtra(Intent.EXTRA_TEXT);
		    	if ( text == null || text.isEmpty() ) {
		    		text = intent.getStringExtra(Intent.EXTRA_SUBJECT);
		    	}
		    	shared = new SharedData<String>(text);
	        	Toast.makeText(this, "Sharing text : "+shared.getData()+" !", Toast.LENGTH_LONG).show();
	        } else {
	        	// TODO Manage the other sharing types
	        	Toast.makeText(this, "Sharing unknown type !", Toast.LENGTH_LONG).show();
	        }
	    } else {
	    	// Must not append
	    	Toast.makeText(this, "Not sharing anything !", Toast.LENGTH_LONG).show();
	    }
	}
}
