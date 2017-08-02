package com.kim0420.ddukbbokki7566;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by alfo6-1 on 2017-07-24.
 */

public class SearchAdapter extends RecyclerView.Adapter {

    ArrayList<SearchData> datas;
    Context context;
    LayoutInflater li;

    public SearchAdapter(ArrayList<SearchData> datas, Context context) {
        this.datas = datas;
        this.context = context;

        li = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        holder h = new holder(li.inflate(R.layout.item_recy_search,parent,false));



        return h;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, final int position) {
        ((holder)h).addr.setText(datas.get(position).addr);
        ((holder)h).date.setText(datas.get(position).date);

        ((holder)h).delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datas.remove(position);
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class holder extends RecyclerView.ViewHolder{
        TextView addr,date;
        ImageView delete;

        public holder(View itemView) {
            super(itemView);
            addr = (TextView)itemView.findViewById(R.id.tvaddr_recy_item);
            date = (TextView)itemView.findViewById(R.id.tvdate_recy_item);

            delete = (ImageView)itemView.findViewById(R.id.icon_recy_item);


        }
    }


}
