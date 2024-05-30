package com.example.Nhi_Do;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;

import java.util.List;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.HotelViewHolder> {

    private List<Hotel> hotelList;
    private final RecycleViewInterface recycleViewInterface;


    public HotelAdapter(List<Hotel> hotelList, Context context, RecycleViewInterface recycleViewInterface) {
        this.hotelList = hotelList;
        this.recycleViewInterface = recycleViewInterface;
    }

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyler_item, parent, false);
        return new HotelViewHolder(view, recycleViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder, int position) {
        Hotel hotel = hotelList.get(position);
        holder.imageView.setImageResource(hotel.getImageResId());
        holder.nameTextView.setText(hotel.getName());
        holder.locationTextView.setText(hotel.getLocation());
        holder.priceTextView.setText(hotel.getPrice());

    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }

    public void setSearchList(List<Hotel> searchList) {
        this.hotelList = searchList;
        notifyDataSetChanged();
    }


    static class HotelViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView, hotelBedTextView, hotelBathTextView, hotelWifiTextView;
        TextView locationTextView;
        TextView priceTextView;

        public HotelViewHolder(@NonNull View itemView, RecycleViewInterface recycleViewInterface) {
            super(itemView);
            imageView = itemView.findViewById(R.id.hotelImage);
            nameTextView = itemView.findViewById(R.id.hotelName);
            locationTextView = itemView.findViewById(R.id.hotelLocation);
            priceTextView = itemView.findViewById(R.id.hotelPrice);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recycleViewInterface != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            recycleViewInterface.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
