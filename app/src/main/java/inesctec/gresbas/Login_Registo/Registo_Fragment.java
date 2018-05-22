package inesctec.gresbas.Login_Registo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import inesctec.gresbas.R;
import inesctec.gresbas.Utils.Utils;
import inesctec.gresbas.Utils.Utils_Registo;


import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

public class Registo_Fragment extends Fragment implements View.OnClickListener {

    private static View view;

    private static FragmentManager fragmentManager;

    private Button button_registar;
    private EditText editText_nome, editText_apelido, editText_email, editText_password, editText_conf_password;

    private ProgressDialog progressDialog;



    private SharedPreferences fakeCache;
    private SharedPreferences.Editor editor;


    String[] LOCATIONSID = {"1", "2"};
    String[] LOCATIONSNAME = {"B 2.2", "B 2.11"};

    ImageView more;

    ImageView takephoto;
    private int GALLERY = 1, CAMERA = 2;
    private static final String IMAGE_DIRECTORY = "/demonuts";

    //------------------------------------------------------------------------------------------------- Class Default Override's ---

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_registo, container, false);

        declarations();

        OnClickListener();

        int i;
        String[] SPINNERLIST = new String[2];
        for (i = 0; i <= 1; i++) {
            SPINNERLIST[i] = "ID: " + LOCATIONSID[i] + " | Name: " + LOCATIONSNAME[i];
        }


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, SPINNERLIST);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MaterialBetterSpinner betterSpinner = (MaterialBetterSpinner) view.findViewById(R.id.list_location);
        betterSpinner.setAdapter(arrayAdapter);


        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "Raleway/Raleway-Medium.ttf");
        EditText edtUserName = (EditText) view.findViewById(R.id.fragment_registo_editText_email);
        EditText edtPassword = (EditText) view.findViewById(R.id.fragment_registo_editText_password);
        Button btnRegisto = (Button) view.findViewById(R.id.fragment_registo_button_registar);
        edtUserName.setTypeface(typeface);
        edtPassword.setTypeface(typeface);
        btnRegisto.setTypeface(typeface);
        betterSpinner.setTypeface(typeface);

        more = (ImageView) view.findViewById(R.id.more);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bitmap;
        more.setImageDrawable(null);


        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();
                    takephoto.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            takephoto.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();
        }

    }


    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }


    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }


    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getActivity(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }
    //------------------------------------------------------------------------------------------------- Random ---

    //onClick Listener
    public void OnClickListener() {

        button_registar.setOnClickListener(this);

    }

    //Declare Everyting
    public void declarations() {

        //Fake_Cache
        fakeCache = getActivity().getSharedPreferences("Fake_Cache_File", MODE_PRIVATE);
        editor = fakeCache.edit();

        progressDialog = new ProgressDialog(getContext());

        fragmentManager = getActivity().getSupportFragmentManager();


        takephoto = (ImageView) view.findViewById(R.id.takephotobutton);
        //TextViews, Buttons, EditTexts, etc...

        editText_email = (EditText) view.findViewById(R.id.fragment_registo_editText_email);
        editText_password = (EditText) view.findViewById(R.id.fragment_registo_editText_password);
        button_registar = (Button) view.findViewById(R.id.fragment_registo_button_registar);

        takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });

    }

    //OnClick Override
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.fragment_registo_button_registar:

                validar_campos_registar();

                break;

        }

    }

    //Validar Campos para Registo
    public void validar_campos_registar() {

        if (fakeCache.getBoolean("internet_connection", false)) {

            final String getNome = editText_nome.getText().toString();
            final String getApelido = editText_apelido.getText().toString();
            final String getEmail = editText_email.getText().toString();
            String getPassword = editText_password.getText().toString();
            final String getConfPassword = editText_conf_password.getText().toString();

            //Check email pattern
            Pattern pattern = Pattern.compile(Utils.email_validation_pattern);
            Matcher matcher = pattern.matcher(getEmail);

            if (getNome.equals("") || getApelido.equals("") || getEmail.equals("") || getPassword.equals("") || getConfPassword.equals("") || !getPassword.equals(getConfPassword) || !matcher.find() || getPassword.length() < 6) {

                if (getNome.equals("")) {
                    editText_nome.setError("Campo obrigatório");
                }

                if (getApelido.equals("")) {
                    editText_apelido.setError("Campo obrigatório");
                }

                if (getEmail.equals("")) {
                    editText_email.setError("Campo obrigatório");
                }

                if (getPassword.equals("")) {
                    editText_password.setError("Campo obrigatório");
                }

                if (getConfPassword.equals("")) {
                    editText_conf_password.setError("Campo obrigatório");
                }

                if (getPassword.length() < 6 && !getPassword.equals("") && !getConfPassword.equals("")) {
                    editText_password.setError("A password deve ter pelo menos seis caracteres");
                }

                if (getPassword.length() >= 6 && !getPassword.equals(getConfPassword) && !getPassword.equals("") && !getConfPassword.equals("")) {
                    editText_conf_password.setError("Password diferente");
                }

                if (!getEmail.equals("") && !matcher.find()) {
                    editText_email.setError("Email inválido");
                }


            } else {

                progressDialog.setMessage("A registar");
                progressDialog.setCancelable(false);
                progressDialog.show();


                Utils.semInternetDialog(getContext());

            }

        }
    }
}
