package com.example.asus.trendhimapp.settings.shippingAddress;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.asus.trendhimapp.R;
import com.example.asus.trendhimapp.shoppingCartPage.credentialsPage.Credentials;

import java.util.ArrayList;

public class AddressAdapter extends ArrayAdapter<Credentials> {

    AddressAdapter(@NonNull Context context, ArrayList<Credentials> addresses) {
        super(context, 0, addresses);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null) {
            convertView = LayoutInflater.from(
                    getContext()).inflate(R.layout.item_listview_address, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.name = convertView.findViewById(R.id.address_name);
            viewHolder.address =  convertView.findViewById(R.id.street_address);
            viewHolder.city =  convertView.findViewById(R.id.city_address);
            viewHolder.zipcode =  convertView.findViewById(R.id.zipcode_address);
            viewHolder.country =  convertView.findViewById(R.id.country_address);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Credentials currentAddress = getItem(position);

        if(currentAddress != null) {
            viewHolder.name.setText(currentAddress.getName());
            viewHolder.address.setText(currentAddress.getAddress());
            viewHolder.city.setText(currentAddress.getCity());
            viewHolder.zipcode.setText(currentAddress.getZipcode());
            viewHolder.country.setText(currentAddress.getCountry());
        }

        return convertView;
    }


    /**
     *  Provide a direct reference to each of the views
     * used to cache the views within the layout for fast access
     */
    class ViewHolder {

        TextView name, address, zipcode, city, country;

    }

}
