package com.kim0420.ddukbbokki7566;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by alfo6-1 on 2017-10-16.
 */

public class DiaryAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<diarydata> data;
    LayoutInflater li;

    public DiaryAdapter(Context context, ArrayList<diarydata> data) {
        this.context = context;
        this.data = data;
        li = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        vh v = new vh(li.inflate(R.layout.item_recy_diary,parent,false));

        return v;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((vh)holder).c.getVisibility()==View.GONE) ((vh)holder).c.setVisibility(View.VISIBLE);
                else ((vh)holder).c.setVisibility(View.GONE);

            }
        };

        if(data.get(position).ctitle!=null) {

            ((vh) holder).t.setText(data.get(position).title.toString() + " : " + data.get(position).ctitle.toString());
            ((vh) holder).a.setText(data.get(position).addr.toString());
            ((vh) holder).d.setText(data.get(position).date.toString());
            ((vh) holder).c.setText(data.get(position).content.toString());

            ((vh) holder).t.setOnClickListener(listener);
            ((vh) holder).a.setOnClickListener(listener);
            ((vh) holder).d.setOnClickListener(listener);
        }





    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class vh extends RecyclerView.ViewHolder{
        TextView t,a,d,c;
        Boolean cv;
        public vh(View itemView) {
            super(itemView);
            cv = false;
            t = (TextView)itemView.findViewById(R.id.tv_title_diary);
            a = (TextView)itemView.findViewById(R.id.tv_addr_diary);
            d = (TextView)itemView.findViewById(R.id.tv_date_diary);
            c = (TextView)itemView.findViewById(R.id.tv_content_diary);




        }
    }
}
