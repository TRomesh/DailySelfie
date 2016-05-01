package com.sliit.dailyselfie.ActivityFeed;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sliit.dailyselfie.R;

/**
 * Created by Tharaka on 11/04/2016.
 */
public class HolderAC extends RecyclerView.ViewHolder {

    TextView t1;
    TextView t2;
    ImageView im;
    Button b1;


    public HolderAC(View itemView) {
        super(itemView);

        t1 = (TextView)itemView.findViewById(R.id.activity_username);
        t2 = (TextView)itemView.findViewById(R.id.activity_fitnesstype);
        im = (ImageView) itemView.findViewById(R.id.activity_image);



    }

}
