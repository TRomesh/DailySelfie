package com.sliit.dailyselfie.ActivityFeed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sliit.dailyselfie.Community.SharePost;
import com.sliit.dailyselfie.R;

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
        String Date = sharepost.get(position).getDate();
        String Image =sharepost.get(position).getPostImage();
        holder.t1.setText(Uname);
        holder.t2.setText(FitType);

    }

    @Override
    public int getItemCount() {
        return sharepost.size();
    }

}
