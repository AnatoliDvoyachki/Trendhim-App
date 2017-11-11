package com.example.asus.trendhimapp.shoppingCart.credentialsPage;

public class Credentials {

    private String email, address, city, zipcode, country, name;

    Credentials(){};

    Credentials(String email, String address, String city, String zipcode, String country, String name){
        this.name = name;
        this.email = email;
        this.address = address;
        this.city = city;
        this.zipcode = zipcode;
        this.country = country;
    }

    public String getEmail() {
        return email;
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
