package com.example.mediaserver.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mediaserver.DirectoryChooserDialog;
import com.example.mediaserver.MainActivity;
import com.example.mediaserver.MediaObject;
import com.example.mediaserver.R;
import com.example.mediaserver.ServerHandler;
import com.example.mediaserver.Setup;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.kusu.loadingbutton.LoadingButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MediaUpload extends AppCompatActivity {

    private List<MediaObject> localMedia;
    private List<MediaObject> serverMedia;
    public LoadingButton upload;
    public LoadingButton checkLocalButton;
    private String chosenDirFromPref;
    private String usernameFromSharedPref;
    private String passwordFromSharedPref;
    private String ipAddressFromSharedPref;

    private TextView statusView;
    private TextView localMediaStatus;
    private List<String> statusMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_upload);
        if(isSharedPrefReady())
        {
            this.upload = findViewById(R.id.uploadButton);
            this.checkLocalButton = findViewById(R.id.checkLocalButton);
            this.statusView = findViewById(R.id.editTextTextMultiLine);
            this.localMediaStatus = findViewById(R.id.statusView);
            this.statusMessages = new ArrayList<String>();

            AsyncCheckToUpload currentCheck = new AsyncCheckToUpload(MediaUpload.this);
            currentCheck.execute();
        }else
        {
            backToSetUp();
        }


        this.upload = findViewById(R.id.uploadButton);
        upload.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //call server handler with the localmedia list
                UploadMedia();
            }
        });
        this. checkLocalButton = findViewById(R.id.checkLocalButton);
        checkLocalButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AsyncCheckToUpload currentCheck = new AsyncCheckToUpload(MediaUpload.this);
                currentCheck.execute();

            }
        });
        this.statusView = findViewById(R.id.editTextTextMultiLine);
        this.localMediaStatus = findViewById(R.id.statusView);
        this.statusMessages = new ArrayList<String>();

    }

    public void cleanStatus(String newStatus)
    {
        this.statusMessages = new ArrayList<String>();
        this.statusMessages.add(newStatus);
        String outputString = "";
        for(String message:  this.statusMessages)
        {
            outputString += message;
        }

        this.statusView.setText(outputString);
    }
    public void updateStatus(String uploadedMediaName)
    {
        this.statusMessages.add(uploadedMediaName);
        String outputString = "";
        for(String message:  this.statusMessages)
        {
            outputString += message;
        }

        this.statusView.setText(outputString);
    }
    private boolean isSharedPrefReady()
    {
        SharedPreferences sharedPref = MediaUpload.this.getSharedPreferences("ServerCredentials", Context.MODE_PRIVATE);
        chosenDirFromPref = sharedPref.getString("chosenDir", null);
        if(chosenDirFromPref == null)
        {
            return false;
        }else
        {
            return true;
        }
    }


    private void loadNewLocalFiles(File[] files)
    {
        //Read all phone media into the local list
        for (File file : files)
        {
            String tempName = file.getName();
            if(!tempName.matches(".thumbnails|localMedia.json|serverMedia.json"))
            {
                if(isFileNew(tempName, this.serverMedia))
                {
                    MediaObject mo = new MediaObject(file.getName(), file.getAbsolutePath());
                    this.localMedia.add(mo);
                }
            }
        }
    }

    private boolean isFileNew(String filename, List<MediaObject> serverMedia)
    {
        for (MediaObject temp : serverMedia) {
            if (filename.equals(temp.getName())) {
                return false;
            }
        }
        return true;
    }

    public String fileToJson(File file) throws IOException {
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line = bufferedReader.readLine();
        while (line != null){
            stringBuilder.append(line).append("\n");
            line = bufferedReader.readLine();
        }
        bufferedReader.close();
        // This response will have Json Format String
        String response = stringBuilder.toString();

        return response;
    }

    public void UploadMedia()
    {
        Activity[] act = {MediaUpload.this};
        ServerHandler sh = new ServerHandler(act, false);
        sh.execute( chosenDirFromPref, usernameFromSharedPref, passwordFromSharedPref, ipAddressFromSharedPref, this.localMedia, this.serverMedia);
        //grab local media json

        //call server handler with the files to upload

        //update the status view

        //upload successfull or something
    }

    private void writeJSON(List<MediaObject> mediaList, String filename, String directory) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        //FileOutputStream fos = new FileOutputStream(directory + "/" + filename + ".json");
        //OutputStreamWriter isr = new OutputStreamWriter(fos, StandardCharsets.UTF_8);

        File file = new File(directory, filename + ".json");
        if (!file.exists()) {
            file.createNewFile();
        }
        try
        {
            FileWriter writer = new FileWriter(file);
            writer.append(gson.toJson(mediaList));
            writer.flush();
            writer.close();
            //Toast.makeText(MainActivity.this, "Saved your text", Toast.LENGTH_LONG).show();
        }
        catch (java.io.IOException e)
        {
            e.printStackTrace();
        }
    }


    public void backToSetUp(){
        Intent intent = new Intent(MediaUpload.this, MainActivity.class);
        startActivity(intent);
    }


    public void startChecktoUploadFromOutside(MediaUpload context)
    {
        AsyncCheckToUpload currentCheck = new AsyncCheckToUpload(context);
        currentCheck.execute();
    }
    private class AsyncCheckToUpload extends AsyncTask
    {
        private WeakReference<MediaUpload> activityWeakReference;

        AsyncCheckToUpload(MediaUpload activity)
        {
            this.activityWeakReference = new WeakReference<MediaUpload>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MediaUpload activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing())
            {

            }else
            {
                activity.upload.showLoading();
                activity.upload.setEnabled(false);
                activity.checkLocalButton.showLoading();
                activity.checkLocalButton.setEnabled(false);

                activity.localMediaStatus.setText("Checking for new local files");
            }

        }

        @Override
        protected Object doInBackground(Object[] objects)
        {
            MediaUpload activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing())
            {

            }else
            {
                SharedPreferences sharedPref = MediaUpload.this.getSharedPreferences("ServerCredentials", Context.MODE_PRIVATE);
                chosenDirFromPref = sharedPref.getString("chosenDir", null);
                if(chosenDirFromPref == null)
                {
                    activity.backToSetUp();
                }
                activity.passwordFromSharedPref = sharedPref.getString("password", null);
                activity.usernameFromSharedPref = sharedPref.getString("username", null);
                activity.ipAddressFromSharedPref = sharedPref.getString("hostIp", null);
                //grab json files
                File serverMediaDirectory = new File(chosenDirFromPref + "/serverMedia" + ".json");

                if(!serverMediaDirectory.exists() || serverMediaDirectory.length() == 0)
                {
                    try {
                        //create an empty list
                        serverMediaDirectory.createNewFile();
                        //create empty array
                        activity.serverMedia = new ArrayList<MediaObject>();
                        activity.localMedia = new ArrayList<MediaObject>();
                        //load local media
                        File directory = new File(chosenDirFromPref);
                        File[] files = directory.listFiles();
                        loadNewLocalFiles(files);
                        //write to the file
                        writeJSON(activity.localMedia, "localMedia", activity.chosenDirFromPref); //this will have all the media to be uploaded
                        writeJSON(activity.serverMedia, "serverMedia", activity.chosenDirFromPref); //this will be empty



                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else
                {
                    //normal repeated use of the app
                    //gson convert
                    Gson gson = new Gson();

                    //read files
                    String tempUploaded = null;

                    try {

                        tempUploaded = fileToJson(serverMediaDirectory);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //decode json files
                    Type listType = new TypeToken<List<MediaObject>>(){}.getType();
                    activity.localMedia = new ArrayList<MediaObject>();
                    activity.serverMedia = gson.fromJson(tempUploaded, listType);


                    //check for new images
                    //Creating a File object for directory
                    File directory = new File(chosenDirFromPref);
                    File[] files = directory.listFiles();

                    //use the function
                    activity.loadNewLocalFiles(files);

                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            MediaUpload activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing())
            {

            }else
            {
                //set time text view

                activity.upload.hideLoading();
                activity.upload.setEnabled(true);
                activity.localMediaStatus.setText("All local media has been scanned. \n Click on Check Local Media to try again");
                activity.checkLocalButton.hideLoading();
                activity.checkLocalButton.setEnabled(true);
            }
        }
    }

}