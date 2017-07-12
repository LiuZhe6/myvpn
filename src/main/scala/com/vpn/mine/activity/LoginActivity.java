package com.vpn.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.vpn.mine.entity.User;
import com.vpn.mine.utils.DataSaver;
import com.vpn.mine.utils.SuccinctProgress;
import okhttp3.*;
import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import com.vpn.mine.R;
/**
 * Created by coder on 17-7-11.
 */
public class LoginActivity extends Activity {


    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private EditText accountEdit;
    private EditText passwordEdit;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //记住密码
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        accountEdit = (EditText) findViewById(R.id.account);
        passwordEdit = (EditText) findViewById(R.id.password);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        boolean isRemember = pref.getBoolean("remember_password", false);
        if (isRemember) {
            String account = pref.getString("account", "");
            String password = pref.getString("password", "");
            accountEdit.setText(account);
            passwordEdit.setText(password);
        }


        //跳转到注册界面
        Button register = (Button) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


        //登录
        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //判断是否同意
                if (checkBox.isChecked()) {
                    String account = accountEdit.getText().toString();
                    String password = passwordEdit.getText().toString();
                    editor = pref.edit();
                    editor.putBoolean("remember_password", true);
                    editor.putString("account", account);
                    editor.putString("password", password);
                    editor.apply();
                    //提交数据给服务器
                    new LoginTask().execute("http://47.52.6.38/Api/Auth/login", account, password);
//                    sendPostToLogin("http://47.52.6.38/Api/Auth/login", account, password);
                } else {
                    Toast.makeText(LoginActivity.this, "请先同意产品使用和服务条款", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendPostToLogin(String url, String email, String password) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }
    //异步消息处理
    class LoginTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            DataSaver.USER = new User();
            SuccinctProgress.showSuccinctProgress(LoginActivity.this, "正在获取", SuccinctProgress.THEME_DOT, false, true);
        }

        @Override
        protected String doInBackground(String... strings) {

            HttpURLConnection connection = null;
            StringBuffer response = null;
            try {
                URL url = new URL(strings[0]);
                String email = strings[1];
                String password = strings[2];
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes("email=" + email + "&password=" + password);
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
            //解析字符串
//        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
            parseJSONOWithJSONObject(s);
        }

        //解析过程
        private void parseJSONOWithJSONObject(String jsonData) {

            try {
                JSONObject jb = new JSONObject(jsonData);
                int err = Integer.parseInt(jb.getString("err"));
                if (err == 0) {
                    JSONObject jb2 = new JSONObject(jb.getString("data"));
                    DataSaver.USER.setUid(jb2.getString("uid"));
                    DataSaver.USER.setToken(jb2.getString("token"));
                    //登录成功
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else if (err == 2) {
                    String data = jb.getString("data");
                    //登录失败
                    Toast.makeText(LoginActivity.this, data, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
