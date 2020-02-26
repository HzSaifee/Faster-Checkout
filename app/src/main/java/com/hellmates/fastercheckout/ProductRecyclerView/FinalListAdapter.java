package com.hellmates.fastercheckout.ProductRecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hellmates.fastercheckout.Product.Info;
import com.hellmates.fastercheckout.R;

import java.util.ArrayList;

public class FinalListAdapter extends RecyclerView.Adapter<FinalListAdapter.MyFinalViewHolder> {

    Context context;
    private ArrayList<Info> data = new ArrayList<>();
    LayoutInflater inflater;

    public FinalListAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyFinalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.final_item_layout,parent,false);
        FinalListAdapter.MyFinalViewHolder holder = new FinalListAdapter.MyFinalViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyFinalViewHolder holder, int position) {
        holder.itemName.setText(data.get(position).getItemName());
        holder.itemMRP.setText("$" + String.valueOf(data.get(position).getItemMRP()));
        holder.itemDisc.setText("-" + String.valueOf(data.get(position).getItemDiscount()) + "%");
        holder.itemSP.setText("$" + String.valueOf(data.get(position).getItemSP()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyFinalViewHolder extends RecyclerView.ViewHolder{

        TextView itemName;
        TextView itemMRP;
        TextView itemDisc;
        TextView itemSP;

        public MyFinalViewHolder(View itemView) {
            super(itemView);

            itemName = (TextView) itemView.findViewById(R.id.final_itemName);
            itemMRP = (TextView) itemView.findViewById(R.id.final_mrp);
            itemDisc = (TextView) itemView.findViewById(R.id.final_discount);
            itemSP = (TextView) itemView.findViewById(R.id.final_sellingPrice);
        }
    }

    public void addItemToFinalList(Info itemInfo){
        this.data.add(itemInfo);
        notifyItemInserted(data.lastIndexOf(itemInfo));
    }
}
