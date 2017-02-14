package com.lyape.wxbottomtab;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lyape.bottomtabbar.BottomTabLayout;
import com.lyape.bottomtabbar.TabItemProvider;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private BottomTabLayout bottomTabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        bottomTabLayout = (BottomTabLayout) findViewById(R.id.bottom_tab_layout);

        TestFragmentAdapter adapter = new TestFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        bottomTabLayout.setUp(new TabItemProvider() {
            private String titles[] = {"消息","联系人","我"};
            private int icons[][] =
                    {{R.mipmap.tab_message_selected,R.mipmap.tab_message_unselected},
                            {R.mipmap.tab_contact_selected,R.mipmap.tab_contact_unselected},
                            {R.mipmap.tab_me_selected,R.mipmap.tab_me_unselected}};
            @Override
            public int getCount() {
                return titles.length;
            }

            @Override
            public View getItem(int position, ViewGroup container) {
                return LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_mainbottom_tab,container,false);
            }

            @Override
            public String getTitle(int position) {
                return titles[position];
            }

            @Override
            public int getSelectedIcon(int position) {
                return icons[position][0];
            }

            @Override
            public int getUnSelectedIcon(int position) {
                return icons[position][1];
            }

            @Override
            public int getSelectedColor() {
                return R.color.tabItemSelect;
            }

            @Override
            public int getUnSelectedColor() {
                return R.color.tabItemUnSelect;
            }
        },viewPager);

    }
}
