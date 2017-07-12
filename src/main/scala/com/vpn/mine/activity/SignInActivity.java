package com.vpn.mine.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vpn.mine.R;
/**
 * Created by coder on 17-7-12.
 */
public class SignInActivity extends AppCompatActivity  implements View.OnClickListener{

    private ImageView cancelImageButton;
    private TextView shareButton;
    private TextView cancelButton;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_dialog);

        cancelImageButton = (ImageView) findViewById(R.id.close_btn);
        shareButton = (TextView) findViewById(R.id.share_button);
        cancelButton = (TextView) findViewById(R.id.cancel_button);
        textView = (TextView) findViewById(R.id.share_task_succeed_tip);

        String str = getIntent().getStringExtra("info");
        textView.setText(str);

        cancelImageButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        shareButton.setOnClickListener(this);
    }


    /**
     * 点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.close_btn:
                finish();
                break;
            case R.id.cancel_button:
                finish();
                break;
            case R.id.share_button:
                Toast.makeText(this,"点击了 兑积分 ",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
