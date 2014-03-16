package com.coffeearmy.piggybank.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.coffeearmy.piggybank.R;

public class CustomCheckIcon extends RadioButton {	
	
	private static final Orientation ORIENTATION = Orientation.BOTTOM_TOP;
	private int mBeginColor;
	private int mEndColor;
	private Drawable mIconBased;
	private int mStyleID;

	public CustomCheckIcon(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		TypedArray customAttributes= context.obtainStyledAttributes(attrs, R.styleable.CustomCheckIcon);
		mStyleID=  customAttributes.getInt(R.styleable.CustomCheckIcon_numCustomStyle, 0);
		int[] colorsFromAttributes= StyleCustomIcon.getBackgroundColor(context, mStyleID);
		mBeginColor= colorsFromAttributes[0];
		mEndColor= colorsFromAttributes[1];
		customAttributes.recycle();
		
		Drawable bgCustomRadioItem= createCustomBackground(context.getResources());
		setBackgroundDrawable(bgCustomRadioItem);		
		setButtonDrawable(android.R.color.transparent);
	}
	
	private Drawable createCustomBackground(Resources r) {
		
		mIconBased = getIconBase();
		Drawable stateChecked=getCheckedIcon(r);
		Drawable stateUncheked=mIconBased;
		
		StateListDrawable stateList= new StateListDrawable();
		stateList.addState(new int[] { android.R.attr.state_checked },stateChecked);
		stateList.addState(new int[] { -android.R.attr.state_checked}, stateUncheked);
		
		return stateList;
	}

		
	private Drawable getIconBase(){
		//ShapeDrawable circle = new ShapeDrawable(new OvalShape());//ORIENTATION, new int[]{mBeginColor,mEndColor}
		GradientDrawable gradient = new GradientDrawable(ORIENTATION, new int[]{mBeginColor,mEndColor});
		gradient.setShape(GradientDrawable.OVAL);
		//gradient.setSize(30, 30);
		return gradient;		
	}
	
	private Drawable getCheckedIcon(Resources r){
		//mIconBased
		Drawable [] layerDrawable= new Drawable[]{mIconBased, r.getDrawable(R.drawable.abc_ic_cab_done_holo_dark)};
		LayerDrawable iconLayer= new LayerDrawable(layerDrawable);
		
		return iconLayer;	
	}
	
	public void setGradientColors(int beginColor, int endColor){
		mBeginColor=beginColor;
		mEndColor=endColor;
		invalidate();
		requestLayout();
	}
	
	public int getStyle(){
		return mStyleID;
	}
	
	
	

}
