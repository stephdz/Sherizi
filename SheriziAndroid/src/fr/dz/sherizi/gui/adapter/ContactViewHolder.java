package fr.dz.sherizi.gui.adapter;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * View holder for contacts list
 */
public class ContactViewHolder {

	private ImageView photo;
	private TextView name;
	private TextView description;

	/*
	 * GETTERS & SETTERS
	 */

	public ImageView getPhoto() {
		return photo;
	}

	public void setPhoto(ImageView photo) {
		this.photo = photo;
	}

	public TextView getName() {
		return name;
	}

	public void setName(TextView name) {
		this.name = name;
	}

	public TextView getDescription() {
		return description;
	}

	public void setDescription(TextView description) {
		this.description = description;
	}
}
