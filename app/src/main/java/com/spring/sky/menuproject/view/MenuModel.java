package com.spring.sky.menuproject.view;

import java.util.List;

/**
 * Created by springsky on 16/10/20.
 */

public class MenuModel {
    public Object key;
    public String value;
    public List<MenuModel> chindMenu;

    public MenuModel(){
        super();
    }

    public MenuModel(Object key, String value, List<MenuModel> chindMenu){
        super();
        this.key = key;
        this.value = value;
        this.chindMenu = chindMenu;
    }

    public boolean hasChind(){
        return (chindMenu != null && chindMenu.size() > 0);
    }
}
