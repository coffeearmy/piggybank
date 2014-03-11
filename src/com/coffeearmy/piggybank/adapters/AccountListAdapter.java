package com.coffeearmy.piggybank.adapters;

import java.util.List;

import com.coffeearmy.piggybank.Account;
import com.coffeearmy.piggybank.PiggybankActivity;
import com.coffeearmy.piggybank.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AccountListAdapter extends ArrayAdapter<Account> {
	
	  private final Context context;
	  private final  List<Account> items;

	  static class ViewHolder {
	    public TextView text;
	    public ImageView image;
	  }

	public AccountListAdapter(Context context, int resource,
			int textViewResourceId, List<Account> objects) {
		super(context, resource, textViewResourceId, objects);
		this.context = context;
	    this.items = objects;
	}
	
	@Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    View rowView = convertView;
	    // reuse views
	    if (rowView == null) {
	      LayoutInflater inflater =  ((Activity) context).getLayoutInflater();
	      rowView = inflater.inflate(R.layout.drawer_row, null);
	      // configure view holder
	      ViewHolder viewHolder = new ViewHolder();
	      viewHolder.text = (TextView) rowView.findViewById(R.id.txtv_account_name);
	      viewHolder.image = (ImageView) rowView
	          .findViewById(R.id.imgv_icon_account);
	      rowView.setTag(viewHolder);
	    }

	    // fill data
	    ViewHolder holder = (ViewHolder) rowView.getTag();
	    Account account = items.get(position);
	    
	    holder.text.setText(account.getName());
	       
	    holder.image.setImageResource(R.drawable.cicle_button_1);
	    rowView.setTag(R.id.account_id, account.getId());
	  

	    return rowView;
	  }

}
