package com.example.f_vacc;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.f_vacc.databinding.ActivityDashboard2Binding;
import com.heinrichreimersoftware.materialdrawer.DrawerActivity;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Dashboard extends DrawerActivity implements DatabaseAccessCallback {

    private Context currentContext = this;
    private HistoryFragment fgHistory = new HistoryFragment();
    private VaccCardFragment fgVaccCard = new VaccCardFragment();
    private UpdateCredsFragment fgUpdateCred = new UpdateCredsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard2);

        DatabaseAccess dbGetDetails = new DatabaseAccess(currentContext);
        dbGetDetails.executeQuery("SELECT * FROM persons WHERE id = " + readFile());

        getSupportFragmentManager().beginTransaction().replace(R.id.flRoot, fgHistory).commit();

        addItem(
                new DrawerItem()
                        .setImage(getResources().getDrawable(R.drawable.ic_history))
                        .setTextPrimary("History")
                        .setTextSecondary("Get establishments scans history")
                        .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                            @Override
                            public void onClick(DrawerItem drawerItem, long id, int position) {
                                getSupportFragmentManager().beginTransaction().replace(R.id.flRoot, fgHistory).commit();
                                closeDrawer();
                            }
                        })
        );
        addDivider();
        addItem(
                new DrawerItem()
                        .setImage(getResources().getDrawable(R.drawable.ic_vacc))
                        .setTextPrimary("Vaccination Card")
                        .setTextSecondary("View Vaccination Card Details")
                        .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                            @Override
                            public void onClick(DrawerItem drawerItem, long id, int position) {
                                getSupportFragmentManager().beginTransaction().replace(R.id.flRoot, fgVaccCard).commit();
                                closeDrawer();
                            }
                        })
        );
        addDivider();
        addItem(
                new DrawerItem()
                        .setImage(getResources().getDrawable(R.drawable.ic_update_creds))
                        .setTextPrimary("Update Credentials")
                        .setTextSecondary("Update your username and password")
                        .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                            @Override
                            public void onClick(DrawerItem drawerItem, long id, int position) {
                                getSupportFragmentManager().beginTransaction().replace(R.id.flRoot, fgUpdateCred).commit();
                                closeDrawer();
                            }
                        })
        );
        addDivider();
        addItem(
                new DrawerItem()
                        .setImage(getResources().getDrawable(R.drawable.ic_logout))
                        .setTextPrimary("Logout")
                        .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                            @Override
                            public void onClick(DrawerItem drawerItem, long id, int position) {
                                logout();
                            }
                        })
        );
    }




    private void logout(){
        File file = new File(Dashboard.this.getFilesDir(), "text");
        try {
            File gpxfile = new File(file, "config");
            FileWriter writer = new FileWriter(gpxfile);
            writer.write("0");
            writer.flush();
            writer.close();
        } catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }

        Intent mIntent = new Intent(this, Login.class);
        startActivity(mIntent);
        finish();
    }

    private String readFile() {
        File fileEvents = new File(Dashboard.this.getFilesDir()+"/text/config");
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

    @Override
    public void QueryResponse(List<String[]> data) {
        if(data != null){
            if(data.size() != 0){
                switch (data.get(0)[0]){
                    case "historyDetails":
                        fgHistory.listHistory(data);
                        break;
                    case "vaccDetails":
                        fgVaccCard.populateVaccinationInfo(data);
                        break;
                    case "creds":
                        fgUpdateCred.setPassword(data);
                        break;
                }
            }
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        Bitmap mIcon11 = null;
        String urldisplay, name, mname, lname;

        protected Bitmap doInBackground(String... urls) {
            urldisplay = urls[0];
            name = urls[1];
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            Toast.makeText(currentContext, "Clicked profile #" + "id", Toast.LENGTH_SHORT).show();
            addProfile(
                    new DrawerProfile()
                            .setRoundedAvatar(
                                    currentContext,
                                    result
                            )
                            .setBackground(getResources().getDrawable(R.drawable.bg))
                            .setName(name)
                            .setDescription("Person")
                            .setOnProfileClickListener(new DrawerProfile.OnProfileClickListener() {
                                @Override
                                public void onClick(DrawerProfile drawerProfile, long id) {
                                    Toast.makeText(currentContext, "Clicked profile #" + id, Toast.LENGTH_SHORT).show();
                                }
                            })
            );
        }
    }
}