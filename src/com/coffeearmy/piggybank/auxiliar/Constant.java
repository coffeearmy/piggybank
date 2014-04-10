package com.coffeearmy.piggybank.auxiliar;

import java.text.DecimalFormat;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;



/** Constants used in EventBus and extras*/
public class Constant {
	
	public static String ACCOUNT_ID="account_id";
	public static String ACCOUNT_NAME="account_name";
	public static String ACCOUNT_MONEY="account_money";
	public static String ACCOUNT_ICON="account_icon";
	
	public static String OPERATION_ID="operation_id";
	public static String OPERATION_SING="operation_sign";
	public static String OPERATION_MONEY="operation_money";
	public static String OPERATION_ICON="operation_icon";
	public static String OPERATION_DATE="operation_date";
	
	//Only when edit an operation previous
	public static String OPERATION_SING_PREVIOUS="operation_sign_previous";
	public static String OPERATION_MONEY_PREVIOUS="operation_money_previous";
	
	//Number formatter
	public static NumberFormat DF = NumberFormat.getInstance();	
	//public static 
	
	//Date formatter
	public static DateFormat dateFormatMMMDD= SimpleDateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
	
	
	//Used in save states
	public static final String ACCOUNT_DIALOG_IS_SHOW = "account_dialog_show";
	public static final String ACCOUNT_DIALOG_NAME_FIELD = "account_dialog_show_name";
	public static final String ACCOUNT_DIALOG_MONEY_FIELD = "account_dialog_show_money";
	public static final String ACCOUNT_DIALOG_ICON_FIELD = "account_dialog_show_icon";
	public static final String MENU_SELECTED_ITEM = "menu_selected_item";
	public static final String OPERATION_ITEM_LIST_SAVE = "operation_item_list_save";
	public static final String OPERATION_DIALOG_SIGN_SAVE = "operation_dialog_sign_save";
	public static final String OPERATION_DIALOG_ICON_SAVE = "operation_dialog_icon_save";
	public static final String OPERATION_DIALOG_MONEY_SAVE = "operation_dialog_money_save";
	public static final String OVERVIEW_ITEM_LIST_SAVE = "overview_item_list_save";
	
	//Loaders
	public static final int LOADER_OVERVIEW_ID = 1;
	public static final int LOADER_ACCOUNT_ID = 2;
	public static final int LOADER_OPERATION_ID = 0;
	

}
