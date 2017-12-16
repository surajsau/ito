package com.surajsau.ito;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    private static final String KEY_STR_VAL_1 = "str_val_1";
    private static final String KEY_INT_VAL_2 = "int_val_2";

    @IntentVar(KEY_STR_VAL_1)
    String val1;

    @IntentVar(KEY_INT_VAL_2)
    int val2;

    TextView tvSample;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        val1 = getIntent().getStringExtra(KEY_STR_VAL_1);
        val2 = getIntent().getIntExtra(KEY_INT_VAL_2, 0);

        tvSample = findViewById(R.id.tvSample);

        tvSample.setText(val1 + "\n" + val2);

        SecondFragment secondFragment = Ito.createSecondFragmentInstance(val1 + " Fragment", val2 + 100);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, secondFragment)
                .commit();
    }
}
