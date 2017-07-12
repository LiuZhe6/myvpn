package com.vpn.mine.activity;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import com.vpn.mine.utils.ChildHolder;
import com.vpn.mine.utils.GroupHolder;
import com.vpn.mine.R;

import java.util.ArrayList;
/**
 * Created by coder on 17-7-12.
 */
public class HelpFragment extends Fragment {

    private View view;

    //help页面控件
    private ExpandableListView exListView;
    //分别存放控件中的group和child中的String
    private ArrayList<String> groupList;
    private ArrayList<String> childList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.help,container,false);

        initHelpPage();

        return view;
    }


    /**
     * 初始化help页面的布局
     */
    private void initHelpPage() {
        exListView = (ExpandableListView)view.findViewById(R.id.question_list);
        groupList = new ArrayList<String>();
        childList = new ArrayList<String>();

        groupList.add("如何免费使用?");
        groupList.add("我应该选择什么线路?");
        groupList.add("无法玩网络游戏?");
        groupList.add("套餐里的终端数是什么意思?");
        groupList.add("为什么不能下载GooglePlay的应用?");

        StringBuilder sb1 = new StringBuilder();
        sb1.append("- 注册时选择使用邮箱注册，可以获得更多的免费时间\n");
        sb1.append("- 每天签到可以随机获得4至24小时的免费使用时间\n");
        sb1.append("- 签到可以获得随机积分，积攒积分可以兑换VIP套餐\n");
        childList.add(sb1.toString());

        StringBuilder sb2 = new StringBuilder();
        sb2.append("- 系统默认会使用“自动选择”帮您分配\n");
        sb2.append("- 网页浏览可以首先考虑尝试亚洲线路\n");
        sb2.append("- 视频浏览可以尝试使用美国线路\n");
        sb2.append("- 如果访问网站对地区有太多要求，则选择同区域线路\n");
        sb2.append("- 如果需要访问CCTV、优酷、土豆等国内线路视频网站，请\n  选择国内线路\n");
        childList.add(sb2.toString());

        StringBuilder sb3 = new StringBuilder();
        sb3.append("  请按以下步骤处理:\n");
        sb3.append("- 请务必开启全局加速加速\n");
        sb3.append("- 请同事开启了分应用加速，请确保游戏处于加速名单中\n");
        childList.add(sb3.toString());

        StringBuilder sb4 = new StringBuilder();
        sb4.append("- 终端也可以理解为设备，比如手机、电脑、平板等\n");
        sb4.append("- 包年套餐可以两台终端同时连接使用，其他套餐则允许一\n  台终端，比如包年套餐可以" +
                "两人同时使用一个账号，只要\n  同时连接终端数不超过套餐规定的即可\n");
        sb4.append("- 如果需要定制终端数，请点击“联系客服”\n");
        childList.add(sb4.toString());

        StringBuilder sb5 = new StringBuilder();
        sb5.append("- 如果您启用了“分应用加速”，那么请确保在加速的名单\n  中，必须要有“下载管理Downloads”及“系统更新\n  Updater”，" +
                "因为GooglePlay给予这些系统组件进行下载\n");
        sb5.append("- 或者您可以在需要GooglePlay下载的时候，关闭“分应用加\n  速”，以保证顺利实现下载\n");
        childList.add(sb5.toString());

        exListView.setAdapter(new MyExpandableViewAdapter(getActivity()));
    }

    //帮助 问题列表适配器
    class MyExpandableViewAdapter extends BaseExpandableListAdapter{

        private Context context;

        public MyExpandableViewAdapter(Context context) {
            this.context = context;
        }

        /**
         * 获取父项的数量
         * @return
         */
        @Override
        public int getGroupCount() {
            return groupList.size();
        }

        /**
         * 获取某个父项的子向数目
         * @param groupPosition
         * @return 只有一项  字符串
         */
        @Override
        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        /**
         * 获得某个父项
         * @param parentPosition
         * @return
         */
        @Override
        public Object getGroup(int parentPosition) {
            return groupList.get(parentPosition);
        }

        /**
         * 获得某个父项的某个子项的id
         * @param groupPosition
         * @param childPosition
         * @return
         */
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return childList.get(groupPosition);
        }

        /**
         * 获取某个父项的id
         * @param parentPosition
         * @return
         */
        @Override
        public long getGroupId(int parentPosition) {
            return parentPosition;
        }
        /**
         * 获得某个父项的某个子项
         * @param parentPos
         * @param childPos
         * @return 返回的是 子序号 0
         */
        @Override
        public long getChildId(int parentPos, int childPos) {
            return 0;
        }

        /**
         * 按函数的名字来理解应该是是否具有稳定的id，这个方法目前一直都是返回false，没有去改动过
         * @return
         */
        @Override
        public boolean hasStableIds() {
            return true;
        }


        /**
         * 获得父项显示的view
         *
         * @param groupPosition
         * @param b
         * @param view
         * @param viewGroup
         * @return
         */
        @Override
        public View getGroupView(int groupPosition, boolean b, View view, ViewGroup viewGroup) {
            View newView = null;
            //自定义的类,用来存储控件的相关信息
            GroupHolder groupHolder = null;
            //这里的view起缓冲的作用

            if (view == null){
                newView = getActivity().getLayoutInflater().cloneInContext(context).inflate(R.layout.parent_item,null);
                groupHolder = new GroupHolder((TextView) newView.findViewById(R.id.parent_title));
                newView.setTag(groupHolder);
            } else {
                groupHolder = (GroupHolder) view.getTag();
                newView = view;
            }
            groupHolder.textView.setText(groupList.get(groupPosition).toString());
            //改变TextView的状态
            changeGroupTextView(groupHolder.textView);

            return newView;
        }


        /**
         * 获得子项显示的view
         *
         * @param groupPosition
         * @param childPosition
         * @param b
         * @param view
         * @param viewGroup
         * @return
         */
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {
            View newView = null;
            ChildHolder childHolder = null;
            if (view ==null){
                newView = getActivity().getLayoutInflater().cloneInContext(context).inflate(R.layout.child_item,null);
                childHolder = new ChildHolder((TextView) newView.findViewById(R.id.child_title));
                newView.setTag(childHolder);
            } else {
                newView = view;
                childHolder = (ChildHolder) view.getTag();
            }

            childHolder.textView.setText(childList.get(groupPosition).toString());
            //改变TextView的状态
            changeChildTextView(childHolder.textView);

            return newView;
        }


        /**
         * 子项是否可选中，如果需要设置子项的点击事件，需要返回true
         * @param i
         * @param i1
         * @return
         */
        @Override
        public boolean isChildSelectable(int i, int i1) {
            return false;
        }


        /**
         * 修改GroupTextView的样式
         * @return
         */
        public void changeGroupTextView(TextView t){
            //设置字体大小为14sp
            t.setTextSize(14);
            //设置TextView高度为
            t.setHeight(200);

            //设置字体和上下边的间隔
            t.setPadding(200,60,50,-1);
            //设置字体左对齐
            t.setGravity(Gravity.START);
        }

        /**
         * 修改ChildTextView的样式
         * @param t
         */
        public void changeChildTextView(TextView t){
            //设置字体大小
            t.setTextSize(13);
            //设置字体和上下边的间隔
            t.setPadding(70,10,10,-1);
            //设置字体左对齐
            t.setGravity(Gravity.START);
            //设置字体颜色
            t.setTextColor(Color.GRAY);
        }
    }


}