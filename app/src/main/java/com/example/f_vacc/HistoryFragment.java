package com.example.f_vacc;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends Fragment{
    private RecyclerView rvHistory;
    Context currentContext;
    TextView tvNoData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.rvHistory = view.findViewById(R.id.rvHistoryContainer);
        this.tvNoData = view.findViewById(R.id.tvNoData);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        currentContext = context;
        DatabaseAccess dbGetHistory = new DatabaseAccess(context);
        dbGetHistory.executeQuery("SELECT \"historyDetails\", establishments.name, DATE_FORMAT(person_tracks.time_in, \"%M %d %Y %h:%i %p\"), DATE_FORMAT(person_tracks.time_out, \"%M %d %Y %h:%i %p\"), person_tracks.id FROM establishments JOIN person_tracks ON establishments.id = person_tracks.establishment_id JOIN persons ON persons.id = person_tracks.person_id WHERE person_tracks.person_id = " + readFile());

    }

    public void listHistory(List<String[]> data){
        if(data.size() == 0){
            tvNoData.setVisibility(View.VISIBLE);
        } else {
            tvNoData.setVisibility(View.GONE);
        }

        List<History> histories = new ArrayList<>();
        for(String[] row : data){
            histories.add(new History(row[1], row[2], row[3], row[4]));
        }

        HistoryAdapter historyAdapter = new HistoryAdapter(histories, currentContext);

        rvHistory.setAdapter(historyAdapter);
        rvHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
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