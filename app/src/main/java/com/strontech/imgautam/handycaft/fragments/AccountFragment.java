package com.strontech.imgautam.handycaft.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.strontech.imgautam.handycaft.ProductFragments.ProductCartFragment;
import com.strontech.imgautam.handycaft.ProductFragments.ProductWishListFragment;
import com.strontech.imgautam.handycaft.R;
import com.strontech.imgautam.handycaft.SellerFragments.AddProductFragment;
import com.strontech.imgautam.handycaft.activities.MainActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import java.io.InputStream;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment implements OnClickListener{

  private Toolbar toolbarAccountFragment;
  private FirebaseAuth auth;
  private FirebaseAuth.AuthStateListener authListener;


 private CircleImageView circleImageViewUserProfilePic;
 private TextView textViewUsername;
 private TextView textViewUserEmail;
 private Button buttonOrders;
 private Button buttonWishList;
 private Button buttonCart;
 private Button buttonUserProfile;
 private Button buttonUserAddress;
 private Button buttonSellWithUs;
  private Button buttonLogout;


  //For Google
  private String username_google;
  private String email_google;
  private String profile_pic_google;


  //Facebook
  private String first_name;
  private String last_name;
  private String imageUrl;



  private View view;
  SharedPreferences sharedPreferences;
  String email;


  public AccountFragment() {
    // Required empty public constructor
  }


  @Override
  public void onResume() {
    super.onResume();
    if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
      ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    view=inflater.inflate(R.layout.fragment_account, container, false);

    Toast.makeText(getActivity(), "Profile Fragment called", Toast.LENGTH_SHORT).show();
    initViews();
    initListeners();
    initObjects();
    return view;
  }



  /**
   * This method for initialization Views
   * */
  private void initViews(){
    toolbarAccountFragment=view.findViewById(R.id.toolbarAccountFragment);

    circleImageViewUserProfilePic=view.findViewById(R.id.circleImageViewUserProfilePic);

    textViewUsername=view.findViewById(R.id.textViewUsername);
    textViewUserEmail=view.findViewById(R.id.textViewUserEmail);
    buttonOrders=view.findViewById(R.id.buttonOrders);
    buttonWishList=view.findViewById(R.id.buttonWishList);
    buttonCart=view.findViewById(R.id.buttonCart);
    buttonUserProfile=view.findViewById(R.id.buttonUserProfile);
    buttonUserAddress=view.findViewById(R.id.buttonUserAddress);
    buttonSellWithUs=view.findViewById(R.id.buttonSellWithUs);
    buttonLogout=view.findViewById(R.id.buttonLogout);
    //buttonSellWithUs=view.findViewById(R.id.buttonSellWithUs);
  }


  /**
   * This method for initialization Listeners
   * */
  private void initListeners() {

    buttonOrders.setOnClickListener(this);
    buttonWishList.setOnClickListener(this);
    buttonCart.setOnClickListener(this);
    buttonUserProfile.setOnClickListener(this);
    buttonUserAddress.setOnClickListener(this);
    buttonSellWithUs.setOnClickListener(this);
    buttonLogout.setOnClickListener(this);
  }


  /**
   * This method for initialization Objects
   * */
  private void initObjects(){

    toolbarAccountFragment.setTitle("My Account");
    toolbarAccountFragment.setTitleTextColor(Color.WHITE);
    toolbarAccountFragment.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
    toolbarAccountFragment.setNavigationOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        FragmentTransaction ft=getFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, new HomeFragment());
        ft.commit();
      }
    });



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

    //get data from sharedPreferences and set it
    email = sharedPreferences.getString("email", null);

    //Google
    username_google=sharedPreferences.getString("username_google",null);
    email_google=sharedPreferences.getString("email_google",null);
    profile_pic_google=sharedPreferences.getString("profile_pic_google",null);

    //Facebook
    first_name = sharedPreferences.getString("facebook_first_name", null);
    last_name = sharedPreferences.getString("facebook_last_name", null);
    imageUrl = sharedPreferences.getString("facebook_image_url", null);


    if (email !=null){
      textViewUsername.setVisibility(View.VISIBLE);
      textViewUserEmail.setText(email);
    }else if (first_name !=null || last_name != null || imageUrl != null){
      textViewUserEmail.setVisibility(View.GONE);
      //Facebook
      textViewUsername.setText(first_name+" "+last_name);
      new AccountFragment.DownloadImage(circleImageViewUserProfilePic).execute(imageUrl);


    }else if (username_google != null || email_google != null || profile_pic_google !=null){
      //Google
      textViewUsername.setText(username_google);
      textViewUserEmail.setText(email_google);
      Glide.with(getActivity()).load(profile_pic_google).into(circleImageViewUserProfilePic);
    }

  }

  /**
   * Download facebook Image
   */
  public class DownloadImage extends AsyncTask<String, Void, Bitmap> {


    public DownloadImage(CircleImageView bmImage) {
      circleImageViewUserProfilePic = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
      String urldisplay = urls[0];
      Bitmap mIcon11 = null;
      try {
        InputStream in = new java.net.URL(urldisplay).openStream();
        mIcon11 = BitmapFactory.decodeStream(in);
      } catch (Exception e) {
//        Log.e("Error", e.getMessage());
        e.printStackTrace();
      }
      return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
      circleImageViewUserProfilePic.setImageBitmap(result);
    }
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
    ((AppCompatActivity) getActivity()).getSupportActionBar().show();
  }

  @Override
  public void onClick(View v) {

    switch (v.getId()){

      case R.id.buttonOrders:

        break;
      case R.id.buttonWishList:
        FragmentTransaction ft1=getFragmentManager().beginTransaction();
        ft1.replace(R.id.mainFrame, new ProductWishListFragment());
        ft1.commit();
        break;
      case R.id.buttonCart:
        FragmentTransaction ft2=getFragmentManager().beginTransaction();
        ft2.replace(R.id.mainFrame, new ProductCartFragment());
        ft2.commit();
        break;
      case R.id.buttonUserProfile:
        FragmentTransaction ft3=getFragmentManager().beginTransaction();
        ft3.replace(R.id.mainFrame, new ProductWishListFragment());
        ft3.commit();
        break;
      case R.id.buttonUserAddress:
        FragmentTransaction ft4=getFragmentManager().beginTransaction();
        ft4.replace(R.id.mainFrame, new ProductWishListFragment());
        ft4.commit();
        break;
      case R.id.buttonLogout:
        logout();
        facebookLogout();
        break;
      case R.id.buttonSellWithUs:
        FragmentTransaction ft=getFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, new AddProductFragment());
        ft.commit();
        break;



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
