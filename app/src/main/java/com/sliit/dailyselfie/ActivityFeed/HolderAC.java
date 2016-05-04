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
public class HolderAC extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener  {

    TextView t1;
    TextView t2;
    TextView t3;
    TextView t4;
    ImageView im;
    Button b1;
    ItemClickListner itemClickListner;
    ItemOnLongClickListner itemOnLongClickListner;

    public HolderAC(View itemView) {
        super(itemView);

        t1 = (TextView)itemView.findViewById(R.id.activity_username);
        t2 = (TextView)itemView.findViewById(R.id.activity_fitnesstype);
        t3 = (TextView)itemView.findViewById(R.id.activityDescription);
        t4 = (TextView)itemView.findViewById(R.id.activity_date);
        im = (ImageView) itemView.findViewById(R.id.activity_image);


          itemView.setOnClickListener(this);
          itemView.setOnLongClickListener(this);



    }

    public void setItemClickListner(ItemClickListner ic){
        this.itemClickListner=ic;
    }


    public void setItemLongClickListner( ItemOnLongClickListner ic){
        this.itemOnLongClickListner=ic;
    }

    @Override
    public void onClick(View v) {
        this.itemClickListner.onITemClick(v,getLayoutPosition());
    }

    @Override
    public boolean onLongClick(View v) {
        this.itemOnLongClickListner.onITemLongClick(v,getLayoutPosition());
        return false;
    }


}
