package com.coffeearmy.piggybank.view;

import com.coffeearmy.piggybank.R;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.View;

public class CustomIcon extends View {

	private static final Orientation ORIENTATION = Orientation.BOTTOM_TOP;
	private int mBeginColor;
	private int mEndColor;
	private Drawable mIconBased;
	private Context mContext;
	private int mStyleID=0;

	public CustomIcon(Context context, AttributeSet attrs) {
		super(context, attrs);

		mContext=context;
		
		TypedArray customAttributes = context.obtainStyledAttributes(attrs,
				R.styleable.CustomIcon);
		mStyleID=  customAttributes.getInt(R.styleable.CustomIcon_numCustomStyleBG, 0);
		
		int[] colorsFromAttributes= StyleCustomIcon.getBackgroundColor(context, mStyleID);
		
		mBeginColor= colorsFromAttributes[0];
		mEndColor= colorsFromAttributes[1];
		
		customAttributes.recycle();
		
		mIconBased = getIconBase();
		
		Drawable bgCustomRadioItem = getCheckedIcon(context.getResources());
		setBackgroundDrawable(bgCustomRadioItem);
	}

	private Drawable getIconBase() {
	
		GradientDrawable gradient = new GradientDrawable(ORIENTATION,
				new int[] { mBeginColor, mEndColor });
		gradient.setShape(GradientDrawable.OVAL);
	
		return gradient;
	}

	private Drawable getCheckedIcon(Resources r) {
		// mIconBased
		Drawable[] layerDrawable = new Drawable[] { mIconBased,
				r.getDrawable(R.drawable.abc_ic_cab_done_holo_dark) };
		LayerDrawable iconLayer = new LayerDrawable(layerDrawable);

		return iconLayer;
	}

	public void setGradientColors(int beginColor, int endColor) {
		mBeginColor = beginColor;
		mEndColor = endColor;
		invalidate();
		requestLayout();
	}

	public void setStyle(int selectedStyle) {
		
		int[] colorsFromAttributes= StyleCustomIcon.getBackgroundColor(mContext, selectedStyle);
		mStyleID=selectedStyle;
		mBeginColor= colorsFromAttributes[0];
		mEndColor= colorsFromAttributes[1];
		invalidate();	
		requestLayout();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		mIconBased = getIconBase();
		
		Drawable bgCustomRadioItem = getCheckedIcon(getResources());
		setBackgroundDrawable(bgCustomRadioItem);
		super.onDraw(canvas);
	}

}
