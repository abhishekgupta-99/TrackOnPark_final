package com.spit.CarTrack;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.filter.entity.ImageFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.vincent.filepicker.activity.ImagePickActivity.IS_NEED_CAMERA;

public class Proof extends AppCompatActivity implements View.OnClickListener {

    ImageView camera_click_picture;
    CircleImageView file_attach;
    Button upload_btn;
    EditText et1, et2;
    Spinner sp;
    Uri photoURI;
    Async_proof_uploader progress;
    Uri tempUri;
    private Uri uri;
    GPSTracker gps;

    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int REQUEST_LOCATION = 1;
    private static final String TAG = "CapturePicture";
    static final int REQUEST_PICTURE_CAPTURE = 1;
    private ImageView image;
    private String pictureFilePath;
    private String deviceIdentifier;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    Bitmap imageBitmap;
    byte[] dataBAOS;
    int rresultCode;

    DatabaseReference databaseReference, databaseReference1;
    public String zoneImageURI = null;

    File mPhotoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proof);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }


        camera_click_picture = findViewById(R.id.camera_click);
        file_attach = findViewById(R.id.image);
        upload_btn = findViewById(R.id.upload_proof);
        progressBar = findViewById(R.id.progressBar);
        upload_btn.setOnClickListener(this);
        upload_btn.setClickable(false);
        upload_btn.setBackgroundColor(getResources().getColor(R.color.grey_100));
        camera_click_picture.setOnClickListener(this);


    }

    public void attach_file(View view) {

        Intent intent1 = new Intent(this, ImagePickActivity.class);
        intent1.putExtra(IS_NEED_CAMERA, true);
        intent1.putExtra(Constant.MAX_NUMBER, 1);
        startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);
    }


    @Override
    public void onClick(View view) {

        if (view == upload_btn) {
            //evaluate_model();
            execute_asyncc();
            //  Uri picture_url=progress.getFirebase_storage_picture();
            // Log.d("Picture urlll",picture_url+"");
            upload_btn.setClickable(false);
            upload_btn.setBackgroundColor(getResources().getColor(R.color.grey_100));


        }


        if (view == camera_click_picture) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {

                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    // Error occurred while creating the File
                }
                if (photoFile != null) {
                    photoURI = FileProvider.getUriForFile(this,
                            BuildConfig.APPLICATION_ID + ".provider",
                            photoFile);

                    Toast.makeText(this, photoURI+"", Toast.LENGTH_SHORT).show();

                    mPhotoFile = photoFile;
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                    //  startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    startActivityForResult(intent, CAMERA_REQUEST_CODE);

                }


            }
        }

    }

    private File createImageFile () throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFile = File.createTempFile(mFileName, ".jpg", storageDir);
        return mFile;
    }

    public void execute_asyncc() {

        Log.d("PHOTO KA URL",Uri.fromFile(new File(photoURI+""))+"");

        //Uri.fromFile(new File(photoURI+""));

        if(photoURI!=null) {
            Log.d("Enter","entered");
            progress = new Async_proof_uploader(this, photoURI, progressBar);
            progress.execute();
        }
        else
            Toast.makeText(this, "Cannot get Image File Path", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data){
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {

            // Bundle extras = data.getExtras();
            //imageBitmap = (Bitmap) extras.get("data");

            //  tempUri = data.getData();
            //   mSelectImage.setImageURI(mImageUri);
            //tempUri = getImageUri(getApplicationContext(), imageBitmap);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            //compress size commented

            //     imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            //     dataBAOS = baos.toByteArray();

            rresultCode = resultCode;

            file_attach.setImageURI(photoURI);


            // file_attach.setImageBitmap(imageBitmap);

            upload_btn.setClickable(true);
            upload_btn.setBackgroundColor(getResources().getColor(R.color.blue400));

            super.onActivityResult(requestCode, resultCode, data);

            //  super.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode== Constant.REQUEST_CODE_PICK_IMAGE)
        {
            if (resultCode == RESULT_OK) {
                ArrayList<ImageFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_IMAGE);

                //Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(list.get(0)));

                photoURI= Uri.fromFile(new File(list.get(0).getPath()));

                Log.d(  "pathhh",list.get(0).getPath()+"");
                Toast.makeText(this, "file path : "+photoURI+"", Toast.LENGTH_SHORT).show();

                file_attach.setImageURI(Uri.fromFile(new File(list.get(0).getPath())));
                upload_btn.setClickable(true);
                upload_btn.setBackgroundColor(getResources().getColor(R.color.blue400));

                //file_attach.setImageDrawable(Drawable.createFromPath(list.get(0).toString()));

            }
        }
    }
}