package fr.dz.sherizi.gui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import fr.dz.sherizi.R;
import fr.dz.sherizi.service.contact.Contact;
import fr.dz.sherizi.service.contact.ContactService;


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
		if ( list == null ) {
			return 0;
		} else if ( list.isEmpty() ) {
			return 1;
		} else {
			return list.size();
		}
	}

	public Object getItem(int position) {
		return list == null || list.isEmpty() ? null : list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if ( list != null ) {
			return getContactConvertView(position, convertView);
		} else if ( list.isEmpty() ){
			return getEmptyConvertView(position, convertView);
		} else {
			return null;
		}
	}

	protected View getEmptyConvertView(int position, View convertView) {
		return LayoutInflater.from(context).inflate(R.layout.contact_item_empty, null);
	}

	protected View getContactConvertView(int position, View convertView) {
		ContactViewHolder holder;
		Contact contact = list.get(position);
		if (convertView == null) {
			holder = new ContactViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.contact_item, null);
			holder.setPhoto((ImageView) convertView.findViewById(R.id.photo));
			holder.setName((TextView) convertView.findViewById(R.id.contactName));
			holder.setDescription((TextView) convertView.findViewById(R.id.contactDescription));
			convertView.setTag(holder);
		} else {
			holder = (ContactViewHolder) convertView.getTag();
		}
		holder.getPhoto().setImageBitmap(ContactService.getInstance().getContactPhoto(convertView.getContext(), contact));
		holder.getName().setText(contact.getName());
		holder.getDescription().setText(contact.getDescription(convertView.getContext()));
		return convertView;
	}
}
