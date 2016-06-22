package vn.philong.game.pikachunew.dlg;

import vn.philong.game.pikachunew.R;
import android.app.Activity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DialogInput extends DialogLayout {

	public static interface IInput {
		public void onInput(String text1, String text2, String codeTelco);
		public void onCancel();
	}

	public DialogInput(Activity context) {
		super(context, R.layout.layout_dialog_input, R.id.confirm_input_cancel);
	}

	private IInput mInput;
	private EditText inputText1, inputText2;


	@Override
	protected void init() {
		Button btOK = (Button) findViewById(R.id.confirm_input_ok);
		inputText1 = (EditText) findViewById(R.id.input_code_1);
		inputText2 = (EditText) findViewById(R.id.input_code_2);
		Button btCancel = (Button) findViewById(R.id.confirm_input_cancel);
		btCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
				if (mInput != null)
					mInput.onCancel();
			}
		});
		btOK.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mInput != null)
					mInput.onInput(inputText1.getText().toString(), inputText2
							.getText().toString(), code);
			}
		});
	}
	private String code;
	public void show(int iHint1, int iHint2, int textDesc,String code, IInput input,int textID) {
		TextView tvTitle = (TextView) findViewById(R.id.input_text);
		tvTitle.setText(context.getString(textDesc)+" "+context.getString(textID));
		inputText1.setHint(iHint1);
		inputText2.setHint(iHint2);
		inputText1.setText("");
		inputText2.setText("");
		this.mInput = input;
		this.code = code;
		show();
	}
	public void show(int iHint1, int textDesc, IInput input) {
		TextView tvTitle = (TextView) findViewById(R.id.input_text);
		tvTitle.setText(textDesc);
		inputText1.setHint(iHint1);
		inputText1.setText("");
		inputText1.setInputType(InputType.TYPE_CLASS_PHONE);
		inputText2.setVisibility(View.GONE);
		this.mInput = input;
		show();
	}
}
