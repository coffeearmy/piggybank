package com.coffeearmy.piggybank.view;

import com.coffeearmy.piggybank.R;
import com.coffeearmy.piggybank.auxiliar.StyleAPP;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.View;

/**
 * Custom View displaying the Icon for the account/operation
 * 
 * TOBE DONE more comments when the classes are finished
 */
public class CustomIcon extends View {

	
	private int mBeginColor;
	private Drawable mIconBased;
	private int mStyleID = 0;
	private Resources mResources;
	private int mIconID;
	private Drawable mCustomOperationIcon;
	private boolean mChangedStyle;

	public CustomIcon(Context context, AttributeSet attrs) {
		super(context, attrs);

		mResources = context.getResources();
		// Read custom attributes
		TypedArray customAttributes = context.obtainStyledAttributes(attrs,
				R.styleable.CustomIcon);
		// Specify the background of the icon, is the same style of the account
		mStyleID = customAttributes.getInt(
				R.styleable.CustomIcon_numCustomStyleBG, 0);
		// Graphic of the icon
		mIconID = customAttributes.getInt(
				R.styleable.CustomIcon_numCustomIconBG, -1);
		// Get two background colors for the gradient
		int[] colorsFromAttributes = getBackgroundColor(mStyleID);

		mBeginColor = colorsFromAttributes[0];
		
		customAttributes.recycle();
		// Get background of the icon
		mIconBased = getIconBase();

		if (mIconID == -1)// Is an Account, and only need the background
			setBackgroundDrawable(mIconBased);
		else {
			// Is an operation, need the bg and a graphic.
			mCustomOperationIcon = getIcon(mIconID);
			setBackgroundDrawable(mCustomOperationIcon);
		}
	}

	private Drawable getIconBase() {

		OvalShape ovalShape = new OvalShape();
		ShapeDrawable sd = new ShapeDrawable(ovalShape);
		sd.getPaint().setColor(mBeginColor);

		return sd;
	}
	
//Maybe usefull if the icons are changed 
//	private Drawable getCustomIcon(int iconID) {
//		// mIconBased
//		Drawable[] layerDrawable = new Drawable[] { mIconBased, getIcon(iconID) };
//		LayerDrawable iconLayer = new LayerDrawable(layerDrawable);
//
//		return iconLayer;
//	}

	public void setGradientColors(int beginColor, int endColor) {
		mBeginColor = beginColor;
		
		invalidate();
		requestLayout();
	}

	public void setStyle(int selectedStyle, int selectedIcon) {

		int[] colorsFromAttributes = getBackgroundColor(selectedStyle);
		mStyleID = selectedStyle;
		mIconID = selectedIcon;
		mBeginColor = colorsFromAttributes[0];
		
		mChangedStyle = true;
		invalidate();
		requestLayout();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mChangedStyle) {
			mIconBased = getIconBase();
			if (mIconID == -1)// Is an Account ICon and only need the background
				setBackgroundDrawable(mIconBased);
			else {
				//Drawable bgCustomRadioItem = getCustomIcon(mIconID);
				setBackgroundDrawable(getIcon(mIconID));
			}
			mChangedStyle = false;
		}
	}

	/** Retrieve the colors associated with style @param i */
	public int[] getBackgroundColor(int i) {	

		return StyleAPP.getBackgroundColor(getContext(), i);
	}

	/** Retrieve the icon associated with style @param i */
	public Drawable getIcon(int i) {

		return StyleAPP.getIcon(getContext(), i);
	}

}
