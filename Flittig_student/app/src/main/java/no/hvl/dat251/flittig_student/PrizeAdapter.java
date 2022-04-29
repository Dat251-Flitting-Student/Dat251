package no.hvl.dat251.flittig_student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.sql.Array;
import java.util.ArrayList;

public class PrizeAdapter extends ArrayAdapter<Prize> {

    private Context context;
    private int resource;


    public PrizeAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Prize> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        convertView = layoutInflater.inflate(resource,parent,false);

        ImageView imageView = convertView.findViewById(R.id.image);
        TextView textProvider = convertView.findViewById(R.id.prize_provider);
        TextView textCost = convertView.findViewById(R.id.prize_cost);

        imageView.setImageResource(getItem(position).getImage());
        textProvider.setText(getItem(position).getOffer());
        textCost.setText(getItem(position).getPrize() + " poeng");



        return convertView;
    }
}
