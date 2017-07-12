package com.vpn.mine.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.vpn.mine.R;
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
public class RegisterActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button back_link_button = (Button) findViewById(R.id.back_link_button);
        back_link_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button register_button = (Button) findViewById(R.id.register_button);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText userNameEdit = (EditText) findViewById(R.id.user_name);
                EditText userEmailEdit = (EditText) findViewById(R.id.user_email);
                EditText passwordEdit = (EditText) findViewById(R.id.password);

                String userName = userNameEdit.getText().toString();
                String userEmail = userEmailEdit.getText().toString();
                String password = passwordEdit.getText().toString();


                if (!isNotChinese(userNameEdit.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "用户名只能由数字、26个英文字母或者下划线组成,且只能由字母开头哦～", Toast.LENGTH_SHORT).show();
                    return;
                }

                //密码不合要求
                if (!isPasswordLegel(passwordEdit.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "密码不能全为数字、字母，由8-16位字母和数字组成", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isEmail(userEmail)) {
                    //提交数据给服务器
//                    Toast.makeText(RegisterActivity.this, "点击了", Toast.LENGTH_SHORT).show();
                    new RegisterTask().execute("http://47.52.6.38/Api/Auth/register", userName, userEmail, password);
                } else {
                    Toast.makeText(RegisterActivity.this, "邮箱格式不正确", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //异步消息处理
    class RegisterTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            SuccinctProgress.showSuccinctProgress(RegisterActivity.this, "正在注册", SuccinctProgress.THEME_DOT, false, true);

        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            StringBuffer response = null;
            try {
                URL url = new URL(strings[0]);
                String name = strings[1];
                String email = strings[2];
                String password = strings[3];
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes("username=" + name + "&email=" + email + "&password=" + password);
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
            //解析字符串
            SuccinctProgress.dismiss();
            parseJSONOWithJSONObject(s);

        }

        //解析过程
        private void parseJSONOWithJSONObject(String jsonData) {

            if (jsonData.equals("{err:-1}")) {
                Toast.makeText(RegisterActivity.this, "未知名的错误，请联系管理员", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONObject jb = new JSONObject(jsonData);
                    int err = Integer.parseInt(jb.getString("err"));
                    String data = jb.getString("data");
                    if (err == 0) {
                        //注册成功
                        Toast.makeText(RegisterActivity.this, data, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        //注册失败
                        Toast.makeText(RegisterActivity.this, data, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    //正则表达式
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean isNotChinese(String str) {

        Pattern p = Pattern.compile("^(?!_)(?!.*?_$)[a-zA-Z0-9_]+$");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isPasswordLegel(String password) {

        Pattern p = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$");
        Matcher m = p.matcher(password);
        if (m.find()) {
            return true;
        } else {
            return false;
        }
    }


}
