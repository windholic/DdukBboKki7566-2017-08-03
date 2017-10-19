package com.kim0420.ddukbbokki7566;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by alfo6-1 on 2017-08-03.
 */

public class SearchdataAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Item> items;
    LayoutInflater li;

    public SearchdataAdapter(Context context, ArrayList<Item> items) {
        this.context = context;
        this.items = items;

        li = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        V v = new V(li.inflate(R.layout.item_recy_navers,parent,false));

        return v;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((V)holder).title.setText(items.get(position).title.toString());
        ((V)holder).addr.setText(items.get(position).address.toString());

//        ((V)holder).cv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context,MapActivity.class);
//                intent.putExtra("item",items.get(position));
//                context.startActivity(intent);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class V extends RecyclerView.ViewHolder{

        CardView cv;
        TextView title,addr;

        public V(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv_search);
            title = (TextView)itemView.findViewById(R.id.tv_naver_title);
            addr = (TextView)itemView.findViewById(R.id.tv_naver_addr);


        }
    }

}
