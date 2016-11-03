package com.spring.sky.menuproject;

import android.app.Activity;
import android.os.Bundle;

import com.spring.sky.menuproject.view.MenuModel;
import com.spring.sky.menuproject.view.MenuView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements MenuView.OnMenuListener{

    MenuView menuView1,menuView2;
    private List<MenuModel>[] menuDataSource1,menuDataSource2 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppInfoUtils.init(this); //计算高度，必须增加
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        menuView1 = (MenuView) findViewById(R.id.menuView1);
        menuView2 = (MenuView) findViewById(R.id.menuView2);

        int count = 4;
        menuDataSource1 = new List[count];
        for (int i = 0; i < count; i++) {
            menuDataSource1[i] = new ArrayList<MenuModel>();
            for (int j = 0; j < 6; j++) {
                MenuModel model = new MenuModel(i*j,i+"-"+j,null);
                menuDataSource1[i].add(model);
                model.chindMenu = new ArrayList<>();
                for (int k = 0; k < 3; k++) {
                    model.chindMenu.add(new MenuModel(i*j*k,i+"-"+j+"-"+k,null));
                }
            }
        }
        menuView1.setHintTexts(new String[]{"menu1测试A","menu1测试B","menu1测试C","menu1测试D"});
        menuView1.setDataSource(menuDataSource1);

        count = 2;
        menuDataSource2 = new List[count];
        for (int i = 0; i < count; i++) {
            menuDataSource2[i] = new ArrayList<MenuModel>();
            for (int j = 0; j < 12; j++) {
                menuDataSource2[i].add(new MenuModel(i*j,i+"-"+j,null));
            }
        }
        menuView2.setHintTexts(new String[]{"menu2测试A","menu2测试B"});
        menuView2.setDataSource(menuDataSource2);

        menuView1.setOnMenuListener(this);
        menuView2.setOnMenuListener(this);
    }

    @Override
    public void onMenu(MenuView.IndexPath indexPath, MenuModel menuModel) {

    }
}
