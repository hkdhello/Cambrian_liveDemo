package com.example.myapplication1;

import androidx.appcompat.app.AppCompatActivity;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.TextView.OnEditorActionListener;
import android.view.View.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity
        implements OnEditorActionListener, OnClickListener{

    private EditText billAmountEditText;
    private TextView tipTextView, totalTextView;
    private Button percentButtonUp, percentButtonDown;
    private String billAmountString = "";
    private float tipPercent = .15f;
    private SharedPreferences savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        billAmountEditText = (EditText) findViewById(R.id.billAmountEditText);
        tipTextView = (TextView) findViewById(R.id.tipTextView);
        totalTextView = (TextView) findViewById(R.id.totalTextView);
        percentButtonUp = (Button) findViewById(R.id.percentButtonUp);
        percentButtonDown = (Button) findViewById(R.id.percentButtonDown);

        billAmountEditText.setOnEditorActionListener(this);
        percentButtonUp.setOnClickListener(this);
        percentButtonDown.setOnClickListener(this);

        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
        if( actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_UNSPECIFIED){
            tipTextView.setText("$10.00");
            totalTextView.setText("$110.00");
        }
        return false;
    }

    public void calculateAndDisplay(){
        billAmountString = billAmountEditText.getText().toString();
        float billAmount;
        if(billAmountString.equals("")){
            billAmount = 0f;
        }
        else{
            billAmount = Float.parseFloat(billAmountString);
        }
        float tipAmount = billAmount * tipPercent;
        float totalAmount = billAmount + tipAmount;
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        tipTextView.setText(currency.format(tipAmount));
        totalTextView.setText(currency.format(totalAmount));
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.percentButtonDown:
                if(tipPercent > 0f) {
                    tipPercent = tipPercent - .01f;
                    calculateAndDisplay();
                }
                break;
            case R.id.percentButtonUp:
                tipPercent = tipPercent + 0.01f;
                calculateAndDisplay();
                break;
            /*default:
                Default is like the else in an if statement
                With onClick it can be dangerous because it
                    will run when something other than our
                    button is clicked
                break;
             */
        }
    }

    @Override
    public void onPause(){
        Editor editor = savedValues.edit();
        editor.putString("billAmountString", billAmountString);
        editor.putFloat("tipPercent", tipPercent);
        editor.commit();

        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();

        billAmountString = savedValues.getString("billAmountString", "" );
        tipPercent = savedValues.getFloat("tipPercent", 0.15f);
    }
}













