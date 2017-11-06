package com.example.asus.trendhimapp.mainActivities.newProducts;

public class NewProduct {

        private String category, key;

        public NewProduct(){}

        NewProduct(String key, String category){
            this.category = category;
            this.key = key;
        }

        String getCategory() {
            return category;
        }

        public String getKey() {
            return key;
        }

}
