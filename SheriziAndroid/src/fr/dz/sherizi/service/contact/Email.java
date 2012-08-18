package fr.dz.sherizi.service.contact;

import android.database.Cursor;
import android.provider.ContactsContract;

/**
 * The email representation for Sherizi
 */
public class Email {

	private String email;
	private String type;


	/**
	 * Constructor from a cursor
	 * @param cursor
	 */
	private Email(Cursor cursor) {
		this.email = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
		this.type = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
	}

	/**
	 * Full constructor
	 * @param email
	 * @param type
	 */
	public Email(String email, String type) {
		this.email = email;
		this.type = type;
	}

	/**
	 * Constructs an Email, only if it's a GMail account
	 * @return
	 */
	public static Email createEmail(Cursor cursor) {
		return new Email(cursor);
	}

	@Override
	public String toString() {
		return "[email=" + email + ", type=" + type + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (email == null ? 0 : email.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Email other = (Email) obj;
		if (email == null) {
			if (other.email != null) {
				return false;
			}
		} else if (!email.equals(other.email)) {
			return false;
		}
		return true;
	}

	/*
	 * GETTERS & SETTERS
	 */

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
