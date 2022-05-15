package com.example.f_vacc;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.google.android.material.card.MaterialCardView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.Headers;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUp extends AppCompatActivity {
    private EditText etSignupEmail, etSignupUsername, etSignupPassword, etSignupConfirmPassword, etSignupFirstname, etSignupMiddlename, etSignupLastname, etAddressBlock, etAddressStreet, etAddressBarangay, etAddressCity, etAddressZip, etSignupFirstdose, etSignupFirstdoseSite, etSignupFirstdoseDate, etSignupSeconddose, etSignupSeconddoseSite, etSignupSeconddoseDate, etSignupBooster, etSignupBoosterDate, etSignupBoosterSite, etSignupOtp;
    private Button btUploadUserImage, btUploadVaccImage, btSignup;
    private Context currentContext = this;
    private MaterialCardView cvUserImage, cvVaccCard;
    private CheckBox cbCertify;
    private TextView tvPasswordError, tvOtpError, tvSignupLabel4;
    private final OkHttpClient client = new OkHttpClient();
    private Bitmap userImage, userVacccard;
    private SmartMaterialSpinner spDose;
    private String etSignupEmailString, etSignupUsernameString, etSignupPasswordString, etSignupConfirmPasswordString, etSignupFirstnameString, etSignupMiddlenameString, etSignupLastnameString, etAddressBlockString, etAddressStreetString, etAddressBarangayString, etAddressCityString, etAddressZipString, etSignupFirstdoseString, etSignupFirstdoseSiteString, etSignupFirstdoseDateString, etSignupSeconddoseString, etSignupSeconddoseSiteString, etSignupSeconddoseDateString, etSignupBoosterString, doseString, etSignupBoosterDateString, etSignupBoosterSiteString, otpString;
    private ScrollView svFirststep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        etSignupEmail = findViewById(R.id.etSignupEmail);
        etSignupUsername = findViewById(R.id.etSignupUsername);
        etSignupPassword = findViewById(R.id.etSignupPassword);
        etSignupConfirmPassword = findViewById(R.id.etSignupConfirmPassword);
        etSignupFirstname = findViewById(R.id.etSignupFirstname);
        etSignupMiddlename = findViewById(R.id.etSignupMiddlename);
        etSignupLastname = findViewById(R.id.etSignupLastname);
        etAddressBlock = findViewById(R.id.etAddressBlock);
        etAddressStreet = findViewById(R.id.etAddressStreet);
        etAddressBarangay = findViewById(R.id.etAddressBarangay);
        etAddressCity = findViewById(R.id.etAddressCity);
        etAddressZip = findViewById(R.id.etAddressZip);
        etSignupFirstdose = findViewById(R.id.etSignupFirstdose);
        etSignupFirstdoseSite = findViewById(R.id.etSignupFirstdoseSite);
        etSignupFirstdoseDate = findViewById(R.id.etSignupFirstdoseDate);
        etSignupSeconddose = findViewById(R.id.etSignupSeconddose);
        etSignupSeconddoseSite = findViewById(R.id.etSignupSeconddoseSite);
        etSignupSeconddoseDate = findViewById(R.id.etSignupSeconddoseDate);
        btUploadUserImage = findViewById(R.id.btUploadUserImage);
        btUploadVaccImage = findViewById(R.id.btUploadVaccImage);
        btSignup = findViewById(R.id.btSignup);
        cvUserImage = findViewById(R.id.cvUserImage);
        cvVaccCard = findViewById(R.id.cvVaccCard);
        cbCertify = findViewById(R.id.cbCertify);
        tvPasswordError = findViewById(R.id.tvPasswordError);
        spDose = findViewById(R.id.spDose);
        etSignupBooster = findViewById(R.id.etSignupBooster);
        etSignupBoosterDate = findViewById(R.id.etSignupBoosterDate);
        etSignupBoosterSite = findViewById(R.id.etSignupBoosterSite);
        svFirststep = findViewById(R.id.svFirststep);
        etSignupOtp = findViewById(R.id.etSignupOtp);
        tvOtpError = findViewById(R.id.tvOtpError);
        tvSignupLabel4 = findViewById(R.id.tvSignupLabel4);

        etSignupFirstdose.setVisibility(View.GONE);
        etSignupFirstdoseDate.setVisibility(View.GONE);
        etSignupFirstdoseSite.setVisibility(View.GONE);
        etSignupSeconddose.setVisibility(View.GONE);
        etSignupSeconddoseDate.setVisibility(View.GONE);
        etSignupSeconddoseSite.setVisibility(View.GONE);
        etSignupBooster.setVisibility(View.GONE);
        etSignupBoosterSite.setVisibility(View.GONE);
        etSignupBoosterDate.setVisibility(View.GONE);

        ActivityResultLauncher<String> userImagePicker = registerForActivityResult(new
                        ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        try {
                            userVacccard = getThumbnail(uri);
                            Drawable d = new BitmapDrawable(currentContext.getResources(), userVacccard);
                            btUploadUserImage.setVisibility(View.INVISIBLE);
                            cvUserImage.setBackgroundDrawable(d);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

        ActivityResultLauncher<String> vaccCardPicker = registerForActivityResult(new
                        ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        try {
                            userImage = getThumbnail(uri);
                            Drawable d = new BitmapDrawable(currentContext.getResources(), userImage);
                            btUploadVaccImage.setVisibility(View.INVISIBLE);
                            cvVaccCard.setBackgroundDrawable(d);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

        etSignupConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable e) {
                if(etSignupConfirmPassword.getText().toString().contentEquals(etSignupPassword.getText().toString())){
                    tvPasswordError.setVisibility(View.GONE);
                } else {
                    tvPasswordError.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //nothing needed here...
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //nothing needed here...
            }
        });

        etSignupFirstdoseDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    showDatePicker(currentContext, etSignupFirstdoseDate);
                }
            }
        });

        etSignupSeconddoseDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                showDatePicker(currentContext, etSignupSeconddoseDate);
                }
            }
        });

        etSignupBoosterDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                showDatePicker(currentContext, etSignupBoosterDate);
                }
            }
        });

        btUploadUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userImagePicker.launch("image/*");
            }
        });

        cvUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userImagePicker.launch("image/*");
            }
        });

        btUploadVaccImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaccCardPicker.launch("image/*");
            }
        });

        cvVaccCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaccCardPicker.launch("image/*");
            }
        });

        cbCertify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbCertify.isChecked()){
                    btSignup.setEnabled(true);
                    btSignup.setBackgroundColor(Color.WHITE);
                } else {
                    btSignup.setEnabled(false);
                    btSignup.setBackgroundColor(Color.DKGRAY);
                }
            }
        });

        etSignupOtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(etSignupOtp.getText().toString().length() == 4){
                    if(etSignupOtp.getText().toString().contentEquals(otpString)){
                        onBackPressed();
                        finish();
                    } else {
                        tvOtpError.setVisibility(View.VISIBLE);
                    }
                } else {
                    tvOtpError.setVisibility(View.GONE);
                }
            }
        });

        List<String> doseList = new ArrayList<>();
        doseList.add("First Dose");
        doseList.add("Second Dose");
        doseList.add("Booster");
        spDose.setItem(doseList);
        spDose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                doseString = doseList.get(position);
                        switch (position){
                    case 0:
                        etSignupFirstdose.setVisibility(View.VISIBLE);
                        etSignupFirstdoseDate.setVisibility(View.VISIBLE);
                        etSignupFirstdoseSite.setVisibility(View.VISIBLE);
                        etSignupSeconddose.setVisibility(View.GONE);
                        etSignupSeconddoseDate.setVisibility(View.GONE);
                        etSignupSeconddoseSite.setVisibility(View.GONE);
                        etSignupBooster.setVisibility(View.GONE);
                        etSignupBoosterSite.setVisibility(View.GONE);
                        etSignupBoosterDate.setVisibility(View.GONE);
                        etSignupSeconddoseString = "";
                        etSignupSeconddoseDateString = "";
                        etSignupSeconddoseSiteString = "";
                        etSignupBoosterString = "";
                        etSignupBoosterSiteString = "";
                        etSignupBoosterDateString = "";
                        break;
                    case 1:
                        etSignupFirstdose.setVisibility(View.VISIBLE);
                        etSignupFirstdoseDate.setVisibility(View.VISIBLE);
                        etSignupFirstdoseSite.setVisibility(View.VISIBLE);
                        etSignupSeconddose.setVisibility(View.VISIBLE);
                        etSignupSeconddoseDate.setVisibility(View.VISIBLE);
                        etSignupSeconddoseSite.setVisibility(View.VISIBLE);
                        etSignupBooster.setVisibility(View.GONE);
                        etSignupBoosterSite.setVisibility(View.GONE);
                        etSignupBoosterDate.setVisibility(View.GONE);
                        etSignupBoosterString = "";
                        etSignupBoosterSiteString = "";
                        etSignupBoosterDateString = "";
                        break;
                    case 2:
                        etSignupFirstdose.setVisibility(View.VISIBLE);
                        etSignupFirstdoseDate.setVisibility(View.VISIBLE);
                        etSignupFirstdoseSite.setVisibility(View.VISIBLE);
                        etSignupSeconddose.setVisibility(View.VISIBLE);
                        etSignupSeconddoseDate.setVisibility(View.VISIBLE);
                        etSignupSeconddoseSite.setVisibility(View.VISIBLE);
                        etSignupBooster.setVisibility(View.VISIBLE);
                        etSignupBoosterSite.setVisibility(View.VISIBLE);
                        etSignupBoosterDate.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        btSignup.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                otpString = String.valueOf(new Double((Math.random() * (9999 - 1000 + 1) + 1000)).intValue());
                Toast.makeText(currentContext, otpString, Toast.LENGTH_LONG).show();

                etSignupEmailString = etSignupEmail.getText().toString();
                etSignupUsernameString = etSignupUsername.getText().toString();
                etSignupPasswordString = etSignupPassword.getText().toString();
                etSignupConfirmPasswordString = etSignupConfirmPassword.getText().toString();
                etSignupFirstnameString = etSignupFirstname.getText().toString();
                etSignupMiddlenameString = etSignupMiddlename.getText().toString();
                etSignupLastnameString = etSignupLastname.getText().toString();
                etAddressBlockString = etAddressBlock.getText().toString();
                etAddressStreetString = etAddressStreet.getText().toString();
                etAddressBarangayString = etAddressBarangay.getText().toString();
                etAddressCityString = etAddressCity.getText().toString();
                etAddressZipString = etAddressZip.getText().toString();
                etSignupFirstdoseString = etSignupFirstdose.getText().toString();
                etSignupFirstdoseSiteString = etSignupFirstdoseSite.getText().toString();
                etSignupFirstdoseDateString = etSignupFirstdoseDate.getText().toString();
                etSignupSeconddoseString = etSignupSeconddose.getText().toString();
                etSignupSeconddoseSiteString = etSignupSeconddoseSite.getText().toString();
                etSignupSeconddoseDateString = etSignupSeconddoseDate.getText().toString();
                etSignupBoosterString = etSignupBooster.getText().toString();
                etSignupBoosterSiteString = etSignupBoosterSite.getText().toString();
                etSignupBoosterDateString = etSignupBoosterDate.getText().toString();

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
                LocalDateTime now = LocalDateTime.now();
                String fileName = etSignupUsernameString + "_" + dtf.format(now);

                DatabaseAccess dbInsertInfos = new DatabaseAccess(currentContext);
                dbInsertInfos.executeNonQuery("INSERT INTO persons(firstname, middlename, lastname, dose, fdose_date, sdose_date, booster_date, fvaccine, svaccine, booster, fvacsite, svacsite, bvacsite, block, street, brgy, city, zip, username, password, email, profile, vacc_card) VALUES('" +
                        etSignupFirstnameString + "', '" +
                        etSignupMiddlenameString + "', '" +
                        etSignupLastnameString + "', '" +
                        doseString + "', '" +
                        etSignupFirstdoseDateString + "', '" +
                        etSignupSeconddoseDateString + "', '" +
                        etSignupBoosterDateString + "', '" +
                        etSignupFirstdoseString + "', '" +
                        etSignupSeconddoseString + "', '" +
                        etSignupBoosterString + "', '" +
                        etSignupFirstdoseSiteString + "', '" +
                        etSignupSeconddoseSiteString + "', '" +
                        etSignupBoosterSiteString + "', '" +
                        etAddressBlockString + "', '" +
                        etAddressStreetString + "', '" +
                        etAddressBarangayString + "', '" +
                        etAddressCityString + "', '" +
                        etAddressZipString + "', '" +
                        etSignupUsernameString + "', '" +
                        etSignupPasswordString + "', '" +
                        etSignupEmailString + "', '" +
                        fileName + ".jpg', '" +
                        fileName+ ".jpg'" +
                        ")");

                new RunHttpRequest().execute(new String[]{fileName, convertToBase64(userVacccard), "https://ucc-bsit-capstone-2021.com/VC Monitoring/monitoring/uploadVaccCard.php"});

                new RunHttpRequest().execute(new String[]{fileName, convertToBase64(userImage), "https://ucc-bsit-capstone-2021.com/VC Monitoring/monitoring/userImageUpload.php"});

                new RunHttpRequest().execute(new String[]{otpString, etSignupEmailString, "https://ucc-bsit-capstone-2021.com/VC Monitoring/monitoring/sendOtp.php"});

                tvSignupLabel4.setText("An OTP was sent to " + etSignupEmailString + ". Please check your email to get the code and type it here.");
                svFirststep.setVisibility(View.GONE);
                tvSignupLabel4.setVisibility(View.VISIBLE);
                etSignupOtp.setVisibility(View.VISIBLE);
            }
        });


    }

    private void showDatePicker(Context context, EditText editText){
        Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                String myFormat="YYYY-MM-dd";
                SimpleDateFormat dateFormat= null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    dateFormat = new SimpleDateFormat(myFormat, Locale.US);
                }
                editText.setText(dateFormat.format(myCalendar.getTime()));
            }
        };
        new DatePickerDialog(context, date, myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public Bitmap getThumbnail(Uri uri) throws FileNotFoundException, IOException {
        InputStream input = this.getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither=true;//optional
        onlyBoundsOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();

        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
            return null;
        }

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        double ratio = (originalSize > 512) ? (originalSize / 512) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true; //optional
        bitmapOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//
        input = this.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio){
        int k = Integer.highestOneBit((int)Math.floor(ratio));
        if(k==0) return 1;
        else return k;
    }

    private void uploadImage(String imageName, String base64String, String url) throws IOException {

    }

    private String convertToBase64(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();

        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }



    class RunHttpRequest extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(params[0], params[1])
                    .build();

            Request request = new Request.Builder()
                    .url(params[2])
                    .post(requestBody)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) try {
                    throw new IOException("Unexpected code " + response);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Headers responseHeaders = response.headers();
                for (int i = 0; i < responseHeaders.size(); i++) {
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                try {
                    Log.d("ERROR: ", response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {

            super.onPostExecute(unused);
        }
    }
}

