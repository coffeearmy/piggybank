package com.coffeearmy.piggybank.adapters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import com.coffeearmy.piggybank.Account;
import com.coffeearmy.piggybank.Operation;
import com.coffeearmy.piggybank.PiggybankActivity;
import com.coffeearmy.piggybank.R;
import com.coffeearmy.piggybank.view.CustomCheckIcon;
import com.coffeearmy.piggybank.view.CustomIcon;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/** Array Adapter for the Overview list, with the last operations from all accounts*/
public class OverviewOperationListAdapter extends ArrayAdapter<Operation> {

	private final Context context;
	private final List<Operation> items;
	private DateFormat mDateFormat;

	//View Holder Pattern
	static class ViewHolder {
		public TextView name;
		public TextView money;
		public TextView date;
		public CustomIcon icon;
	}

	public OverviewOperationListAdapter(Context context, int resource,
			int textViewResourceId, List<Operation> objects) {
		super(context, resource, textViewResourceId, objects);
		this.context = context;
		this.items = objects;
		mDateFormat = SimpleDateFormat.getDateInstance(DateFormat.LONG,
				Locale.getDefault());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		// Reusing views
		if (rowView == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			rowView = inflater.inflate(R.layout.overview_operation_row, null);
			
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.name = (TextView) rowView
					.findViewById(R.id.txtvAccountOperationOverview);
			viewHolder.money = (TextView) rowView
					.findViewById(R.id.txtvOperationsMoneyOverview);
			viewHolder.date = (TextView) rowView
					.findViewById(R.id.txtvDateOverview);
			viewHolder.icon = (CustomIcon) rowView
					.findViewById(R.id.imgvIconOperationOverview);
			//Save the viewHolder
			rowView.setTag(viewHolder);
		}

		// Fill the data in the view
		ViewHolder holder = (ViewHolder) rowView.getTag();
		if (holder != null) {

			Operation operation = items.get(position);
			Account account = operation.getAccount();
			///TODO Check for a possible bug
			if (account != null && operation != null) {
				// Log.d("operation_list_tag",
				// operation.toString()+" "+account.getName()+" "+holder.name!=null?"OK"
				// : "NNO");
				holder.name.setText(account.getName());

				String operationLabel = Double.toString(operation.getMoney());
				if (operation.getSign()) {
					operationLabel = "+ " + operationLabel;
					holder.money.setTextColor(context.getResources().getColor(
							R.color.Holo_green));
				} else {
					operationLabel = "- " + operationLabel;
					holder.money.setTextColor(context.getResources().getColor(
							R.color.Holo_red));
				}
				holder.money.setText(operationLabel);
				String formatedDate = mDateFormat.format(operation.getDate());

				holder.date.setText(formatedDate);
				// Create custom icon
				holder.icon.setStyle(account.getIcon(),operation.getIcon());
				//Save ID account for edit or delete the row
				///TODO BE DONE add operation id
				rowView.setTag(R.id.account_id, account.getId());
			}
		}

		return rowView;
	}
	//Change list items with a given Operation List
	public void changeDataSet(List<Operation> list) {
		items.clear();
		items.addAll(list);
		notifyDataSetChanged();
	}

}
