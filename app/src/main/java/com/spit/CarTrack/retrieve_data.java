package com.spit.CarTrack;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;
import org.w3c.dom.Text;
import java.util.Calendar;
import java.util.Locale;

import static java.lang.Byte.valueOf;

public class retrieve_data extends AppCompatActivity {

    private FirestoreRecyclerAdapter<CarDetails, Car_Viewholer> adapter;
    /*TextView proof;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_data);

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        Query query = rootRef.collection("Cars");

        Log.d("QUERY", query+"");
              //  .orderBy("TimeStamp", Query.Direction.ASCENDING);

       /* proof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(retrieve_data.this,Proof.class);
                startActivity(intent);

            }
        });
*/


        FirestoreRecyclerOptions<CarDetails> response = new FirestoreRecyclerOptions.Builder<CarDetails>()
                .setQuery(query, CarDetails.class)
                .build();

        Log.d("RESPONSE FROM FIRESTORE",response.toString());
        adapter = new FirestoreRecyclerAdapter<CarDetails, Car_Viewholer>(response) {

            @Override
            protected void onBindViewHolder(@NonNull Car_Viewholer holder, int position, @NonNull final CarDetails car) {
                final String documentId = getSnapshots().getSnapshot(position).getId();
                Log.d("docId", documentId);
                holder.address.setText(car.getAddresss());
                holder.timestamp.setText(getDate(car.getTimeStamp()+""));
                holder.uploader.setText(car.getUploader()+"");
                holder.Label.setText(car.getLabel()+"");
                holder.accuracy.setText(Float.parseFloat(car.getAccuracy())*100+"%"+"");
                Picasso.get().load(car.getImage_Url()+"").into(holder.car_picture);
                Log.d("URLLLL",car.getImage_Url()+"");
             //   Toast.makeText(retrieve_data.this, car.getAccuracy()+"", Toast.LENGTH_SHORT).show();

//                if(car.isProof_status())
//                {
//                    holder.card.setCardBackgroundColor(Color.CYAN);
//                }
                holder.navigate_gmaps.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       // Toast.makeText(retrieve_data.this, car.getLatitude()+"", Toast.LENGTH_SHORT).show();
                        try {
                            Uri gmmIntentUri = Uri.parse("geo:"+car.getLatitude()+","+car.getLongitude()+"?q="+car.getAddresss());
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(retrieve_data.this, "Google Maps Not Installed", Toast.LENGTH_SHORT).show();

                        }

                    }
                });

                holder.proof.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                                Intent i= new Intent(getApplicationContext(), Proof.class);
                                i.putExtra("docId",documentId);
                                startActivity(i);
                    }
                });

                holder.whatsapp_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                       // Uri imgUri = Uri.parse(pictureFile.getAbsolutePath());
                        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                        whatsappIntent.setType("text/plain");
                        whatsappIntent.setPackage("com.whatsapp");
                        String msg = build_the_whatsapp_msg_string(car.getAddresss(),getDate(car.getTimeStamp()+""),car.getUploader()+"",car.getLabel()+"",Float.parseFloat(car.getAccuracy())*100+"%"+"",car.getImage_Url()+"");
                        whatsappIntent.putExtra(Intent.EXTRA_TEXT, msg);
                        //whatsappIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
                      //  whatsappIntent.setType("image/jpeg");
                        whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        try {
                            startActivity(whatsappIntent);
                        } catch (android.content.ActivityNotFoundException ex) {
                            //ToastHelper.MakeShortText("Whatsapp have not been installed.");
                        }

                    }
                });


            }

            @NonNull
            @Override
            public Car_Viewholer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_details_card, parent, false);
                Log.d("inflate",view+"");
                return new Car_Viewholer(view);
            }
        };
        recyclerView.setAdapter(adapter);

    }

    private String build_the_whatsapp_msg_string(String addresss, String date, String uploader, String label, String accuracy, String image_url) {

        String msg = "*TRACK ON PARK*: _BMC_"+"\n"+"The abandoned vehicle details are as follows: "+"\n"+"*Towing Status*: "+"Pending"+"\n"+"Uploaded on: "+date+"\n"+"Address: " +addresss+"\n"+"Uploader: "+uploader+"\n"+"Label: "+label+"\n"+"Accuracy: "+accuracy+"\n"+"Vehicle Photo: "+image_url;
        return  msg;
    }

    private String getDate(String s) {

        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(Long.parseLong(s) * 1000);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }


    private class Car_Viewholer extends RecyclerView.ViewHolder {
        private View view;
        public TextView address;
        public TextView uploader;
        public TextView timestamp;
        public TextView accuracy;
        public ImageView navigate_gmaps;
        public ImageView car_picture;
       // public TextView accuracy;
        public TextView Label;
        public  TextView proof;
        public CardView card;
        public ImageView whatsapp_img;

    //    public TextView zoneInitials;

        Car_Viewholer(View itemView) {
            super(itemView);
            view = itemView;

            card= itemView.findViewById(R.id.vehicle_cardview);

            address = itemView.findViewById(R.id.address);
            uploader = itemView.findViewById(R.id.uploader);
            timestamp = itemView.findViewById(R.id.days);
            navigate_gmaps=itemView.findViewById(R.id.navigate_gps);
            car_picture=itemView.findViewById(R.id.car_pic);
            accuracy=itemView.findViewById(R.id.accuracy);
            Label=itemView.findViewById(R.id.carType);
            proof =itemView.findViewById(R.id.proof_card);
            whatsapp_img =itemView.findViewById(R.id.whatsapp);


        //    accuracy = itemView.findViewById(R.id.textView_site_id);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }
}
