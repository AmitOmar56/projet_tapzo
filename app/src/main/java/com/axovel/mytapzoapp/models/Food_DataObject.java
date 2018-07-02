package com.axovel.mytapzoapp.models;

/**
 * Created by Soft1 on 30-Aug-17.
 */

public class Food_DataObject {
    private String name;
    private String cuisine;
    private int image;
   public Food_DataObject(String name,String cuisine,int image){
       this.name=name;
       this.cuisine=cuisine;
       this.image=image;
   }
   public String getName(){
       return name;
   }
   public void setName(String name){
       this.name=name;
   }
   public String getCuisine(){
       return cuisine;
   }
   public void setCuisine(String cuisine){
       this.cuisine=cuisine;
   }
   public int getImage(){
       return image;
   }
   public void setImage(int image){
       this.image=image;
   }
}
