package com.samluys.tab;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.samluys.tablib.OnTabSelectListener;
import com.samluys.tablib.QFBottomTabLayout;
import com.samluys.tablib.QFTabEntity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String[] mTitles = {"首页", "社区", "本地圈", "消息", "发现"};
    private int[] mIconUnselectIds = {
            R.mipmap.tab1, R.mipmap.tab2,
            R.mipmap.tab3, R.mipmap.tab4, R.mipmap.tab5};
    private int[] mIconSelectIds = {
            R.mipmap.tab1_selected, R.mipmap.tab2_selected,
            R.mipmap.tab3_selected, R.mipmap.tab4_selected, R.mipmap.tab5_selected};

    private int[] mCoverImageIds = {
            0, R.mipmap.icon_cover,
            0, R.mipmap.icon_cover1, 0};

    private ArrayList<QFTabEntity> mTabEntities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        QFBottomTabLayout mTabLayout_1 = findViewById(R.id.tl_1);
        QFBottomTabLayout mTanLayout_2 = findViewById(R.id.tl_2);
        QFBottomTabLayout mTanLayout_3 = findViewById(R.id.tl_3);
        QFBottomTabLayout mTanLayout_4 = findViewById(R.id.tl_4);
        QFBottomTabLayout mTanLayout_5 = findViewById(R.id.tl_5);
        QFBottomTabLayout mTanLayout_6 = findViewById(R.id.tl_6);

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }

        // 默认5栏
        mTabLayout_1.setTabData(mTabEntities);

        // 默认5栏 + 切换显示发布入口
        ArrayList<QFTabEntity> mTabEntities2 = new ArrayList<>();
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities2.add(new TabEntity(
                    mTitles[i],
                    mIconSelectIds[i],
                    mIconUnselectIds[i],
                    mCoverImageIds[i]));
        }
        mTanLayout_2.setTabData(mTabEntities2);
        mTanLayout_2.showMsg(3, 99);
        mTanLayout_2.showDot(2);
        mTanLayout_2.showMsg(4, 9);
        mTanLayout_2.showMsg(1, 109);
        mTanLayout_2.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

            }

            @Override
            public void onTabReselect(int position) {

            }

            @Override
            public void onTabPublish(int position) {
                Toast.makeText(MainActivity.this, "点击发布按钮， 位置：" + position, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onDoubleClick(int position) {
                Toast.makeText(MainActivity.this, "双击了" + position, Toast.LENGTH_LONG).show();
            }
        });

        // 自定义导航栏TAB数量 最少2个最多6个
        ArrayList<QFTabEntity> list3 = new ArrayList<>();
        for (int i = 0; i < mTitles.length - 1; i++) {
            list3.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        mTanLayout_3.setTabData(list3);
        mTanLayout_3.showDot(3);

        // 纯文字
        mTanLayout_4.setTabData(mTabEntities);
        mTanLayout_4.showMsg(3,99);
        mTanLayout_4.showMsg(4,9);
        mTanLayout_4.showMsg(0,999);
        mTanLayout_4.showDot(2);
        mTanLayout_4.showDot(1);

        // 纯图片
        mTanLayout_5.setTabData(mTabEntities);
        mTanLayout_5.showMsg(3,9);
        mTanLayout_5.showMsg(1,99);
        mTanLayout_5.showMsg(4,999);
        mTanLayout_5.showDot(2);

        // 自定义发布在中间
        mTanLayout_6.setTabData(list3);
        mTanLayout_6.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

            }

            @Override
            public void onTabReselect(int position) {

            }

            @Override
            public void onTabPublish(int position) {

                Toast.makeText(MainActivity.this, "点击了中间的发布按钮， 位置：" + position, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onDoubleClick(int position) {

                Log.e("MainActivity", "双击了");

            }
        });
    }
}
