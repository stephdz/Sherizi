package fr.dz.sherizi.gui.adapter;

import android.widget.TextView;

/**
 * View holder for contacts list
 */
public class ContactViewHolder {

	private TextView name;
	private TextView emails;
	private TextView devices;

	/*
	 * GETTERS & SETTERS
	 */

	public TextView getName() {
		return name;
	}

	public void setName(TextView name) {
		this.name = name;
	}

	public TextView getEmails() {
		return emails;
	}

	public void setEmails(TextView emails) {
		this.emails = emails;
	}

	public TextView getDevices() {
		return devices;
	}

	public void setDevices(TextView devices) {
		this.devices = devices;
	}
}
