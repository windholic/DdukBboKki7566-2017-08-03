package com.kim0420.ddukbbokki7566;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by alfo6-1 on 2017-08-21.
 */

public class MenuAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<FoodMenu> data;
    LayoutInflater li;

    public MenuAdapter(Context context, ArrayList<FoodMenu> data) {
        this.context = context;
        this.data = data;

        li = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Hold hold = new Hold(li.inflate(R.layout.item_recy_menu,parent,false));


        return hold;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        ((Hold)holder).tv.setText(data.get(position).menu);

        ((Hold)holder).btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.remove(position);
                notifyDataSetChanged();
            }
        });

        if(data.get(position).willdel==true){
            ((Hold)holder).cb.setVisibility(View.GONE);
            ((Hold)holder).btn.setVisibility(View.VISIBLE);
        }else{
            ((Hold)holder).cb.setVisibility(View.VISIBLE);
            ((Hold)holder).btn.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class Hold extends RecyclerView.ViewHolder{

        ImageView btn;
        TextView tv;
        CheckBox cb;

        public Hold(View itemView) {
            super(itemView);

            btn = (ImageView)itemView.findViewById(R.id.btn_delete_menuitem);
            tv = (TextView)itemView.findViewById(R.id.tv_menu);

            cb = (CheckBox)itemView.findViewById(R.id.cb_menu);


        }
    }
}
