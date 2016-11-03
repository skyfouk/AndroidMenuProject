package com.spring.sky.menuproject.view;

import android.content.Context;
import android.graphics.drawable.PaintDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;


import com.spring.sky.menuproject.R;

import java.util.List;

/**
 * Created by springsky on 16/10/20.
 */

public class MenuPopupWindow extends PopupWindow implements AdapterView.OnItemClickListener {

    Context mContext;
    private ListView leftLv,rightLv;
    private OnMenuListener onMenuListener;
    private List<MenuModel> leftList,rightList;
    private MenuAdapter menuLeftAdapter,menuRightAdapter;
    private int column;
    boolean hasSecond;

    public MenuPopupWindow(Context context){
        this.mContext = context;
        View view = LayoutInflater.from(mContext).inflate(R.layout.menu_popup_window, null);
        leftLv = (ListView) view.findViewById(R.id.leftLv);
        leftLv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        rightLv = (ListView) view.findViewById(R.id.rightLv);
        rightLv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        setContentView(view);

        setBackgroundDrawable(new PaintDrawable());
        setFocusable(true);


        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);

        setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                darkView.startAnimation(animOut);
//                darkView.setVisibility(View.GONE);

                leftLv.setSelection(0);
                rightLv.setSelection(0);
                if( onMenuListener != null ){
                    onMenuListener.onMenuDismiss();
                }
            }
        });

        menuLeftAdapter = new MenuAdapter(mContext);
        menuLeftAdapter.setColumn(0);
        menuLeftAdapter.setList(leftList);
        leftLv.setAdapter(menuLeftAdapter);
        leftLv.setOnItemClickListener(this);

        menuRightAdapter = new MenuAdapter(mContext);
        menuRightAdapter.setColumn(1);
        menuRightAdapter.setList(rightList);
        rightLv.setAdapter(menuRightAdapter);
        rightLv.setOnItemClickListener(this);
    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
    }

    public void setLeftList(int column,List<MenuModel> leftList) {
        this.column = column;
        this.leftList = leftList;
        hasSecond = false;
        for (MenuModel childModel : leftList){
            if(childModel.hasChind()){
                hasSecond = true;
                break;
            }
        }
        menuLeftAdapter.setList(leftList);
        if(!hasSecond){
            rightLv.setVisibility(View.GONE);
            setRightList(null);
        }else {
            rightLv.setVisibility(View.VISIBLE);
        }
    }

    public void setSelect(int row,int item){
        if(row < 0 || leftList == null || row >= leftList.size()){
            return;
        }
        MenuModel leftModel = leftList.get(row);
        leftLv.setSelection(row);
        menuLeftAdapter.setSelectPosition(row);

        setRightList(leftModel.chindMenu);
        if(item < 0 || rightList ==null || item >= rightList.size()){
            return;
        }
        rightLv.setSelection(item);
        menuRightAdapter.setSelectPosition(item);
    }

    private void setRightList(List<MenuModel> rightList) {
        this.rightList = rightList;
        menuRightAdapter.setList(rightList);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if(parent.getId() == leftLv.getId()){
            MenuModel model = leftList.get(position);
            if(leftLv.getSelectedItemPosition() == position){
                return;
            }
            if(model.hasChind()){
                menuLeftAdapter.setSelectPosition(position);
                setRightList(model.chindMenu);
            }else {
                dismiss();
            }
            onMenuClick(position,0,model);
        }else {
            menuRightAdapter.setSelectPosition(position);
            MenuModel model = rightList.get(position);
            onMenuClick(menuLeftAdapter.getSelectPosition(),position,model);
            dismiss();
        }
    }

    void onMenuClick(int row,int item,MenuModel model){
        if(onMenuListener != null){
            onMenuListener.onMenu(column,row,item,model);
        }
    }

    public void setOnMenuListener(OnMenuListener onMenuListener) {
        this.onMenuListener = onMenuListener;
    }

    public static interface OnMenuListener{
        void onMenu(int column, int row, int item, MenuModel menuModel);
        void onMenuDismiss();
    }

}
