package com.samluys.tablib;

public interface OnTabSelectListener {
    void onTabSelect(int position);
    void onTabReselect(int position);
    void onTabPublish(int position);
    void onDoubleClick(int position);
}