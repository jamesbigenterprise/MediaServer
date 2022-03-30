package com.example.mediaserver.ui.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediaserver.R;

import java.io.File;
import java.util.ArrayList;

public class LocalMedia extends Fragment
{

    private String chosenDir;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {


        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_local_media, null);
        RecyclerView recyclerView = root.findViewById(R.id.imagegallery);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(inflater.getContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        //path for the images
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("ServerCredentials", Context.MODE_PRIVATE);
        String chosenDirFromPref = sharedPref.getString("chosenDir", null);
        if(chosenDirFromPref == null)
        {
            //do not continue
        }else
        {
            this.chosenDir = chosenDirFromPref;
            ArrayList<CreateList> createLists = prepareData(this.chosenDir);
            MyAdapter adapter = new MyAdapter(inflater.getContext(), createLists);
            recyclerView.setAdapter(adapter);
        }
        return inflater.inflate(R.layout.fragment_local_media, container, false);




    }


    private ArrayList<CreateList> prepareData(String path)
    {
        //path for all the images for now

        ArrayList<CreateList> theimage = new ArrayList<>();
        File f = new File(path);
        File file[] = f.listFiles();
        Context context = getActivity();

        for (File current: file)
        {
            if(!current.isDirectory())
            {
                CreateList createList = new CreateList(context, current.getPath());
                theimage.add(createList);
            }


        }



        return theimage;
    }
}
