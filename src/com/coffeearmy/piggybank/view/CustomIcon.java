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

/** Custom View displaying the Icon for the account/operation
 * 
 * TOBE DONE more comments when the classes are finished  
 */
public class CustomIcon extends View {

	private static final Orientation ORIENTATION = Orientation.BOTTOM_TOP;
	private int mBeginColor;
	private int mEndColor;
	private Drawable mIconBased;
	private Context mContext;
	private int mStyleID = 0;
	private Resources mResources;
	private int mIconID;

	public CustomIcon(Context context, AttributeSet attrs) {
		super(context, attrs);

		mContext = context;
		mResources = context.getResources();
		TypedArray customAttributes = context.obtainStyledAttributes(attrs,
				R.styleable.CustomIcon);
		mStyleID = customAttributes.getInt(
				R.styleable.CustomIcon_numCustomStyleBG, 0);
		mIconID = customAttributes.getInt(
				R.styleable.CustomIcon_numCustomIconBG, -1);

		int[] colorsFromAttributes = getBackgroundColor(mStyleID);

		mBeginColor = colorsFromAttributes[0];
		mEndColor = colorsFromAttributes[1];

		customAttributes.recycle();

		mIconBased = getIconBase();
		if (mIconID == -1)// Is an Account ICon and only need the background
			setBackgroundDrawable(mIconBased);
		else {
			Drawable bgCustomRadioItem = getCustomIcon(mIconID);
			setBackgroundDrawable(bgCustomRadioItem);
		}
	}

	private Drawable getIconBase() {

		GradientDrawable gradient = new GradientDrawable(ORIENTATION,
				new int[] { mBeginColor, mEndColor });
		gradient.setShape(GradientDrawable.OVAL);

		return gradient;
	}

	private Drawable getCustomIcon(int iconID) {
		// mIconBased
		Drawable[] layerDrawable = new Drawable[] { mIconBased, getIcon(iconID) };
		LayerDrawable iconLayer = new LayerDrawable(layerDrawable);

		return iconLayer;
	}

	public void setGradientColors(int beginColor, int endColor) {
		mBeginColor = beginColor;
		mEndColor = endColor;
		invalidate();
		requestLayout();
	}

	public void setStyle(int selectedStyle, int selectedIcon) {

		int[] colorsFromAttributes = getBackgroundColor(selectedStyle);
		mStyleID = selectedStyle;
		mIconID = selectedIcon;
		mBeginColor = colorsFromAttributes[0];
		mEndColor = colorsFromAttributes[1];
		invalidate();
		requestLayout();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		mIconBased = getIconBase();
		if (mIconID == -1)// Is an Account ICon and only need the background
			setBackgroundDrawable(mIconBased);
		else {
			Drawable bgCustomRadioItem = getCustomIcon(mIconID);
			setBackgroundDrawable(bgCustomRadioItem);
		}

		super.onDraw(canvas);
	}
    /** Retrieve the colors associated with style @param i */
	public int[] getBackgroundColor(int i) {

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

}
