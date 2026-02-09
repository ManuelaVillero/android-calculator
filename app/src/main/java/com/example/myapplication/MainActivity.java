package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvDisplay;
    private TextView tvHistory;
    private Button btnMode;

    private Calculator calculator;

    private boolean historyEnabled = false;
    private final StringBuilder historyLog = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link UI
        tvDisplay = findViewById(R.id.tvDisplay);
        tvHistory = findViewById(R.id.tvHistory);
        btnMode = findViewById(R.id.btnMode);

        calculator = new Calculator();

        // Start in Standard mode (history hidden)
        setHistoryVisible(false);
        btnMode.setText("Standard - No History");
        tvHistory.setText("(no calculations yet)");

        // Mode toggle
        btnMode.setOnClickListener(v -> {
            historyEnabled = !historyEnabled;

            if (historyEnabled) {
                btnMode.setText("Advanced - With History");
                setHistoryVisible(true);

                if (historyLog.length() == 0) {
                    tvHistory.setText("(no calculations yet)");
                } else {
                    tvHistory.setText(historyLog.toString());
                }
            } else {
                btnMode.setText("Standard - No History");
                setHistoryVisible(false);
                historyLog.setLength(0);
                tvHistory.setText("(no calculations yet)");
            }
        });

        // Digits
        wireDigit(R.id.btn0, "0");
        wireDigit(R.id.btn1, "1");
        wireDigit(R.id.btn2, "2");
        wireDigit(R.id.btn3, "3");
        wireDigit(R.id.btn4, "4");
        wireDigit(R.id.btn5, "5");
        wireDigit(R.id.btn6, "6");
        wireDigit(R.id.btn7, "7");
        wireDigit(R.id.btn8, "8");
        wireDigit(R.id.btn9, "9");

        // Operators (MATCH XML IDs)
        wireOp(R.id.btnPlus, "+");
        wireOp(R.id.btnMinus, "-");
        wireOp(R.id.btnMultiply, "*");
        wireOp(R.id.btnDivide, "/");

        // Clear
        Button btnClear = findViewById(R.id.btnClear);
        btnClear.setOnClickListener(v -> {
            calculator.push("C");
            tvDisplay.setText("0");
        });

        // Equals
        Button btnEquals = findViewById(R.id.btnEquals);
        btnEquals.setOnClickListener(v -> {
            String expr = calculator.getExpression(); // before calculate clears it

            try {
                int result = calculator.calculate();
                tvDisplay.setText(String.valueOf(result));

                if (historyEnabled) {
                    historyLog.append(expr).append("=").append(result).append("\n");
                    tvHistory.setText(historyLog.toString());
                }

            } catch (ArithmeticException ex) {
                tvDisplay.setText("Error");
                calculator.push("C");
            }
        });
    }

    private void setHistoryVisible(boolean visible) {
        if (tvHistory == null) return;
        tvHistory.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void wireDigit(int id, String digit) {
        Button b = findViewById(id);
        b.setOnClickListener(v -> {
            calculator.push(digit);
            tvDisplay.setText(calculator.getExpression());
        });
    }

    private void wireOp(int id, String op) {
        Button b = findViewById(id);
        b.setOnClickListener(v -> {
            calculator.push(op);
            tvDisplay.setText(calculator.getExpression());
        });
    }
}
