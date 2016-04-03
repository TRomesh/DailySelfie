package com.sliit.dailyselfie;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Tharaka on 22/03/2016.
 */
public class MyAdapter extends RecyclerView.Adapter<MyHolder> {

    Context c;
    String []players;
    String []name;
    int []image;
    Dialog d;

    public MyAdapter(Context cntx,String []plyrs,String []nme,int []img){

        this.c=cntx;
        this.players=plyrs;
        this.name=nme;
        this.image=img;

    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model,null);

        MyHolder holder=new MyHolder(v);



        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
       holder.image.setImageResource(image[position]);
       holder.txt1.setText(name[position]);
       holder.txt2.setText(name[position]);

        holder.setItemClickListner(new ItemClickListner() {
            @Override
            public void onITemClick(View v, int pos) {

                Snackbar.make(v, players[position] + " : " + name[position], Snackbar.LENGTH_SHORT).show();

            }


        });

        holder.setItemLongClickListner(new ItemOnLongClickListner() {

            @Override
            public void onITemLongClick(View v, int pos) {
                d = new Dialog(c);
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setContentView(R.layout.customlayout);
                ImageView im = (ImageView) d.findViewById(R.id.customimg1);
                TextView tx = (TextView) d.findViewById(R.id.customtext1);
                im.setImageResource(image[position]);
                tx.setText(name[position]);
                d.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return name.length;
    }
}
