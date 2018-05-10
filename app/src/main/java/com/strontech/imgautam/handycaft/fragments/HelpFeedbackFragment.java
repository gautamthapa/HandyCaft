package com.strontech.imgautam.handycaft.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.strontech.imgautam.handycaft.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HelpFeedbackFragment extends Fragment {


  private View view;
  private Toolbar toolbarHelpFeedback;
  public HelpFeedbackFragment() {
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
    view= inflater.inflate(R.layout.fragment_help_feedback, container, false);

    initViews();
    initListeners();
    initObjects();


  return view;
  }

  private void initViews() {

    toolbarHelpFeedback=view.findViewById(R.id.toolbarHelpFeedback);
  }

  private void initListeners() {

  }

  private void initObjects() {

    toolbarHelpFeedback.setTitle("Help and Feedback");
    toolbarHelpFeedback.setTitleTextColor(Color.WHITE);
    toolbarHelpFeedback.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
    toolbarHelpFeedback.setNavigationOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        FragmentTransaction ft=getFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, new HomeFragment());
        ft.commit();
      }
    });


  }

  @Override
  public void onStop() {
    super.onStop();
    ((AppCompatActivity) getActivity()).getSupportActionBar().show();
  }
}
