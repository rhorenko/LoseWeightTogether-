package mast.avalons;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CalcBMIActivity extends Activity implements OnClickListener{
private EditText inputHeight;
private EditText inputWeight;
private EditText outputBmi;
private TextView levelName;
private TextView textbmi;
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.calcbmi);
       
   inputHeight =(EditText)findViewById(R.id.inputH);
   inputWeight =(EditText)findViewById(R.id.inputW);
   outputBmi =(EditText)findViewById(R.id.output_calc_bmi);
   levelName =(TextView)findViewById(R.id.levelNameText);
   textbmi = (TextView)findViewById(R.id.textbmi);
   final Button btn_calc_bmi = (Button)findViewById(R.id.button1);
  
   btn_calc_bmi.setOnClickListener(this);
   
    }
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
        case R.id.button1:
        	float bmi = Bmi.calculateBMI(Float.parseFloat(inputWeight.getText().toString()), (float)(0.01*Float.parseFloat(inputHeight.getText().toString())), true, true);
            outputBmi.setText(Float.toString(bmi));
            levelName.setText(Bmi.getLevelName(bmi,this));
            textbmi.setText(R.string.textbmi);
            break;
        
         }
	}
}
