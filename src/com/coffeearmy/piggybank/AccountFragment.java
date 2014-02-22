package com.coffeearmy.piggybank;

import com.coffeearmy.piggybank.R;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;


public class AccountFragment extends Fragment {

	public static final String ACCOUNT_ID = "account_ID";
	private static final int CURRENT_SAVE = 0;
	private ListView mTransactionList;
	private TextSwitcher mTxtSwitchSaves;
	 @Override
	  public View onCreateView(LayoutInflater inflater,
	                           ViewGroup container,
	                           Bundle savedInstanceState) {
	    View result=inflater.inflate(R.layout.account_drawer_layout, container, false);
	   
        //Get list items
       
        //Get total saves
      
        
        //Setup List
//        mTransactionList= (ListView) result.findViewById(R.id.lstTransaction);
//        //Set Adapter
//     
//        //Set total saves
//        mTxtSwitchSaves = (TextSwitcher) result.findViewById(R.id.txtSavesTotal);
//     
//        mTxtSwitchSaves.setFactory(new ViewFactory() {
//			
//			public View makeView() {
//				TextView myText = new TextView(PiggybankActivity.getContext());
//                myText.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
//                myText.setTextSize(40);
//                myText.setTextColor(Color.WHITE);
//                return myText;				
//			}
//		});
//        // Declare the in and out animations and initialize them  NINEOLDANIMATION
////        Animation in = AnimationUtils.loadAnimation(this,android.R.anim.slide_in_left);
////        Animation out = AnimationUtils.loadAnimation(this,android.R.anim.slide_out_right);
////        
////        // set the animation type of textSwitcher
////        mTxtSwitchSaves.setInAnimation(in);
////        mTxtSwitchSaves.setOutAnimation(out);
//        
//        //Set currect Saves
//        mTxtSwitchSaves.setText("30");
        
        //Layout where the input will appear
//        inputViewLayout = (LinearLayout) findViewById(R.id.inputLayout);
//        inputViewHandler = new InputView(this);
	    return(result);
	  }



}
