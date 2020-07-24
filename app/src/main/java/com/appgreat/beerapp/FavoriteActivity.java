package com.appgreat.beerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.appgreat.beerapp.Adapters.BeerAdapter;
import com.appgreat.beerapp.Model.Beer;
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Beer> beerList;
    private BeerAdapter adapter;
    private TextView noFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.app_bar_favorites);

        recyclerView = findViewById(R.id.favorites_recyclerview);
        noFavorites = findViewById(R.id.favorite_tx_nome);
        noFavorites.setVisibility(View.GONE);
        beerList = new ArrayList<>();

        getBeers();

    }

    private void getBeers(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FavoriteActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        beerList = SugarRecord.listAll(Beer.class);

        if(beerList.size() > 0) {
            adapter = new BeerAdapter(beerList, FavoriteActivity.this, new BeerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Beer item) {
                    Intent intent = new Intent(FavoriteActivity.this, DetailActivity.class);
                    intent.putExtra("beer", item);
                    startActivity(intent);
                }

                @Override
                public void onItemFavorite(Beer item, int position) {
                    Beer beerS = item;
                    beerList.remove(position);
                    SugarRecord.delete(beerS);
                    adapter.notifyItemRemoved(position);
                    adapter.notifyDataSetChanged();
                    if (beerList.size() == 0) noFavorites.setVisibility(View.VISIBLE);
                }
            });
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }else{
            noFavorites.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
