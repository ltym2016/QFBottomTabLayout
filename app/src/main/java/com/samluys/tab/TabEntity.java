package com.samluys.tab;


import com.samluys.tablib.QFTabEntity;

public class TabEntity implements QFTabEntity {
    public String title;
    public int selectedIcon;
    public int unSelectedIcon;
    public int tabCoverIcon;

    public TabEntity(String title, int selectedIcon, int unSelectedIcon) {
        this.title = title;
        this.selectedIcon = selectedIcon;
        this.unSelectedIcon = unSelectedIcon;
    }

    public TabEntity(String title, int selectedIcon, int unSelectedIcon, int tabCoverIcon) {
        this.title = title;
        this.selectedIcon = selectedIcon;
        this.unSelectedIcon = unSelectedIcon;
        this.tabCoverIcon = tabCoverIcon;
    }

    @Override
    public String getTabTitle() {
        return title;
    }

    @Override
    public int getTabSelectedIcon() {
        return selectedIcon;
    }

    @Override
    public int getTabUnselectedIcon() {
        return unSelectedIcon;
    }

    @Override
    public int getTabCoverIcon() {
        return tabCoverIcon;
    }
}
