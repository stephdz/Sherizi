package fr.dz.sherizi.server.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import fr.dz.sherizi.server.utils.Constants;

@Entity
public class User implements Serializable {

	private static final long serialVersionUID = Constants.SHERIZI_SERVER_VERSION;

	// Constants
	public static final String EMAIL_FIELD = "email";
	public static final String DEVICE_NAME_FIELD = "deviceName";
	public static final String REGISTRATION_ID_FIELD = "registrationID";

	// Primary key
	@Id
	private Key id;

	// Email
	private String email;

	// Device name
	private String deviceName;

	// GCM registration ID
	private String registrationID;

	/**
	 * Updates the key compounded of email and deviceName
	 */
	public void updateKey() {
		this.id = KeyFactory.createKey(getClass().getSimpleName(), email + " => " + deviceName);
	}

	/*
	 * GETTERS & SETTERS
	 */

	public String getRegistrationID() {
		return registrationID;
	}

	public void setRegistrationID(String registrationID) {
		this.registrationID = registrationID;
	}

	public Key getId() {
		return id;
	}

	public void setId(Key id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
		updateKey();
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
		updateKey();
	}
}
