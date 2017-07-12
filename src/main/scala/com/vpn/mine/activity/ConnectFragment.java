package com.vpn.mine.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vpn.mine.activity.SelectRegionActivity;
import com.vpn.mine.entity.HttpResult;
import com.vpn.mine.entity.Node;
import com.vpn.mine.entity.User;
import com.vpn.mine.utils.DataSaver;
import com.vpn.mine.utils.SuccinctProgress;
import org.json.JSONException;
import org.json.JSONObject;
import com.vpn.mine.R;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by coder on 17-7-12.
 */
public class ConnectFragment extends Fragment implements View.OnClickListener {

    private View view;

    //获取节点区域
    private LinearLayout regionSelectLinearLayout;

    //节点名称TextView
    private TextView regionSelectTextView;

    //节点图标
    private ImageView regionIcon;

    //智能分流布局
    private LinearLayout diffluenceLayout;

    //智能分流的状态
    private TextView diffluenceStatusTextView;

    //智能分流的开关
    private Switch diffluenceSwitch;

    //账号有效期布局
    private LinearLayout userInfoLayout;

    //账号有效期文本框
    private TextView userInfoTextView;

    //签到
    private LinearLayout signInLayout;

    //签到状态
    private TextView signInStatusTextView;

    //分应用加速
    private LinearLayout applicationsLayout;

    //分应用加速开关
    private Switch applicationSwitch;

    //分应用加速状态
    private TextView applicationSpeedStatus;

    private ProgressBar progressBar;

    //SharedPreference
    private SharedPreferences preferences;

    private SharedPreferences.Editor editor;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.connect,container,false);

        //初始化
        initView();
        initData();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1://选择国家节点返回的
                if (resultCode == getActivity().RESULT_OK){
                    String regionName = data.getStringExtra("select_region_name");
                    int regionIconId = data.getIntExtra("select_region_icon_id",-1);
                    if (regionIconId != -1){
                        regionSelectTextView.setText(regionName);
                        regionIcon.setImageResource(regionIconId);
                    }

                }
                break;
        }
    }

    private void initData() {

        //获取用户信息到期时间
        new GetExpirationTimeTask().execute("http://47.52.6.38/Api/User/getUserInfo",DataSaver.USER.getUid(), DataSaver.USER.getToken());

        //获取签到状态
        new GetSignInStatusTask().execute("http://47.52.6.38/Api/User/checkinStatus",DataSaver.USER.getUid(),DataSaver.USER.getToken());
    }

    private void initView() {

        regionSelectLinearLayout = (LinearLayout) view.findViewById(R.id.region_select_button);
        regionSelectLinearLayout.setOnClickListener(this);
        regionSelectTextView = (TextView) view.findViewById(R.id.current_region_tv);
        regionIcon = (ImageView) view.findViewById(R.id.region_flag);
        diffluenceLayout = (LinearLayout) view.findViewById(R.id.all_spec_mode_layout);
        diffluenceStatusTextView = (TextView) view.findViewById(R.id.accelerate_mode_tv);
        diffluenceSwitch = (Switch) view.findViewById(R.id.switch_all_accelerate);
        diffluenceSwitch.setOnClickListener(this);
        userInfoLayout = (LinearLayout) view.findViewById(R.id.user_info_layout);
        userInfoLayout.setOnClickListener(this);
        userInfoTextView = (TextView) view.findViewById(R.id.user_expire_date_tv);
        signInLayout = (LinearLayout) view.findViewById(R.id.sign_in);
        signInLayout.setOnClickListener(this);
        signInStatusTextView = (TextView) view.findViewById(R.id.check_in_left_time_tv);
        applicationsLayout = (LinearLayout) view.findViewById(R.id.application_layout);
        applicationsLayout.setOnClickListener(this);
        applicationSwitch = (Switch) view.findViewById(R.id.switch_solo_app_config);
        applicationSwitch.setOnClickListener(this);
        applicationSpeedStatus = (TextView) view.findViewById(R.id.solo_app_config_status);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = preferences.edit();

        //取sharedPreference中的值
        diffluenceSwitch.setChecked(preferences.getBoolean("diffluence_switch",true));
        applicationSwitch.setChecked(preferences.getBoolean("applications_speed_switch",false));
        changeDiffluenceStatusTextView();
        changeApplicationSpeedStatusTextView();

    }

    /**
     * LinearLayout点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.region_select_button:
                new GetNodeListFromServerTask().execute("http://47.52.6.38/Api/User/getNodeList", DataSaver.USER.getUid(), DataSaver.USER.getToken());
                break;
            case R.id.switch_all_accelerate://智能分流
                //点击的时候，实际已经取反
                diffluenceSwitch.setChecked(diffluenceSwitch.isChecked());
                changeDiffluenceStatusTextView();
                editor.putBoolean("diffluence_switch",diffluenceSwitch.isChecked());
                break;
            case R.id.user_info_layout:
                new GetUserInfoTask().execute("http://47.52.6.38/Api/User/getUserInfo",DataSaver.USER.getUid(), DataSaver.USER.getToken());
                break;
            case R.id.sign_in:
                new SignInTask().execute("http://47.52.6.38/Api/User/checkin",DataSaver.USER.getUid(), DataSaver.USER.getToken());
                break;
            case R.id.application_layout:
                //点击了分应用加速的Layout后，先判断switch状态
                if (applicationSwitch.isChecked()){
                    Intent intent = new Intent(getActivity(),ApplicationActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(getContext(),"请先打开加速开关",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.switch_solo_app_config:
                editor.putBoolean("applications_speed_switch",applicationSwitch.isChecked());
                editor.apply();

                changeApplicationSpeedStatusTextView();
                break;
            default:
                break;
        }
    }

    /**
     * 获取用户账户到期时间任务
     */
    class GetExpirationTimeTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            return sendPostRequest(strings[0],strings[1],strings[2]);
        }

        @Override
        protected void onPostExecute(String s) {
            User user = new User();
            try {
                JSONObject jb = new JSONObject(s);
                int  err = Integer.parseInt(jb.getString("err"));
                //没有错误则继续解析
                if (err == 0){
                    String data = jb.getString("data");
                    JSONObject jsonOb = new JSONObject(data);

                    user.setExpiration(jsonOb.getString("expiration"));
                    userInfoTextView.setText(user.getExpiration());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 获取签到状态任务
     */
    class GetSignInStatusTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            return sendPostRequest(strings[0],strings[1],strings[2]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jb = new JSONObject(s);
                int code = jb.getInt("err");
                String data = jb.getString("data");
                signInStatusTextView.setText(data);

                //若没有领取，则改变字体颜色
                if (code == 0){
                    signInStatusTextView.setTextColor(0xffff4081);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取节点列表的任务
     */
    class GetNodeListFromServerTask extends AsyncTask<String,Integer,String>{


        private User user;


        @Override
        protected void onPreExecute() {
            SuccinctProgress.showSuccinctProgress(getActivity(),"正在获取",SuccinctProgress.THEME_DOT,false,true);
        }

        /**
         *
         * @param strings url,uid,token
         * @return
         */
        @Override
        protected String doInBackground(String... strings) {
            user = new User();
            user.setUid(strings[1]);
            user.setToken(strings[2]);

            return sendPostRequest(strings[0],strings[1],strings[2]);
        }

        @Override
        protected void onPostExecute(String s) {
            SuccinctProgress.dismiss();

            ArrayList<Node> nodeList = null;
            try {
                JSONObject jb = new JSONObject(s);
                int resultCode = Integer.parseInt(jb.getString("err"));
                //正确
                if (resultCode == 0){
                    String data = jb.getString("data");
                    System.out.println("err" + resultCode);
                    System.out.println(data);
                    Gson gson = new Gson();
                    nodeList = gson.fromJson(data,new TypeToken<ArrayList<Node>>()
                    {}.getType());
                } else {

                    Toast.makeText(getContext(),"网络连接超时",Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //传入ArrayList
            Intent intent = new Intent(getActivity(),SelectRegionActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("uid",user.getUid());
            bundle.putString("token",user.getToken());
            bundle.putSerializable("nodeList",nodeList);
            intent.putExtra("bundle",bundle);
            startActivityForResult(intent,1);

        }
    }

    /**
     * 获取用户信息的任务
     */
    class GetUserInfoTask extends AsyncTask<String,Void,String>{

        private User user;

        @Override
        protected void onPreExecute() {
            user = new User();
            SuccinctProgress.showSuccinctProgress(getActivity(),"正在获取",SuccinctProgress.THEME_DOT,false,true);
        }

        /**
         *
         * @param strings url,uid,token
         * @return
         */
        @Override
        protected String doInBackground(String... strings) {
            //设置uid 和 token 到一个全局对象中
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
                    user.setUsername(jsonOb.getString("username"));
                    user.setEmail(jsonOb.getString("email"));
                    user.setConnections(jsonOb.getString("connections"));
                    user.setExpiration(jsonOb.getString("expiration"));
                    user.setLevel(jsonOb.getString("level"));
                    userInfoTextView.setText(user.getExpiration());
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
    }


    /**
     * 签到任务
     */
    class SignInTask extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            SuccinctProgress.showSuccinctProgress(getActivity(),"正在获取",SuccinctProgress.THEME_DOT,false,true);
        }

        @Override
        protected String doInBackground(String... strings) {
            return sendPostRequest(strings[0],strings[1],strings[2]);
        }

        @Override
        protected void onPostExecute(String s) {
            SuccinctProgress.dismiss();
            HttpResult httpResult = new HttpResult();
            try {
                JSONObject jb = new JSONObject(s);
                int err = Integer.parseInt(jb.getString("err"));
                /*httpResult.setStatus(jb.getString("status"));*/
                if (err == 0){
                    //签到成功则，获取数据 并弹出对话框
                    httpResult.setData(jb.getString("data"));
                   /* Intent intent = new Intent(getActivity(),SignInActivity.class);
                    intent.putExtra("info",httpResult.getInfo());
                    startActivity(intent);*/
                    signInStatusTextView.setText("今日已领取");
                }else if (err == -1){
                    String  data = jb.getString("data");
                    Toast.makeText(getActivity(),data,Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(),"您已进入没有网络的异次元时代",Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            StringBuilder sb = new StringBuilder();
            sb.append("status:" + httpResult.getStatus() + "\n");
            sb.append("data:" + httpResult.getInfo() + "\n");


//            Toast.makeText(getContext(),sb.toString(),Toast.LENGTH_SHORT).show();

        }
    }

    /**
     *
     * @param url http地址
     * @param uid   用户的uid
     * @param token token令牌
     * @return http返回结果
     */
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

    /**
     * 根据diffluenceSwitch状态修改TextView的文字
     */
    public void changeDiffluenceStatusTextView(){
        if (diffluenceSwitch.isChecked()){
            diffluenceStatusTextView.setText("已开启 (推荐)");
        } else {
            diffluenceStatusTextView.setText("已关闭 (全局模式)");
        }
    }

    /**
     * 根据applicationSwitch的状态来修改TextView的文字
     */
    public void changeApplicationSpeedStatusTextView(){
        if (applicationSwitch.isChecked()){
            applicationSpeedStatus.setText("已打开");
        } else {
            applicationSpeedStatus.setText("已关闭");
        }
    }

}
