package jp.bap.traning.simplechat.model;

import lombok.Data;

/**
 * Created by Admin on 6/28/2018.
 */
@Data
public class UploadedImage {
    public String id;
    public String title;
    public String description;
    public String type;
    public boolean animated;
    public int width;
    public int height;
    public int size;
    public int views;
    public int bandwidth;
    public String vote;
    public boolean favorite;
    public String account_url;
    public String deletehash;
    public String name;
    public String link;
}
