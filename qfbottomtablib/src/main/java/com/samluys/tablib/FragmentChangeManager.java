package com.samluys.tablib;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentChangeManager {
    private FragmentManager mFragmentManager;
    private int mContainerViewId;
    /** Fragment切换数组 */
    private ArrayList<Fragment> mFragments;
    /** 当前选中的Tab */
    private int mCurrentTab;

    public FragmentChangeManager(FragmentManager fm, int containerViewId, ArrayList<Fragment> fragments) {
        this.mFragmentManager = fm;
        this.mContainerViewId = containerViewId;
        this.mFragments = fragments;
        initFragments();
    }

    /** 初始化fragments */
    private void initFragments() {

        if (mCurrentTab != 0) {
            mFragmentManager.beginTransaction()
                    .add(mContainerViewId, mFragments.get(mCurrentTab)).commitAllowingStateLoss();
        } else {
            setFragments(0);
        }
    }

    /** 界面切换控制 */
    public void setFragments(int index) {

        mFragmentManager.beginTransaction()
                .add(mContainerViewId, mFragments.get(index)).commitAllowingStateLoss();
        showCurrentFragment(index);
    }

    public void showCurrentFragment(int index) {
        for (int i = 0; i < mFragments.size(); i++) {
            if (i == index) {
                mFragments.get(i).onResume();
                mFragmentManager.beginTransaction().show(mFragments.get(i))
                        .commitAllowingStateLoss();
            } else {
                mFragmentManager.beginTransaction().hide(mFragments.get(i))
                        .commitAllowingStateLoss();
            }
        }
        mCurrentTab = index;
    }

    public int getCurrentTab() {
        return mCurrentTab;
    }

    public Fragment getCurrentFragment(int index) {
        return mFragments.get(index);
    }
}