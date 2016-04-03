package com.sliit.dailyselfie;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Tharaka on 22/03/2016.
 */
public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {

    ImageView image;
    TextView txt1;
    TextView txt2;
    ItemClickListner itemClickListner;
    ItemOnLongClickListner itemOnLongClickListner;

    public MyHolder(View itemView) {
        super(itemView);

        image=(ImageView)itemView.findViewById(R.id.recyclerImage);
        txt1=(TextView)itemView.findViewById(R.id.recyclerText);
        txt2=(TextView)itemView.findViewById(R.id.recyclerText1);

        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);

    }



    public void setItemClickListner(ItemClickListner ic){
        this.itemClickListner=ic;
    }


    public void setItemLongClickListner(ItemOnLongClickListner ic){
        this.itemOnLongClickListner=ic;
    }

    @Override
    public void onClick(View v) {
        this.itemClickListner.onITemClick(v, getLayoutPosition());
    }


    @Override
    public boolean onLongClick(View v) {
        this.itemOnLongClickListner.onITemLongClick(v,getLayoutPosition());
        return true;
    }
}
