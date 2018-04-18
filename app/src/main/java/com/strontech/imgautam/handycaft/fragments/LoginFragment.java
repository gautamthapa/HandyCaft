package com.strontech.imgautam.handycaft.fragments;


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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.strontech.imgautam.handycaft.R;
import com.strontech.imgautam.handycaft.helper.InputValidation;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements OnClickListener {

  private LinearLayout linearLayoutLogin;

  private TextInputLayout textInputLayoutEmail;
  private TextInputLayout textInputLayoutPassword;

  private TextInputEditText textInputEditTextEmail;
  private TextInputEditText textInputEditTexPassword;

  private Button buttonLogin;
  private TextView textViewLinkRegister;

  private LoginButton buttonLoginFacebook;
  private SignInButton buttonGoogleSignIn;

  private Button buttonFacebookLogin;
  private Button buttonGoogleLogin;

  private FirebaseAuth auth;
  private ProgressBar progressBar;

  private InputValidation inputValidation;

  SharedPreferences sharedPreferences;
  SharedPreferences.Editor editor;
  String FILE="myEmailPass";

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

    progressBar=view.findViewById(R.id.progressBar);

  }


  /**
   * this method is to initialize listeners
   */
  private void initListeners() {

    buttonLogin.setOnClickListener(this);
    textViewLinkRegister.setOnClickListener(this);

  }


  /**
   * this method to initialize the Objects
   */
  private void initObjects() {

    inputValidation = new InputValidation(getActivity());

    sharedPreferences=this.getActivity().getSharedPreferences(FILE, Context.MODE_PRIVATE);
    editor=sharedPreferences.edit();
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
    }
  }

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
                Toast.makeText(getActivity(), "Auth Failed", Toast.LENGTH_SHORT).show();
              }
            } else {

              editor.putString("email",email);
              editor.putString("password",password);
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
   * */
  public void restartActivity(){
    Intent intent = getActivity().getIntent();
    //intent.addFlags(Intent.FLAG_ACTIVITY_);
    getActivity().finish();
    startActivity(intent);
    getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
  }

}
















