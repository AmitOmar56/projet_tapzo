package com.axovel.mytapzoapp.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Umesh Chauhan on 11-03-2016.
 * Axovel Private Limited
 */
public class APIResponse {

    private String title;
    private String site;
    private String price;
    private String url;
    private String saving;
    private String product_sub_cate;
    private String image;
    private List<String> offers;

    public List<String> getOffers() {
        return offers;
    }

    public void setOffers(List<String> offers) {
        this.offers = offers;
    }

    public String isSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSaving() {
        return saving;
    }

    public void setSaving(String saving) {
        this.saving = saving;
    }

    public String getProduct_sub_cate() {
        return product_sub_cate;
    }

    public void setProduct_sub_cate(String product_sub_cate) {
        this.product_sub_cate = product_sub_cate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public JSONObject getJSONObject() {

        JSONObject obj = new JSONObject();
        try {
            obj.put("title", title);
            obj.put("site", site);
            obj.put("price", price);
            obj.put("url", url);
            obj.put("saving", saving);
            obj.put("image", image);
            obj.put("offers", offers);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

}
