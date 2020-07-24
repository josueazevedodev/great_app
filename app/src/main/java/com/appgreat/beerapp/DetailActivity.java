package com.appgreat.beerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.appgreat.beerapp.Model.Beer;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private TextView tx_nome;
    private TextView tx_tagline;
    private TextView tx_description;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.app_bar_detail);

        Beer beer = (Beer) getIntent().getSerializableExtra("beer");

        tx_nome = findViewById(R.id.detail_tx_name);
        tx_tagline = findViewById(R.id.detail_tx_tagline);
        tx_description = findViewById(R.id.detail_tx_description);
        img = findViewById(R.id.detail_img_beer);

        Picasso.get().load(beer.getImage_url()).into(img);
        tx_nome.setText(beer.getName());
        tx_tagline.setText(beer.getTagline());
        tx_description.setText(beer.getDescription());

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
