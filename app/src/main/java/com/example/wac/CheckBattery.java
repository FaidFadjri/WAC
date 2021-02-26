package com.example.wac;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CheckBattery extends AppCompatActivity {

    public Uri imageUri;
    FirebaseStorage storage;
    StorageReference storageReference;

//    Firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView nomorKendaraan;

//    Ban Depan
    TextView BaikFisikDepanKanan, RusakFisikDepanKanan, baikTebalDepanKanan, rusakTebalDepanKanan;
    Button UploadDepanKanan;
    EditText KetebalanDepanKanan;
    ImageView ImageDepanKanan;

    TextView BaikFisikDepanKiri, RusakFisikDepanKiri, KetebalanDepanKiri, baikTebalDepanKiri, rusakTebalDepanKiri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_battery);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        nomorKendaraan = findViewById(R.id.nama_mobil);
        //DepanKanan
        BaikFisikDepanKanan = findViewById(R.id.FisikBaikDepanKanan);
        RusakFisikDepanKanan = findViewById(R.id.FisikRusakDepanKanan);
        KetebalanDepanKanan = findViewById(R.id.ketebalanDepanKanan);
        baikTebalDepanKanan = findViewById(R.id.baikTebalDepanKanan);
        rusakTebalDepanKanan = findViewById(R.id.rusakTebalDepanKanan);
        UploadDepanKanan = findViewById(R.id.uploadDepanKanan);
        ImageDepanKanan = findViewById(R.id.imageDepanKanan);

        //GetData No Kendaraan
        SharedPreferences getId = getSharedPreferences("id", Context.MODE_PRIVATE);
        String nopol = getId.getString("id", "0");


        Glide.with(this)
                .load("https://www.talkwalker.com/images/2020/blog-headers/image-analysis.png")
                .centerCrop()
                .into(ImageDepanKanan);

        //set No Kendaraan
        nomorKendaraan.setText(nopol);
        getOldData();


        //Kirim Depan Kanan
        BaikFisikDepanKanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> data = new HashMap<>();
                data.put("FisikDepanKanan", "baik");
                db.collection("mobil").document(nomorKendaraan.getText().toString()).set(data, SetOptions.merge());
                RusakFisikDepanKanan.setAlpha((float) 0.2);
                BaikFisikDepanKanan.setAlpha(1);
            }
        });

        RusakFisikDepanKanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> data = new HashMap<>();
                data.put("FisikDepanKanan", "rusak");
                db.collection("mobil").document(nomorKendaraan.getText().toString()).set(data, SetOptions.merge());
                BaikFisikDepanKanan.setAlpha((float) 0.2);
                RusakFisikDepanKanan.setAlpha(1);
            }
        });

        baikTebalDepanKanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> data = new HashMap<>();
                data.put("StatusDepanKanan", "baik");
                db.collection("mobil").document(nomorKendaraan.getText().toString()).set(data, SetOptions.merge());
                rusakTebalDepanKanan.setAlpha((float) 0.2);
                baikTebalDepanKanan.setAlpha(1);
            }
        });

        rusakTebalDepanKanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> data = new HashMap<>();
                data.put("StatusDepanKanan", "rusak");
                db.collection("mobil").document(nomorKendaraan.getText().toString()).set(data, SetOptions.merge());
                baikTebalDepanKanan.setAlpha((float) 0.2);
                rusakTebalDepanKanan.setAlpha(1);
            }
        });

        UploadDepanKanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImageDepanKanan();
            }
        });

    }

    private void uploadImageDepanKanan() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            uploadPicture();
        }
    }

    private void uploadPicture() {
        StorageReference riverRef = storageReference.child("images/DepanBelakang.jpg");
        riverRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String imgURL = riverRef.getDownloadUrl().toString();
                        Map<String, Object> data = new HashMap<>();
                        data.put("imgURL", imgURL);
                        db.collection("mobil").document(nomorKendaraan.getText().toString()).set(data, SetOptions.merge())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(CheckBattery.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                                        ImageDepanKanan.setImageURI(imageUri);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(CheckBattery.this, "Failed Upload, Check Connection", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CheckBattery.this, "Gagal Upload", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getOldData() {
        db.collection("mobil").document(nomorKendaraan.getText().toString())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            if (documentSnapshot.getString("FisikDepanKanan") != null && documentSnapshot.getString("StatusDepanKanan") != null){
                                String fisikBanDepanKanan = documentSnapshot.getString("FisikDepanKanan");
                                String statusKetebalanDepanKanan = documentSnapshot.getString("StatusDepanKanan");

                                if (fisikBanDepanKanan.equals("baik")) {

                                    RusakFisikDepanKanan.setAlpha((float) 0.2);
                                    BaikFisikDepanKanan.setAlpha(1);

                                    if (statusKetebalanDepanKanan.equals("baik")) {
                                        baikTebalDepanKanan.setAlpha(1);
                                        rusakTebalDepanKanan.setAlpha((float) 0.3);
                                    } else if (statusKetebalanDepanKanan.equals("rusak")) {
                                        rusakTebalDepanKanan.setAlpha(1);
                                        baikTebalDepanKanan.setAlpha((float) 0.3);
                                    }

                                } else if (fisikBanDepanKanan.equals("rusak")){
                                    BaikFisikDepanKanan.setAlpha((float) 0.2);
                                    RusakFisikDepanKanan.setAlpha(1);

                                    if (statusKetebalanDepanKanan.equals("baik")) {
                                        baikTebalDepanKanan.setAlpha(1);
                                        rusakTebalDepanKanan.setAlpha((float) 0.3);
                                    } else if (statusKetebalanDepanKanan.equals("rusak")) {
                                        rusakTebalDepanKanan.setAlpha(1);
                                        baikTebalDepanKanan.setAlpha((float) 0.3);
                                    }
                                }
                            }
                        }

                    }
                });
    }

    private void getImage() {
        db.collection("mobil").document(nomorKendaraan.getText().toString())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.getString("imgURL") != null) {
                            Picasso.get().load("com.google.android.gms.tasks.zzu@2c2b65e").into(ImageDepanKanan);
                        }
                    }
                });
    }
}