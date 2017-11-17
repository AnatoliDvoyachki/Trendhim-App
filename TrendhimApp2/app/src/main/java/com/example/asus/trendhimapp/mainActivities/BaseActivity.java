package com.example.asus.trendhimapp.mainActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.asus.trendhimapp.R;
import com.example.asus.trendhimapp.aboutUsPage.AboutUs;
import com.example.asus.trendhimapp.categoryPage.CategoryProductActivity;
import com.example.asus.trendhimapp.loginPage.TabbedActivity;
import com.example.asus.trendhimapp.shoppingCart.ShoppingCartActivity;
import com.example.asus.trendhimapp.util.Constants;
import com.example.asus.trendhimapp.weeklyLookPage.WeeklyLookActivity;
import com.example.asus.trendhimapp.wishlistPage.WishlistActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static DrawerLayout drawer;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks
        int id = item.getItemId();

        switch (id) {

            /*
             * Start Log In Activity whenever the Log In button is clicked
             * and the user is not logged in.
             * If the user is logged in - Display Toast
             */
            case R.id.action_log_in:
                if (!isUserOnline()) {
                    Intent toLogin = new Intent(getApplicationContext(), TabbedActivity.class);
                    startActivity(toLogin);
                } else if (isUserOnline())
                    Toast.makeText(getApplicationContext(), R.string.already_logged_in_message, Toast.LENGTH_LONG).show();

                break;

            /*
             * Start Wish list Activity whenever the wish list button is clicked
             * and the user is logged in.
             * If the user is not logged in - Display Toast
             */
            case R.id.action_wishlist:
                if (isUserOnline()) {
                    Intent toWishList = new Intent(BaseActivity.this, WishlistActivity.class);
                    startActivity(toWishList);
                } else
                    Toast.makeText(getApplicationContext(), R.string.not_logged_in_unsuccess_message, Toast.LENGTH_LONG).show();

                break;

            /*
             * Start Shopping cart Activity whenever the shopping cart button is clicked
             * and the user is not logged in.
             * If the user is logged in - Display Toast
             */
            case R.id.action_shopping_cart:
                if (isUserOnline()) {
                    Intent toShoppingCart = new Intent(getApplicationContext(), ShoppingCartActivity.class);
                    startActivity(toShoppingCart);
                } else
                    Toast.makeText(getApplicationContext(), R.string.not_logged_in_unsuccess_message, Toast.LENGTH_LONG).show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks
        Intent intent;
        int id = item.getItemId();

        switch (id) {
            case R.id.bracelets:
                intent = new Intent(this, CategoryProductActivity.class);
                intent.putExtra(Constants.KEY_CATEGORY, Constants.TABLE_NAME_BRACELETS); //send category to open the correct category page
                startActivity(intent);
                break;
            case R.id.bags:
                intent = new Intent(this, CategoryProductActivity.class);
                intent.putExtra(Constants.KEY_CATEGORY, Constants.TABLE_NAME_BAGS);
                startActivity(intent);
                break;
            case R.id.watches:
                intent = new Intent(this, CategoryProductActivity.class);
                intent.putExtra(Constants.KEY_CATEGORY, Constants.TABLE_NAME_WATCHES);
                startActivity(intent);
                break;
            case R.id.beardCare:
                intent = new Intent(this, CategoryProductActivity.class);
                intent.putExtra(Constants.KEY_CATEGORY, Constants.TABLE_NAME_BEARD_CARE);
                startActivity(intent);
                break;
            case R.id.necklaces:
                intent = new Intent(this, CategoryProductActivity.class);
                intent.putExtra(Constants.KEY_CATEGORY, Constants.TABLE_NAME_NECKLACES);
                startActivity(intent);
                break;
            case R.id.ties:
                intent = new Intent(this, CategoryProductActivity.class);
                intent.putExtra(Constants.KEY_CATEGORY, Constants.TABLE_NAME_TIES);
                startActivity(intent);
                break;
            case R.id.bowTies:
                intent = new Intent(this, CategoryProductActivity.class);
                intent.putExtra(Constants.KEY_CATEGORY, Constants.TABLE_NAME_BOW_TIES);
                startActivity(intent);
                break;
            case R.id.weeklyLook:
                intent = new Intent(this, WeeklyLookActivity.class);
                startActivity(intent);
                break;
            case R.id.logOut:
                if (user != null) {
                    auth.signOut();
                    Toast.makeText(getApplicationContext(), R.string.log_out_success_message, Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(getApplicationContext(), R.string.need_to_log_in_first_message, Toast.LENGTH_LONG).show();
                break;
            case R.id.aboutUs:
                intent = new Intent(getApplicationContext(), AboutUs.class);
                startActivity(intent);
                break;

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Start Main Activity whenever the navigation header is clicked
     *
     * @param view
     */
    public void backToHomePage(View view) {
        Intent toMain = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(toMain);
    }

    /**
     * Check if the user is already logged in
     *
     * @return true if the user is logged in - false if not
     */
    public boolean isUserOnline() {
        //Check track of the current user
        FirebaseUser currentUser = auth.getCurrentUser();
        boolean isLoggedIn = false;

        // User already logged in
        if (currentUser != null)
            isLoggedIn = true;

        return isLoggedIn;
    }

}
