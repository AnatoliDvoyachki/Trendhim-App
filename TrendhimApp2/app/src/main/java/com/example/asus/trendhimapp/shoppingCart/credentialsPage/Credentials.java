package com.example.asus.trendhimapp.shoppingCart.credentialsPage;

public class Credentials {

    private String userEmail, address, city, zipcode, country, name;

    Credentials(){};

    Credentials(String userEmail, String address, String city, String zipcode, String country, String name){
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

    String getAddress() {
        return address;
    }

    String getCity() {
        return city;
    }

    String getZipcode() {
        return zipcode;
    }

    String getCountry() {
        return country;
    }

    public String getName() {
        return name;
    }
}
