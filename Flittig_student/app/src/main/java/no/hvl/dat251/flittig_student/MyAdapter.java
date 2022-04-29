package no.hvl.dat251.flittig_student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import no.hvl.dat251.flittig_student.R;
import no.hvl.dat251.flittig_student.User;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<User> list;
    Integer i = 1;

    public MyAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user =list.get(position);
        holder.name.setText(user.getName());
        holder.points_total.setText(user.getPoints_total().toString());
        Integer pos = (Integer)position + 1;
        String str_pos = pos.toString();
        holder.place.setText(str_pos + ".");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView points_total;
        TextView place;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.idTVName);
            points_total = itemView.findViewById(R.id.idTVPoints);
            place = itemView.findViewById(R.id.idTVPlace);
        }
    }

}
