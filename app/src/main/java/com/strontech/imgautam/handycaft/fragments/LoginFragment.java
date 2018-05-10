package com.strontech.imgautam.handycaft.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequest.GraphJSONObjectCallback;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.strontech.imgautam.handycaft.R;
import com.strontech.imgautam.handycaft.activities.MainActivity;
import com.strontech.imgautam.handycaft.helper.InputValidation;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements OnClickListener, GoogleApiClient.OnConnectionFailedListener {

  private LinearLayout linearLayoutLogin;

  private TextInputLayout textInputLayoutEmail;
  private TextInputLayout textInputLayoutPassword;

  private TextInputEditText textInputEditTextEmail;
  private TextInputEditText textInputEditTexPassword;

  private Button buttonLogin;
  private TextView textViewLinkRegister;

  private LoginButton buttonLoginFacebook;
  private SignInButton buttonGoogleSignIn;





  private FirebaseAuth auth;
  private ProgressBar progressBar;



  //Google Login
  private Button buttonGoogleLogin;
 // private GoogleSignInOptions googleSignInOptions;
  private static final int RC_SIGN_IN = 007;
  private GoogleApiClient mGoogleApiClient;


  //For Facebook
  private CallbackManager callbackManager;
  private AccessTokenTracker accessTokenTracker;
  private ProfileTracker profileTracker;
  private LoginManager loginManager;
  private Button buttonFacebookLogin;
  private URL urlProfilePicure;
  private String userId;
  private String firstName;
  private String lastName;
  private String email;
  private String birthday;
  private String gender;
  private String TAG = "LoginFragment";


  private InputValidation inputValidation;

  SharedPreferences sharedPreferences;
  SharedPreferences.Editor editor;
  String FILE = "myEmailPass";

  View view;

  public LoginFragment() {
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
    view = inflater.inflate(R.layout.fragment_login, container, false);

      //GoogleNew
      GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
              .requestEmail()
              .build();

      mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
              .enableAutoManage(getActivity(), this)
              .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
              .build();


    FacebookSdk.sdkInitialize(FacebookSdk.getApplicationContext());
    initViews();
    initListeners();
    initObjects();

    //Go to back to home fragment
    Toolbar toolbar = view.findViewById(R.id.toolbarLogin);
    toolbar.setTitle("Sign up");
    toolbar.setTitleTextColor(Color.WHITE);
    toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
    toolbar.setNavigationOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        getActivity().onBackPressed();
      }
    });
    return view;
  }


  @Override
  public void onStop() {
    super.onStop();
    ((AppCompatActivity) getActivity()).getSupportActionBar().show();
  }

    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

    /**
   * this method is to initialize views
   */
  private void initViews() {


    linearLayoutLogin = view.findViewById(R.id.linearLayoutLogin);

    textInputLayoutEmail = view.findViewById(R.id.textInputLayoutEmail);
    textInputLayoutPassword = view.findViewById(R.id.textInputLayoutPassword);

    textInputEditTextEmail = view.findViewById(R.id.textInputEditTextEmail);
    textInputEditTexPassword = view.findViewById(R.id.textInputEditTexPassword);

    buttonLogin = view.findViewById(R.id.buttonLogin);

    textViewLinkRegister = view.findViewById(R.id.textViewLinkRegister);

    buttonFacebookLogin = view.findViewById(R.id.buttonFacebookLogin);
    buttonGoogleLogin = view.findViewById(R.id.buttonGoogleLogin);

    progressBar = view.findViewById(R.id.progressBar);

  }


  /**
   * this method is to initialize listeners
   */
  private void initListeners() {

    //facebook

//    callbackManager = CallbackManager.Factory.create();
//    profileTrack()
    setupFacebook();
    buttonLogin.setOnClickListener(this);
    buttonFacebookLogin.setOnClickListener(this);
    buttonGoogleLogin.setOnClickListener(this);
    textViewLinkRegister.setOnClickListener(this);

  }


  /**
   * this method to initialize the Objects
   */
  private void initObjects() {

    inputValidation = new InputValidation(getActivity());

    sharedPreferences = this.getActivity().getSharedPreferences(FILE, Context.MODE_PRIVATE);
    editor = sharedPreferences.edit();
    //Get firebase auth instance







    auth = FirebaseAuth.getInstance();
    checkUser();

  }

  private void checkUser() {

    if (auth.getCurrentUser() != null) {
      FragmentTransaction ft = getFragmentManager().beginTransaction();
      ft.replace(R.id.mainFrame, new HomeFragment());
      ft.commit();
    }
  }

  /**
   * this implemented method is to listen the click on view
   */
  @Override
  public void onClick(View v) {

    switch (v.getId()) {

      case R.id.buttonLogin:
        EmailAndPasswordSignIn();
        break;
      case R.id.textViewLinkRegister:
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, new RegisterFragment());
        //ft.add(R.id.mainFrame, new RegisterFragment(), "Register");
        ft.addToBackStack(null);
        ft.commit();
        break;

      case R.id.buttonFacebookLogin:
        logInWithFacebook();
        break;

      case R.id.buttonGoogleLogin:
        logInWithGoogle();
        break;

    }
  }



  /**
   * This method For Login Using Email and Password
   * */
  private void EmailAndPasswordSignIn() {

    if (!inputValidation
        .isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, "Enter Email")) {
      return;
    }
    if (!inputValidation.isInputEditTextFilled(textInputEditTexPassword, textInputLayoutPassword,
        "Enter Password")) {
      return;
    }

    final String email, password;
    email = textInputEditTextEmail.getText().toString().trim();
    password = textInputEditTexPassword.getText().toString().trim();

    progressBar.setVisibility(View.VISIBLE);
    //authenticate the user
    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(),
        new OnCompleteListener<AuthResult>() {
          @Override
          public void onComplete(@NonNull Task<AuthResult> task) {
            // If sign in fails, display a message to the user. If sign in succeeds
            // the auth state listener will be notified and logic to handle the
            // signed in user can be handled in the listener.
            progressBar.setVisibility(View.GONE);

            if (!task.isSuccessful()) {
              //there is an error
              if (password.length() < 6) {
                textInputLayoutPassword.setError("Please enter 6 digit password");
              } else {
//                Toast.makeText(getActivity(), "Auth Failed", Toast.LENGTH_SHORT).show();
              }
            } else {

              editor.putString("email", email);
              editor.putString("password", password);
              editor.commit();

              //restart activity
              restartActivity();

              FragmentTransaction ft = getFragmentManager().beginTransaction();
              ft.replace(R.id.mainFrame, new HomeFragment());
              ft.commit();


            }

          }
        });
  }


  /**
   * This method for restarting the activity for take changes
   */
  public void restartActivity() {
    Intent intent = getActivity().getIntent();
    //intent.addFlags(Intent.FLAG_ACTIVITY_);
    getActivity().finish();
    startActivity(intent);
    getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
  }


  /**
   * This method Login With Facebook From Button
   */

  private void logInWithFacebook() {
    accessTokenTracker.startTracking();
    loginManager.logInWithReadPermissions(getActivity(), Arrays
        .asList("public_profile", "email", "user_birthday"));
  }


  /**
   * This method setup Facebook
   */
  private void setupFacebook() {
    loginManager=LoginManager.getInstance();
    callbackManager=CallbackManager.Factory.create();
    accessTokenTracker=new AccessTokenTracker() {
      @Override
      protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
          AccessToken currentAccessToken) {

      }
    };

    LoginManager.getInstance().registerCallback(callbackManager,
        new FacebookCallback<LoginResult>() {
          @Override
          public void onSuccess(LoginResult loginResult) {
            if (loginResult.getRecentlyGrantedPermissions().contains("email")) {
              requestObjectUser(loginResult.getAccessToken());
            } else {
              LoginManager.getInstance().logOut();
              Toast.makeText(getActivity(), "Error permissions", Toast.LENGTH_SHORT).show();
            }
          }

          @Override
          public void onCancel() {

          }

          @Override
          public void onError(FacebookException error) {
            Log.d("ERROR", error.toString());
          }
        });

    if (AccessToken.getCurrentAccessToken() != null) {
      requestObjectUser(AccessToken.getCurrentAccessToken());
    }
  }

  private void requestObjectUser(final AccessToken accessToken) {
    GraphRequest request=GraphRequest.newMeRequest(accessToken,
        new GraphRequest.GraphJSONObjectCallback() {
          @Override
          public void onCompleted(JSONObject object, GraphResponse response) {
            Log.d(TAG, object.toString());
            Log.d(TAG, response.toString());
            try
            {
              userId=object.getString("id");
              urlProfilePicure=new URL("https://graph.facebook.com/" + userId + "/picture?width=96&height=96");
              if (object.has("first_name"))
                firstName=object.getString("first_name");
              if(object.has("last_name"))
                lastName = object.getString("last_name");
              if (object.has("email"))
                email = object.getString("email");
              if (object.has("birthday"))
                birthday = object.getString("birthday");
              if (object.has("gender"))
                gender = object.getString("gender");

//              Intent main=new Intent(getActivity(), MainActivity.class);
//              main.putExtra("name", firstName);
//              main.putExtra("surname", lastName);
//              main.putExtra("imageUrl", urlProfilePicure.toString());
//              startActivity(main);
              Toast.makeText(getActivity(), "Method called "+firstName+lastName, Toast.LENGTH_LONG).show();
              editor.putString("facebook_first_name",firstName);
              editor.putString("facebook_last_name",lastName);
              editor.putString("facebook_image_url",urlProfilePicure.toString());
              editor.commit();



              //restart activity
              restartActivity();

              FragmentTransaction ft = getFragmentManager().beginTransaction();
              ft.replace(R.id.mainFrame, new HomeFragment());
              ft.commit();

            }catch (JSONException e){
              e.printStackTrace();
            }catch (MalformedURLException e){
              e.printStackTrace();
            }
          }
        });
    Bundle parameters=new Bundle();
    parameters.putString("fields","id, first_name, last_name, email, birthday, gender , location");
    request.setParameters(parameters);
    request.executeAsync();
  }


  /**
   * This method For Login Using Google
   * */
  private void logInWithGoogle() {
    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
    startActivityForResult(signInIntent, RC_SIGN_IN);

  }

  private void handleSignInResult(GoogleSignInResult result) {
    Log.d(TAG, "handleSignInResult:" + result.isSuccess());
      Toast.makeText(getActivity(), "handleSignInResult: "+result.isSuccess(), Toast.LENGTH_SHORT).show();
    if (result.isSuccess()) {
      // Signed in successfully, show authenticated UI.
      GoogleSignInAccount acct = result.getSignInAccount();

      Log.e(TAG, "display name: " + acct.getDisplayName());

      String personName = acct.getDisplayName();
      String personPhotoUrl = acct.getPhotoUrl().toString();
      String email = acct.getEmail();

      Log.e(TAG, "Name: " + personName + ", email: " + email
              + ", Image: " + personPhotoUrl);

      Toast.makeText(getActivity(), "Name: " + personName + ", email: " + email
              + ", Image: " + personPhotoUrl, Toast.LENGTH_SHORT).show();



      editor.putString("username_google",personName);
      editor.putString("email_google",email);
      editor.putString("profile_pic_google",personPhotoUrl);
      editor.commit();

      //restart activity
     // restartActivity();

      FragmentTransaction ft = getFragmentManager().beginTransaction();
      ft.replace(R.id.mainFrame, new HomeFragment());
      ft.commit();

      /*          *//*Toast.makeText(getActivity(), "Name: " + personName + ", email: " + email
                    + ", Image: " + personPhotoUrl, Toast.LENGTH_SHORT).show();
*//*
       *//*txtName.setText(personName);
            txtEmail.setText(email);
            Glide.with(getApplicationContext()).load(personPhotoUrl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProfilePic);
*//*
  //          updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
    //        updateUI(false);*/
    }
  }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //This is For Facebook Login
        callbackManager.onActivityResult(requestCode, resultCode, data);




        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
            Toast.makeText(getActivity(), "OnActivityResult()"+result, Toast.LENGTH_SHORT).show();
        }

    }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

  }


  @Override
  public void onStart() {
    super.onStart();

    /*OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
    if (opr.isDone()) {
      // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
      // and the GoogleSignInResult will be available instantly.
      Log.d(TAG, "Got cached sign-in");
      GoogleSignInResult result = opr.get();
      handleSignInResult(result);
    } else {
      // If the user has not previously signed in on this device or the sign-in has expired,
      // this asynchronous branch will attempt to sign in the user silently.  Cross-device
      // single sign-on will occur in this branch.
      //showProgressDialog();
      opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
        @Override
        public void onResult(GoogleSignInResult googleSignInResult) {
          //      hideProgressDialog();
          handleSignInResult(googleSignInResult);
        }
      });*/
    //}
  }
}
















