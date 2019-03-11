package com.leedsride.rentalapp.LeedsRide;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrdersViewHolder> {
    private ArrayList<MyOrdersRecycler> orderList;
    private Context context;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private OnItemClickListener itemListener;
    private Boolean headers[false, false, false, false];

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void SetOnItemClickListener(OnItemClickListener listener) {
        itemListener = listener;
    }

    public static class OrdersViewHolder extends RecyclerView.ViewHolder {
        public TextView header;
        public TextView info;
        public View colourStrip;
        public TextView sectionHeader;
        public ImageView colouredCircle;

        public OrdersViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            header = itemView.findViewById(R.id.ordersHeader);
            info = itemView.findViewById(R.id.orderInfo);
            colourStrip = itemView.findViewById(R.id.colourStrip);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public static class OrdersHeaderViewHolder extends RecyclerView.ViewHolder {

        public TextView sectionHeader;
        public ImageView colouredCircle;

        public OrdersHeaderViewHolder(View itemView) {
            super(itemView);
            sectionHeader = itemView.findViewById(R.id.sectionHeader);
            colouredCircle = itemView.findViewById(R.id.sectionHeaderColour);

        }
    }


    public OrderAdapter(ArrayList<MyOrdersRecycler> mOrdersList, Context argContext) {
        orderList = mOrdersList;
        context = argContext;
    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_orders_item,
                    parent,
                    false);
            OrdersViewHolder ovh = new OrdersViewHolder(view, itemListener);
            return ovh;
        }
        else if (viewType == TYPE_HEADER){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_item_header,
                    parent,
                    false);
            OrdersHeaderViewHolder ohvh = new OrdersHeaderViewHolder(view);
            return ohvh;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {

        MyOrdersRecycler currentItem = orderList.get(position);

        if (currentItem.getItemIsHeader()){
            holder.sectionHeader.setText(currentItem.getItemHeader());

            if (currentItem.getItemOrderType().equals("active")) {
                holder.colouredCircle.setColorFilter(context.getResources().getColor(R.color.lightGreen));
            } else if (currentItem.getItemOrderType().equals("available")) {
                holder.colouredCircle.setColorFilter(context.getResources().getColor(R.color.Orange));
            } else if (currentItem.getItemOrderType().equals("upComing")) {
                holder.colouredCircle.setColorFilter(context.getResources().getColor(R.color.primaryBlue));
            } else {
                holder.colouredCircle.setColorFilter(R.color.lightGrey);
            }
        }
        else {

            if (currentItem.getItemOrderType().equals("active")) {
                holder.colourStrip.setBackgroundColor(context.getResources().getColor(R.color.lightGreen));
            } else if (currentItem.getItemOrderType().equals("available")) {
                holder.colourStrip.setBackgroundColor(context.getResources().getColor(R.color.Orange));
            } else if (currentItem.getItemOrderType().equals("upComing")) {
                holder.colourStrip.setBackgroundColor(context.getResources().getColor(R.color.primaryBlue));
            } else {
                holder.colourStrip.setBackgroundColor(R.color.lightGrey);
                holder.header.setTextColor(context.getResources().getColor(R.color.lightGrey));
                holder.info.setTextColor(context.getResources().getColor(R.color.lightGrey));
            }

            holder.header.setText(currentItem.getItemHeader());
            holder.info.setText(currentItem.getItemInfo());
        }

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    @override
    public int getItemViewType(int position) {
        MyOrdersRecycler currentItem = orderList.get(position);
        if (currentItem.getItemIsHeader()) {
            return 0;
        }
        else {
            return 1;
        }
    }
}
