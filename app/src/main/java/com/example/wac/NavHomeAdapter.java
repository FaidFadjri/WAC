package com.example.wac;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class NavHomeAdapter extends FirestoreRecyclerAdapter<NavHomeItem, NavHomeAdapter.CarHolder> {

    private NavHomeAdapter.OnItemClickListener listener;

    public NavHomeAdapter(@NonNull FirestoreRecyclerOptions<NavHomeItem> options) {
        super(options);
    }
    @Override
    protected void onBindViewHolder(@NonNull CarHolder holder, int position, @NonNull NavHomeItem model) {

       String data = model.getTipe() + " " + model.getNopol();
       holder.tipe_kendaraan.setText(data);
    }

    @NonNull
    @Override
    public CarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_item, parent, false);
        return new CarHolder(v);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }


    public class CarHolder extends RecyclerView.ViewHolder {

        TextView tipe_kendaraan;
        ImageView delete;

        public CarHolder(@NonNull View itemView) {
            super(itemView);

            tipe_kendaraan = itemView.findViewById(R.id.title_car);
            delete = itemView.findViewById(R.id.delete_list);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onDeleteClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
        void onDeleteClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnItemClickListener(NavHomeAdapter.OnItemClickListener listener){
        this.listener = listener;
    }
}
