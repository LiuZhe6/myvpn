package com.vpn.mine.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.vpn.mine.R;
/**
 * Created by coder on 17-7-12.
 */
public class AccountInfoActivity extends AppCompatActivity {

    private String user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);

        //得到用户信息并显示
        Intent intent = getIntent();
        user_name = intent.getStringExtra("user_name");
        String user_email = intent.getStringExtra("user_email");
        String user_account_type = intent.getStringExtra("user_account_type");
        String expire_date_title_free = intent.getStringExtra("expire_date_title_free");

        TextView user_name_id = (TextView)findViewById(R.id.user_name);
        TextView user_email_id = (TextView)findViewById(R.id.user_email);
        TextView user_account_type_id = (TextView)findViewById(R.id.user_account_type);
        TextView expire_date_title_free_id = (TextView)findViewById(R.id.expire_date_title_free);

        user_name_id.setText(user_name);
        user_email_id.setText(user_email);
        user_account_type_id.setText(user_account_type);
        expire_date_title_free_id.setText(expire_date_title_free);

        //菜单点击事件
        final Button menu = (Button)findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(menu);
            }
        });

    }

    //menu菜单
    private void showPopupMenu(View view) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(this, view);
        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.main, popupMenu.getMenu());
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    //修改密码
                    case R.id.change_passwd:
                        Intent intent = new Intent(AccountInfoActivity.this,ChangePasswordActivity.class);
                        intent.putExtra("user_name",user_name);
                        startActivity(intent);
                        break;
                    //设备下线
                    case R.id.offline_device:
                        Toast.makeText(AccountInfoActivity.this, "还没实现", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        // PopupMenu关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {

            }
        });
        popupMenu.show();
    }

}

