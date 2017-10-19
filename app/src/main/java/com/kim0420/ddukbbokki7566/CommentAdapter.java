package com.kim0420.ddukbbokki7566;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by alfo6-1 on 2017-08-11.
 */

public class CommentAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Comment> list;

    LayoutInflater li;

    public CommentAdapter(Context context, ArrayList<Comment> list) {
        this.context = context;
        this.list = list;

        li = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Ch holder = new Ch(li.inflate(R.layout.item_recy_comm,parent,false));


        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((Ch)holder).num.setText(list.get(position).num);
        ((Ch)holder).date.setText(list.get(position).date);
        ((Ch)holder).cmt.setText(list.get(position).comment);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Ch extends RecyclerView.ViewHolder{

        TextView num,date,cmt;

        public Ch(View itemView) {
            super(itemView);
            num = (TextView)itemView.findViewById(R.id.tv_num);
            date = (TextView)itemView.findViewById(R.id.tv_date);
            cmt = (TextView)itemView.findViewById(R.id.tv_comment);
        }
    }






}
