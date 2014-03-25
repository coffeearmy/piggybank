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

/** Custom Radio Button for choosing the Icon for the account/operation
 * The View uses a StateListDrawable for the Checked events, and layer lists
 * for the drawables.
 * TOBE DONE more comments when the classes are finished  
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
		
		mResources=context.getResources();
		TypedArray customAttributes = context.obtainStyledAttributes(attrs,
				R.styleable.CustomCheckIcon);
		//Retrieve custom parameters for the bg colors and the icon
		mStyleID = customAttributes.getInt(
				R.styleable.CustomCheckIcon_numCustomStyle, 0);
		mIconID = customAttributes.getInt(
				R.styleable.CustomIcon_numCustomIconBG, -1);
		int[] colorsFromAttributes = getBackgroundColor(context, mStyleID);
		
		//Colors for the Background Gradient
		mBeginColor = colorsFromAttributes[0];
		mEndColor = colorsFromAttributes[1];
		customAttributes.recycle();

		Drawable bgCustomRadioItem = createCustomBackground();
		setBackgroundDrawable(bgCustomRadioItem);
		setButtonDrawable(android.R.color.transparent);
	}

	private Drawable createCustomBackground() {

		mIconBased = getIconBase();
		Drawable stateChecked = getCheckedIcon();
		Drawable stateUncheked = mIconBased;
		if(mIconID>-1){
			 stateUncheked = setIcon(mIconBased);	
		}
		StateListDrawable stateList = new StateListDrawable();
		stateList.addState(new int[] { android.R.attr.state_checked },
				stateChecked);
		stateList.addState(new int[] { -android.R.attr.state_checked },
				stateUncheked);

		return stateList;
	}

	private Drawable setIcon(Drawable mIconBased2) {
		Drawable[] layerDrawable = new Drawable[] { mIconBased,
				 getIcon(mIconID) };
		LayerDrawable iconLayer = new LayerDrawable(layerDrawable);

		return iconLayer;
	}

	private Drawable getIconBase() {
		// ShapeDrawable circle = new ShapeDrawable(new
		// OvalShape());//ORIENTATION, new int[]{mBeginColor,mEndColor}
		GradientDrawable gradient = new GradientDrawable(ORIENTATION,
				new int[] { mBeginColor, mEndColor });
		gradient.setShape(GradientDrawable.OVAL);
		// gradient.setSize(30, 30);
		return gradient;
	}

	private Drawable getCheckedIcon() {
		// mIconBased
		Drawable[] layerDrawable = new Drawable[] { mIconBased,
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

		// Array with the array id for every buttom
		TypedArray arrayBackgroundColors = mResources
				.obtainTypedArray(R.array.icon_bg_values_array);
		// Get array id
		int backgroundColors = arrayBackgroundColors.getResourceId(i,
				R.array.icon_BG_1);
		TypedArray colorArray = mResources.obtainTypedArray(backgroundColors);
		int[] colors = new int[] { colorArray.getColor(0, Color.CYAN),
				colorArray.getColor(1, Color.CYAN), };

		arrayBackgroundColors.recycle();
		colorArray.recycle();

		return colors;
	}
	/** Retrieve the icon associated with style @param i */
	public Drawable getIcon(int i) {

		TypedArray arrayBackgroundIcon = mResources
				.obtainTypedArray(R.array.icon_op_values_array);
		Drawable customIcon = arrayBackgroundIcon.getDrawable(i);

		arrayBackgroundIcon.recycle();

		return customIcon;
	}

	public int getIconSelected() {
		return mIconID;
	}
	public int getStyle() {
		return mStyleID;
	}

}
