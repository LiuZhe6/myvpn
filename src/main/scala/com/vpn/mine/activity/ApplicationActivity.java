package com.vpn.mine.activity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.vpn.mine.R;
import com.vpn.mine.entity.AppInfo;
import com.vpn.mine.utils.ApkTool;
import com.vpn.mine.utils.DataSaver;
import com.vpn.mine.utils.SuccinctProgress;

import java.util.List;
/**
 * Created by coder on 17-7-12.
 */
public class ApplicationActivity extends AppCompatActivity implements View.OnClickListener{

    //反选按钮
    private TextView reverseTexTView;

    //保存
    private TextView saveTextView;

    //总开关
    private Switch mainSwitch;

    //设置recyclerView
    private RecyclerView recyclerView;

    //应用列表
    private List<AppInfo> appInfoList;

    //适配器
    private ApplicationListRecyclerAdapter adapter;

    //SharedPreferences
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solo_app_config);

        initView();
        initTools();
    }

    /**
     * 初始化工具
     */
    private void initTools() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        reverseTexTView = (TextView) findViewById(R.id.reverse);
        saveTextView = (TextView) findViewById(R.id.save);
        mainSwitch = (Switch) findViewById(R.id.general_switch_button);
        recyclerView = (RecyclerView) findViewById(R.id.application_recycler_view);

        reverseTexTView.setOnClickListener(this);
        saveTextView.setOnClickListener(this);
        mainSwitch.setOnClickListener(this);
        //获取应用列表任务
        new GetApplicationsTask().execute();



        //设置recyclerView
        recyclerView = (RecyclerView) findViewById(R.id.application_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器 在task里面


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.save:
                int s = appInfoList.size();
                editor.putBoolean("data",true);
                for (int i = 0; i < s;i++){
                    AppInfo app = appInfoList.get(i);
                    editor.putBoolean(app.getAppName(),app.isSelect());
                }
                editor.apply();
                //将修改标记清除
                DataSaver.CHANGED = false;
                Toast.makeText(getApplicationContext(),"保存成功!",Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
                finish();
                break;
            case R.id.reverse:
                DataSaver.CHANGED = true;
                int openedCount = 0;
                int size = appInfoList.size();
                for (int i = 0;i<size;i++){
                    if (appInfoList.get(i).isSelect()){
                        appInfoList.get(i).setSelect(false);
                    } else {
                        appInfoList.get(i).setSelect(true);
                        openedCount = 1;
                    }
                }

                //主随客动
                if (openedCount == 1){
                    mainSwitch.setChecked(true);
                }else {
                    mainSwitch.setChecked(false);
                }

                adapter.notifyDataSetChanged();
                break;

            case R.id.general_switch_button:
//                Toast.makeText(getApplicationContext(),"开关",Toast.LENGTH_SHORT).show();
                DataSaver.CHANGED = true;
                int size2 = appInfoList.size();
                for (int i = 0;i<size2;i++){
                    if (mainSwitch.isChecked()){
                        appInfoList.get(i).setSelect(true);
                    } else {
                        appInfoList.get(i).setSelect(false);
                    }
                }
                adapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }


    /**
     * 获取应用列表任务
     */
    class GetApplicationsTask extends AsyncTask<Void, Void, List<AppInfo>> {

        List<AppInfo> a;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            SuccinctProgress.showSuccinctProgress(ApplicationActivity.this,"正在获取应用列表",SuccinctProgress.THEME_DOT,false,true);

        }

        @Override
        protected List<AppInfo> doInBackground(Void... voids) {
            //扫描得到APP列表
            a = ApkTool.scanLocalInstallAppList(ApplicationActivity.this.getPackageManager());

            return a;
        }

        @Override
        protected void onPostExecute(List<AppInfo> appInfos) {
            super.onPostExecute(appInfos);
            SuccinctProgress.dismiss();
            //待添加RecyclerView
            for (AppInfo appInfo : a){
                System.out.println(appInfo.getAppName()+"\n");
            }
            appInfoList = appInfos;
            adapter = new ApplicationListRecyclerAdapter(appInfoList);
            recyclerView.setAdapter(adapter);

        }
    }

    static class AppViewHolder extends RecyclerView.ViewHolder{

        LinearLayout linearLayout;

        ImageView appIcon;

        TextView appName;

        Switch switcher;

        public AppViewHolder(View view) {
            super(view);
            linearLayout = (LinearLayout) view.findViewById(R.id.list_app_linear);
            appIcon = (ImageView) view.findViewById(R.id.app_icon);
            appName = (TextView) view.findViewById(R.id.app_name);
            switcher = (Switch) view.findViewById(R.id.switch_button);
        }

    }

    private class ApplicationListRecyclerAdapter extends RecyclerView.Adapter<AppViewHolder>{

        private List<AppInfo> appInfoList;

        public ApplicationListRecyclerAdapter(List<AppInfo> appInfoList) {
            this.appInfoList =  appInfoList;
        }


        @Override
        public AppViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_app_info,parent,false);
            return new AppViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final AppViewHolder holder, int position) {
            AppInfo appInfo = appInfoList.get(position);

            //处理一波sharedpreference
            //如果data值不存在则返回false
            if (preferences.getBoolean("data",false)){
                //按照名称获取，默认值为true
                appInfo.setSelect(preferences.getBoolean(appInfo.getAppName(),true));
            }

            holder.appName.setText(appInfo.getAppName());
            holder.appIcon.setImageDrawable(appInfo.getImage());
            //设置switch状态 根据存储信息进行设置
            holder.switcher.setChecked(appInfo.isSelect());
            //设置switch 不可点击
            holder.switcher.setClickable(false);

            holder.linearLayout.setTag(position);
            holder.linearLayout.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    //设置修改标记
                    DataSaver.CHANGED = true;

                    int position = holder.getAdapterPosition();
                    if (holder.switcher.isChecked()){
                        holder.switcher.setChecked(false);
                        appInfoList.get(position).setSelect(false);
                    }else {
                        holder.switcher.setChecked(true);
                        appInfoList.get(position).setSelect(true);
                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return appInfoList.size();
        }

    }

    /**
     * 返回键 监听器
     */
    @Override
    public void onBackPressed() {

        //如果修改标记被修改 那么就弹出提示框
        if (DataSaver.CHANGED){
            Toast.makeText(getApplicationContext(),"请先保存修改！",Toast.LENGTH_SHORT).show();
            return;
        }

        finish();
    }
}

