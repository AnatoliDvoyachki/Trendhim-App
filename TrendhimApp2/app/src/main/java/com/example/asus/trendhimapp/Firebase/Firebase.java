package com.example.asus.trendhimapp.Firebase;

import com.example.asus.trendhimapp.ProductPage.Constants;
import com.example.asus.trendhimapp.ProductPage.Products.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
                    if (tableName.equals(Constants.TABLE_NAME_BAGS)) {
                        Bag bag = ds.getValue(Bag.class);
                        productList.add(bag);
                    } else if (tableName.equals(Constants.TABLE_NAME_BEARD_CARES)) {
                        BeardCare beardCare = ds.getValue(BeardCare.class);
                        productList.add(beardCare);
                    } else if (tableName.equals(Constants.BOW_TIE)) {
                        BowTie bowTie = ds.getValue(BowTie.class);
                        productList.add(bowTie);
                    } else if (tableName.equals(Constants.TABLE_NAME_BRACELETS)) {
                        Bracelet bracelet = ds.getValue(Bracelet.class);
                        productList.add(bracelet);
                    } else if (tableName.equals(Constants.TABLE_NAME_NECKLACES)) {
                        Necklace necklace = ds.getValue(Necklace.class);
                        productList.add(necklace);
                    } else if (tableName.equals(Constants.TABLE_NAME_TIES)) {
                        Tie tie = ds.getValue(Tie.class);
                        productList.add(tie);
                    } else {
                        Watch watch = ds.getValue(Watch.class);
                        productList.add(watch);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        return productList;
    }

}
