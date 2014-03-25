package com.coffeearmy.piggybank.adapters;

import java.util.List;

import com.coffeearmy.piggybank.Account;
import com.coffeearmy.piggybank.PiggybankActivity;
import com.coffeearmy.piggybank.R;
import com.coffeearmy.piggybank.view.CustomCheckIcon;
import com.coffeearmy.piggybank.view.CustomIcon;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**  Array Adapter for list the accounts in the Drawer Menu. */
public class AccountListAdapter extends ArrayAdapter<Account> {
	
	  private final Context context;
	  private final  List<Account> items;
	  
	  //ViewHolder pattern for reusing views
	  static class ViewHolder {
	    public TextView text;
	    public TextView money;
	    public CustomIcon icon;
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
	    // if rowView is null, we need to create a new view.
	    // if not, we can reuse it changing only the data displayed.
	    if (rowView == null) {
	      LayoutInflater inflater =  ((Activity) context).getLayoutInflater();
	      rowView = inflater.inflate(R.layout.drawer_row, null);
	      // configure view holder
	      ViewHolder viewHolder = new ViewHolder();
	      viewHolder.text = (TextView) rowView.findViewById(R.id.txtvDrawerAccountName);
	      viewHolder.money = (TextView) rowView.findViewById(R.id.txtvDrawerAccountMoney);
	      viewHolder.icon = (CustomIcon) rowView
	          .findViewById(R.id.imgvDrawerIconAccount);
	      //Save the viewHolder in the Tag of the view.
	      rowView.setTag(viewHolder);
	    }

	    // Retrieve the View holder with the elements references
	    ViewHolder holder = (ViewHolder) rowView.getTag();
	    Account account = items.get(position);
	    
	    holder.text.setText(account.getName());
	    
	    String operationLabel = Double.toString(account.getMoney());
	    //Change the color if is <0 or >0
	   
		if (account.getMoney()>0) {
			operationLabel = "+ " + operationLabel;
			holder.money.setTextColor(context.getResources().getColor(
					R.color.Holo_green));
		} else {
			
			holder.money.setTextColor(context.getResources().getColor(
					R.color.Holo_red));
		}
		holder.money.setText(operationLabel);
	    
	    //Create custom icon, from CustomIcon.java
	    holder.icon.setStyle(account.getIcon(),-1);
	    rowView.setTag(R.id.account_id, account.getId());
	  

	    return rowView;
	  }
	
	/** Change the displayed items for the a passed list */ 
	public void changeDataSet( List<Account> list){
		items.clear();
		items.addAll(list);
		notifyDataSetChanged();
	}

}
