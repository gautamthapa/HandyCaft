package com.strontech.imgautam.handycaft.ProductFragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.strontech.imgautam.handycaft.R;
import com.strontech.imgautam.handycaft.adapters.ProductWishRecyclerAdapter;
import com.strontech.imgautam.handycaft.fragments.HomeFragment;
import com.strontech.imgautam.handycaft.model.CartHandiCraft;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductWishListFragment extends Fragment {

  private Toolbar toolbarWishListFragment;
  private RecyclerView recyclerViewShowWishListItems;
  private RecyclerView.Adapter adapter;

  private LinearLayout circleProgressBarLayout;
  private CircleProgressBar circleProgressBar;

  private View view;

  private List<CartHandiCraft> cartHandiCraftList;

  private DatabaseReference databaseReference;

  public ProductWishListFragment() {
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
    view = inflater.inflate(R.layout.fragment_product_wish_list, container, false);


    initViews();
    initListeners();
    initObjects();

    return view;

  }

  private void initViews() {

    toolbarWishListFragment=view.findViewById(R.id.toolbarWishListFragment);
    circleProgressBarLayout = view.findViewById(R.id.circleProgressBarLayout);
    circleProgressBar = view.findViewById(R.id.circleProgressBar);
    recyclerViewShowWishListItems = view.findViewById(R.id.recyclerViewShowWishListItems);
  }

  private void initListeners() {

  }

  private void initObjects() {

    toolbarWishListFragment.setTitle("Wishlist Items");
    toolbarWishListFragment.setTitleTextColor(Color.WHITE);
    toolbarWishListFragment.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
    toolbarWishListFragment.setNavigationOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        FragmentTransaction ft=getFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, new HomeFragment());
        ft.commit();
      }
    });

    cartHandiCraftList = new ArrayList<CartHandiCraft>();

    circleProgressBar.setColorSchemeResources(R.color.colorPrimary);
    setUpRecyclerView();

    databaseReference = FirebaseDatabase.getInstance().getReference("Wish Item");
    getDataFromDatabase();
  }


  private void setUpRecyclerView() {

    recyclerViewShowWishListItems.setLayoutManager(new LinearLayoutManager(getActivity()));
   /* recyclerViewShowWishListItems.setLayoutManager(new LinearLayoutManager(getActivity()) {
      @Override
      public boolean canScrollHorizontally() {
        return false;
      }

      @Override
      public boolean canScrollVertically() {
        return false;
      }
    });*/
    recyclerViewShowWishListItems.setItemAnimator(new DefaultItemAnimator());

  }


  private void getDataFromDatabase() {

    circleProgressBarLayout.setVisibility(View.VISIBLE);
    circleProgressBar.setVisibility(View.VISIBLE);

    databaseReference.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        circleProgressBarLayout.setVisibility(View.GONE);
        circleProgressBar.setVisibility(View.GONE);

        if (cartHandiCraftList !=null){
          cartHandiCraftList.clear();
        }

        for (DataSnapshot postSnapshot:dataSnapshot.getChildren()){
          CartHandiCraft cartHandiCraft=postSnapshot.getValue(CartHandiCraft.class);
          cartHandiCraftList.add(cartHandiCraft);
        }


        adapter=new ProductWishRecyclerAdapter(getActivity(), cartHandiCraftList);
        recyclerViewShowWishListItems.setAdapter(adapter);
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });
  }



  @Override
  public void onStop() {
    super.onStop();
    ((AppCompatActivity) getActivity()).getSupportActionBar().show();

  }
}
