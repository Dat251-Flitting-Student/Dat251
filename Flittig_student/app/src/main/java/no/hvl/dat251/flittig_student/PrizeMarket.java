package no.hvl.dat251.flittig_student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

import no.hvl.dat251.flittig_student.databinding.ActivityHomeBinding;
import no.hvl.dat251.flittig_student.databinding.ActivityPrizeMarketBinding;

public class PrizeMarket extends AppCompatActivity {

    private ListView listView;
    private ActivityPrizeMarketBinding binding;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPrizeMarketBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        menu();

        listView = findViewById(R.id.list_view);

        //Create data
        ArrayList<Prize> prizes = new ArrayList<>();

        prizes.add(new Prize("Gratis kaffe", 300, "Sammen", R.drawable.sammen));
        prizes.add(new Prize("10% avslag", 100, "Akademika", R.drawable.akademika));
        prizes.add(new Prize("Gratis drink", 500, "Kronbar", R.drawable.kronbar));


        //adapter
        PrizeAdapter prizeAdapter = new PrizeAdapter(this, R.layout.list_item, prizes);
        listView.setAdapter(prizeAdapter);
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            TextView offer = view.findViewById(R.id.prize_provider);
            String offertxt = offer.getText().toString();
            ImageView image = view.findViewById(R.id.image);

            Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();



            onButtonShowPopupWindowClick(view, offertxt, bitmap);
        });

    }

    public void onButtonShowPopupWindowClick(View view, String offer, Bitmap image) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        ImageView imgview = popupView.findViewById(R.id.image);
        imgview.setImageBitmap(image);

        TextView textView = popupView.findViewById(R.id.theoffer);
        textView.setText(offer);
        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        btn = popupView.findViewById(R.id.bruk);
        // dismiss the popup window when touched
        btn.setOnClickListener(view1 -> {
            popupWindow.dismiss();
        });
    }

    private void menu() {
        binding.bottomNavigationView.setSelectedItemId(R.id.ic_prize);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.ic_calendar:
                    //CheckedInActivity.pauseValue = (int) (CheckedInActivity.chronometer.getBase() - SystemClock.elapsedRealtime());
                    return true;

                case R.id.ic_home:
                    Intent intent = new Intent(this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    //CheckedInActivity.pauseValue = (int) (CheckedInActivity.chronometer.getBase() - SystemClock.elapsedRealtime());
                    return true;

                case R.id.ic_prize:
                    //CheckedInActivity.pauseValue = (int) (CheckedInActivity.chronometer.getBase() - SystemClock.elapsedRealtime());
                    return true;

                case R.id.ic_profile:
                    intent = new Intent(this, ProfileActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    //CheckedInActivity.pauseValue = (int) (CheckedInActivity.chronometer.getBase() - SystemClock.elapsedRealtime());
                    return true;

                case R.id.ic_scores:
                    //CheckedInActivity.pauseValue = (int) (CheckedInActivity.chronometer.getBase() - SystemClock.elapsedRealtime());
                    return true;

            }
            return true;
        });
    }
}