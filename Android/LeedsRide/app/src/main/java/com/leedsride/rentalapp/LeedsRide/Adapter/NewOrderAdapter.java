package com.leedsride.rentalapp.LeedsRide.Adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.leedsride.rentalapp.LeedsRide.MyOrdersRecycler;
import com.leedsride.rentalapp.LeedsRide.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<MyOrdersRecycler> orderList;
    private Context context;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private OnItemClickListener itemListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void SetOnItemClickListener(OnItemClickListener listener) {
        itemListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        MyOrdersRecycler currentItem = orderList.get(position);
        if (currentItem.getItemIsHeader()) {
            return 0;
        }
        else {
            return 1;
        }
    }

    public static class OrdersViewHolder extends RecyclerView.ViewHolder {
        public TextView header;
        public TextView info;
        public View colourStrip;

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

    public NewOrderAdapter(ArrayList<MyOrdersRecycler> mOrdersList, Context argContext) {
        orderList = mOrdersList;
        context = argContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
        else { return null; }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyOrdersRecycler currentItem = orderList.get(position);
        switch (holder.getItemViewType()) {
            case 0: {
                int colour;
                OrdersHeaderViewHolder viewHolder = (OrdersHeaderViewHolder)holder;
                viewHolder.sectionHeader.setText(currentItem.getItemHeader());

                if (viewHolder.colouredCircle.getDrawable() != null) {
                    if (currentItem.getItemOrderType().equals("active")) {
                        colour = context.getResources().getColor(R.color.lightGreen);
                        viewHolder.colouredCircle.getDrawable().setColorFilter(colour, PorterDuff.Mode.SRC_ATOP);
                    } else if (currentItem.getItemOrderType().equals("late")) {
                        colour = context.getResources().getColor(R.color.red);
                        viewHolder.colouredCircle.getDrawable().setColorFilter(colour, PorterDuff.Mode.SRC_ATOP);

                    } else if (currentItem.getItemOrderType().equals("available")) {
                        colour = context.getResources().getColor(R.color.Orange);
                        viewHolder.colouredCircle.getDrawable().setColorFilter(colour, PorterDuff.Mode.SRC_ATOP);

                    } else if (currentItem.getItemOrderType().equals("upComing")) {
                        colour = context.getResources().getColor(R.color.primaryBlue);
                        viewHolder.colouredCircle.getDrawable().setColorFilter(colour, PorterDuff.Mode.SRC_ATOP);

                    } else {
                        colour = context.getResources().getColor(R.color.darkGreyBlue);
                        viewHolder.colouredCircle.getDrawable().setColorFilter(colour, PorterDuff.Mode.SRC_ATOP);
                    }
                }
            }
            break;
            case 1: {
                OrdersViewHolder viewHolder = (OrdersViewHolder)holder;

                if (currentItem.getItemOrderType().equals("active")) {
                    viewHolder.colourStrip.setBackgroundColor(context.getResources().getColor(R.color.lightGreen));
                } else if (currentItem.getItemOrderType().equals("late")) {
                    viewHolder.colourStrip.setBackgroundColor(context.getResources().getColor(R.color.red));
                } else if (currentItem.getItemOrderType().equals("available")) {
                    viewHolder.colourStrip.setBackgroundColor(context.getResources().getColor(R.color.Orange));
                } else if (currentItem.getItemOrderType().equals("upComing")) {
                    viewHolder.colourStrip.setBackgroundColor(context.getResources().getColor(R.color.primaryBlue));
                } else {
                    viewHolder.colourStrip.setBackgroundColor(context.getResources().getColor(R.color.darkGreyBlue));
                    viewHolder.header.setTextColor(context.getResources().getColor(R.color.lightGrey));
                    viewHolder.info.setTextColor(context.getResources().getColor(R.color.lightGrey));
                }

                viewHolder.header.setText(currentItem.getItemHeader());
                viewHolder.info.setText(currentItem.getItemInfo());
            }
            break;
            default:
                break;
        }

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
