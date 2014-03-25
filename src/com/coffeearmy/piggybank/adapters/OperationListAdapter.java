package com.coffeearmy.piggybank.adapters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import com.coffeearmy.piggybank.Account;
import com.coffeearmy.piggybank.Operation;
import com.coffeearmy.piggybank.R;
import com.coffeearmy.piggybank.adapters.AccountListAdapter.ViewHolder;
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

/** Array Adapter for listing operations from one account  */
public class OperationListAdapter extends ArrayAdapter<Operation> {

	  private final Context context;
	  private final  List<Operation> items;
	  private DateFormat mDateFormat;
	  
	  //ViewHolder pattern
	  static class ViewHolder {
		    public TextView text;
		    public TextView date;
		    public CustomIcon icon;
		  }
	
	public OperationListAdapter(Context context, int resource,
			int textViewResourceId, List<Operation> objects) {
		super(context, resource, textViewResourceId, objects);
		this.context = context;
	    this.items = objects;
	    //Date format used in the list
	    mDateFormat= SimpleDateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
	}
	  
	
	@Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    View rowView = convertView;
	    // Reusing Views
	    if (rowView == null) {
	      LayoutInflater inflater =  ((Activity) context).getLayoutInflater();
	      rowView = inflater.inflate(R.layout.account_row, null);
	      // configure view holder
	      ViewHolder viewHolder = new ViewHolder();
	      viewHolder.text = (TextView) rowView.findViewById(R.id.txtv_operation_money);
	      viewHolder.icon = (CustomIcon) rowView
	          .findViewById(R.id.imgv_icon_operation);
	      viewHolder.date =(TextView) rowView.findViewById(R.id.txtv_operation_date);
	      //Store the viewHolder in the tag of the view
	      rowView.setTag(viewHolder);
	    }

	    // fill data
	    ViewHolder holder = (ViewHolder) rowView.getTag();
	    Operation operation = items.get(position);
	    
	    String operationLabel=Double.toString(operation.getMoney());
	    
	    //Use red for negative amounts and green for positive amounts
	    if(operation.getSign()){
	    	operationLabel="+ "+operationLabel;
	    	holder.text.setTextColor( context.getResources().getColor(R.color.Holo_green));
	    }else{
	    	operationLabel="- "+operationLabel;
	    	holder.text.setTextColor(context.getResources().getColor(R.color.Holo_red));
	    }
	    holder.text.setText(operationLabel);
	    //Format the data with a short date format
	    String formatedDate =mDateFormat.format(operation.getDate());
	    holder.date.setText(formatedDate);   
	    
	    //Custom Icon from CustomIcon.java
	    holder.icon.setStyle(operation.getAccount().getIcon(),operation.getIcon());
	    
	    //Save IDs from the account and the operation for Edit and Delete operations
	    rowView.setTag(R.id.account_id, operation.getAccountId());	  
	    rowView.setTag(R.id.operation_id, operation.getId());
	    return rowView;
	  }

	/**Change the data of the list with a given Operation List*/
	public void changeDataSet(List<Operation> opertaionList) {
		items.clear();
		items.addAll(opertaionList);
		notifyDataSetChanged();		
	}


}
