package com.strontech.imgautam.handycaft.userfragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.strontech.imgautam.handycaft.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment {

    private CircleImageView circleImageViewUserProfilePic;
    private TextView textViewUsername;
    private TextView textViewUserEmail;
    private TextView textViewUserMobNumber;
    private TextView textViewUserAddress;

    DatabaseReference databaseReference;

    View view;
    public UserProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_user_profile, container, false);

        initViews();
        initListeners();
        initObjects();

        return view;
    }


    /**
     * This method is to initialization Views
     */
    private void initViews(){

        circleImageViewUserProfilePic=view.findViewById(R.id.circleImageViewUserProfilePic);
        textViewUsername=view.findViewById(R.id.textViewUsername);
        textViewUserEmail=view.findViewById(R.id.textViewUserEmail);
        textViewUserMobNumber=view.findViewById(R.id.textViewUserMobNumber);
        textViewUserAddress=view.findViewById(R.id.textViewUserAddress);
    }


    /**
     * This method is to initialization Listeners
     */
    private void initListeners(){

    }


    /**
     * This method is to initialization Objects
     */
    private void initObjects(){

        databaseReference= FirebaseDatabase.getInstance().getReference("UserInfo");

        getDataFromDb();
    }

    /**
     * This method to get Data from FirebaseDatabase and set
     * */
    private void getDataFromDb() {

    }

}
