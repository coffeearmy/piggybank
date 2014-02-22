package com.coffeearmy.piggybank;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class NewOperationFragment extends Fragment {
	public static final String FRAG_NEW_OPERATION = "FRAG_NEWOPERATION";
	private ImageButton addButton;
	private ImageButton minusButton;
	private EditText inputCuantity;
	private Button acceptTransation;
	private TextView signLabel;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View result = inflater.inflate(R.layout.operation_layout, container,
				false);

		return result;
	}

	public void InputViewSetup(View inputView) {

		addButton = (ImageButton) inputView.findViewById(R.id.ibtnAddSaves);
		minusButton = (ImageButton) inputView.findViewById(R.id.ibtnMinusSaves);
		inputCuantity = (EditText) inputView.findViewById(R.id.edtCuantity);
		acceptTransation = (Button) inputView
				.findViewById(R.id.btnOKInputTransaction);
		signLabel = (TextView) inputView.findViewById(R.id.txtVsign);

		// By default will be a add operation
		signLabel.setText("+");
		addButton.setSelected(true);
		inputCuantity.setText("0");

		addButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				signLabel.setText("+");
			}
		});

		minusButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				signLabel.setText("-");
			}
		});
		acceptTransation.setTag(this);
		acceptTransation.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
//				if (inputCuantity.length() != 0) {
//					((InputView) v.getTag()).Operate(signLabel.getText()
//							.toString(), inputCuantity.getText().toString());
//					// disapear inputView
//					((InputView) v.getTag()).setViewGone();
//				}

			}
		});

	}

	protected void Operate(String sign, String value) {
		boolean isAddingOp = false;
		double cuantityFromStringInputEdit = 0.0;
		if (sign.equals("+")) {
			isAddingOp = true;
		}

		cuantityFromStringInputEdit = Double.parseDouble(value);

//		if (mEventListener != null) {
//			mEventListener.onOperationIsPerformed(isAddingOp,
//					cuantityFromStringInputEdit);
//		}
	}

}
