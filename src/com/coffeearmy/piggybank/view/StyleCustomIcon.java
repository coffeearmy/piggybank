package com.coffeearmy.piggybank.view;

import com.coffeearmy.piggybank.R;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;

public class StyleCustomIcon {
	
	
	public StyleCustomIcon(Context c) {}
	
	public static int[] getBackgroundColor(Context c, int i){
		
		Resources res = c.getResources();
		//Array with the array id for every buttom
		TypedArray arrayBackgroundColors = res.obtainTypedArray(R.array.icon_bg_values_array);
		//Get array id
		int backgroundColors=arrayBackgroundColors.getResourceId(i, R.array.icon_BG_1);
		TypedArray colorArray=res.obtainTypedArray(backgroundColors);
		int [] colors = new int[]{
				colorArray.getColor(0, Color.CYAN),
				colorArray.getColor(1, Color.CYAN),
		};
		
		arrayBackgroundColors.recycle();
		colorArray.recycle();
		
		return colors;		
	}

}
