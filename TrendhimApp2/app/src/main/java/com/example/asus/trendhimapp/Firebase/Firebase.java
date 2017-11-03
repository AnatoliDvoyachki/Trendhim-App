package com.example.asus.trendhimapp.Firebase;

import com.example.asus.trendhimapp.ProductPage.Products.*;
import com.google.firebase.database.*;

import java.util.ArrayList;

public final class Firebase {
    private static FirebaseDatabase firebaseDatabase;
    private static DatabaseReference myRef;

    // No instantiation
    private Firebase() {}

    /*
    *
    * Fetches an array list of all objects inside a Firebase entity specified by @param tableName
    * Example: ArrayList<Product> bagList = Firebase.getAll(Constants.TABLE_NAME_BAGS); - to get all bags
    */
    public static ArrayList<Product> getAll(final String tableName) {
        final ArrayList<Product> productList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference(tableName);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Product theProduct = ds.getValue(Product.class);
                    productList.add(theProduct);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return productList;
    }

}
