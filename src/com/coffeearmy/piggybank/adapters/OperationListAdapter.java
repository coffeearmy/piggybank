package com.coffeearmy.piggybank.adapters;

import java.util.List;

import com.coffeearmy.piggybank.Account;
import com.coffeearmy.piggybank.Operation;
import com.coffeearmy.piggybank.R;
import com.coffeearmy.piggybank.adapters.AccountListAdapter.ViewHolder;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class OperationListAdapter extends ArrayAdapter<Operation> {

	  private final Context context;
	  private final  List<Operation> items;
	  
	  static class ViewHolder {
		    public TextView text;
		    public ImageView image;
		  }
	
	public OperationListAdapter(Context context, int resource,
			int textViewResourceId, List<Operation> objects) {
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
	    Operation operation = items.get(position);
	    String operationLabel=Double.toString(operation.getMoney());
	    if(operation.getOperation()==0){
	    	operationLabel="+ "+operationLabel;
	    	holder.text.setTextColor( context.getResources().getColor(R.color.Holo_green));
	    }else{
	    	operationLabel="- "+operationLabel;
	    	holder.text.setTextColor(context.getResources().getColor(R.color.Holo_red));
	    }
	    holder.text.setText(operationLabel);
	       
	    holder.image.setImageResource(R.drawable.cicle_button_1);
	    rowView.setTag(R.id.account_id, operation.getId());	  

	    return rowView;
	  }


}
