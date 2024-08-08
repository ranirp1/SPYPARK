package com.example.spypark;

import android.content.Context;
import android.content.Intent;
import android.icu.text.Transliterator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class listViewAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    List<Model> modelList;
    ArrayList<Model> arrayList;

    public listViewAdapter(Context context, List<Model> modelList) {
        mContext = context;
        this.modelList = modelList;
        inflater= LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<Model>();
        this.arrayList.addAll(modelList);

    }

    public class ViewHolder{
        TextView mTitleTv, mDescTv;
        ImageView mIconIv;


    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Object getItem(int i) {
        return modelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder;
        if (view == null){

            holder = new ViewHolder();
            view = inflater.inflate(R.layout.row, null);


            holder.mTitleTv = view.findViewById(R.id.title);
            holder.mIconIv = view.findViewById(R.id.icon);
            holder.mDescTv = view.findViewById(R.id.description);

            view.setTag(holder);

        }
        else{
            holder= (ViewHolder) view.getTag();
        }

        holder.mTitleTv.setText(modelList.get(position).getTitle());
        holder.mDescTv.setText(modelList.get(position).getDesc());
        holder.mIconIv.setImageResource(modelList.get(position).getIcon());


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(modelList.get(position).getTitle().equals("Phoenix")){
                    Intent intent = new Intent(mContext, bookInfo.class);
                    intent.putExtra("actionbarTitle","Phoenix");
                    mContext.startActivity(intent);
                }
                if(modelList.get(position).getTitle().equals("Orion")){
                    Intent intent = new Intent(mContext, bookInfo.class);
                    intent.putExtra("actionbarTitle","Orion");
                    mContext.startActivity(intent);
                }
            }
        });

        return view;
    }

    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        modelList.clear();
        if(charText.length()==0){
            modelList.addAll(arrayList);
        }
        else{
            for(Model model:arrayList){
                if (model.getTitle().toLowerCase(Locale.getDefault()).contains(charText)){
                    modelList.add(model);
                }
            }
        }
        notifyDataSetChanged();
    }
}
