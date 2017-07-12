package com.vpn.mine.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.vpn.mine.activity.AboutActivity;
import com.vpn.mine.activity.AccountInfoActivity;
import com.vpn.mine.activity.LoginActivity;
import com.vpn.mine.entity.User;
import com.vpn.mine.utils.DataSaver;
import com.vpn.mine.utils.SuccinctProgress;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import com.vpn.mine.R;
/**
 * Created by coder on 17-7-12.
 */
public class OptionFragment extends Fragment  implements View.OnClickListener{

    private View view;

    private LinearLayout AccountInfoemationLayout;

    private LinearLayout VisitWebLayout;

    private LinearLayout aboutLayout;

    private LinearLayout to_shareLayout;

    private LinearLayout logoutLayout;



    /*public OptionFragment() {

    }*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.option,container,false);
        initView();
        return view;
    }

    private void initView() {
        AccountInfoemationLayout = view.findViewById(R.id.AccountInfoemation);
        AccountInfoemationLayout.setOnClickListener(this);
        VisitWebLayout = view.findViewById(R.id.VisitWeb);
        VisitWebLayout.setOnClickListener(this);
        aboutLayout = view.findViewById(R.id.about);
        aboutLayout.setOnClickListener(this);
        to_shareLayout = view.findViewById(R.id.to_share);
        to_shareLayout.setOnClickListener(this);
        logoutLayout = view.findViewById(R.id.logout);
        logoutLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.AccountInfoemation:
                new GetAccountInfoFromServerTask().execute("http://47.52.6.38/Api/User/getUserInfo", DataSaver.USER.getUid(), DataSaver.USER.getToken());
                break;
            case R.id.VisitWeb:
                Intent intentn = new Intent(Intent.ACTION_VIEW);
                intentn.setData(Uri.parse("http://47.52.6.38/"));
                startActivity(intentn);
                break;
            case R.id.about:
                Intent intent = new Intent(getContext(),AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.to_share:
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                //分享内容_官网网址
                shareIntent.putExtra(Intent.EXTRA_TEXT, "http://47.52.6.38/");
                shareIntent.setType("text/plain");

                //设置分享列表的标题，并且每次都显示分享列表
                startActivity(Intent.createChooser(shareIntent, "分享到"));
                break;
            case R.id.logout:
                new LogOutFromServerTask().execute("http://47.52.6.38/Api/User/logout",DataSaver.USER.getUid(), DataSaver.USER.getToken());
                break;
            default:
                break;
        }

    }

    //账户信息_异步消息处理
    class GetAccountInfoFromServerTask extends AsyncTask<String,Integer,String> {

        private User user;

        @Override
        protected void onPreExecute() {
            user = new User();
            SuccinctProgress.showSuccinctProgress(getActivity(),"正在获取",SuccinctProgress.THEME_DOT,false,true);
        }

        @Override
        protected String doInBackground(String... strings) {
            user.setUid(strings[1]);
            user.setToken(strings[2]);
            return sendPostRequest(strings[0],strings[1],strings[2]);
        }

        @Override
        protected void onPostExecute(String s) {
            SuccinctProgress.dismiss();

            try {
                JSONObject jb = new JSONObject(s);
                int  err = Integer.parseInt(jb.getString("err"));
                //没有错误则继续解析
                if (err == 0){
                    String data = jb.getString("data");
                    JSONObject jsonOb = new JSONObject(data);
                    String user_name = jsonOb.getString("username");
                    String user_email = jsonOb.getString("email");
                    String user_account_type = jsonOb.getString("level");
                    String expire_date_title_free = jsonOb.getString("expiration");
                    /*userInfoTextView.setText(user.getExpiration());*/
                    //跳转到账户信息界面
                    Intent intent = new Intent(getContext(),AccountInfoActivity.class);
                    intent.putExtra("user_name",user_name);
                    intent.putExtra("user_email",user_email);
                    intent.putExtra("user_account_type",user_account_type);
                    intent.putExtra("expire_date_title_free",expire_date_title_free);
                    startActivity(intent);
                }else {
                    Toast.makeText(getContext(), "获取账户信息失败！", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            StringBuilder sb = new StringBuilder();
            sb.append("uid:" + user.getUid() + "\n");
            sb.append("token:" + user.getToken() + "\n");
            sb.append("username:" + user.getUsername() + "\n");
            sb.append("email:" + user.getEmail() + "\n");
            sb.append("connections:" + user.getConnections() + "\n");
            sb.append("expiration:" + user.getExpiration() + "\n");
            sb.append("level:" + user.getLevel() + "\n");

            Toast.makeText(getContext(),sb.toString(),Toast.LENGTH_SHORT).show();
        }


        /*private void parseJSONOWithJSONObject(String jsonData){

            try {
                JSONObject jb = new JSONObject(jsonData);
                int err = Integer.parseInt(jb.getString("err"));
                Toast.makeText(MainActivity.this, "err="+err, Toast.LENGTH_SHORT).show();
                String data = jb.getString("data");
                if (err==0){
                    //获取信息成功
                    Toast.makeText(MainActivity.this, "获取信息成功", Toast.LENGTH_SHORT).show();
                    JSONObject datajb = new JSONObject(data);
                    //账户信息
                    String user_name = datajb.getString("username");
                    String user_email = datajb.getString("email");
                    String user_account_type = datajb.getString("level");
                    String expire_date_title_free = datajb.getString("expiration");
                    //跳转到账户信息界面
                    Intent intent = new Intent(MainActivity.this,AccountInfoActivity.class);
                    intent.putExtra("user_name",user_name);
                    intent.putExtra("user_email",user_email);
                    intent.putExtra("user_account_type",user_account_type);
                    intent.putExtra("expire_date_title_free",expire_date_title_free);
                    startActivity(intent);
                }else {
                    //获取信息失败
                    Toast.makeText(MainActivity.this, "获取信息失败", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }*/

    }

    //退出_异步消息处理
    class LogOutFromServerTask extends AsyncTask<String,Integer,String>{

        private User user;

        @Override
        protected void onPreExecute() {
            user = new User();
            SuccinctProgress.showSuccinctProgress(getActivity(),"",SuccinctProgress.THEME_DOT,false,true);
        }

        @Override
        protected String doInBackground(String... strings) {
            user.setUid(strings[1]);
            user.setToken(strings[2]);
            return sendPostRequest(strings[0],strings[1],strings[2]);
        }

        @Override
        protected void onPostExecute(String s) {
            SuccinctProgress.dismiss();

            try {
                JSONObject jb = new JSONObject(s);
                int  err = Integer.parseInt(jb.getString("err"));
                //没有错误则退出
                if (err == 0){
                    onDestroy();
                    onDetach();
                    Intent intent = new Intent(getContext(),LoginActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(getContext(), "异常错误！", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }


    public String sendPostRequest(String url,String uid, String token){
        HttpURLConnection connection = null;
        StringBuffer response = null;
        try {
            URL u = new URL(url);
            connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("POST");
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes("uid="+uid+"&token="+token);
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.connect();

            int code = connection.getResponseCode();
            if (code == 200){
                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                response = new StringBuffer();
                String line ;
                while ((line = reader.readLine())!=null){
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

}

