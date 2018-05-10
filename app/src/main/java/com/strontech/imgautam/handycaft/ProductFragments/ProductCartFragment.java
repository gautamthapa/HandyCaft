package com.strontech.imgautam.handycaft.ProductFragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.strontech.imgautam.handycaft.R;
import com.strontech.imgautam.handycaft.SellerFragments.AddProductFragment;
import com.strontech.imgautam.handycaft.activities.LoginActivity;
import com.strontech.imgautam.handycaft.adapters.ProductCartRecyclerAdapter;
import com.strontech.imgautam.handycaft.fragments.HomeFragment;
import com.strontech.imgautam.handycaft.model.CartHandiCraft;
import com.strontech.imgautam.handycaft.model.HandiCraft;
import com.strontech.imgautam.handycaft.model.HandiCraft;
import com.strontech.imgautam.handycaft.userfragments.UserAddressFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductCartFragment extends Fragment implements View.OnClickListener{

  private Toolbar toolbarFragmentCart;

  private RecyclerView recyclerView;

  private RecyclerView.Adapter adapter;

  private LinearLayout linearLayoutCart;
  private LinearLayout linearLayoutCartEmpty;

  private LinearLayout circleProgressBarLayout;
  private CircleProgressBar circleProgressBar;

  private DatabaseReference databaseReference;

  private List<CartHandiCraft> cartHandiCrafts;

  private TextView textViewItemCount;
  private TextView textViewTotalAmount;
  private TextView textViewTotalAmountPayable;

  private Button buttonTotalAmount;
  private Button buttonShopNow;
  private Button buttonContinueBuy;


  //To check user logged in or not
  SharedPreferences sharedPreferences;
  SharedPreferences.Editor editor;
  String email;
  String email_google;
  String first_name;
  String last_name;


  View view;
  int position=0;

  String initial_price;
  int sumSp;


  public ProductCartFragment() {
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
    view = inflater.inflate(R.layout.fragment_product_cart, container, false);

    initViews();
    initListeners();
    initObjects();

    return view;
  }

  /**
   * This method ot initialize views
   */
  private void initViews() {

    toolbarFragmentCart=view.findViewById(R.id.toolbarFragmentCart);

    linearLayoutCart=view.findViewById(R.id.linearLayoutCart);
    linearLayoutCartEmpty=view.findViewById(R.id.linearLayoutCartEmpty);
    recyclerView = view.findViewById(R.id.recyclerViewShowCartItems);

    circleProgressBarLayout=view.findViewById(R.id.circleProgressBarLayout);
    circleProgressBar=view.findViewById(R.id.circleProgressBar);

    textViewItemCount=view.findViewById(R.id.textViewItemCount);
    textViewTotalAmount=view.findViewById(R.id.textViewTotalAmount);
    textViewTotalAmountPayable=view.findViewById(R.id.textViewTotalAmountPayable);

    buttonTotalAmount=view.findViewById(R.id.buttonTotalAmount);
    buttonShopNow=view.findViewById(R.id.buttonShopNow);
    buttonContinueBuy=view.findViewById(R.id.buttonContinueBuy);
  }


  /**
   * This method ot initialize Listeners
   */
  private void initListeners() {
    buttonShopNow.setOnClickListener(this);
    buttonContinueBuy.setOnClickListener(this);
    buttonTotalAmount.setOnClickListener(this);
  }



  /**
   * This method ot initialize Objects
   */
  private void initObjects() {

    setUpToolbar();
    cartHandiCrafts = new ArrayList<>();
    sharedPreferences = getActivity().getSharedPreferences("myEmailPass", Context.MODE_PRIVATE);


    circleProgressBar.setColorSchemeResources(R.color.colorPrimary);
    setUpRecyclerView();


    databaseReference = FirebaseDatabase.getInstance().getReference("Cart Items");

    getDataFromFirebase();


    //get data from ProductDescFragment and send to Adapter

    Bundle b=getArguments();
    if (b != null) {
      initial_price=b.getString("initial_price");
    }
  }


  private void setUpToolbar() {
    toolbarFragmentCart.setTitle("Your cart");
    toolbarFragmentCart.setTitleTextColor(Color.WHITE);
    toolbarFragmentCart.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
    toolbarFragmentCart.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        getActivity().onBackPressed();
      }
    });
  }


  private void setUpRecyclerView() {

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()){
      @Override
      public boolean canScrollHorizontally() {
        return false;
      }

      @Override
      public boolean canScrollVertically() {
        return false;
      }
    });
    recyclerView.setItemAnimator(new DefaultItemAnimator());
  }


  private void getDataFromFirebase() {
    circleProgressBarLayout.setVisibility(View.VISIBLE);
    circleProgressBar.setVisibility(View.VISIBLE);


    final List<String> updatedSpList=new ArrayList<String>();

    databaseReference.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        circleProgressBarLayout.setVisibility(View.GONE);
        circleProgressBar.setVisibility(View.GONE);

        if (cartHandiCrafts != null && updatedSpList !=null) {
          cartHandiCrafts.clear();
          updatedSpList.clear();
        }
        for (DataSnapshot postDataSnapshot : dataSnapshot.getChildren()) {
          CartHandiCraft cartHandiCraft = postDataSnapshot.getValue(CartHandiCraft.class);
          cartHandiCrafts.add(cartHandiCraft);
          updatedSpList.add(cartHandiCraft.getProduct_sp());

        }
        //Toast.makeText(getActivity(), ""+updatedSpList.toString(), Toast.LENGTH_SHORT).show();
        sumSp=0;
        for (int i=0; i<updatedSpList.size(); i++){
          sumSp= sumSp+Integer.parseInt(updatedSpList.get(i));
        }
        adapter = new ProductCartRecyclerAdapter(getActivity(), cartHandiCrafts);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //position=adapter.getItemCount();

        if (cartHandiCrafts.size()==0){
          linearLayoutCart.setVisibility(View.GONE);
          linearLayoutCartEmpty.setVisibility(View.VISIBLE);
        }else {
          linearLayoutCartEmpty.setVisibility(View.GONE);
          linearLayoutCart.setVisibility(View.VISIBLE);
          textViewItemCount.setText(""+cartHandiCrafts.size());
          textViewTotalAmount.setText("₹"+sumSp);
          textViewTotalAmountPayable.setText("₹"+sumSp);
          buttonTotalAmount.setText("₹"+sumSp);
        }

       // Toast.makeText(getActivity(), "Total amount: "+sumSp, Toast.LENGTH_SHORT).show();
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



  @Override
  public void onClick(View v) {

    if (v.getId() == buttonShopNow.getId()){
      FragmentTransaction ft=getFragmentManager().beginTransaction();
      ft.replace(R.id.mainFrame, new HomeFragment());
      ft.commit();
    }else if (v.getId() == buttonContinueBuy.getId()){


      goToAddressOrLogin();

    }
  }

  private void goToAddressOrLogin() {

    email = sharedPreferences.getString("email", null);
    email_google=sharedPreferences.getString("email_google",null);
    first_name = sharedPreferences.getString("facebook_first_name", null);
    last_name = sharedPreferences.getString("facebook_last_name", null);

    if (email !=null || email_google !=null || first_name !=null || last_name !=null){
      FragmentTransaction ft=getFragmentManager().beginTransaction();
      ft.replace(R.id.mainFrame, new UserAddressFragment());
      ft.addToBackStack(null);
      ft.commit();
    }else {
      Intent intent=new Intent(getActivity(), LoginActivity.class);
      startActivity(intent);
    }

  }
}
