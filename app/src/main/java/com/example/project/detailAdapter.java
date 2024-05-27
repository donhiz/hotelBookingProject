//package com.example.project;
//
//import android.annotation.SuppressLint;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.ArrayList;
//
//public class detailAdapter extends RecyclerView.Adapter<detailAdapter.ViewHolder> {
//    ArrayList<String> hotelNames;
//    ArrayList<String> hotelLocations;
//    ArrayList<String> hotelPrices;
//    ArrayList<Integer> hotelImages;
//    ArrayList<String> hotelDescriptions;
//
//    public void HotelAdapter(ArrayList<String> hotelNames, ArrayList<String> hotelLocations, ArrayList<String> hotelPrices, ArrayList<Integer> hotelImages, ArrayList<String> hotelDescriptions) {
//        this.hotelNames = hotelNames;
//        this.hotelLocations = hotelLocations;
//        this.hotelPrices = hotelPrices;
//        this.hotelImages = hotelImages;
//        this.hotelDescriptions = hotelDescriptions;
//    }
//
//    @NonNull
//    @Override
//    public detailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyler_item, parent, false);
//        ViewHolder vh = new ViewHolder(v);
//
//        return vh;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
//        holder.hotelName.setText(hotelNames.get(position));
//        holder.hotelLocation.setText(hotelLocations.get(position));
//        holder.hotelPrice.setText(hotelPrices.get(position));
//        holder.hotelImage.setImageResource(hotelImages.get(position));
//        holder.hotelDescription.setText(hotelDescriptions.get(position));
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(), hotelNames.get(position), Toast.LENGTH_SHORT).show();
//
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return hotelNames.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        TextView hotelName,hotelLocation,hotelPrice,hotelDescription;
//        ImageView hotelImage;
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            hotelName = itemView.findViewById(R.id.hotelName);
//            hotelLocation = itemView.findViewById(R.id.hotelLocation);
//            hotelPrice = itemView.findViewById(R.id.hotelPrice);
//            hotelPrice = itemView.findViewById(R.id.hotelPrice);
//            hotelImage = itemView.findViewById(R.id.hotelImage);
//            hotelDescription = itemView.findViewById(R.id.hotelDescription);
//
//        }
//    }
//}
