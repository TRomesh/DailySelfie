package com.sliit.dailyselfie.ActivityFeed;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sliit.dailyselfie.R;
import com.sliit.dailyselfie.TimeLine.ItemClickListner;
import com.sliit.dailyselfie.TimeLine.ItemOnLongClickListner;

import java.util.ArrayList;

/**
 * Created by Tharaka on 11/04/2016.
 */
public class AdapterAC extends RecyclerView.Adapter<HolderAC> {

    Context c;
    ArrayList<SharePost> sharepost;

    public AdapterAC(Context c, ArrayList<SharePost> sharepost) {
        this.c = c;
        this.sharepost = sharepost;
    }

    @Override
    public HolderAC onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_postmodel,parent,false);
        HolderAC hac = new HolderAC(v);
        return hac;
    }

    @Override
    public void onBindViewHolder(HolderAC holder, int position) {
        String Uname =sharepost.get(position).getPostSharer();
        String  FitType =sharepost.get(position).getPostType();
        String Description =sharepost.get(position).getPostDescription();
        String Date = sharepost.get(position).getPostedTime();
        String Image = sharepost.get(position).getPostImage();
        byte[] imageAsBytes = Base64.decode(Image, Base64.DEFAULT);
        Bitmap bmp1 = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        holder.t1.setText(Uname);
        holder.t2.setText(FitType);
        holder.t3.setText(Description);
        holder.t4.setText(Date);
        holder.im.setImageBitmap(bmp1);

     holder.setItemClickListner(new com.sliit.dailyselfie.ActivityFeed.ItemClickListner() {
       @Override
           public void onITemClick(View v, int pos) {
              Toast.makeText(v.getContext(),"On Click",Toast.LENGTH_SHORT).show();
       }
     });

        holder.setItemLongClickListner(new com.sliit.dailyselfie.ActivityFeed.ItemOnLongClickListner() {
            @Override
            public boolean onITemLongClick(View v, int pos) {
                Toast.makeText(v.getContext(),"On Long Click",Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return sharepost.size();
    }

}
