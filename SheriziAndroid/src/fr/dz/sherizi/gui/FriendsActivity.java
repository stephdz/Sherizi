package fr.dz.sherizi.gui;

import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.appspot.api.services.sherizi.model.User;

import fr.dz.sherizi.R;
import fr.dz.sherizi.gui.PullToRefreshListView.OnRefreshListener;
import fr.dz.sherizi.gui.adapter.ContactAdapter;
import fr.dz.sherizi.listener.SheriziActionListener;
import fr.dz.sherizi.service.contact.Contact;
import fr.dz.sherizi.service.share.ShareManager;
import fr.dz.sherizi.service.share.SharedData;

public class FriendsActivity extends SheriziActivity {

	// Contacts list
	private PullToRefreshListView contactsList;
	private Handler contactsLoadedHandler = new Handler() {
			@Override
			@SuppressWarnings("unchecked")
	        public void handleMessage(Message msg) {
	        	handleContactsLoaded((List<Contact>) msg.obj);
	        }
	};

	// Datas describing what to share
	private SharedData<?> shared;

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
        contactsList.setLockScrollWhileRefreshing(true);
        contactsList.setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
               getSheriziApplication().updateFriends();
            }
        });
        contactsList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
				if ( ! contactsList.isRefreshing() ) {
					share((Contact)contactsList.getItemAtPosition(position));
				}
			}
        });

        // Register handler for filling the friends list
        getSheriziApplication().setFriendsLoadedHandler(contactsLoadedHandler);
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
	            makeToast("Sharing URI : "+shared.getData()+" !");
		    } else if ( extras.containsKey(Intent.EXTRA_TEXT) ) {
		    	String text = intent.getStringExtra(Intent.EXTRA_TEXT);
		    	if ( text == null || text.isEmpty() ) {
		    		text = intent.getStringExtra(Intent.EXTRA_SUBJECT);
		    	}
		    	shared = new SharedData<String>(text);
		    	makeToast("Sharing text : "+shared.getData()+" !");
	        } else {
	        	// TODO Manage the other sharing types
	        	makeToast("Sharing unknown type !");
	        }
	    } else {
	    	// Must not append
	    	makeToast("Not sharing anything !");
	    }
	}

	/**
	 * Handler called when contacts are loaded
	 * @param friends
	 */
	protected void handleContactsLoaded(List<Contact> friends) {
		contactsList.setAdapter(new ContactAdapter(FriendsActivity.this, friends));
    	if ( friends == null ) {
    		contactsList.setRefreshing();
    		getSheriziApplication().updateFriends();
    	} else {
            contactsList.onRefreshComplete();
    	}
	}

	/**
	 * Share datas to the contact
	 * @param contact
	 */
	protected void share(Contact contact) {
		contactsList.setTextRefreshing("Activation du bluetooth");
		contactsList.setRefreshing();

		// Activating bluetooth
		ShareManager shareManager = ShareManager.getDefaultShareManager(this, new SheriziActionListener() {
			@Override
			public void onSuccess() {
				runOnUiThread(new Runnable() {
					public void run() {
						contactsList.onRefreshComplete();
					}
				});
			}
			@Override
			public void onError(Throwable t) {
				makeToast("Error during share : "+t.getMessage());
			}
			@Override
			public void onInfo(Object information) {
				makeToast((String) information);
			}
		});
		shareManager.share(shared, new User());
	}
}
