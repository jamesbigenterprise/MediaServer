package com.example.mediaserver.ui.main;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.mediaserver.R;

import java.io.File;

public class CreateList {

    private String image_title;
    private Integer image_id;
    private String path;
    private RelativeLayout rl;

    public CreateList(Context context, String path)
    {
        // Initialize a new ImageView widget
        ImageView iv = new ImageView(context);

        // Set an image for ImageView

        Bitmap bmp = BitmapFactory.decodeFile(path);
        iv.setImageBitmap(bmp);


        // Finally, add the ImageView to layout
        rl.addView(iv);

        this.path = path;
        File file = new File(path);



        this.image_id = iv.getId();
        this.image_title = file.getName();



    }
    public String getImage_title() {
        return this.image_title;
    }


    public Integer getImage_ID() {
        return this.image_id;
    }




}