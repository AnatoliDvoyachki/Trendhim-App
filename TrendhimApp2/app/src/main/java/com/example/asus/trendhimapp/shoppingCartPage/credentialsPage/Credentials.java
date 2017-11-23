package com.example.asus.trendhimapp.shoppingCartPage.credentialsPage;

public class Credentials {

    private String userEmail, address, city, zipcode, country, name;

    public Credentials(){} // No-argument constructor for the Firebase queries

    public Credentials(String userEmail, String address, String city, String zipcode, String country, String name){
        this.name = name;
        this.userEmail = userEmail;
        this.address = address;
        this.city = city;
        this.zipcode = zipcode;
        this.country = country;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getCountry() {
        return country;
    }

    public String getName() {
        return name;
    }
}
