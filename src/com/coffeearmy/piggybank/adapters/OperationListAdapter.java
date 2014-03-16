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

public class OperationListAdapter extends ArrayAdapter<Operation> {

	  private final Context context;
	  private final  List<Operation> items;
	  private DateFormat mDateFormat;
	  
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
	    mDateFormat= SimpleDateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
	}
	  
	
	@Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    View rowView = convertView;
	    // reuse views
	    if (rowView == null) {
	      LayoutInflater inflater =  ((Activity) context).getLayoutInflater();
	      rowView = inflater.inflate(R.layout.account_row, null);
	      // configure view holder
	      ViewHolder viewHolder = new ViewHolder();
	      viewHolder.text = (TextView) rowView.findViewById(R.id.txtv_operation_money);
	      viewHolder.icon = (CustomIcon) rowView
	          .findViewById(R.id.imgv_icon_operation);
	      viewHolder.date =(TextView) rowView.findViewById(R.id.txtv_operation_date);
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
	    String formatedDate =mDateFormat.format(operation.getDate());
	    holder.date.setText(formatedDate);   
	    
	    holder.icon.setStyle(operation.getType());
	    rowView.setTag(R.id.account_id, operation.getId());	  

	    return rowView;
	  }


	public void changeDataSet(List<Operation> opertaionList) {
		items.clear();
		items.addAll(opertaionList);
		notifyDataSetChanged();		
	}


}
