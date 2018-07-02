package com.axovel.mytapzoapp.models;

/**
 * Created by axovel on 8/21/2017.
 */

public class Offers_product_Data {

    private String titleProduct;
    private String descriptionProduct;

    private String imageUrl;
    private String urlSend;

    public String getPriceProduct() {
        return priceProduct;
    }

    public void setPriceProduct(String priceProduct) {
        this.priceProduct = priceProduct;
    }

    private String priceProduct;
    public Offers_product_Data(String titleProduct,String descriptionProduct,String imageUrl,String urlSend){

        this.titleProduct=titleProduct;
        this.descriptionProduct=descriptionProduct;
        this.imageUrl=imageUrl;
        this.urlSend=urlSend;



    }



    public String getTitleProduct() {
        return titleProduct;
    }

    public void setTitleProduct(String titleProduct) {
        this.titleProduct = titleProduct;
    }

    public String getDescriptionProduct() {
        return descriptionProduct;
    }

    public void setDescriptionProduct(String descriptionProduct) {
        this.descriptionProduct = descriptionProduct;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public String getUrlSend() {
        return urlSend;
    }

    public void setUrlSend(String urlSend) {
        this.urlSend = urlSend;
    }
}
