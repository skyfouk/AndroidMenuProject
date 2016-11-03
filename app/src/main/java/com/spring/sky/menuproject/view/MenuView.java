package com.spring.sky.menuproject.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.spring.sky.menuproject.AppInfoUtils;
import com.spring.sky.menuproject.R;

import java.util.List;

/**
 * Created by springsky on 16/10/24.
 */

public class MenuView extends LinearLayout implements View.OnClickListener, MenuPopupWindow.OnMenuListener {
    public String[] hintTexts;
    public List[] dataSource;
    public TextView[] textViews;
    private int textColor = R.color.gray_80;
    private int textColorSelected = R.color.orange;
    private int textSize;
    private int lineHeight ;
    private MenuPopupWindow menuPopupWindow;
    private OnMenuListener onMenuListener;
    View lineView;
    TextView lastTv;
    private IndexPath[] indexPaths;

    public MenuView(Context context) {
        super(context);
        init(context);
    }

    public MenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setHintTexts(String[] hintTexts) {
        this.hintTexts = hintTexts;
    }

    public void setDataSource(List[] dataSource) {
        this.dataSource = dataSource;
        reloadData();
    }

    public void setIndexPath(IndexPath indexPath) {
        setIndexPath(indexPath, false);
    }

    public void setIndexPath(IndexPath indexPath, boolean actionMenu) {
        indexPaths[indexPath.column] = indexPath;
        if (actionMenu) {
            TextView lastTv = textViews[indexPath.column];
            List<MenuModel> list = dataSource[indexPath.column];
            if(list == null || indexPath.row >= list.size()){
                return;
            }
            MenuModel left = list.get(indexPath.row);

            MenuModel menuModel = null;
            if (indexPath.item < 0) {
                menuModel = left;
            } else {
                MenuModel right = left.chindMenu.get(indexPath.item);
                menuModel = right;
            }
            lastTv.setText(menuModel.value);
            if (onMenuListener != null) {
                onMenuListener.onMenu(indexPath, menuModel);
            }
        }
    }

    public List[] getDataSource() {
        return dataSource;
    }

    private void init(Context context) {
        menuPopupWindow = new MenuPopupWindow(context);
        menuPopupWindow.setOnMenuListener(this);
        AppInfoUtils.getViewHeight(this);
        textSize = AppInfoUtils.spToPx(6);
        lineHeight = AppInfoUtils.dipToPx(1);
    }



    private void reloadData() {
        removeAllViews();
        if (dataSource == null || dataSource.length < 1) {
            return;
        }
        int count = dataSource.length;
        int height = getMeasuredHeight() - lineHeight;
        setOrientation(LinearLayout.VERTICAL);
        LinearLayout menuBaseView = new LinearLayout(getContext());
        menuBaseView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, height));
        menuBaseView.setWeightSum(count);
        menuBaseView.setGravity(Gravity.CENTER);
        menuBaseView.setOrientation(LinearLayout.HORIZONTAL);
        indexPaths = new IndexPath[count];
        textViews = new TextView[count];
        for (int i = 0; i < count; i++) {
            indexPaths[i] = new IndexPath(i, 0, -1);

            LinearLayout tempBaseView = new LinearLayout(getContext());
            tempBaseView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, height, 1));
            tempBaseView.setGravity(Gravity.CENTER);
            TextView tv = new TextView(getContext());
            tv.setTextColor(getResources().getColor(textColor));
            tv.setTextSize(textSize);
            LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            tv.setGravity(Gravity.CENTER);
            tv.setLayoutParams(params);
            tv.setMaxLines(1);
            tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.triangle_down, 0);
            tv.setCompoundDrawablePadding(AppInfoUtils.dipToPx(2));
            tv.setId(i);
            tv.setOnClickListener(this);
            textViews[i] = tv;
            tempBaseView.addView(tv);
            menuBaseView.addView(tempBaseView);
            if (hintTexts != null && i < hintTexts.length) {
                tv.setText(hintTexts[i]);
            }
            View lineView = new View(getContext());
            lineView.setBackgroundColor(getResources().getColor(R.color.main_bg_in));
            menuBaseView.addView(lineView, new LayoutParams(AppInfoUtils.dipToPx(1), height - AppInfoUtils.dipToPx(8)));
        }
        addView(menuBaseView);

        lineView = new View(getContext());
        lineView.setBackgroundColor(getResources().getColor(R.color.main_bg_in));
        addView(lineView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, lineHeight));
    }

    @Override
    public void onClick(View v) {
        lastTv = (TextView) v;
        int column = v.getId();
        List<MenuModel> list = dataSource[column];
        lastTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.triangle_up, 0);
        lastTv.setTextColor(getResources().getColor(textColorSelected));
        menuPopupWindow.setLeftList(column, list);
        IndexPath indexPath = indexPaths[column];
        menuPopupWindow.setSelect(indexPath.row, indexPath.item);
//        int[] location = new int[2];
//        lineView.getLocationOnScreen(location);
        menuPopupWindow.showAsDropDown(lineView);
//        menuPopupWindow.showAtLocation(this,Gravity.BOTTOM,0,0);
    }

    @Override
    public void onMenu(int column, int row, int item, MenuModel menuModel) {
        TextView lastTv = textViews[column];
        lastTv.setText(menuModel.value);
        IndexPath indexPath = indexPaths[column];
        indexPath.row = row;
        indexPath.item = item;
        onMenuDismiss();
        if (onMenuListener != null) {
            onMenuListener.onMenu(indexPath, menuModel);
        }
    }

    @Override
    public void onMenuDismiss() {
        lastTv.setTextColor(getResources().getColor(R.color.gray_80));
        lastTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.triangle_down, 0);
    }

    public void setOnMenuListener(OnMenuListener onMenuListener) {
        this.onMenuListener = onMenuListener;
    }

    public static interface OnMenuListener {
        void onMenu(IndexPath indexPath, MenuModel menuModel);
    }

    public static class IndexPath {
        public int column; //index
        public int row; //left
        public int item; //right

        public IndexPath(int column, int row, int item) {
            this.column = column;
            this.row = row;
            this.item = item;
        }
    }
}
