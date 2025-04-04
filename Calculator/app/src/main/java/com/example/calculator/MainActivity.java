package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    double input1 = 0, input2 = 0;
    TextView edt1;
    boolean Addition, Subtract, Multiplication, Division, mRemainder, decimal;

    Button button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    Button buttonAdd, buttonSub, buttonMul, buttonDivision, Remainder;
    Button buttonEqual, buttonDel, buttonDot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt1 = findViewById(R.id.display);

        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        button7 = findViewById(R.id.button7);
        button8 = findViewById(R.id.button8);
        button9 = findViewById(R.id.button9);

        buttonDot = findViewById(R.id.buttonDot);
        buttonAdd = findViewById(R.id.buttonadd);
        buttonSub = findViewById(R.id.buttonsub);
        buttonMul = findViewById(R.id.buttonmul);
        buttonDivision = findViewById(R.id.buttondiv);
        Remainder = findViewById(R.id.Remainder);
        buttonDel = findViewById(R.id.buttonDel);
        buttonEqual = findViewById(R.id.buttoneql);

        View.OnClickListener numberClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                edt1.setText(edt1.getText().toString() + b.getText().toString());
            }
        };

        button0.setOnClickListener(numberClickListener);
        button1.setOnClickListener(numberClickListener);
        button2.setOnClickListener(numberClickListener);
        button3.setOnClickListener(numberClickListener);
        button4.setOnClickListener(numberClickListener);
        button5.setOnClickListener(numberClickListener);
        button6.setOnClickListener(numberClickListener);
        button7.setOnClickListener(numberClickListener);
        button8.setOnClickListener(numberClickListener);
        button9.setOnClickListener(numberClickListener);

        buttonAdd.setOnClickListener(v -> handleOperator("add"));
        buttonSub.setOnClickListener(v -> handleOperator("sub"));
        buttonMul.setOnClickListener(v -> handleOperator("mul"));
        buttonDivision.setOnClickListener(v -> handleOperator("div"));
        Remainder.setOnClickListener(v -> handleOperator("mod"));

        buttonEqual.setOnClickListener(v -> calculateResult());

        buttonDel.setOnClickListener(v -> {
            edt1.setText("");
            input1 = 0.0;
            input2 = 0.0;
        });

        buttonDot.setOnClickListener(v -> {
            if (!decimal) {
                edt1.setText(edt1.getText() + ".");
                decimal = true;
            }
        });
    }

    private void handleOperator(String type) {
        if (edt1.getText().length() != 0) {
            input1 = Double.parseDouble(edt1.getText().toString());
            edt1.setText("");
            decimal = false;
            switch (type) {
                case "add": Addition = true; break;
                case "sub": Subtract = true; break;
                case "mul": Multiplication = true; break;
                case "div": Division = true; break;
                case "mod": mRemainder = true; break;
            }
        }
    }

    private void calculateResult() {
        if (edt1.getText().length() != 0) {
            input2 = Double.parseDouble(edt1.getText().toString());

            if (Addition) {
                edt1.setText((input1 + input2) + "");
                Addition = false;
            } else if (Subtract) {
                edt1.setText((input1 - input2) + "");
                Subtract = false;
            } else if (Multiplication) {
                edt1.setText((input1 * input2) + "");
                Multiplication = false;
            } else if (Division) {
                edt1.setText((input1 / input2) + "");
                Division = false;
            } else if (mRemainder) {
                edt1.setText((input1 % input2) + "");
                mRemainder = false;
            }
        }
    }
}
