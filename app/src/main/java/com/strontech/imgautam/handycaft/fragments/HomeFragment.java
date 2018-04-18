package com.strontech.imgautam.handycaft.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.strontech.imgautam.handycaft.R;
import com.strontech.imgautam.handycaft.adapters.ProductRecyclerAdapter;
import com.strontech.imgautam.handycaft.helper.Constants;
import com.strontech.imgautam.handycaft.model.HandiCraft;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


  private LinearLayout circleProgressBarLayout;
  private CircleProgressBar circleProgressBar;

  private RecyclerView recyclerView;
  private RecyclerView.Adapter adapter;


  private DatabaseReference databaseReference;


  private List<HandiCraft> handiCrafts;
  View view;

  public HomeFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment

    view = inflater.inflate(R.layout.fragment_home, container, false);

    initViews();
    initObjects();
    return view;
  }


  /**
   * This method is to initialize views
   */
  private void initViews() {

    circleProgressBarLayout = view.findViewById(R.id.circleProgressBarLayout);
    circleProgressBar = view.findViewById(R.id.circleProgressBar);
    recyclerView = view.findViewById(R.id.recyclerView);
  }


  /**
   * This method is to initialize objects
   */
  private void initObjects() {

    circleProgressBar.setColorSchemeResources(R.color.colorPrimary);
    setUpRecyclerView();

    handiCrafts = new ArrayList<>();
    //progressDialog = new ProgressDialog(getActivity());

    databaseReference = FirebaseDatabase.getInstance()
        .getReference(Constants.DATABASE_PATH_UPLOADS);
    getDataFromFirebase();

  }

  private void setUpRecyclerView() {

    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
    recyclerView.setLayoutManager(layoutManager);
    // recyclerView.addItemDecoration(new );

//    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2, GridLayoutManager.VERTICAL, false){
//      @Override
//      public boolean canScrollHorizontally() {
//        return false;
//      }
//
//      @Override
//      public boolean canScrollVertically() {
//        return false;
//      }
//    });

    recyclerView.setItemAnimator(new DefaultItemAnimator());
  }


  /**
   * This method is to get data from Firebase data
   */
  private void getDataFromFirebase() {

//    progressDialog.setMessage("Please wait...");
//    progressDialog.show();
    circleProgressBarLayout.setVisibility(View.VISIBLE);
    circleProgressBar.setVisibility(View.VISIBLE);
    databaseReference.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
    //    progressDialog.dismiss();

        circleProgressBarLayout.setVisibility(View.GONE);
        circleProgressBar.setVisibility(View.GONE);
        if (handiCrafts != null) {
          handiCrafts.clear();
        }
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
          HandiCraft handiCraft = postSnapshot.getValue(HandiCraft.class);
          handiCrafts.add(handiCraft);
        }

        adapter = new ProductRecyclerAdapter(getActivity(), handiCrafts);
        recyclerView.setAdapter(adapter);
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });
  }
}
