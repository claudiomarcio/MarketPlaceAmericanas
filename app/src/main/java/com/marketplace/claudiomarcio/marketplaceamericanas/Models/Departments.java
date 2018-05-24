package com.marketplace.claudiomarcio.marketplaceamericanas.Models;

import java.util.ArrayList;

/**
 * Created by Claudio Marcio on 23/05/2018.
 */

public class Departments {

    public String title;
    public String link;
    public String category;
    public ArrayList<Departments> listCategory;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Departments> getListCategory() {
        return listCategory;
    }

    public void setListCategory(ArrayList<Departments> listCategory) {
        this.listCategory = listCategory;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
