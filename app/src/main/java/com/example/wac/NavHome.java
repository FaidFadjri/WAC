package com.example.wac;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class NavHome extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private NavHomeAdapter adapter;
    CollectionReference collectionReference = db.collection("mobil");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nav_home, container, false);

        Query query = collectionReference.orderBy("tanggal", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<NavHomeItem> options = new FirestoreRecyclerOptions.Builder<NavHomeItem>()
                .setQuery(query, NavHomeItem.class)
                .build();
        adapter = new NavHomeAdapter(options);

        RecyclerView recyclerView = view.findViewById(R.id.list_car);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new NavHomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                SharedPreferences saveid = getActivity().getSharedPreferences("id", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = saveid.edit();
                editor.putString("id", documentSnapshot.getId());
                editor.apply();
                Intent intent = new Intent(getActivity(), CheckMenu.class);
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(DocumentSnapshot documentSnapshot, int position) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("Apakah Anda Ingin Menghapus Data Ini ?");
                alertDialog.setPositiveButton("YA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String id = documentSnapshot.getId();
                        db.collection("mobil").document(id).delete();
                        dialog.dismiss();
                    }
                }).setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        dialog.dismiss();
                    }
                });
                AlertDialog dialogShow = alertDialog.create();
                dialogShow.show();
            }
        });

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
