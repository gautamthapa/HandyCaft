package com.strontech.imgautam.handycaft.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.strontech.imgautam.handycaft.ProductFragments.ProductDescFragment;
import com.strontech.imgautam.handycaft.R;
import com.strontech.imgautam.handycaft.model.HandiCraft;
import java.util.ArrayList;
import java.util.List;

public class ProductRecyclerAdapter extends
    RecyclerView.Adapter<ProductRecyclerAdapter.ViewHolder> {

  private Context context;
  private List<HandiCraft> handiCrafts;



//  //for test
//  public ProductRecyclerAdapter() {
//  }
////=========================

  public ProductRecyclerAdapter(Context context, List<HandiCraft> handiCrafts) {
    this.context = context;
    this.handiCrafts = handiCrafts;
  }


  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout,
        parent, false);
    ViewHolder viewHolder = new ViewHolder(view, handiCrafts, context);

    return viewHolder;
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    HandiCraft handiCraft = handiCrafts.get(position);
    holder.textViewProductName.setText(handiCraft.getProduct_name());
    holder.textViewProductSP.setText("Rs. " + handiCraft.getProduct_sp());
    holder.textViewProductMRP.setText("Rs. " + handiCraft.getProduct_mrp());
    holder.textViewProductDiscount.setText(handiCraft.getProduct_discount() + "% off");

    strikeThroughText(holder.textViewProductMRP);

    Glide.with(context).load(handiCraft.getProduct_image()).into(holder.imageViewProductImage);
  }

  @Override
  public int getItemCount() {
    return handiCrafts.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

    ImageView imageViewProductImage;
    TextView textViewProductName;
    TextView textViewProductSP;
    TextView textViewProductMRP;
    TextView textViewProductDiscount;
    TextView textViewProductQuantity;
    TextView textViewProductDesc;


    List<HandiCraft> handiCraftsList = new ArrayList<HandiCraft>();

    ProductDescFragment productDescFragment;
    Context context;

    public ViewHolder(View itemView, List<HandiCraft> handiCraftsList, Context context) {
      super(itemView);

      this.handiCraftsList = handiCraftsList;
      this.context = context;

      itemView.setOnClickListener(this);

      productDescFragment = new ProductDescFragment();

      imageViewProductImage = itemView.findViewById(R.id.imageViewProductImage);
      textViewProductName = itemView.findViewById(R.id.textViewProductName);
      textViewProductSP = itemView.findViewById(R.id.textViewProductSP);
      textViewProductMRP = itemView.findViewById(R.id.textViewProductMRP);
      textViewProductDiscount = itemView.findViewById(R.id.textViewProductDiscount);
      //textViewProductQuantity =itemView.findViewById(R.id.textViewProductQuantity);

    }

    @Override
    public void onClick(View v) {
      int position = getAdapterPosition();
      HandiCraft handiCraft = this.handiCraftsList.get(position);

      Bundle b = new Bundle();

      b.putString("product_key", handiCraft.getProduct_id());
      b.putString("product_image", handiCraft.getProduct_image());
      b.putString("product_name", handiCraft.getProduct_name());
      b.putString("product_sp", handiCraft.getProduct_sp());
      b.putString("product_mrp", handiCraft.getProduct_mrp());
      b.putString("product_discount", handiCraft.getProduct_discount());
      b.putString("product_quantity", handiCraft.getProduct_quantity());
      b.putString("product_highlight", handiCraft.getProduct_highlight());
      b.putString("product_description", handiCraft.getProduct_desc());

      productDescFragment.setArguments(b);

      AppCompatActivity activity = (AppCompatActivity) v.getContext();
      Toast.makeText(activity, "" + handiCraft.getProduct_desc(), Toast.LENGTH_SHORT).show();

      FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
      ft.replace(R.id.mainFrame, productDescFragment);
      ft.addToBackStack("Home");
      ft.commit();
    }
  }

  private void strikeThroughText(TextView price) {
    price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
  }

}
