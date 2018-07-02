package com.axovel.mytapzoapp.customAdapter;


import com.axovel.mytapzoapp.models.Item;

/**
 * Created by lenovo on 2/23/2016.
 */
public interface ItemClickListener {
    void itemClicked(Item item);
    void itemClicked(Section section);
}
