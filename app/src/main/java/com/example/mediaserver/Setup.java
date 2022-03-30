 package com.example.mediaserver;

import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.kusu.loadingbutton.LoadingButton;

public class Setup extends AppCompatActivity {

    public LoadingButton button;
    public TextView connectionMessage;
    public TextView selectedFolder;

    //various input boxes

    private EditText usernameInput;
    private EditText passwordInput;
    private EditText hostIpAddress;
    private TextView warningView;
    private String m_chosenDir;
    private String username;
    private String password;
    private String ipAddress;

    View.OnFocusChangeListener yourFocusChageListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus)
        {

            username = usernameInput.getText().toString();
            password = passwordInput.getText().toString();
            ipAddress = hostIpAddress.getText().toString();


            String ipv4 = "(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])";
            boolean usernameReady = false;
            boolean passwordReady = false;
            boolean ipAddressReady = false;


            //check all 4 inputs


            if(!TextUtils.isEmpty(usernameInput.getText().toString()))
            {
                usernameReady = true;
            }else
            {
                usernameReady = false;
            }
            if(!TextUtils.isEmpty(passwordInput.getText().toString()))
            {
                passwordReady = true;
            }else
            {
                passwordReady = false;
            }
            if(!TextUtils.isEmpty(hostIpAddress.getText().toString()))
            {
                //A complete ip has 4 - 12 digits with at least 3 periods

                Pattern r = Pattern.compile(ipv4);
                Matcher m = r.matcher(ipAddress);
                if(m.find())
                {
                    ipAddressReady = true;
                }

            }else
            {
                ipAddressReady = false;
            }
            //
            //Update the warning view
            warningView =  findViewById(R.id.textViewWarnings);
            warningView.setText(" mediaLocation: " + m_chosenDir
                    + "\nusernameReady: " + usernameReady
                    + "\n username: " + username
                    + "\npasswordReady: " + passwordReady
                    + "\npassword: " + password
                    + "\nipAddressReady: " + ipAddressReady
                    + "\nipAddress: " + ipAddress
                    + "\nm_chosenDir: " + m_chosenDir);





        if(usernameReady & passwordReady & ipAddressReady)
        {
            button.setEnabled(true);
        }
      }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        m_chosenDir = "";

        //test for the directory picker
        Button dirChooserButton = (Button) findViewById(R.id.mediaFolderLocation);
        dirChooserButton.setOnClickListener(new View.OnClickListener()
        {

            private boolean m_newFolderEnabled = false;

            @Override
            public void onClick(View v)
            {
                // Create DirectoryChooserDialog and register a callback
                DirectoryChooserDialog directoryChooserDialog =
                        new DirectoryChooserDialog(Setup.this,
                                new DirectoryChooserDialog.ChosenDirectoryListener()
                                {
                                    @Override
                                    public void onChosenDir(String chosenDir)
                                    {
                                        m_chosenDir = chosenDir;
                                        Toast.makeText(
                                                Setup.this, "Chosen directory: " +
                                                        chosenDir, Toast.LENGTH_LONG).show();
                                        selectedFolder.setText(chosenDir);
                                    }
                                });
                // Toggle new folder button enabling
                directoryChooserDialog.setNewFolderEnabled(m_newFolderEnabled);
                // Load directory chooser dialog for initial 'm_chosenDir' directory.
                // The registered callback will be called upon final directory selection.
                directoryChooserDialog.chooseDirectory(m_chosenDir);
                m_newFolderEnabled = ! m_newFolderEnabled;
            }
        });
        //end of test

        button = (LoadingButton) findViewById(R.id.buttonNext);
        connectionMessage = findViewById(R.id.connectionMessage);
        selectedFolder = findViewById(R.id.selectedFolder);

        //mediaFolderInput = findViewById(R.id.mediaFolderLocation);
        usernameInput = findViewById(R.id.editTextUsername);
        passwordInput = findViewById(R.id.editTextTextPassword);
        hostIpAddress =  findViewById(R.id.editTextIpAddress);

        //mediaFolderInput.setOnFocusChangeListener(yourFocusChageListener);
        usernameInput.setOnFocusChangeListener(yourFocusChageListener);
        passwordInput.setOnFocusChangeListener(yourFocusChageListener);
        hostIpAddress.setOnFocusChangeListener(yourFocusChageListener);


        button.setEnabled(false);


        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {



                //save credentials to shared preferences
                Context context = Setup.this;
                SharedPreferences sharedPref = context.getSharedPreferences("ServerCredentials", Context.MODE_PRIVATE);
                // Creating an Editor object to edit(write to the file)
                SharedPreferences.Editor myEdit = sharedPref.edit();

                myEdit.putString("chosenDir",m_chosenDir);
                myEdit.putString("password", password);
                myEdit.putString("username", username);
                myEdit.putString("hostIp", ipAddress);

                myEdit.apply();
                myEdit.commit();


                String chosenDirFromPref = sharedPref.getString("chosenDir", null);

                System.out.println(chosenDirFromPref);
                //openNewActivity();

                Activity[] act = {Setup.this};
                ServerHandler sh = new ServerHandler( act, true);
                    sh.execute( m_chosenDir, username, password, ipAddress);
            }

        });
    }


}