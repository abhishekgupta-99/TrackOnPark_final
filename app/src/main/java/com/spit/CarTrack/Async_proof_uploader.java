package com.spit.CarTrack;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

public class Async_proof_uploader extends AsyncTask<String, String, String> {

    Context ctx;
    ProgressDialog dialog;
    ProgressBar progressBar;
    byte[] imagearray;
    GPSTracker gps;
    int currentprogress=0;
    Bitmap image;
    Uri tempuri;
    Uri proof_uri;
    String firebase_storage_picture;
    private ProgressDialog pgdialog;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    //FirebaseVisionImageLabeler image_labeler;



    public String getFirebase_storage_picture() {
        return firebase_storage_picture;
    }




    public Async_proof_uploader(Context ctx, Uri dataBaos, ProgressBar progressBar) {
        this.ctx=ctx;
        //uploadfunc= new Click();
        pref = ctx.getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        pgdialog=new ProgressDialog(ctx);

        this.progressBar=progressBar;
        progressBar.setMax(100);
        this.tempuri=dataBaos;
    //    this.image_labeler=labeler;
    }


    @Override
    protected String doInBackground(String... voids) {



        upload_in_background_storage();


        //return upload_url;
        return null;
    }


    private void upload_in_background_storage() {
        FirebaseStorage storage;
        final StorageReference storageRef;
        storage = FirebaseStorage.getInstance();

        storageRef = storage.getReference().child("" + new Date().getTime());

        final UploadTask uploadTask = storageRef.putFile(tempuri);

        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                System.out.println("Upload is " + progress + "% done");
                currentprogress = (int) progress;

                publishProgress(currentprogress+"");
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(currentprogress);


            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("Upload is paused");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                      //  Toast.makeText(ctx, "ENTERED SUCCCESSS", Toast.LENGTH_LONG).show();

                       // String[] label_conf= new String[2];
                        Log.d("proof storage url", "onSuccess: uri= "+ "trial");
                       // firebase_storage_picture=uri+"";
                        proof_uri=uri;
                        Log.d("Proof urii storage",proof_uri.toString());
                       // editor.putString("last_proof_upload_url", uri+"");
                        //editor.commit(); // commit changes

                        if(currentprogress==100)
                        {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ctx, "Successfully Uploaded", Toast.LENGTH_SHORT).show();

                        }

                        Proof p=new Proof();
                        p.update_proof_details_firestore(ctx,proof_uri);


                           // uploadfunc.evaluate_model(tempuri,ctx,image_labeler,label_conf);
                     //   Toast.makeText(ctx, "The car is "+label_conf[0]+" , with a confidence of "+  label_conf[1], Toast.LENGTH_LONG).show();
                      //  uploadfunc.get_LatLong(ctx,return_label_conf[0],return_label_conf[1]);




                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ctx, "Please Connect To Internet", Toast.LENGTH_SHORT).show();
            }
        });

     //   return firebase_storage_picture+"";

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setProgress(0);
        progressBar.setVisibility(View.VISIBLE);
        this.pgdialog.setMessage("Progress start");
        this.pgdialog.show();
    }


    @Override
    protected void onPostExecute(String aVoid) {
        super.onPostExecute(aVoid);

      //  firebase_storage_picture=aVoid;

        progressBar.setVisibility(View.GONE);
       // Log.d("Proof urii", proof_uri.toString());

        if (pgdialog.isShowing()) {
            pgdialog.dismiss();
        }
    }


    @Override
    protected void onProgressUpdate(String... values) {

    }

}