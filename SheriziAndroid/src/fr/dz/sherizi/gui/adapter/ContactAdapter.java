package fr.dz.sherizi.gui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import fr.dz.sherizi.R;
import fr.dz.sherizi.service.contact.Contact;


/**
 * Contact adapter for friends list
 */
public class ContactAdapter extends BaseAdapter {

	private List<Contact> list;
	private Context context;

	/**
	 * Constructor
	 * @param context
	 * @param contacts
	 */
	public ContactAdapter(Context context, List<Contact> contacts) {
		this.context = context;
		this.list = contacts;
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ContactViewHolder holder;
		if (convertView == null) {
			holder = new ContactViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.contact_item, null);
			holder.setName((TextView) convertView.findViewById(R.id.contactName));
			holder.setEmails((TextView) convertView.findViewById(R.id.contactEmails));
			holder.setDevices((TextView) convertView.findViewById(R.id.contactDevices));
			convertView.setTag(holder);
		} else {
			holder = (ContactViewHolder) convertView.getTag();
		}
		holder.getName().setText(list.get(position).getName());
		holder.getEmails().setText(list.get(position).getEmails().toString());
		holder.getDevices().setText(list.get(position).getDevices().toString());
		return convertView;
	}
}
