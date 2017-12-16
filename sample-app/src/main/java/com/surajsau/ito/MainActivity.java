package com.surajsau.ito;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnSecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSecond = findViewById(R.id.btnSecond);

        btnSecond.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSecond:
                String val1 = "Activity val1";
                int val2 = 100;
                startActivity(Ito.createSecondActivityIntent(this, val1, val2));
                break;
        }
    }
}
