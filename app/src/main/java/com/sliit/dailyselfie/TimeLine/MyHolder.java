package com.sliit.dailyselfie.TimeLine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sliit.dailyselfie.R;

/**
 * Created by Tharaka on 22/03/2016.
 */
public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {

    ImageView image;
    TextView txt1;
    TextView txt2;
    Button b1;
    TextView txt3;
    ItemClickListner itemClickListner;
    ItemOnLongClickListner itemOnLongClickListner;

    public MyHolder(final View itemView) {
        super(itemView);

        image=(ImageView)itemView.findViewById(R.id.recyclerImage);
        txt1=(TextView)itemView.findViewById(R.id.recyclerText);
        txt2=(TextView)itemView.findViewById(R.id.recyclerText1);
        b1=(Button)itemView.findViewById(R.id.postShare);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"shared",Toast.LENGTH_SHORT).show();
            }
        });

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
