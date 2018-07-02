package com.axovel.mytapzoapp.models;

/**
 * Created by axovel on 8/23/2017.
 */

public class ProductSearchData {

    private String titleApp;
    private String titleProductdata;
    private long productPrice;
    private String imageUrl;

    private String productAffiliateLink;

    public ProductSearchData(String titleApp, String titleProductdata, long productPrice,String productAffiliateLink, String imageUrl) {
        this.titleApp = titleApp;
        this.titleProductdata = titleProductdata;
        this.productPrice = productPrice;
        this.imageUrl = imageUrl;
        this.productAffiliateLink=productAffiliateLink;
    }

    public String getTitleApp() {
        return titleApp;
    }

    public void setTitleApp(String titleApp) {
        this.titleApp = titleApp;
    }

    public String getTitleProductdata() {
        return titleProductdata;
    }

    public void setTitleProductdata(String titleProductdata) {
        this.titleProductdata = titleProductdata;
    }

    public long getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(long productPrice) {
        this.productPrice = productPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProductAffiliateLink() {
        return productAffiliateLink;
    }

    public void setProductAffiliateLink(String productAffiliateLink) {
        this.productAffiliateLink = productAffiliateLink;
    }


}
