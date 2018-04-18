package com.strontech.imgautam.handycaft.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.strontech.imgautam.handycaft.R;
import com.strontech.imgautam.handycaft.UserInfo;
import com.strontech.imgautam.handycaft.helper.InputValidation;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements OnClickListener{


  private LinearLayout linearLayoutRegister;

  private TextInputLayout textInputLayoutUserName;
  private TextInputLayout textInputLayoutUserEmail;
  private TextInputLayout textInputLayoutUserMobNumber;
  private TextInputLayout textInputLayoutUserPassword;
  private TextInputLayout textInputLayoutUserConfirmPassword;

  private TextInputEditText textInputEditTextUserName;
  private TextInputEditText textInputEditTextUserEmail;
  private TextInputEditText textInputEditTextUserMobNumber;
  private TextInputEditText textInputEditTextUserPassword;
  private TextInputEditText textInputEditTextUserConfirmPassword;

  private Button buttonRegisterUser;
  private TextView textViewLoginLink;

  private ProgressBar progressBar;

  private UserInfo userInfo;
  private InputValidation inputValidation;

  private FirebaseAuth firebaseAuth;
  private FirebaseAuth.AuthStateListener authStateListener;
  private DatabaseReference databaseReference;

  private View view;

  private Context context;
  public RegisterFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    view =inflater.inflate(R.layout.fragment_register, container, false);

    initViews();
    initListeners();
    initObjects();

    return view;
  }



  /**
   * This method is to initialize views
   */
  private void initViews() {

    linearLayoutRegister=view.findViewById(R.id.linearLayoutRegister);

    textInputLayoutUserName=view.findViewById(R.id.textInputLayoutUserName);
    textInputLayoutUserEmail=view.findViewById(R.id.textInputLayoutUserEmail);
    textInputLayoutUserMobNumber=view.findViewById(R.id.textInputLayoutUserMobNumber);
    textInputLayoutUserPassword=view.findViewById(R.id.textInputLayoutUserPassword);
    textInputLayoutUserConfirmPassword=view.findViewById(R.id.textInputLayoutUserConfirmPassword);

    textInputEditTextUserName=view.findViewById(R.id.textInputEditTextUserName);
    textInputEditTextUserEmail=view.findViewById(R.id.textInputEditTextUserEmail);
    textInputEditTextUserMobNumber=view.findViewById(R.id.textInputEditTextUserMobNumber);
    textInputEditTextUserPassword=view.findViewById(R.id.textInputEditTextUserPassword);
    textInputEditTextUserConfirmPassword=view.findViewById(R.id.textInputEditTextUserConfirmPassword);

    buttonRegisterUser=view.findViewById(R.id.buttonRegisterUser);
    textViewLoginLink=view.findViewById(R.id.textViewLoginLink);

    progressBar=view.findViewById(R.id.progressBar);
  }


  /**
   * This method is to initialize listeners
   */
  private void initListeners(){
    buttonRegisterUser.setOnClickListener(this);
    textViewLoginLink.setOnClickListener(this);
  }

  /**
   * This method is to initialize objects to be used
   */
  private void initObjects(){

    inputValidation=new InputValidation(getActivity());
    firebaseAuth=FirebaseAuth.getInstance();

    if (firebaseAuth.getCurrentUser() !=null){
      FragmentTransaction ft=getFragmentManager().beginTransaction();
          ft.replace(R.id.mainFrame, new HomeFragment());
          ft.commit();
    }

//    authStateListener =new AuthStateListener() {
//      @Override
//      public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//
//        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
//        if (user!=null){
//          FragmentTransaction ft=getFragmentManager().beginTransaction();
//          ft.replace(R.id.mainFrame, new HomeFragment());
//          ft.commit();
//        }
//      }
//    };

  }

  @Override
  public void onClick(View v) {

    switch (v.getId()){
      case R.id.buttonRegisterUser:
        postDataToFirebaseDb();
        break;
        case R.id.textViewLoginLink:
          FragmentTransaction ft=getFragmentManager().beginTransaction();
          ft.replace(R.id.mainFrame, new LoginFragment());
          ft.commit();
          break;
    }

  }



  /**
   * This method is to validate the input text fields and post data to Firebase database
   * */
  private void postDataToFirebaseDb(){

    if (!inputValidation.isInputEditTextFilled(textInputEditTextUserName, textInputLayoutUserName,
        "Username")){
      return;
    }
    if (!inputValidation.isInputEditTextFilled(textInputEditTextUserEmail, textInputLayoutUserEmail,
        "Enter Username")){
      return;
    }
    if (!inputValidation.isInputEditTextEmail(textInputEditTextUserEmail, textInputLayoutUserEmail,
        "Enter valid Email")){
      return;
    }
    if (!inputValidation.isInputEditTextFilled(textInputEditTextUserMobNumber, textInputLayoutUserMobNumber,
        "Enter valid Mobile Number")){
      return;
    }
    if (!inputValidation.isInputEditTextFilled(textInputEditTextUserPassword, textInputLayoutUserPassword,
        "Enter Password")){
      return;
    }
    if (!inputValidation.isInputEditTextMatches(textInputEditTextUserPassword, textInputEditTextUserConfirmPassword, textInputLayoutUserConfirmPassword,
        "Password Does Not Match")){
      return;
    }

    final String username, email, mob_number, password;

    username=textInputEditTextUserName.getText().toString().trim();
    email=textInputEditTextUserEmail.getText().toString().trim();
    mob_number=textInputEditTextUserMobNumber.getText().toString().trim();
    password=textInputEditTextUserConfirmPassword.getText().toString().trim();



    progressBar.setVisibility(View.VISIBLE);

    //create User

    firebaseAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(getActivity(),
            new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {

                Toast.makeText(getActivity(), "Registration Successful", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener
                
                if (!task.isSuccessful()){
                  Toast.makeText(getActivity(), "Authentication Failed."+task.getException(), Toast.LENGTH_SHORT).show();
                }else {
                  FragmentTransaction ft=getFragmentManager().beginTransaction();
                  ft.replace(R.id.mainFrame, new HomeFragment());
                  ft.commit();
                }
              }
            });


//    Query emailQuery=FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("name").equalTo(username);
//    emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//      @Override
//      public void onDataChange(DataSnapshot dataSnapshot) {
//        if (dataSnapshot.getChildrenCount()>0){
//          Toast.makeText(getActivity(), "Email id already registered", Toast.LENGTH_SHORT).show();
//        }else {
//
//          firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
//              getActivity(),
//              new OnCompleteListener<AuthResult>() {
//                @Override
//
//                public void onComplete(@NonNull Task<AuthResult> task) {
//
//                  if (!task.isSuccessful()){
//                    Toast.makeText(getActivity(), "Signup Error", Toast.LENGTH_SHORT).show();
//                  }else {
//                    String user_id=firebaseAuth.getCurrentUser().getUid();
//                    DatabaseReference current_user_db= FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
//
//                    Map newPost=new HashMap();
//                    newPost.put("name", username);
//                    newPost.put("email", email);
//                    newPost.put("mob_number", mob_number);
//                    newPost.put("password",password);
//
//                    current_user_db.setValue(newPost);
//
//                    Toast.makeText(context, "Registered successfully", Toast.LENGTH_SHORT).show();
//                  }
//                }
//              });
//
//        }
//      }
//
//      @Override
//      public void onCancelled(DatabaseError databaseError) {
//
//
//      }
//    });



























  }


  @Override
  public void onResume() {
    super.onResume();
    progressBar.setVisibility(View.GONE);
  }


  @Override
  public void onStart() {
    super.onStart();

  //  firebaseAuth.addAuthStateListener(authStateListener);
  }

  @Override
  public void onStop() {
    super.onStop();
  //  firebaseAuth.removeAuthStateListener(authStateListener);
  }
}
