package com.axovel.mytapzoapp.models;

import java.io.Serializable;

/**
 * Created by axovel on 7/30/2017.
 */

public class TapzoComponentData implements Serializable {

    private String componentName;
    private String componentOffers;
    private int componentImage;
    public TapzoComponentData(String componentName,String componentOffers,int componentImage){
        this.componentName=componentName;
        this.componentOffers=componentOffers;
        this.componentImage=componentImage;
    }


    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getComponentOffers() {
        return componentOffers;
    }

    public void setComponentOssers(String componentOssers) {
        this.componentOffers = componentOssers;
    }

    public int getComponentImage() {
        return componentImage;
    }

    public void setComponentImage(int componentImage) {
        this.componentImage = componentImage;
    }


}
