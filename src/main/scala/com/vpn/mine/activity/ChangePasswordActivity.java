package com.vpn.mine.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vpn.mine.activity.OptionFragment;
import com.vpn.mine.R;
import com.vpn.mine.entity.User;
import com.vpn.mine.utils.DataSaver;
import com.vpn.mine.utils.SuccinctProgress;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Created by coder on 17-7-12.
 */
public class ChangePasswordActivity extends AppCompatActivity {

    private TextView userName;

    private EditText newPassword;

    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        //设置用户名
        Intent intent = getIntent();
        String user_name = intent.getStringExtra("user_name");

        userName = (TextView)findViewById(R.id.user_name);
        userName.setText(user_name);

        //保存并退出
        save = (Button)findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断密码是否符合格式
                newPassword = (EditText)findViewById(R.id.new_password);
                if ( !isPasswordLegel(newPassword.getText().toString())){
                    Toast.makeText(ChangePasswordActivity.this, "密码格式错误!\n密码不能全为数字、字母，由8-16位字母和数字组成!\n请重新输入密码!", Toast.LENGTH_SHORT).show();
                }else {
                    new ChangePasswordTask().execute("http://47.52.6.38/Api/User/reset_password",DataSaver.USER.getUid(), DataSaver.USER.getToken(),newPassword.getText().toString());
                }
            }
        });

    }

    //异步处理修改密码
    class ChangePasswordTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            SuccinctProgress.showSuccinctProgress(ChangePasswordActivity.this, "", SuccinctProgress.THEME_DOT, false, true);
        }

        @Override
        protected String doInBackground(String... strings) {


            HttpURLConnection connection = null;
            StringBuffer response = null;
            try {
                URL url = new URL(strings[0]);
                String uid = strings[1];
                String token = strings[2];
                String new_password = strings[3];
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes("uid=" + uid + "&token=" + token + "&new_password=" + new_password);
                connection.setConnectTimeout(8000);
                connection.setReadTimeout(8000);
                connection.connect();

                int code = connection.getResponseCode();
                if (code == 200) {
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    response = new StringBuffer();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    return response.toString();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                connection.disconnect();
            }
            return "{err:-1}";
        }

        @Override
        protected void onPostExecute(String s) {
            SuccinctProgress.dismiss();
            Toast.makeText(ChangePasswordActivity.this, s, Toast.LENGTH_SHORT).show();
            //解析字符串
//        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
            parseJSONOWithJSONObject(s);
        }

        //解析过程
        private void parseJSONOWithJSONObject(String jsonData) {

            try {
                JSONObject jb = new JSONObject(jsonData);
                int err = Integer.parseInt(jb.getString("err"));
                String data = jb.getString("data");
                if (err == 0) {
                    //修改密码成功
                    Toast.makeText(ChangePasswordActivity.this, data, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    //修改密码失败
                    Toast.makeText(ChangePasswordActivity.this, data, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    public static boolean isPasswordLegel(String password){

        Pattern p = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$");
        Matcher m = p.matcher(password);
        if (m.find()) {
            return true;
        }else {
            return false;
        }
    }
}
