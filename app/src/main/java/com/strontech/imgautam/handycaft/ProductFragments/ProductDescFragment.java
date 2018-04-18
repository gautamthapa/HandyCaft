package com.strontech.imgautam.handycaft.ProductFragments;


import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.strontech.imgautam.handycaft.R;
import com.strontech.imgautam.handycaft.model.CartHandiCraft;
import com.strontech.imgautam.handycaft.model.HandiCraft;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductDescFragment extends Fragment implements OnClickListener {


  private ImageView imageViewProductImage;

  private ImageButton imageButtonWishList;
  private ImageButton imageButtonShare;

  private TextView textViewProductName;
  private TextView textViewProductSP;
  private TextView textViewProductMRP;
  private TextView textViewProductDiscount;
  private TextView textViewProductHighlights;
  private TextView textViewProductDetails;

  private Button buttonAddToCartItem;
  private Button buttonGoToCartItem;
  private Button buttonBuyProduct;

  private String productId;
  private String productImage;
  private String productName;
  private String productSP;
  private String productMRP;
  private String productDiscount;
  private String productQuantity;
  private String productHighlight;
  private String productDesc;

  private DatabaseReference databaseReference;

  View view;

  public ProductDescFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    view = inflater.inflate(R.layout.fragment_product_desc, container, false);

    initViews();
    initObjects();

    return view;
  }


  /**
   * This method is to initialize views
   */
  private void initViews() {

    imageViewProductImage = view.findViewById(R.id.imageViewProductImage);

    imageButtonWishList = view.findViewById(R.id.imageButtonWishList);
    imageButtonShare = view.findViewById(R.id.imageButtonShare);

    textViewProductName = view.findViewById(R.id.textViewProductName);
    textViewProductSP = view.findViewById(R.id.textViewProductSP);
    textViewProductMRP = view.findViewById(R.id.textViewProductMRP);
    textViewProductDiscount = view.findViewById(R.id.textViewProductDiscount);
    textViewProductHighlights = view.findViewById(R.id.textViewProductHighlights);
    textViewProductDetails = view.findViewById(R.id.textViewProductDetails);

    buttonAddToCartItem = view.findViewById(R.id.buttonAddToCartItem);
    buttonGoToCartItem = view.findViewById(R.id.buttonGoToCartItem);
    buttonBuyProduct = view.findViewById(R.id.buttonBuyProduct);

  }


  /**
   * This method is to initialize objects
   */
  private void initObjects() {

    buttonAddToCartItem.setOnClickListener(this);

    buttonGoToCartItem.setOnClickListener(this);

    databaseReference = FirebaseDatabase.getInstance().getReference("Cart Items");
    Bundle b = getArguments();
    if (b != null) {
      setData(b);
    }
  }

  private void setData(Bundle b) {

    productId=b.getString("product_key");
    productImage = b.getString("product_image");
    productName = b.getString("product_name");
    productSP = b.getString("product_sp");
    productMRP = b.getString("product_mrp");
    productDiscount = b.getString("product_discount");
    productQuantity = b.getString("product_quantity");
    productHighlight = b.getString("product_highlight");
    productDesc = b.getString("product_description");

    Toast.makeText(getActivity(), "" + productDesc, Toast.LENGTH_SHORT).show();

    Glide.with(getActivity()).load(productImage).into(imageViewProductImage);
    textViewProductName.setText(productName);
    textViewProductSP.setText("Rs. " + productSP);
    textViewProductMRP.setText("Rs. " + productMRP);
    textViewProductDiscount.setText(productDiscount + "% off");
    textViewProductHighlights.setText(productHighlight);
    textViewProductDetails.setText(productDesc);

    strikeThroughText(textViewProductMRP);

    //    Toast.makeText(getActivity(), productImage+"\n"
//        +productName+"\n"
//        +productSP+"\n"
//        +productMRP+"\n"
//        +productDiscount+"\n"
//        +productQuantity+"\n"
//        +productDesc+"\n",
//        Toast.LENGTH_LONG).show();

  }

  private void strikeThroughText(TextView price) {
    price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.buttonAddToCartItem:
        addItemToCart();
        break;
      case R.id.buttonGoToCartItem:
        goToProductCartFragment();
        break;
    }
  }


  /**
   * This is method is to add item to cart
   */
  private void addItemToCart() {

    int index = 1;

    if (index == 1) {

      buttonAddToCartItem.setVisibility(View.VISIBLE);
      buttonAddToCartItem.setVisibility(View.GONE);
//      String id = databaseReference.push().getKey();
//
//      HandiCraft handiCraft = new HandiCraft();
//      handiCraft.setProduct_image(productImage);
//      handiCraft.setProduct_name(productName);
//      handiCraft.setProduct_mrp(productMRP);
//      handiCraft.setProduct_sp(productSP);
//      handiCraft.setProduct_discount(productDiscount);
//      handiCraft.setProduct_quantity(productQuantity);
//      handiCraft.setProduct_desc(productDesc);


      //take another Bean class to set data of cart items
      CartHandiCraft cartHandiCraft=new CartHandiCraft();
      cartHandiCraft.setProduct_id(productId);
      cartHandiCraft.setProduct_image(productImage);
      cartHandiCraft.setProduct_name(productName);
      cartHandiCraft.setProduct_mrp(productMRP);
      cartHandiCraft.setProduct_sp(productSP);
      cartHandiCraft.setProduct_discount(productDiscount);
      cartHandiCraft.setProduct_quantity(productQuantity);
      cartHandiCraft.setProduct_highlight(productHighlight);
      cartHandiCraft.setProduct_desc(productDesc);

      databaseReference.child(productId).setValue(cartHandiCraft);
      Toast.makeText(getActivity(), "Item added to cart", Toast.LENGTH_SHORT).show();

    }
    buttonAddToCartItem.setVisibility(View.GONE);
    buttonGoToCartItem.setVisibility(View.VISIBLE);

  }


  private void goToProductCartFragment() {


    //Send Initial data to cart fragment
    Bundle p=new Bundle();
    p.putString("initial_price",productSP);

    new ProductCartFragment().setArguments(p);

    //==================================



    FragmentTransaction ft = getFragmentManager().beginTransaction();
    ft.replace(R.id.mainFrame, new ProductCartFragment());
    ft.commit();
  }
}
