package com.sliit.dailyselfie.TimeLine;

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
import android.widget.Toast;

import com.sliit.dailyselfie.R;

import java.util.ArrayList;

/**
 * Created by Tharaka on 22/03/2016.
 */
public class MyAdapter extends RecyclerView.Adapter<MyHolder> {

    Context c;
    Dialog d;
    ArrayList<Posts> posts;




    public MyAdapter(Context cntx,ArrayList<Posts> posts){
        this.c=cntx;
        this.posts = posts;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model,null);
        MyHolder holder=new MyHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {

        holder.txt1.setText(posts.get(position).getChallangeName());

        holder.setItemClickListner(new ItemClickListner() {
            @Override
            public void onITemClick(View v, int pos) {
                Snackbar.make(v,posts.get(pos).getChallangeName(),Snackbar.LENGTH_SHORT).show();
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
                //im.setImageResource(image[position]);
                //tx.setText(name[position]);
                d.show();
            }
        });

        holder.b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"hello "+position,Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}
