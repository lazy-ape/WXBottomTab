package com.lyape.bottomtabbar;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by xuqiang on 2017/2/13.
 */

public interface TabItemProvider {
    int  getCount();
    View getItem(int position, ViewGroup container);
    String getTitle(int position);
    int getSelectedIcon(int position);
    int getUnSelectedIcon(int position);
    int getSelectedColor();
    int getUnSelectedColor();

}
