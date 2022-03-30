package com.example.mediaserver;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;


import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.mediaserver.ui.main.MediaUpload;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CompletableFuture;


public class ServerHandler extends AsyncTask {


    private WeakReference<MediaUpload> activityWeakReference;
    private WeakReference<Setup> testConnectionactivityWeakReference;

    //private String remoteHost = "sftp://10.40.12.219";
    private String remoteHost;
    private String username;
    private String password;
    private List<MediaObject>localMedia;
    private List<MediaObject>serverMedia;
    private boolean testConnection;
    private boolean connectionSuccesfull;


    public ServerHandler(Activity[] values, boolean isTest)
    {
        if(!isTest)
        {
            this.activityWeakReference = new WeakReference<MediaUpload>((MediaUpload)values[0]);
            this.testConnectionactivityWeakReference = null;
            this.testConnection = false;
            this.connectionSuccesfull = false;
        }else
        {
            this.activityWeakReference = null;
            this.testConnectionactivityWeakReference = new WeakReference<Setup>((Setup)values[0]);
            this.testConnection = true;

        }

    }

    //Source: https://www.baeldung.com/java-file-sftp
    private ChannelSftp setupJsch() throws JSchException {

        JSch jsch = new JSch();
        //jsch.setKnownHosts("/known_hosts");
        Session jschSession = jsch.getSession(this.username, this.remoteHost,22);
        jschSession.setPassword(this.password);
        jschSession.setConfig("StrictHostKeyChecking", "no");
        jschSession.connect();
        System.out.println("Session: " + jschSession.isConnected());

        return (ChannelSftp) jschSession.openChannel("sftp");
    }


    private boolean testConnection() throws JSchException {
        ChannelSftp channelSftp = setupJsch();
        channelSftp.connect();
        return channelSftp.isConnected();
    }

    private void whenUploadFileUsingJsch_thenSuccess(String parentDir) throws JSchException, SftpException, FileNotFoundException {
       ChannelSftp channelSftp = setupJsch();
       channelSftp.connect();
        for (MediaObject file : this.localMedia)
        {
            FileInputStream input = new FileInputStream(file.getFileLocation());
            channelSftp.put(input, file.getName());
            file.uploadSuccessful();
            this.serverMedia.add(file);
            publishProgress("MediaName: " + file.getName() + " Was succesfully uploaded.\n");

        }



        //covert to files
        try {
            writeJSON(localMedia, "localMedia", parentDir);
            writeJSON(serverMedia, "serverMedia", parentDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        channelSftp.exit();
        channelSftp.disconnect();
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


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if(testConnection)
        {
            Setup activity = testConnectionactivityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }

            activity.button.setEnabled(false);
            activity.button.showLoading();
        }else
        {
            // Connection attempt failed, check the credentials and try again
            MediaUpload activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            activity.upload.setEnabled(false);
            activity.upload.showLoading();
            //
            activity.cleanStatus("Starting Media Upload");

        }

    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);

        //add the current uploaded media to the status view
        MediaUpload activity = activityWeakReference.get();
        if(activity == null || activity.isFinishing()|| this.testConnection) {
            return;
        }
        String uploadedMediaName = (String) values[0];
        activity.updateStatus(uploadedMediaName);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        //grab the data
        //two scenarios
        //setup, just checking the connection
        if(objects.length < 5 & this.testConnection)
        {
            String m_chosenDir = (String) objects[0];
            this.username = (String) objects[1];
            this.password = (String) objects[2];
            this.remoteHost = (String)objects[3];

            //this is just setup
            try {
                this.connectionSuccesfull =  testConnection();
            } catch (JSchException e) {
                e.printStackTrace();
            }
        }else if(!this.testConnection)
        {
            String m_chosenDir = (String) objects[0];
            this.username = (String) objects[1];
            this.password = (String) objects[2];
            this.remoteHost = (String)objects[3];
            this.localMedia = (List<MediaObject>)objects[4];
            this.serverMedia = (List<MediaObject>)objects[5];
            //media will be uploaded
            try {
                whenUploadFileUsingJsch_thenSuccess(m_chosenDir);
            } catch (JSchException e) {
                e.printStackTrace();
            } catch (SftpException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if(this.testConnection)
        {
            //was the connection succesfull?
            if(this.connectionSuccesfull)
            {
                Intent intent = new Intent(testConnectionactivityWeakReference.get(), MediaUpload.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                testConnectionactivityWeakReference.get().startActivity(intent);
            }else
            {
                // Connection attempt failed, check the credentials and try again
                Setup activity = testConnectionactivityWeakReference.get();
                if (activity == null || activity.isFinishing()) {
                    return;
                }
                activity.button.setEnabled(true);
                activity.button.hideLoading();
                activity.connectionMessage.setText("Connection attempt failed, check the credentials and try again");
            }

        }else
        {

            MediaUpload activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            //activity.upload.setEnabled(true);
            activity.upload.hideLoading();
            activity.updateStatus("\nMedia Upload Done");
            //done uploading
            //Done uploading Media
            activity.startChecktoUploadFromOutside(activity);
        }


    }
}
