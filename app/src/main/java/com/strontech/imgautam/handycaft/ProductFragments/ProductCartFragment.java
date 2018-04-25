package com.strontech.imgautam.handycaft.ProductFragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.strontech.imgautam.handycaft.adapters.ProductCartRecyclerAdapter;
import com.strontech.imgautam.handycaft.model.CartHandiCraft;
import com.strontech.imgautam.handycaft.model.HandiCraft;
import com.strontech.imgautam.handycaft.model.HandiCraft;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductCartFragment extends Fragment {


  private RecyclerView recyclerView;

  private RecyclerView.Adapter adapter;

  private LinearLayout circleProgressBarLayout;
  private CircleProgressBar circleProgressBar;

  private DatabaseReference databaseReference;

  private List<CartHandiCraft> cartHandiCrafts;

  private TextView textViewItemCount;
  private TextView textViewTotalAmount;
  private TextView textViewTotalAmountPayable;

  private Button buttonTotalAmount;
  private Button buttonContinueBuy;




  View view;
  int position=0;

  String initial_price;
  int sumSp;


  public ProductCartFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    view = inflater.inflate(R.layout.fragment_product_cart, container, false);

    initViews();
    initObjects();

    return view;
  }

  /**
   * This method ot initialize views
   */
  private void initViews() {

    recyclerView = view.findViewById(R.id.recyclerViewShowCartItems);

    circleProgressBarLayout=view.findViewById(R.id.circleProgressBarLayout);
    circleProgressBar=view.findViewById(R.id.circleProgressBar);

    textViewItemCount=view.findViewById(R.id.textViewItemCount);
    textViewTotalAmount=view.findViewById(R.id.textViewTotalAmount);
    textViewTotalAmountPayable=view.findViewById(R.id.textViewTotalAmountPayable);

    buttonTotalAmount=view.findViewById(R.id.buttonTotalAmount);
    buttonContinueBuy=view.findViewById(R.id.buttonContinueBuy);
  }

  /**
   * This method ot initialize Objects
   */
  private void initObjects() {

    setUpRecyclerView();

    cartHandiCrafts = new ArrayList<>();

    circleProgressBar.setColorSchemeResources(R.color.colorPrimary);
    setUpRecyclerView();


    databaseReference = FirebaseDatabase.getInstance().getReference("Cart Items");

    getDataFromFirebase();


    //get data from ProductDescFragment and send to Adapter

    Bundle b=getArguments();
    if (b != null) {
      initial_price=b.getString("initial_price");
    }

    Toast.makeText(getActivity(), ""+initial_price, Toast.LENGTH_SHORT).show();

//    HandiCraft handiCraft=new HandiCraft();
//    Toast.makeText(getActivity(), "Product price: "+handiCraft.getProduct_sp(), Toast.LENGTH_SHORT).show();
  }


  private void setUpRecyclerView() {
//    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
//    recyclerView.setLayoutManager(layoutManager);

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

          //get data....
//          if (cartHandiCraft != null) {
//            Log.d("Price: ","Product Price"+cartHandiCraft.getProduct_sp());
//          }
          //Toast.makeText(getActivity(), "Price: "+handiCraft.getProduct_sp(), Toast.LENGTH_SHORT).show();

        }
        //Toast.makeText(getActivity(), ""+updatedSpList.toString(), Toast.LENGTH_SHORT).show();
        sumSp=0;
        for (int i=0; i<updatedSpList.size(); i++){
          sumSp= sumSp+Integer.parseInt(updatedSpList.get(i));
        }
        adapter = new ProductCartRecyclerAdapter(getActivity(), cartHandiCrafts);
        recyclerView.setAdapter(adapter);
        //position=adapter.getItemCount();

        textViewItemCount.setText("Rs. "+cartHandiCrafts.size());
        textViewTotalAmount.setText("Rs. "+sumSp);
        textViewTotalAmountPayable.setText("Rs. "+sumSp);
        buttonTotalAmount.setText("Rs. "+sumSp);

        Toast.makeText(getActivity(), "Total amount: "+sumSp, Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });
  }
}
