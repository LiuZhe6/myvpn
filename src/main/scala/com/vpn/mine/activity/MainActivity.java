package com.vpn.mine.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.vpn.mine.activity.ConnectFragment;
import com.vpn.mine.activity.HelpFragment;
import com.vpn.mine.activity.MainFragmentPagerAdapter;
import com.vpn.mine.activity.OptionFragment;
import com.vpn.mine.activity.PurchaseFragment;
import com.vpn.mine.R;

import java.util.ArrayList;

/**
 * Created by coder on 17-7-12.
 */
public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{

    //    四个布局
    private View connectView ;
    private View purchaseView;
    private View helpView;
    private View optionView;

    private ViewPager viewPager ;
    private ArrayList<Fragment> fragmentList;

    //  四个标签 和 TabLayout
    private TabLayout tabLayout ;
    private TabLayout.Tab connectTab;
    private TabLayout.Tab purchaseTab;
    private TabLayout.Tab helpTab;
    private TabLayout.Tab optionTab;

    private ConnectFragment connectFragment;
    private PurchaseFragment purchaseFragment;
    private HelpFragment helpFragment;
    private OptionFragment optionFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);

        //创建控件和点击事件
        initView();
        initEvents();
    }

    private void initEvents() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab == tabLayout.getTabAt(0)){
                    viewPager.setCurrentItem(0);
                } else if (tab == tabLayout.getTabAt(1)){
                    viewPager.setCurrentItem(1);
                } else if (tab == tabLayout.getTabAt(2)){
                    viewPager.setCurrentItem(2);
                } else if (tab == tabLayout.getTabAt(3)){
                    viewPager.setCurrentItem(3);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initView() {
        connectFragment  = new ConnectFragment();
        purchaseFragment = new PurchaseFragment();
        helpFragment = new HelpFragment();
        optionFragment = new OptionFragment();
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(connectFragment);
        fragmentList.add(purchaseFragment);
        fragmentList.add(helpFragment);
        fragmentList.add(optionFragment);

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        viewPager.setAdapter(new MainFragmentPagerAdapter(fragmentManager,fragmentList));
        //默认显示连接页面
        viewPager.setCurrentItem(0);

        //设置tabLayout与viewPager级联
        tabLayout.setupWithViewPager(viewPager);

        //实例化Tab
        connectTab = tabLayout.getTabAt(0);
        purchaseTab = tabLayout.getTabAt(1);
        helpTab = tabLayout.getTabAt(2);
        optionTab = tabLayout.getTabAt(3);

        //重新设置标签名称，上面会清除全部标签
        connectTab.setText("连接");
        purchaseTab.setText("购买");
        helpTab.setText("帮助");
        optionTab.setText("选项");
    }

//  以下三个方法是Tab事件

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    class NewPagerAdapter extends PagerAdapter{

        private ArrayList<View> viewList;

        public NewPagerAdapter(ArrayList<View> viewList) {
            this.viewList = viewList;
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position));
            return viewList.get(position);
        }
    }

    class MyOnClickListener implements View.OnClickListener {

        private int index;

        public MyOnClickListener(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View view) {
            viewPager.setCurrentItem(index);
        }
    }
}
