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
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.asus.trendhimapp.categoryPage.CategoryProductActivity;
import com.example.asus.trendhimapp.login.LoginActivity;
import com.example.asus.trendhimapp.shoppingCartActivity.ShoppingCartActivity;
import com.example.asus.trendhimapp.weeklyLook.WeeklyLookActivity;
import com.example.asus.trendhimapp.wishlistPage.WishlistActivity;
import com.example.asus.trendhimapp.util.Constants;
import com.example.asus.trendhimapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        Intent intent;
        int id = item.getItemId();

        switch (id) {
            case R.id.bracelets:
                intent = new Intent(this, CategoryProductActivity.class);
                intent.putExtra("category", Constants.TABLE_NAME_BRACELETS);
                startActivity(intent);
                break;
            case R.id.bags:
                intent = new Intent(this, CategoryProductActivity.class);
                intent.putExtra("category", Constants.TABLE_NAME_BAGS);
                startActivity(intent);
                break;
            case R.id.watches:
                intent = new Intent(this, CategoryProductActivity.class);
                intent.putExtra("category", Constants.TABLE_NAME_WATCHES);
                startActivity(intent);
                break;
            case R.id.beardCare:
                intent = new Intent(this, CategoryProductActivity.class);
                intent.putExtra("category", Constants.TABLE_NAME_BEARD_CARE);
                startActivity(intent);
                break;
            case R.id.necklaces:
                intent = new Intent(this, CategoryProductActivity.class);
                intent.putExtra("category", Constants.TABLE_NAME_NECKLACES);
                startActivity(intent);
                break;
            case R.id.ties:
                intent = new Intent(this, CategoryProductActivity.class);
                intent.putExtra("category", Constants.TABLE_NAME_TIES);
                startActivity(intent);
                break;
            case R.id.bowTies:
                intent = new Intent(this, CategoryProductActivity.class);
                intent.putExtra("category", Constants.TABLE_NAME_BOW_TIES);
                startActivity(intent);
                break;
            case R.id.weeklyLook:
                intent = new Intent(this, WeeklyLookActivity.class);
                startActivity(intent);
                break;
            case R.id.logOut:
                if (user != null) {
                    auth.signOut();
                    Toast.makeText(getApplicationContext(), "You have successfully logged out", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(getApplicationContext(), "You need to log in first", Toast.LENGTH_LONG).show();
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
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    /**
     * Start Log In Activity whenever the Log In button is clicked
     * and the user is not signed in.
     * If the user is signed in - Display Toast
     *
     * @param view
     */
    public void main_to_login(View view) {

        if (!isUserOnline()) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        } else if (isUserOnline()) {
            Toast.makeText(getApplicationContext(), "You are already logged in", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Display Toast if the user is already signed in
     *
     * @return true if the user is signed in - false if not
     */
    public boolean isUserOnline() {
        //Check track of the current user
        FirebaseUser currentUser = auth.getCurrentUser();
        boolean isLoggedIn = false;

        if (currentUser != null) {
            // User already logged in
            isLoggedIn = true;
        }

        return isLoggedIn;
    }

    public void openWishList(View view) {
        if (isUserOnline()) {
            Intent toWishList = new Intent(BaseActivity.this, WishlistActivity.class);
            startActivity(toWishList);
        } else {
            Toast.makeText(getApplicationContext(), R.string.not_logged_in_unsuccess_message, Toast.LENGTH_LONG).show();
        }
    }


    public void openShoppingCart(View view) {
        if (isUserOnline()) {
            Intent intent = new Intent(getApplicationContext(), ShoppingCartActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), R.string.not_logged_in_unsuccess_message, Toast.LENGTH_LONG).show();
        }
    }

}
