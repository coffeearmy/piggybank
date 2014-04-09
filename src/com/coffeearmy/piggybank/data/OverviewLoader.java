package com.coffeearmy.piggybank.data;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.coffeearmy.piggybank.Operation;

public class OverviewLoader extends AsyncTaskLoader<List<Operation>> {

	private Context mContext;
	private Date mDateLastWeek;

	public OverviewLoader(Context context) {
		super(context);		
		mContext=context;
	}

	@Override
	public List<Operation> loadInBackground() {
			return OperationHandler.getInstance(mContext).getLastOperationListbyDate(getLastWeek());
	}
	
	private Date getLastWeek(){
		if(mDateLastWeek==null){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -7);
		Date dateLastWeek = calendar.getTime();
		mDateLastWeek=dateLastWeek;
		}
		return mDateLastWeek;
	}

}