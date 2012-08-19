package fr.dz.sherizi.service.contact;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

import com.appspot.api.services.sherizi.model.User;
import com.appspot.api.services.sherizi.model.Users;

import fr.dz.sherizi.common.exception.SheriziException;
import fr.dz.sherizi.service.server.SheriziServerService;
import fr.dz.sherizi.utils.Utils;

/**
 * Service for contact management
 */
public class ContactService {

	// Contact Service instance
	private static final ContactService instance = new ContactService();

	/**
	 * Private constructor : it's a singleton
	 */
	private ContactService() {

	}

	/**
	 * @return the contact service instance
	 */
	public static ContactService getInstance() {
		return instance;
	}

	/**
	 * Retrieve all contacts connected to Sherizi server
	 * TODO Get only the email adresses and then query the contacts to improve performance
	 * @param context
	 * @return
	 */
	public List<Contact> getSheriziContacts(Context context) throws SheriziException {

		// Gets all emails
		List<String> allEmails = getAllEmails(context);

		// Retrieves connected contacts
		List<User> allUsers = new ArrayList<User>();
		StringBuffer emails = new StringBuffer(1024);
		boolean first = true;
		for ( String email : allEmails ) {
			// Executes partial query to the server => email string should not be too long
			if ( emails.length()+email.length()+1 >= 1024 ) {
				Users users;
				try {
					users = SheriziServerService.getInstance().searchFriends(emails.toString()).execute();
				} catch ( IOException e ) {
					throw new SheriziException("An error occured while fetching Sherizi friends.");
				}
				allUsers.addAll(users.getItems());
				emails = new StringBuffer(1024);
				first = true;
			}

			if ( ! first ) {
				emails.append(",");
			} else {
				first = false;
			}

			emails.append(email);
		}
		if ( emails.length() > 0 ) {
			Users users;
			try {
				users = SheriziServerService.getInstance().searchFriends(emails.toString()).execute();
			} catch ( IOException e ) {
				throw new SheriziException("An error occured while fetching Sherizi friends.");
			}
			allUsers.addAll(users.getItems());
		}

		// Retrieves contacts from users
		return getContactsFromUsers(context, allUsers);
	}

	/**
	 * Returns the current user's list of email adresses
	 * @return
	 */
	public Set<Email> getConnectedUserEmails(Context context) {

		// Gets the contacts from the google account
		String googleAccount = Utils.getGoogleAccount(context);
		Set<String> emails = new HashSet<String>();
		emails.add(googleAccount);
		List<Contact> contacts = getContactsFromEmails(context, emails);

		// Prepares the result
		Set<Email> result = new HashSet<Email>();
		for ( Contact contact : contacts ) {
			result.addAll(contact.getEmails());
		}
		result.add(new Email(googleAccount, Utils.GOOGLE_ACCOUNT_TYPE));

		return result;
	}

	/**
	 * Returns the contact photo
	 * @param context
	 * @param contact
	 * @return
	 */
	public Bitmap getContactPhoto(Context context, Contact contact) {
	    Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.valueOf(contact.getId()));
	    InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(), uri);
	    if (input == null) {
	        return null;
	    }
	    return BitmapFactory.decodeStream(input);
	}

	/**
	 * Returns contacts matching emails
	 * @param context
	 * @param emails
	 * @return
	 */
	protected List<Contact> getContactsFromEmails(Context context, Set<String> emails) {
		return getContactsFromIds(context, getContactsIdsFromEmails(context, emails));
	}

	/**
	 * Retrieves contacts from users
	 * @param context
	 * @param users
	 * @return
	 */
	protected List<Contact> getContactsFromUsers(Context context, List<User> users) {

		// Prepares a map of email and devices
		Map<String,Set<String>> userDevices = new HashMap<String,Set<String>>();
		for ( User user : users ) {
			if ( ! userDevices.containsKey(user.getEmail()) ) {
				userDevices.put(user.getEmail(), new HashSet<String>());
			}
			userDevices.get(user.getEmail()).add(user.getDeviceName());
		}

		// Gets contacts ids from users
		List<String> contactIds = getContactsIdsFromEmails(context, userDevices.keySet());

		// Gets contacts from ids
		List<Contact> result = getContactsFromIds(context, contactIds);

		// Adds the devices to the contacts
		List<Contact> toBeRemoved = new ArrayList<Contact>();
		for ( Contact contact : result ) {
			for ( Email email : contact.getEmails() ) {
				if ( userDevices.containsKey(email.getEmail()) ) {
					contact.getDevices().addAll(userDevices.get(email.getEmail()));
				}
			}

			// We remove the current device if it's the current user
			if ( contact.isCurrentUser(context) ) {
				contact.getDevices().remove(Utils.getDeviceName(context));
			}

			// We remove the users that don't have a device (or current user if he has only one device)
			if ( contact.getDevices().isEmpty() ) {
				toBeRemoved.add(contact);
			}
		}
		result.removeAll(toBeRemoved);

		return result;
	}

	/**
	 * Gets contacts from ids
	 * @param context
	 * @param contactIds
	 * @return
	 */
	protected List<Contact> getContactsFromIds(Context context,	List<String> contactIds) {
		if ( contactIds == null || contactIds.isEmpty() ) {
			return new ArrayList<Contact>();
		}

		// Prepares the query
	    Uri uri = ContactsContract.Contacts.CONTENT_URI;
	    String[] projection = new String[] {
	        ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME
	    };
	    StringBuffer selection = new StringBuffer();
	    selection.append(ContactsContract.Contacts._ID+" IN (");
	    boolean first = true;
	    for ( String id : contactIds ) {
	    	selection.append((first?"":",")+id);
	    	first = false;
	    }
	    selection.append(")");
	    String[] selectionArgs = null;
	    String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

	    // Runs the query
	    ContentResolver resolver = context.getContentResolver();
	    Cursor cursor = context.getContentResolver().query(uri, projection, selection.toString(), selectionArgs, sortOrder);

	    // Prepares the final result
	    List<Contact> result = new ArrayList<Contact>();
        while (cursor.moveToNext()) {
        	Contact contact = Contact.createContact(resolver, cursor);
        	if ( contact != null ) {
        		result.add(contact);
        	}
        }
        cursor.close();

	    return result;
	}

	/**
	 * Returns contacts ids matching users emails
	 * @param context
	 * @return
	 */
	protected List<String> getContactsIdsFromEmails(Context context, Set<String> emails) {
		if ( emails == null || emails.isEmpty() ) {
			return new ArrayList<String>();
		}

		// Prepares the query
	    Uri uri = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
	    String[] projection = new String[] {
	    	ContactsContract.CommonDataKinds.Email.CONTACT_ID, ContactsContract.CommonDataKinds.Email.DATA
	    };
	    StringBuffer selection = new StringBuffer();
	    //selection.append(ContactsContract.Contacts.IN_VISIBLE_GROUP +"='1' ");
	    selection.append(/*"AND "+*/ContactsContract.CommonDataKinds.Email.DATA+" IN (");
	    boolean first = true;
	    for ( String email : emails ) {
	    	selection.append((first?"":",")+"'"+email+"'");
	    	first = false;
	    }
	    selection.append(")");
	    String[] selectionArgs = null;
	    String sortOrder = ContactsContract.Contacts._ID + " COLLATE LOCALIZED ASC";

	    // Runs the query
	    Cursor cursor = context.getContentResolver().query(uri, projection, selection.toString(), selectionArgs, sortOrder);

	    // Prepares the final result
	    List<String> result = new ArrayList<String>();
        while (cursor.moveToNext()) {
        	result.add(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID)));
        }
        cursor.close();

	    return result;
	}

	/**
	 * Returns all email adresses from the contacts
	 * @param context
	 * @return
	 */
	protected List<String> getAllEmails(Context context) {
		List<String> result = new ArrayList<String>();
		Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, null, null, null );
		while (cursor.moveToNext()) {
			result.add(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)));
		}
		cursor.close();
		return result;
	}

	/**
	 * Returns all contacts that has an available email address
	 * @param context
	 * @return
	 */
	protected List<Contact> getAllContacts(Context context) {
		// Prepares the query
	    Uri uri = ContactsContract.Contacts.CONTENT_URI;
	    String[] projection = new String[] {
	        ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME
	    };
	    String selection = null;
	    String[] selectionArgs = null;
	    String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

	    // Runs the query
	    ContentResolver resolver = context.getContentResolver();
	    Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);

	    // Prepares the final result
	    List<Contact> result = new ArrayList<Contact>();
        while (cursor.moveToNext()) {
        	Contact contact = Contact.createContact(resolver, cursor);
        	if ( contact != null ) {
        		result.add(contact);
        	}
        }
        cursor.close();

	    return result;
	}
}
