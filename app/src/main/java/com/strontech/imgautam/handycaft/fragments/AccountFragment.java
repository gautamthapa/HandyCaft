package com.strontech.imgautam.handycaft.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.strontech.imgautam.handycaft.R;
import com.strontech.imgautam.handycaft.SellerFragments.AddProductFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment implements OnClickListener{

  private FirebaseAuth auth;
  private FirebaseAuth.AuthStateListener authListener;

  private TextView textViewEmail;
  private Button buttonLogout;
  private Button buttonSellWithUs;

  private View view;
  SharedPreferences sharedPreferences;
  String email;


  public AccountFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    view=inflater.inflate(R.layout.fragment_account, container, false);

    Toast.makeText(getActivity(), "Profile Fragment called", Toast.LENGTH_SHORT).show();
    initViews();
    initObjects();
    return view;
  }



  /**
   * This method for initialization Views
   * */
  private void initViews(){
    textViewEmail=view.findViewById(R.id.textViewEmail);
    buttonLogout=view.findViewById(R.id.buttonLogout);
    buttonSellWithUs=view.findViewById(R.id.buttonSellWithUs);
  }


  /**
   * This method for initialization Objects
   * */
  private void initObjects(){



    buttonLogout.setOnClickListener(this);
    buttonSellWithUs.setOnClickListener(this);
    //checkAuthStateListener();
    auth=FirebaseAuth.getInstance();
    sharedPreferences=getActivity().getSharedPreferences("myEmailPass", Context.MODE_PRIVATE);

    setInformation();


  }




/**
 * This method not working
 * */
  private void checkAuthStateListener() {

//    authListener = new FirebaseAuth.AuthStateListener() {
//      @Override
//      public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//        FirebaseUser user = firebaseAuth.getCurrentUser();
//        if (user == null) {
//          Toast.makeText(getActivity(), "authListener", Toast.LENGTH_SHORT).show();
//          // user auth state is changed - user is null
//          // launch login activity
//          FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
//          ft.replace(R.id.mainFrame, new LoginFragment());
//          ft.commit();
//        }
//      }
//    };
  }


  /**
   * This method is to set all user information
   * */
  private void setInformation() {
    email=sharedPreferences.getString("email", null);
    textViewEmail.setText(email);
  }





  @Override
  public void onStart() {
    super.onStart();
    //auth.addAuthStateListener(authListener);
  }

  @Override
  public void onStop() {
    super.onStop();
//    if (authListener != null) {
//      auth.removeAuthStateListener(authListener);
//    }
  }

  @Override
  public void onClick(View v) {

    switch (v.getId()){

      case R.id.buttonLogout:
        logout();
        facebookLogout();
        break;
      case R.id.buttonSellWithUs:
        FragmentTransaction ft=getFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, new AddProductFragment());
        ft.commit();
    }

  }

  /**
   * This method is to logout
   * */
  private void logout(){
    auth.signOut();
    sharedPreferences.edit().remove("email").apply();

    //restart activity
    restartActivity();

    FragmentTransaction ft=getFragmentManager().beginTransaction();
    ft.replace(R.id.mainFrame, new HomeFragment());
    ft.commit();
  }


  /**
   * This method is to Logout from facebook
   * */
  private void facebookLogout() {
    LoginManager.getInstance().logOut();
    sharedPreferences.edit().clear().apply();
    //restart activity
    restartActivity();

    FragmentTransaction ft=getFragmentManager().beginTransaction();
    ft.replace(R.id.mainFrame, new HomeFragment());
    ft.commit();
  }

  /**
   * This method for restarting the activity for take changes
   * */
  public void restartActivity(){
    Intent intent = getActivity().getIntent();
    //intent.addFlags(Intent.FLAG_ACTIVITY_);
    getActivity().finish();
    startActivity(intent);
    getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
  }
}
