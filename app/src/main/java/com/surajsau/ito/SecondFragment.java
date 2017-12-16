package com.surajsau.ito;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SecondFragment extends Fragment {

    private static final String KEY_STR_VAL_1 = "frag_str_val_1";
    private static final String KEY_INT_VAL_2 = "frag_int_val_2";

    @BundleVar(KEY_STR_VAL_1)
    String strVal1;

    @BundleVar(KEY_INT_VAL_2)
    int intVal2;

    private TextView tvSample;

    public SecondFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        strVal1 = getArguments().getString(KEY_STR_VAL_1);
        intVal2 = getArguments().getInt(KEY_INT_VAL_2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvSample = view.findViewById(R.id.tvSample);

        tvSample.setText(strVal1 + "\n" + intVal2);
    }
}
