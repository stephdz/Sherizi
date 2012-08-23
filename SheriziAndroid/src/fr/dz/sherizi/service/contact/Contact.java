package fr.dz.sherizi.service.contact;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.appspot.api.services.sherizi.model.User;

import fr.dz.sherizi.R;
import fr.dz.sherizi.utils.Utils;

/**
 * The contact representation for Sherizi
 */
public class Contact {

	private String id;
	private String name;
	private List<Email> emails = new ArrayList<Email>();
	private Set<User> users = new HashSet<User>();

	/**
	 * Constructor from a cursor
	 * @param cursor
	 */
	private Contact(ContentResolver resolver, Cursor cursor) {
		// Basic informations
		this.id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
		this.name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

		// Emails
		Cursor emailCur = resolver.query (
				ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
				ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
				new String[] { id }, null );
		while (emailCur.moveToNext()) {
			this.emails.add(Email.createEmail(emailCur));
		}
		emailCur.close();
	}

	/**
	 * Copy constructor
	 * @param contact
	 */
	public Contact(Contact contact) {
		this.id = contact.id;
		this.name = contact.name;
		this.emails = contact.emails;
		this.users = contact.users;
	}

	/**
	 * Constructs a contact, only if it has one or more valid email
	 * @param resolver
	 * @param cursor
	 * @return
	 */
	public static Contact createContact(ContentResolver resolver, Cursor cursor) {
		Contact result = new Contact(resolver, cursor);

		// Only keeps contacts with emails
		if ( ! result.getEmails().isEmpty() ) {
			return result;
		} else {
			return null;
		}
	}

	/**
	 * Returns true if this user is the user connected on the device
	 * @param context
	 * @return
	 */
	public boolean isCurrentUser(Context context) {
		boolean result = false;
		String googleAccount = Utils.getGoogleAccount(context);
		for ( Email email : emails ) {
			if ( googleAccount.equals(email.getEmail()) ) {
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	 * Returns the contact description
	 * @param context
	 * @return
	 */
	public String getDescription(Context context) {
		StringBuffer result = new StringBuffer();
		result.append(context.getString(R.string.contact_description_start));
		boolean first = true;
		for ( String device : getDevices() ) {
			if ( first ) {
				first = false;
			} else {
				result.append(",");
			}
			result.append(" "+device);
		}
		result.append(context.getString(R.string.contact_description_end));
		return result.toString();
	}

	/**
	 * Returns the contact description
	 * @param context
	 * @return
	 */
	public Set<String> getDevices() {
		Set<String> result = new HashSet<String>();
		for ( User user : users ) {
			result.add(user.getDeviceName());
		}
		return result;
	}

	/**
	 * Duplicates the contact
	 */
	public Contact duplicate() {
		return new Contact(this);
	}

	/*
	 * GETTERS & SETTERS
	 */

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Email> getEmails() {
		return emails;
	}

	public void setEmails(List<Email> emails) {
		this.emails = emails;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
}
