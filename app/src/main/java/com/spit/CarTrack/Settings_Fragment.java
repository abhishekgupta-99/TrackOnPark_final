package com.spit.CarTrack;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Settings_Fragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_settings__fragment, container, false);


        Button button = view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Help.class);
                intent.putExtra("some", "some data");
                startActivity(intent);
            }
        });

        Button buttonLog = view.findViewById(R.id.logout);
        buttonLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MainActivity.class);
                intent.putExtra("some", "some data");
                startActivity(intent);
            }
        });

        Button buttonAbout = view.findViewById(R.id.about);
        buttonAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),About.class);
                intent.putExtra("some", "some data");
                startActivity(intent);
            }
        });

        Button buttonContact = view.findViewById(R.id.contact);
        buttonContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Contact.class);
                intent.putExtra("some", "some data");
                startActivity(intent);
            }
        });

        Button buttonTerm = view.findViewById(R.id.conditions);
        buttonTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Terms.class);
                intent.putExtra("some", "some data");
                startActivity(intent);
            }
        });

        Button buttonPrivate = view.findViewById(R.id.privacy);
        buttonPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),PrivacyPolicy.class);
                intent.putExtra("some", "some data");
                startActivity(intent);
            }
        });




        return view;
    }}

