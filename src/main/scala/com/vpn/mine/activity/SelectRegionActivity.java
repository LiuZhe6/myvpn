package com.vpn.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.VpnService;
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
import com.vpn.mine.entity.Node;
import com.vpn.mine.entity.User;
import com.vpn.mine.utils.DataSaver;

import java.util.ArrayList;

/**
 * Created by coder on 17-7-12.
 */
public class SelectRegionActivity extends MainActivity {

    //用户信息
    private User user;
    //节点列表
    private ArrayList<Node> nodeList;

    //SharedPreference
    private SharedPreferences preferences;

    private SharedPreferences.Editor editor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region_select);

        //得到fragment传入的bundle
        Bundle bundle = getIntent().getExtras().getBundle("bundle");
        String uid = bundle.getString("uid");
        String token = bundle.getString("token");
        user = new User(uid, token);
        nodeList = (ArrayList<Node>) bundle.getSerializable("nodeList");
        //添加第一个 “自动选择”
        Node node = new Node();
        node.setNodeName("自动选择");
        node.setNid(0);
        node.setArea("auto");
        nodeList.add(0, node);
        //将nodeList存入DataSaver
        DataSaver.NODES = new ArrayList<>(nodeList);
        System.out.println("测试一波，服务器名字为:"+DataSaver.NODES.get(1).getNodeName());

        preferences = PreferenceManager.getDefaultSharedPreferences(SelectRegionActivity.this);
        editor = preferences.edit();


        //设置RecycleView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.region_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        NodeRecyclerAdapter nodeRecyclerAdapter = new NodeRecyclerAdapter(nodeList);
        recyclerView.setAdapter(nodeRecyclerAdapter);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        TextView textView;

        RadioButton radioButton;

        LinearLayout layout;


        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.region_icon);
            textView = (TextView) view.findViewById(R.id.region_name);
            radioButton = (RadioButton) view.findViewById(R.id.checkbox_button);
            layout = (LinearLayout) view.findViewById(R.id.node_list_layout);
        }
    }

    class NodeRecyclerAdapter extends RecyclerView.Adapter<ViewHolder> implements View.OnClickListener {

        private ArrayList<Node> nodeArrayList;


        public NodeRecyclerAdapter(ArrayList<Node> nodeList) {
            nodeArrayList = nodeList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_region_info, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Node node = nodeArrayList.get(position);
            System.out.println(node.getNodeName());
            holder.textView.setText(node.getNodeName());
            String drawableName = "region_" + node.getArea().toLowerCase();
            System.out.println("drawableName : " + drawableName);
            int picId = getResources().getIdentifier(drawableName, "drawable", getPackageName());
            holder.imageView.setImageResource(picId);

            if (DataSaver.NODE_INDEX == position) {
                holder.radioButton.setChecked(true);
            } else {
                holder.radioButton.setChecked(false);
            }
            //设置radiobutton不可点击
            holder.radioButton.setClickable(false);

            holder.layout.setTag(position);
            holder.layout.setOnClickListener(this);
        }

        @Override
        public int getItemCount() {
            return nodeArrayList.size();
        }


        /**
         * radioButton点击事件
         *
         * @param view
         */
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.node_list_layout:
                    int pos = (int) view.getTag();
                    DataSaver.NODE_INDEX = pos;
                    notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "点击了" + nodeArrayList.get(DataSaver.NODE_INDEX).getNodeName(),
                            Toast.LENGTH_SHORT).show();
                    DataSaver.NODE_NAME = nodeArrayList.get(DataSaver.NODE_INDEX).getNodeName();
                    //写入设置到preference
                    editor.putString("node_name", DataSaver.NODE_NAME);
                    editor.apply();

                    //待添加当前VPN连接状态的判断
                    //准备连接vpn
                    prepareStartService();


                    //如果成功启动后返回选择的序号
                    Intent intent = new Intent();
                    intent.putExtra("select_region_name", DataSaver.NODE_NAME);
                    String drawableName = "region_" + nodeArrayList.get(DataSaver.NODE_INDEX).getArea().toLowerCase();
                    int picId = getResources().getIdentifier(drawableName, "drawable", getPackageName());
                    intent.putExtra("select_region_icon_id", picId);
                    setResult(RESULT_OK, intent);
                    //销毁活动
                    finish();
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
//            System.out.println("准备进入serviceLoad");
            serviceLoad();
        } else {
            //待添加
//            System.out.println("没有执行serviceLoad");
        }
    }


}

