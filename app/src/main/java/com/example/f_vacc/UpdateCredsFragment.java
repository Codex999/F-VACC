package com.example.f_vacc;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class UpdateCredsFragment extends Fragment {

    Context currentContext;
    String userPassword, username;
    EditText etNewUsername, etNewPassword, etCurrentPassword;
    Button btUpdateCreads;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update_creds, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etNewUsername = view.findViewById(R.id.etNewUsername);
        etNewPassword = view.findViewById(R.id.etNewPassword);
        etCurrentPassword = view.findViewById(R.id.etCurrentPassword);
        btUpdateCreads = view.findViewById(R.id.btUpdateCreds);

        btUpdateCreads.setOnClickListener(v -> {
            if(!this.userPassword.contentEquals(etCurrentPassword.getText().toString())){
                Snackbar.make(v, "Current password incorrect", Snackbar.LENGTH_LONG).show();
            } else {
                DatabaseAccess updatetCreds = new DatabaseAccess(currentContext);
                updatetCreds.executeNonQuery("UPDATE persons SET username = '" + etNewUsername.getText().toString() + "', password = '" + etNewPassword.getText().toString() + "' WHERE id = " + readFile());
                Snackbar.make(v, "Credentials updated successfully!", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        currentContext = context;
        DatabaseAccess dbGetCreds = new DatabaseAccess(context);
        dbGetCreds.executeQuery("SELECT \"creds\", username, password FROM persons WHERE id = " + readFile());
    }

    public void setPassword(List<String[]> data){
        this.username = data.get(0)[1];
        this.userPassword = data.get(0)[2];

        etNewUsername.setText(this.username);
        etNewPassword.setText(this.userPassword);
    }

    private String readFile() {
        File fileEvents = new File(currentContext.getFilesDir()+"/text/config");
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileEvents));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            br.close();
        } catch (IOException e) { }
        String result = text.toString();
        return result;
    }
}