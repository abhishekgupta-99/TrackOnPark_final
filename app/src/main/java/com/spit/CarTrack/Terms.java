package com.spit.CarTrack;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

public class Terms extends AppCompatActivity {

    PDFView terms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        terms= findViewById(R.id.pdfterms);

        terms.fromAsset("terms.pdf").load();
    }
}
