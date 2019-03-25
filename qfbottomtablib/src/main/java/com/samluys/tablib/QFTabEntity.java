package com.samluys.tablib;


import androidx.annotation.DrawableRes;

public interface QFTabEntity {
    String getTabTitle();

    @DrawableRes
    int getTabSelectedIcon();

    @DrawableRes
    int getTabUnselectedIcon();

    @DrawableRes
    int getTabCoverIcon();
}