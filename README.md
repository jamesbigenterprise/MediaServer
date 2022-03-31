# MediaServer
The app takes the credentials of an existing SFTP server and uploads media from a device folder to the server.

Media Server

How to set up the server and use the app\.

You don't need to wait until your pages are finalized; inserting a table of contents can be an early step in creating your document\. As the pages of your document develop, Word helps you keep the table of contents up to date\.

<span style="color:#3B3838">What you will need</span>

Here is a list of the devices and pieces of software you will need to set up the server and use the app\.

Windows 10 computer\.

Android Smartphone \(with Android 11\+\)\.

Media Server APK\. Download it Here:[https://github\.com/jamesbigenterprise/MediaServer/raw/master/app/release/app\-release\.apk](https://github.com/jamesbigenterprise/MediaServer/raw/master/app/release/app-release.apk)

Bitvise Server\. Download it Here:[https://dl\.bitvise\.com/BvSshServer\-917\.exe](https://dl.bitvise.com/BvSshServer-917.exe)

Bitvise settings\. Download it here:[https://github\.com/jamesbigenterprise/MediaServer/raw/master/app/release/BitviseSettings9\.17\.wst](https://github.com/jamesbigenterprise/MediaServer/raw/master/app/release/BitviseSettings9.17.wst)

<span style="color:#3B3838">Installing</span> <span style="color:#3B3838">the APK</span>

These steps might be different in your device, depending on the Manufacturer\. The goal is to allow the installation of apps from external stores\.

On your Android device, open "Settings" and click on "Apps"\.

Scroll down and click on "Special App Access"\.

Click on "Install Unknown Apps"\.

![A picture containing applicationDescription automatically generated](/content/images/e6c6714c-d3c6-4ef3-97e8-8ba3bf539479.png)

Select the app you are going to install the APK from\. Usually this will be the browser if the APK will be downloaded from the internet or the Files App if the APK will be transferred to the device\.![Graphical user interface, applicationDescription automatically generated](/content/images/fd2d19ac-c723-4f81-a471-3a9ee81610bf.png)

Toggle "Allow from this source" to activate\.

Locate the APK in your files app and click on it to install\.

![Graphical user interface, applicationDescription automatically generated](/content/images/664de8b6-92d7-4a3e-afb6-4c65fabdeba9.png)

Skip the google Play warnings\.

<span style="color:#3B3838">Install and Setup</span> <span style="color:#3B3838">The</span> <span style="color:#3B3838">Server Client</span>

The Bitvise SSH Server client provides a free for personal use version which provides one connection to the server for free\. For this step you will need:

Bitvise Server\. Download it Here:[https://dl\.bitvise\.com/BvSshServer\-917\.exe](https://dl.bitvise.com/BvSshServer-917.exe)

Bitvise settings\. Download it here:[https://github\.com/jamesbigenterprise/MediaServer/raw/master/app/release/BitviseSettings9\.17\.wst](https://github.com/jamesbigenterprise/MediaServer/raw/master/app/release/BitviseSettings9.17.wst)

Username: MuffinMan

Password: BeHappy

Create the Server Folder\. This is the folder that will be used as the server\. Create the folder MediaServer in your root directory \(C:\\)\. The path should be C:\MediaServer

Install Bitvise and select personal edition\.

![Graphical user interface, text, application, chat or text messageDescription automatically generated](/content/images/a77be0d7-4360-4796-898b-981247b64c19.png)

Once installed you will import the settings\. Under Settings, click on "Import" and select the Bitvise settings you downloaded previously\.

Select Replace settings

![Graphical user interface, text, application, emailDescription automatically generated](/content/images/1f37b1e9-eeff-42a6-9161-989911e52f17.png)

Select the settings file you downloaded\.

![A screenshot of a computerDescription automatically generated](/content/images/39965eb5-de96-44bd-afe1-534b860bd4b0.png)

Get the IP address of the server computer and enter it under Settings\> Edit Advanced Settings \> IPV4Click on the current IP Address and change it to your computer's IP address\.

![Graphical user interface, applicationDescription automatically generated](/content/images/4aa6a6b3-0214-420f-b98f-5e0ae2dbb2fc.png)

Click OK twice to return to the homepage\. Click on Start Server\.

![Graphical user interface, text, applicationDescription automatically generated](/content/images/79481001-b6f2-4af8-8a93-7c3e48dbb3da.png)

<span style="color:#3B3838">Edit the imported</span> <span style="color:#3B3838">s</span><span style="color:#3B3838">ettings</span> <span style="color:#3B3838">on Bitvise</span>

You may want to change the imported settings in Bitvise\. Here are a few common settings and how to change them\.

## Server Folder

From the Main Bitvise Server page click on Edit Advanced Settings\> Access control \> Virtual accounts\. 

Double click on the current virtual account "MuffinMan"![Graphical user interface, tableDescription automatically generated](/content/images/fdba208b-91c0-407b-9f4e-75e943f18e50.png)

Navigate to File Transfer \> Mount Points, edit the current entry and provide the new directory under "Real root path"\.

![A screenshot of a computerDescription automatically generated](/content/images/913cf0ed-ccbf-480a-b67f-538bfa7435ce.png)

## IP Address

Follow Edit advanced Settings \> IPv4, double click on the current entry and provide the new IP Address\.

![Graphical user interface, applicationDescription automatically generated](/content/images/8673bdfc-07cc-418d-919b-49c072ff07ae.png)

## User And Password

Navigate to Edit advanced settings \> Access control \> Virtual accounts and click on the current account\.![Graphical user interface, tableDescription automatically generated](/content/images/451ecb0c-a413-4132-99b5-0539bd9ce131.png)

Change the user on "Virtual account name" and the password on "Virtual account password"![Graphical user interface, text, application, emailDescription automatically generated](/content/images/3f850b11-430c-4106-ba45-57c4966cf73d.png)

<span style="color:#3B3838">Using the</span> <span style="color:#3B3838">Media Server</span> <span style="color:#3B3838">For</span> <span style="color:#3B3838">The First Time</span>

Here are the steps to set up and use the Media Server App\. A list of the credentials used will be provided, make sure to enter your credentials if you made any changes to the imported settings\. 

Host IP Address: 192\.168\.30\.143

Username: MuffinMan

Password: BeHappy

Server Folder Path: C:\MediaServer

When you open the Media Server App for the first time it will ask for file access permission, click on MediaServer

![Graphical user interface, applicationDescription automatically generated](/content/images/72443850-01f5-416f-a16b-93e16a68f476.png)

Allow access to the app and click the back button until you return to the app\.

![Graphical user interface, text, applicationDescription automatically generated](/content/images/444fda01-155f-4d35-b1e4-a3fccc1457c7.png)

Allow access to Photos and Media

![Graphical user interface, applicationDescription automatically generated](/content/images/6b2a45c9-f4e0-4eda-b2a3-40953b7b6539.png)

Click Setup to start\.

![Graphical user interface, application, TeamsDescription automatically generated](/content/images/4c2bf68a-e612-4d6e-b8c8-756bb18e80af.png)

Click on the Media Folder button to select the folder in your device where the media will come from\.

![Graphical user interfaceDescription automatically generated](/content/images/ecbb09ba-8b2e-4b5b-b826-bcba061fdfaa.png)

Navigate to the desired folder and hit ok

![Graphical user interface, applicationDescription automatically generated](/content/images/88881164-868d-45ed-9c8b-75d2975c9b67.png) ![Graphical user interface, text, applicationDescription automatically generated](/content/images/3b6bcafb-c4f2-47c8-a560-8e04cc731ec9.png)

Fill all the credential fields\. Once you navigate out of the IP address field the inputs will be validated, if acceptable the Next button will be enabled\.

![A screenshot of a calculatorDescription automatically generated with medium confidence](/content/images/2594b9c3-9c51-4617-a757-f06d1d45c05c.png)

The app will prepare the media to be uploaded, once you see the home screen you can hit "Upload Media To Server"\.

![](/content/images/ab251d31-5140-47ab-bc04-0e4c1fe4afcd.png)

The Status view will inform you about each media successfully uploaded to the server\. By the end you will see the message "Media Upload Done" meaning that all the photos in the selected folder have been uploaded\.

![Graphical user interface, text, chat or text messageDescription automatically generated](/content/images/01e9ccfd-6971-4e97-88b8-c5b2c29c9e0b.png)

If you try to upload again without any new media in the selected folder nothing will be uploaded\.

![Background patternDescription automatically generated](/content/images/58a4511d-1372-4284-90c2-04364e25e48f.png)

If you add new media to the folder\.

![Graphical user interface, applicationDescription automatically generated](/content/images/9f5c28da-1c7b-4054-93ef-9413b90c9f14.png)

Click on "Check Local Media" to prepare the new media for upload\.

![Graphical user interface, text, application, chat or text messageDescription automatically generated](/content/images/2ea21e5b-0f01-47dd-ae4e-1b17d3937c4f.png)

Now click on "Upload Media To Server" and you will notice that the new media will be uploaded to the server\.

![TextDescription automatically generated with medium confidence](/content/images/927bfd9d-7cdb-4722-aafa-f1dfbcdf95c1.png)


