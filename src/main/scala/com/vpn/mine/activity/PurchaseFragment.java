package com.vpn.mine.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.vpn.mine.R;
/**
 * Created by coder on 17-7-12.
 */
public class PurchaseFragment extends Fragment {


    public PurchaseFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.purchase,container,false);
        return v;
    }

}
