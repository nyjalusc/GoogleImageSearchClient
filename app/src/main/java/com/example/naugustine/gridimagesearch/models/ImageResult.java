package com.example.naugustine.gridimagesearch.models;

import java.io.Serializable;

public class ImageResult implements Serializable {
   private String fullURL;
   private String thumbURL;
   private String title;

   public void setFullURL(String fullURL) {
      this.fullURL = fullURL;
   }

   public void setThumbURL(String thumbURL) {
      this.thumbURL = thumbURL;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public String getFullURL() {
      return fullURL;
   }

   public String getThumbURL() {
      return thumbURL;
   }

   public String getTitle() {
      return title;
   }
}
