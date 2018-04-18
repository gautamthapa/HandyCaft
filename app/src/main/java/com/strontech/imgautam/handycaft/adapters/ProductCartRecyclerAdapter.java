package com.strontech.imgautam.handycaft.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.strontech.imgautam.handycaft.R;
import com.strontech.imgautam.handycaft.model.CartHandiCraft;
import com.strontech.imgautam.handycaft.model.HandiCraft;
import com.strontech.imgautam.handycaft.model.HandiCraft;
import java.util.ArrayList;
import java.util.List;

public class ProductCartRecyclerAdapter extends
    RecyclerView.Adapter<ProductCartRecyclerAdapter.ViewHolder> {

  private Context context;
  private List<CartHandiCraft> cartHandiCrafts;
//  HandiCraft handiCraft;


  //Test
  String numm;


  int amount;

  public ProductCartRecyclerAdapter(Context context, List<CartHandiCraft> cartHandiCrafts) {
    this.context = context;
    this.cartHandiCrafts = cartHandiCrafts;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

    View v = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.product_cart_item_layout, parent, false);

    ViewHolder viewHolder = new ViewHolder(v, cartHandiCrafts, context);
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    CartHandiCraft cartHandiCraft = cartHandiCrafts.get(position);

    //Toast.makeText(context, handiCraft.getProduct_name(), Toast.LENGTH_SHORT).show();
    holder.textViewProductName.setText(cartHandiCraft.getProduct_name());
    holder.textViewProductStock.setText(cartHandiCraft.getProduct_quantity());
    holder.textViewProductTotalAmt.setText(cartHandiCraft.getProduct_sp());

    //numm=handiCraft.getProduct_name();
    Glide.with(context).load(cartHandiCraft.getProduct_image()).into(holder.imageViewProductImage);
    //set data to spinner
    holder.spinnerQuantity.setAdapter(
        spinnerSetQuantity(Integer.parseInt(cartHandiCraft.getProduct_quantity()))
    );
    // spinnerSetQuantity(Integer.parseInt(handiCraft.getProduct_quantity()));
    // String sp=handiCraft.getProduct_sp();
//    amount= Integer.parseInt(handiCraft.getProduct_sp());
    //  Toast.makeText(context, ""+sp, Toast.LENGTH_SHORT).show();
  }


  /**
   * spinnerSetQuantity
   */
  private ArrayAdapter spinnerSetQuantity(int product_quantity) {
    List<Integer> quantity = new ArrayList<>();
    for (int i = 1; i <= product_quantity; i++) {
      //  Toast.makeText(this, ""+i+"\n", Toast.LENGTH_SHORT).show();
      quantity.add(i);
    }

    ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(context,
        android.R.layout.simple_spinner_item,
        quantity);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    return adapter;
  }

  @Override
  public int getItemCount() {
    return cartHandiCrafts.size();
  }


  /**
   * This is Inner Class For Adapter ViewHolder Class
   */
  public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

    ImageView imageViewProductImage;

    TextView textViewProductName;
    TextView textViewProductStock;
    TextView textViewProductTotalAmt;
    Spinner spinnerQuantity;

    Button buttonRemoveCartItem;
    Button buttonMoveToWishListCartItem;

    //update price
    DatabaseReference databaseReferenceCart, databaseReferenceInitial;
    List<CartHandiCraft> cartHandiCraftList = new ArrayList<CartHandiCraft>();
    Context context;


    String product_idd;
    String product_image;
    String product_name;
    String product_mrp;
    String product_sp;
    String product_quantity;
    String product_spinner_pos;
    String product_highlight;
    String product_desc;

    int spinnerPosition;


    List<String> stringList;


    int quantity;
    int amount;

    int a;

    public ViewHolder(View itemView, List<CartHandiCraft> cartHandiCraftList, Context context) {
      super(itemView);

      this.cartHandiCraftList = cartHandiCraftList;
      this.context = context;

      imageViewProductImage = itemView.findViewById(R.id.imageViewProductImage);

      textViewProductName = itemView.findViewById(R.id.textViewProductName);
      textViewProductStock = itemView.findViewById(R.id.textViewProductStock);
      textViewProductTotalAmt = itemView.findViewById(R.id.textViewProductTotalAmt);

      spinnerQuantity = itemView.findViewById(R.id.spinnerQuantity);

      buttonRemoveCartItem = itemView.findViewById(R.id.buttonRemoveCartItem);
      buttonMoveToWishListCartItem = itemView.findViewById(R.id.buttonMoveToWishListCartItem);

      buttonMoveToWishListCartItem.setOnClickListener(this);

      stringList = new ArrayList<String>();
      //DatabaseReferences
      databaseReferenceCart = FirebaseDatabase.getInstance().getReference("Cart Items");
      databaseReferenceInitial = FirebaseDatabase.getInstance().getReference("uploads");

      calculateTotalAmount();

      //Check Data get/////////////////////
//      int pos=getAdapterPosition()+1;
//      HandiCraft handiCraft =handiCraftList.get(pos);
////
////      Toast.makeText(context, "Price: "+handiCraft.getProduct_sp(), Toast.LENGTH_SHORT).show();
//
//
//      //int pos=getAdapterPosition();
//      Log.d("Position: ", "At "+handiCraft.getProduct_sp());
//
      //---------------------------------------------------------------------------

    }

    private void calculateTotalAmount() {

//      databaseReferenceCart.child(product_idd).addValueEventListener(new ValueEventListener() {
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//
//        }
//
//        @Override
//        public void onCancelled(DatabaseError databaseError) {
//
//        }
//      });
//
      spinnerQuantity.setOnItemSelectedListener(new OnItemSelectedListener() {


        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

          int positions = getAdapterPosition();
          CartHandiCraft cartHandiCraft = cartHandiCraftList.get(positions);

          a = position;
          //update price
          product_idd = cartHandiCraft.getProduct_id();
          product_image = cartHandiCraft.getProduct_image();
          product_name = cartHandiCraft.getProduct_name();
          product_mrp = cartHandiCraft.getProduct_mrp();
          product_sp = cartHandiCraft.getProduct_sp();
          product_quantity = cartHandiCraft.getProduct_quantity();
          product_spinner_pos = cartHandiCraft.getProduct_spinner_pos();
          product_highlight = cartHandiCraft.getProduct_highlight();
          product_desc = cartHandiCraft.getProduct_desc();

          quantity = (int) spinnerQuantity.getItemAtPosition(position);

          // spinnerQuantity.setSelection(Integer.parseInt(cartHandiCraft.getProduct_spinner_pos()));

          Log.d("gtm: ", "Position get back" + spinnerPosition);
//          String spinnerPos=spinnerQuantity.getSelectedItem().toString();
//          ArrayAdapter myAdapter=(ArrayAdapter)spinnerQuantity.getAdapter();
//          int pos=myAdapter.getPosition(spinnerPos);
//          spinnerQuantity.setSelection(pos);
          spinnerPosition = spinnerQuantity.getSelectedItemPosition();
          // Log.w("gtm", "Position: " + pos);

          //pos=spinnerQuantity.getLastVisiblePosition();
          amount = Integer.parseInt(cartHandiCraft.getProduct_sp());

          //databaseReference.setValue(amount)

          Log.d("Product Id","Id"+product_idd);
          //get initial price from server
          databaseReferenceInitial.child(product_idd)
              .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                  for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String initialData = postSnapshot.getValue(String.class);
                    stringList.add(initialData);
                  }
                  // Log.d("Initial", " Price: " + stringList.get(8));

                  //Update
                  int pri= Integer.parseInt(stringList.get(8));
                  Log.d("Price","Price"+pri);

//                  spinnerPosition=spinnerQuantity.getSelectedItemPosition();
//                  Log.d("gtm","Position: "+spinnerPosition);
                //  Log.d("gtm", "Quantity: " + spinnerPosition);

                  amount = (pri * quantity);
                  textViewProductTotalAmt.setText("Rs. " + amount);
                  //spinnerQuantity.setSelection(spinnerPosition);
// Log.d("Imgautam","Total price"+amount);
                  updatePrice(product_idd, String.valueOf(amount),
                      product_image,
                      product_name,
                      product_mrp,
                      //String.valueOf(quantity),
                      product_quantity,
                      String.valueOf(spinnerPosition),
                      product_highlight,
                      product_desc);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
              });


        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
      });
    }

    /**
     * Update price...
     */
    private void updatePrice(String id, String price, String product_image, String product_name,
        String product_mrp,
        String product_quantity,
        String product_spinner_pos,
        String product_highlight,
        String product_desc) {

      //Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
      //  Log.d("DatabaseReference: ","FB"+databaseReference.toString());

      CartHandiCraft cartHandiCraft = new CartHandiCraft();
      cartHandiCraft.setProduct_id(id);
      cartHandiCraft.setProduct_sp(price);
      cartHandiCraft.setProduct_image(product_image);
      cartHandiCraft.setProduct_name(product_name);
      cartHandiCraft.setProduct_mrp(product_mrp);
      cartHandiCraft.setProduct_quantity(product_quantity);
      cartHandiCraft.setProduct_spinner_pos(product_spinner_pos);
      cartHandiCraft.setProduct_highlight(product_highlight);
      cartHandiCraft.setProduct_desc(product_desc);

      DatabaseReference df = FirebaseDatabase.getInstance().getReference("Cart Items").child(id);
      df.setValue(cartHandiCraft);
      //databaseReference.setValue(cartHandiCraft);

    }


    /**
     * This is override method
     */
    @Override
    public void onClick(View v) {

      final int position = getAdapterPosition();
      CartHandiCraft cartHandiCraft = this.cartHandiCraftList.get(position);

      final int price;
      int total;

      final List<HandiCraft> handiCraftList = new ArrayList<>();
      //HandiCraft handiCraft=handiCraftList.get(position);
      if (v.getId() == buttonMoveToWishListCartItem.getId()) {
        //name=handiCraft.getProduct_name();
        //Toast.makeText(, ""+name, Toast.LENGTH_SHORT).show();

//get dt
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance()
            .getReference("uploads").child(product_idd);

        //Log.d("GTM","ID "+product_idd);

        //handiCraftListss = new ArrayList<>();
        final String[] pricee = new String[1];
        final int count = 200;
        databaseReference1.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {

            for (DataSnapshot post : dataSnapshot.getChildren()) {
              String str = post.getValue(String.class);
              //  handiCraftListss.add(str);

            }
            // Log.d("GTM", "Price: " + handiCraftListss);
            // Log.d("GTM", "Selling Price: " + handiCraftListss.get(8));
//            pricee[1] =handiCraftListss.get(8);
            //  a= Integer.parseInt(handiCraftListss.get(8));
//            a=(a*count);
//
//            Log.d("GTM", "Selling Price: Inner: "+a);

          }

          @Override
          public void onCancelled(DatabaseError databaseError) {

          }
        });

//
//        final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance()
//            .getReference("uploads");
//
//        databaseReference1.addValueEventListener(new ValueEventListener() {
//          @Override
//          public void onDataChange(DataSnapshot dataSnapshot)
//          {
//            for (DataSnapshot post : dataSnapshot.getChildren()) {
//              HandiCraft handiCraft = post.getValue(HandiCraft.class);
//              handiCraftList.add(handiCraft);
//              if (handiCraft != null) {
//                Log.d("GTM", "Price: " + handiCraft.getProduct_sp());
//              }
//            }
//
//          }
//
//          @Override
//          public void onCancelled(DatabaseError databaseError) {
//
//          }
//        });
        // HandiCraft handiCraft1=new HandiCraft();

        price = Integer.parseInt(cartHandiCraft.getProduct_quantity());
        total = Integer.parseInt(cartHandiCraft.getProduct_sp());
        total = total * price;
        //textViewProductTotalAmt.setText("Rs. "+total);

        //Log.d("Product: ","Name: "+total);
      }
      Log.d("GTM", "Selling Price Outer: : " + a);
//      switch (v.getId()){
//        case R.id.buttonMoveToWishListCartItem:
//          name=handiCraft.getProduct_name();
//          Toast.makeText(context, ""+name, Toast.LENGTH_SHORT).show();
//      }

    }
  }
}
