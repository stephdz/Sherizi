package fr.dz.sherizi.server.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import fr.dz.sherizi.server.model.version.UserV1;

@Entity
public class User implements UserV1 {

	@Id
	private String registrationID;

	private String googleAccount;

	/*
	 * GETTERS & SETTERS
	 */

	@Override
	public String getRegistrationID() {
		return registrationID;
	}

	@Override
	public void setRegistrationID(String registrationID) {
		this.registrationID = registrationID;
	}

	@Override
	public String getGoogleAccount() {
		return googleAccount;
	}

	@Override
	public void setGoogleAccount(String googleAccount) {
		this.googleAccount = googleAccount;
	}
}
