package com.example.f_vacc;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class VaccCardFragment extends Fragment {

    Context currentContext;
    TextView tvFdVaccineName, tvFdVaccineSite, tvFdVaccineDate, tvSdVaccineName, tvSdVaccineSite, tvSdVaccineDate, tvBdVaccineName, tvBdVaccineSite, tvBdVaccineDate;
    String tvFdVaccineNameString, tvFdVaccineSiteString, tvFdVaccineDateString, tvSdVaccineNameString, tvSdVaccineSiteString, tvSdVaccineDateString, tvBdVaccineNameString, tvBdVaccineSiteString, tvBdVaccineDateString;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vacc_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvFdVaccineName = view.findViewById(R.id.tvFdVaccineName);
        tvFdVaccineSite = view.findViewById(R.id.tvFdVaccineSite);
        tvFdVaccineDate = view.findViewById(R.id.tvFdVaccineDate);
        tvSdVaccineName = view.findViewById(R.id.tvSdVaccineName);
        tvSdVaccineSite = view.findViewById(R.id.tvSdVaccineSite);
        tvSdVaccineDate = view.findViewById(R.id.tvSdVaccineDate);
        tvBdVaccineName = view.findViewById(R.id.tvBdVaccineName);
        tvBdVaccineSite = view.findViewById(R.id.tvBdVaccineSite);
        tvBdVaccineDate = view.findViewById(R.id.tvBdVaccineDate);

        tvFdVaccineNameString = tvFdVaccineName.getText().toString();
        tvFdVaccineSiteString = tvFdVaccineSite.getText().toString();
        tvFdVaccineDateString = tvFdVaccineDate.getText().toString();
        tvSdVaccineNameString = tvSdVaccineName.getText().toString();
        tvSdVaccineSiteString = tvSdVaccineSite.getText().toString();
        tvSdVaccineDateString = tvSdVaccineDate.getText().toString();
        tvBdVaccineNameString = tvBdVaccineName.getText().toString();
        tvBdVaccineSiteString = tvBdVaccineSite.getText().toString();
        tvBdVaccineDateString = tvBdVaccineDate.getText().toString();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        currentContext = context;

        DatabaseAccess dbGetVaccInfo = new DatabaseAccess(context);
        dbGetVaccInfo.executeQuery("SELECT \"vaccDetails\", persons.* FROM persons WHERE id = " + readFile());
    }

    public void populateVaccinationInfo(List<String[]> data){
        tvFdVaccineNameString = tvFdVaccineNameString + data.get(0)[11];
        tvFdVaccineSiteString = tvFdVaccineSiteString + data.get(0)[14];
        tvFdVaccineDateString = tvFdVaccineDateString + data.get(0)[8];
        tvSdVaccineNameString = tvSdVaccineNameString + data.get(0)[12];
        tvSdVaccineSiteString = tvSdVaccineSiteString + data.get(0)[15];
        tvSdVaccineDateString = tvSdVaccineDateString + data.get(0)[9];
        tvBdVaccineNameString = tvBdVaccineNameString + data.get(0)[13];
        tvBdVaccineSiteString = tvBdVaccineSiteString + data.get(0)[16];
        tvBdVaccineDateString = tvBdVaccineDateString + data.get(0)[10];

        tvFdVaccineName.setText(tvFdVaccineNameString);
        tvFdVaccineSite.setText(tvFdVaccineSiteString);
        tvFdVaccineDate.setText(tvFdVaccineDateString);
        tvSdVaccineName.setText(tvSdVaccineNameString);
        tvSdVaccineSite.setText(tvSdVaccineSiteString);
        tvSdVaccineDate.setText(tvSdVaccineDateString);
        tvBdVaccineName.setText(tvBdVaccineNameString);
        tvBdVaccineSite.setText(tvBdVaccineSiteString);
        tvBdVaccineDate.setText(tvBdVaccineDateString);
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