package com.sliit.dailyselfie.TimeLine;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.sliit.dailyselfie.ActivityFeed.SharePost;
import com.sliit.dailyselfie.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by Tharaka on 22/03/2016.
 */
public class MyAdapter extends RecyclerView.Adapter<MyHolder> {

    Context c;
    Dialog d;
    ArrayList<Posts> posts;
    Firebase FB;
    SharePost SP;




    public MyAdapter(Context cntx, ArrayList<Posts> posts){
        this.c=cntx;
        this.posts = posts;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model,null);
        MyHolder holder=new MyHolder(v);
        FB=new Firebase("https://dailyselfie.firebaseio.com/sharedpost");
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {

        holder.txt1.setText(posts.get(position).getCreated_at());
        Glide.with(c).load(posts.get(position).getImage()).into(holder.image);

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
                SP=new SharePost();
                Toast.makeText(v.getContext(),"hello "+position,Toast.LENGTH_SHORT).show();
                Bitmap bmp = BitmapFactory.decodeFile(posts.get(position).getImage());
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 40, stream);
                byte[] byteArray = stream.toByteArray();
                String imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
                SP.setPostSharer("Tharaka");
                SP.setPostType(posts.get(position).getChallangeName());
                SP.setPostDescription(posts.get(position).getDescription());
                SP.setPostedTime(posts.get(position).getCreated_at());
                SP.setPostImage(imageFile);
                uploadPost(SP);




            }
        });


    }

    public void uploadPost(SharePost posts){
        FB.push().setValue(posts);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}
