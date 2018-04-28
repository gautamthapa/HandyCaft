package com.strontech.imgautam.handycaft.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.strontech.imgautam.handycaft.ProductFragments.ProductCartFragment;
import com.strontech.imgautam.handycaft.ProductFragments.ProductWishListFragment;
import com.strontech.imgautam.handycaft.R;
import com.strontech.imgautam.handycaft.broadcast.ConnectivityReceiver;
import com.strontech.imgautam.handycaft.fragments.HelpFeedbackFragment;
import com.strontech.imgautam.handycaft.fragments.HomeFragment;
import com.strontech.imgautam.handycaft.fragments.LoginFragment;
import com.strontech.imgautam.handycaft.fragments.AccountFragment;
import com.strontech.imgautam.handycaft.fragments.TermsPolicyFragment;
import com.strontech.imgautam.handycaft.helper.Converter;
import com.strontech.imgautam.handycaft.userfragments.UserOrdersFragment;
import de.hdodenhof.circleimageview.CircleImageView;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener,
    OnClickListener {


  private NavigationView navigationView;
  private DrawerLayout drawer;
  private LinearLayout linearLayoutUserSignUp;
  private LinearLayout linearLayoutUserLogin;

  private View navHeader;
  private CircleImageView imageViewProfile;
  private TextView textViewName;
  private TextView textViewEmail;
  private Toolbar toolbar;
  private FloatingActionButton fab;


  //index to identify current nav menu item
  public static int navItemIndex = 0;

  //tags used to attach the fragments
  private static final String TAG_HOME = "home";
  private static String CURRENT_TAG = TAG_HOME;


  //toolbar titles respected to selected nav menu items
  private String[] activityTitles;

  //flag to load home fragment when user presses back key
  private boolean shouldLoadHomeFragOnBackPress = true;
  private Handler mHandler;

  private DatabaseReference databaseReference;

  SharedPreferences sharedPreferences;
  SharedPreferences.Editor editor;
  String name;
  String email;
  int size = 0;


  //For Google
  private String username_google;
  private String email_google;
  private String profile_pic_google;


  //Facebook
  String TAG = "MainActivity";
  private String first_name;
  private String last_name;
  private String imageUrl;



  private Menu menu;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    mHandler = new Handler();

    drawer = findViewById(R.id.drawer_layout);
    fab = findViewById(R.id.fab);
    navigationView = findViewById(R.id.nav_view);

    //SharedPreferences
    sharedPreferences = getSharedPreferences("myEmailPass", Context.MODE_PRIVATE);

    databaseReference = FirebaseDatabase.getInstance().getReference();

    /**
     * Check Internet Connection
     * */
    //Check Internet Connection
    checkInternetConnection();

    invalidateOptionsMenu();
    //Navigation view drawer(Nav Header)
    //
    navHeader = navigationView.getHeaderView(0);
    linearLayoutUserSignUp = navHeader.findViewById(R.id.linearLayoutUserSignUp);
    linearLayoutUserLogin = navHeader.findViewById(R.id.linearLayoutUserLoggedIn);
    imageViewProfile = navHeader.findViewById(R.id.imageViewProfileImage);
    textViewName = navHeader.findViewById(R.id.textViewName);
    textViewEmail = navHeader.findViewById(R.id.textViewEmail);

    //register OnClickListener
    linearLayoutUserLogin.setOnClickListener(this);
    linearLayoutUserSignUp.setOnClickListener(this);

    //get data from sharedPreferences and set it
    email = sharedPreferences.getString("email", null);

    //Google
    username_google=sharedPreferences.getString("username_google",null);
    email_google=sharedPreferences.getString("email_google",null);
    profile_pic_google=sharedPreferences.getString("profile_pic_google",null);

    //Facebook
    first_name = sharedPreferences.getString("facebook_first_name", null);
    last_name = sharedPreferences.getString("facebook_last_name", null);
    imageUrl = sharedPreferences.getString("facebook_image_url", null);

    Toast.makeText(this, email, Toast.LENGTH_SHORT).show();

    if (email !=null){
      linearLayoutUserLogin.setVisibility(View.VISIBLE);
      linearLayoutUserSignUp.setVisibility(View.GONE);
      textViewName.setVisibility(View.GONE);
      textViewEmail.setText(email);
      loadHomeFragment();
    }else if (first_name !=null || last_name != null || imageUrl != null){
      linearLayoutUserLogin.setVisibility(View.VISIBLE);
      linearLayoutUserSignUp.setVisibility(View.GONE);
      textViewEmail.setVisibility(View.GONE);
      //Facebook
      textViewName.setText(first_name+" "+last_name);
      new MainActivity.DownloadImage(imageViewProfile).execute(imageUrl);
      loadHomeFragment();

    }else if (username_google != null || email_google != null || profile_pic_google !=null){

      linearLayoutUserLogin.setVisibility(View.VISIBLE);
      linearLayoutUserSignUp.setVisibility(View.GONE);
      //Google
      textViewName.setText(username_google);
      textViewEmail.setText(email_google);
      Glide.with(MainActivity.this).load(profile_pic_google).into(imageViewProfile);

      loadHomeFragment();
    }

/*
    if (email != null || first_name != null || last_name != null
        || username_google !=null || email_google != null || profile_pic_google!=null) {

      FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
      ft.replace(R.id.mainFrame, new HomeFragment());
      ft.commit();

      linearLayoutUserLogin.setVisibility(View.VISIBLE);
      linearLayoutUserSignUp.setVisibility(View.GONE);
      textViewEmail.setText(email);

      //Google
      textViewName.setText(username_google);
      textViewEmail.setText(email_google);
      Glide.with(MainActivity.this).load(profile_pic_google).into(imageViewProfile);

      //Facebook
      textViewName.setText(first_name+" "+last_name);
      new MainActivity.DownloadImage(imageViewProfile).execute(imageUrl);

*/

      //Set via Glide
      /*Glide.with(MainActivity.this).load(imageUrl).asBitmap()
          .into(new BitmapImageViewTarget(imageViewProfile){
            @Override
            protected void setResource(Bitmap resource) {
              RoundedBitmapDrawable drawable= RoundedBitmapDrawableFactory.create(getApplicationContext()
                  .getResources(),resource);
              drawable.setCircular(true);
            }
          });
*/


     else {

      Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
      linearLayoutUserSignUp.setVisibility(View.VISIBLE);
      linearLayoutUserLogin.setVisibility(View.GONE);
      loadHomeFragment();
    }



    //load toolbar titles from string resources
    activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

    fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();
      }
    });



    /**
     * load nav header data
     * */
    loadNavHeaderData();

    /**
     * initializing navigation menu
     * */
    // setUpNavigationView();

    if (savedInstanceState == null) {
      navItemIndex = 0;
      CURRENT_TAG = TAG_HOME;
      //loadHomeFragment();
    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.addDrawerListener(toggle);
    toggle.syncState();

    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);


  }

  /**
   * This method is Load Home Fragment
   * */
  private void loadHomeFragment(){
    //Start Home Fragment
    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    ft.add(R.id.mainFrame, new HomeFragment(), "Home");
    ft.commit();
    drawer.closeDrawers();
    fab.show();
  }


  /**
   * Check Internet Connection
   */
  private void checkInternetConnection() {
    boolean isConnected = ConnectivityReceiver.isConnected();
    showSnack(isConnected);
  }

  private void showSnack(boolean isConnected) {
    //Showing the status in SnackBar

    String message;
    int color;
    if (isConnected) {
      message = "Connected to Internet";
      color = Color.WHITE;
    } else {
      message = "Not Connected to Internet";
      color = Color.RED;
    }
    //Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();

    Snackbar snackbar = Snackbar.make(findViewById(R.id.mainFrame), message, Snackbar.LENGTH_LONG);

    View sbView = snackbar.getView();
    TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
    textView.setTextColor(color);
    snackbar.show();
  }


  /**
   * This method is to count Total child of Firebase cart items
   */
  private void countTotalChildFirebase() {
    databaseReference.child("Cart Items").addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {

//        for(DataSnapshot snap:dataSnapshot.getChildren()){
//          size= (int) snap.getChildrenCount();
//
//
//        }

        size = (int) dataSnapshot.getChildrenCount();

        MenuItem menuItem = null;
        menuItem = menu.findItem(R.id.cart_action);
        menuItem.setIcon(Converter
            .convertLayoutToImage(MainActivity.this, size, R.drawable.ic_shopping_cart_black_24dp));

        Toast.makeText(MainActivity.this, "Cart Items: " + size, Toast.LENGTH_LONG).show();
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });
  }


  /**
   * Load navigation menu header information
   * like profile image, username, email
   */
  private void loadNavHeaderData() {

//    linearLayoutUserSignUp.setOnClickListener(new OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.mainFrame, new LoginFragment());
//        fragmentTransaction.addToBackStack("addLogin");
//        fragmentTransaction.commit();
//        drawer.closeDrawers();
//        fab.hide();
//      }
//    });
//fab.show();
//

  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    this.menu = menu;
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main_menu, menu);
    countTotalChildFirebase();

//    MenuItem menuItem=menu.findItem(R.id.cart_action);
//    menuItem.setIcon(Converter.convertLayoutToImage(MainActivity.this, size, R.drawable.ic_shopping_cart_black_24dp));
//    Toast.makeText(this, "For Cart Icon "+size, Toast.LENGTH_SHORT).show();
    //invalidateOptionsMenu();
    return super.onCreateOptionsMenu(menu);
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }
    if (id == R.id.cart_action) {
      FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
      ft.replace(R.id.mainFrame, new ProductCartFragment());
      ft.commit();
    }

    return super.onOptionsItemSelected(item);
  }


  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();

    if (id == R.id.nav_home) {

      FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
      ft.replace(R.id.mainFrame, new HomeFragment());
      ft.commit();
      drawer.closeDrawers();
    } else if (id == R.id.nav_my_cart) {
      FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
      ft.replace(R.id.mainFrame, new ProductCartFragment());
      ft.commit();
      drawer.closeDrawers();

    } else if (id == R.id.nav_my_wish_list) {

      FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
      ft.replace(R.id.mainFrame, new ProductWishListFragment());
      ft.commit();

    } else if (id == R.id.nav_my_orders) {
      FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
      ft.replace(R.id.mainFrame, new UserOrdersFragment());
      ft.commit();
      drawer.closeDrawers();

    } else if (id == R.id.nav_my_account) {
      FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
      ft.replace(R.id.mainFrame, new AccountFragment());
      ft.commit();
      drawer.closeDrawers();

    } else if (id == R.id.nav_share) {
      shareApp();
    }else if (id == R.id.nav_help_feedback) {
      FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
      ft.replace(R.id.mainFrame, new HelpFeedbackFragment());
      ft.commit();
      drawer.closeDrawers();

    }else if (id == R.id.nav_terms_policy) {
      FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
      ft.replace(R.id.mainFrame, new TermsPolicyFragment());
      ft.commit();
      drawer.closeDrawers();

    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }


  /**
   * This is method for Sharing App
   */
  private void shareApp() {
    Intent shareIntent = new Intent(Intent.ACTION_SEND);
    shareIntent.setType("text/plain");
    String shareText = "This is sample E-Commerce App Click here to download:  http://www.mediafire.com/file/d43fmmdg34yo41w/AddToCartBadgeCount.zip";
    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "HandyCraft");
    shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
    startActivity(Intent.createChooser(shareIntent, "Share via"));
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {

      case R.id.linearLayoutUserLoggedIn:
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, new AccountFragment());
        ft.commit();
        Toast.makeText(this, "LoggedIn..", Toast.LENGTH_SHORT).show();
        drawer.closeDrawers();
        fab.hide();
        break;
      case R.id.linearLayoutUserSignUp:
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame, new LoginFragment());
        fragmentTransaction.addToBackStack("addLogin");
        fragmentTransaction.commit();
        Toast.makeText(this, "SignUp & Login Fragment", Toast.LENGTH_SHORT).show();
        drawer.closeDrawers();
        fab.hide();

        break;
    }
  }


  /**
   * Download facebook Image
   */
  public class DownloadImage extends AsyncTask<String, Void, Bitmap> {


    public DownloadImage(CircleImageView bmImage) {
      imageViewProfile = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
      String urldisplay = urls[0];
      Bitmap mIcon11 = null;
      try {
        InputStream in = new java.net.URL(urldisplay).openStream();
        mIcon11 = BitmapFactory.decodeStream(in);
      } catch (Exception e) {
//        Log.e("Error", e.getMessage());
        e.printStackTrace();
      }
      return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
      imageViewProfile.setImageBitmap(result);
    }
  }


  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    for (Fragment fragment : getSupportFragmentManager().getFragments()) {
      //System.out.println("@#@");
      fragment.onActivityResult(requestCode, resultCode, data);
    }
  }
}
