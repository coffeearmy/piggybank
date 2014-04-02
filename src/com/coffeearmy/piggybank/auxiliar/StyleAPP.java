package com.coffeearmy.piggybank.auxiliar;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.coffeearmy.piggybank.R;

/** This class is for access the different custom styles declared in array.xml*/
public class StyleAPP {

	/** Retrieve the colors associated with style @param i */
	static public int[] getBackgroundColor(Context c, int i) {
		
		Resources resources = c.getResources();
		// Array with the array id for every buttom
		TypedArray arrayBackgroundColors = resources
				.obtainTypedArray(R.array.icon_bg_values_array);
		// Get array id
		int backgroundColors = arrayBackgroundColors.getResourceId(i,
				R.array.icon_BG_1);
		TypedArray colorArray = resources.obtainTypedArray(backgroundColors);
		int[] colors = new int[] { colorArray.getColor(0, Color.CYAN),
				colorArray.getColor(1, Color.CYAN), };

		arrayBackgroundColors.recycle();
		colorArray.recycle();

		return colors;
	}
	/** Retrieve the icon associated with style @param i */
	static public Drawable getIcon(Context c, int i) {
		
		Resources resources = c.getResources();
		TypedArray arrayBackgroundIcon = resources
				.obtainTypedArray(R.array.icon_op_values_array);
		Drawable customIcon = arrayBackgroundIcon.getDrawable(i);

		arrayBackgroundIcon.recycle();

		return customIcon;
	}
}
