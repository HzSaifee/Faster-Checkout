package com.hellmates.fastercheckout.ProductRecyclerView;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hellmates.fastercheckout.Product.Info;
import com.hellmates.fastercheckout.R;

import java.util.ArrayList;

public class MyCustomAdapter extends RecyclerView.Adapter<MyCustomAdapter.MyViewHolder> {

    Context context;
    private ArrayList<Info> data = new ArrayList<>();
    private double total = 0.0;
    LayoutInflater inflater;

    public MyCustomAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_layout,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final Info currentItemInfo = data.get(position);

        holder.itemName.setText(data.get(position).getItemName());
        holder.itemMRP.setText("$" + String.valueOf(data.get(position).getItemMRP()));
        holder.itemDisc.setText("-" + String.valueOf(data.get(position).getItemDiscount()) + "%");
        holder.itemSP.setText("$" + String.valueOf(data.get(position).getItemSP()));

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentItemPosition = data.indexOf(currentItemInfo);
                total = total - currentItemInfo.getItemSP();
                total = Double.parseDouble(String.format("%.2f", total));
                Log.d("Total sp =", String.valueOf(currentItemInfo.getItemSP()));
                Log.d("Total =", String.valueOf(total));
                TextView totalTV = (TextView) ((Activity)context).findViewById(R.id.total);
                totalTV.setText(getTotal());
                data.remove(currentItemPosition);
                notifyItemRemoved(currentItemPosition);

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addItem(Info itemInfo){
        this.data.add(itemInfo);
        notifyItemInserted(data.lastIndexOf(itemInfo));
        total = total + itemInfo.getItemSP();
        total = Double.parseDouble(String.format("%.2f", total));
        Log.d("Total =", String.valueOf(total));
    }

    public String getTotal() {
        return String.valueOf(this.total);
    }

    public ArrayList<Info> getItemList(){return data;}

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView itemName;
        TextView itemMRP;
        TextView itemDisc;
        TextView itemSP;
        ImageView remove;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemName = (TextView) itemView.findViewById(R.id.itemName);
            itemMRP = (TextView) itemView.findViewById(R.id.mrp);
            itemDisc = (TextView) itemView.findViewById(R.id.discount);
            itemSP = (TextView) itemView.findViewById(R.id.sellingPrice);
            remove = (ImageView) itemView.findViewById(R.id.remove);
        }
    }
}
