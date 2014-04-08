package com.coffeearmy.piggybank.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.coffeearmy.piggybank.R;
import com.coffeearmy.piggybank.auxiliar.StyleAPP;

/**
 * Custom Radio Button for choosing the Icon for the account/operation The View
 * uses a StateListDrawable for the Checked events, and layer lists for the
 * drawables. TOBE DONE more comments when the classes are finished
 */
public class CustomCheckIcon extends RadioButton {

	private static final Orientation ORIENTATION = Orientation.BOTTOM_TOP;
	private int mBeginColor;
	private int mEndColor;
	private Drawable mIconBased;
	private int mStyleID;
	private int mIconID;
	private Resources mResources;

	public CustomCheckIcon(Context context, AttributeSet attrs) {
		super(context, attrs);

		mResources = context.getResources();
		TypedArray customAttributes = context.obtainStyledAttributes(attrs,
				R.styleable.CustomCheckIcon);
		// Retrieve custom parameters for the bg colors and the icon
		mStyleID = customAttributes.getInt(
				R.styleable.CustomCheckIcon_numCustomStyle, 0);
		mIconID = customAttributes.getInt(
				R.styleable.CustomIcon_numCustomIconBG, -1);
		int[] colorsFromAttributes = getBackgroundColor(context, mStyleID);

		// Colors for the Background Gradient
		mBeginColor = colorsFromAttributes[0];
		mEndColor = colorsFromAttributes[1];
		customAttributes.recycle();

		Drawable bgCustomRadioItem = createCustomBackground();
		setBackgroundDrawable(bgCustomRadioItem);
		setButtonDrawable(android.R.color.transparent);
	}

	private Drawable createCustomBackground() {
		
		Drawable stateUncheked;		
		
		if (mIconID > -1) {
			//Its a Operation Icon without BG
			stateUncheked = getIcon(mIconID);
		}else{
			//Its a Piggybank Icon without Icon
			stateUncheked = getIconBase();
		}
		Drawable stateChecked = getCheckedDrawableIcon(stateUncheked);
		
		StateListDrawable stateList = new StateListDrawable();
		stateList.addState(new int[] { android.R.attr.state_checked },
				stateChecked);
		stateList.addState(new int[] { -android.R.attr.state_checked },
				stateUncheked);

		return stateList;
	}


	private Drawable getIconBase() {
		
			OvalShape ovalShape = new OvalShape();		
			ShapeDrawable sd = new ShapeDrawable(ovalShape);
			sd.getPaint().setColor(mBeginColor);
		return sd;
			
	}
	

	private Drawable getCheckedDrawableIcon(Drawable selection) {
		// mIconBased
		Drawable[] layerDrawable = new Drawable[] { selection,
				mResources.getDrawable(R.drawable.abc_ic_cab_done_holo_dark) };
		LayerDrawable iconLayer = new LayerDrawable(layerDrawable);

		return iconLayer;
	}

	public void setGradientColors(int beginColor, int endColor) {
		mBeginColor = beginColor;
		mEndColor = endColor;
		invalidate();
		requestLayout();
	}

	/** Retrieve the colors associated with style @param i */
	public int[] getBackgroundColor(Context c, int i) {
		return StyleAPP.getBackgroundColor(getContext(), i);
	}

	/** Retrieve the icon associated with style @param i */
	public Drawable getIcon(int i) {
		return StyleAPP.getIcon(getContext(), i);
	}

	public int getIconSelected() {
		return mIconID;
	}

	public int getStyle() {
		return mStyleID;
	}

}
