package com.appgreat.beerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.appgreat.beerapp.Adapters.BeerAdapter;
import com.appgreat.beerapp.Core.RetrofitConfig;
import com.appgreat.beerapp.Model.Beer;
import com.orm.SugarContext;
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private RecyclerView recyclerView;
    private List<Beer> beerList;
    private BeerAdapter adapter;
    private ProgressBar progressBarTop;
    private ProgressBar progressBarBottom;
    private int page;
    private boolean getControler;
    private LinearLayout connectionLayut;
    private Button bt_con;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(R.string.app_bar_title);
        instanceComponents();

    }

    private void getBeers(){
        Call<List<Beer>> call = new RetrofitConfig().getBeerService().getBeerPage(page,10);
        call.enqueue(new Callback<List<Beer>>() {
            @Override
            public void onResponse(Call<List<Beer>> call, Response<List<Beer>> response) {
                List<Beer> beers = response.body();
                connectionLayut.setVisibility(View.GONE);

                for(int i=0;i<beers.size();i++) {
                    Beer item = beers.get(i);
                    Beer temp = SugarRecord.findById(Beer.class, item.getId());

                    if(temp == null){
                        item.setFavorite(0);
                    }else{
                        item.setFavorite(1);
                    }
                    beerList.add(new Beer(item.getId(),item.getName(),item.getTagline(),item.getImage_url(),item.getDescription(),item.getFavorite()));
                    adapter.notifyItemInserted(beerList.size()-1);
                }
                page++;
                progressBarTop.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Beer>> call, Throwable t) {
                progressBarTop.setVisibility(View.GONE);
                connectionLayut.setVisibility(View.VISIBLE);
            }
        });
    }

    private void instanceComponents(){
        SugarContext.init( this );
        recyclerView = findViewById(R.id.main_recyclerview);
        progressBarTop = findViewById(R.id.main_top_progressBar);
        progressBarBottom = findViewById(R.id.main_bottom_progressBar);
        connectionLayut = findViewById(R.id.main_connection);
        bt_con = findViewById(R.id.main_br_con);
        connectionLayut.setVisibility(View.GONE);
        page = 1;
        progressBarBottom.setVisibility(View.GONE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        beerList = new ArrayList<Beer>();
        adapter = new BeerAdapter(beerList, MainActivity.this, new BeerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Beer item) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("beer", item);
                startActivity(intent);
            }

            @Override
            public void onItemFavorite(Beer item, int position) {
                Beer beerS = item;
                if(beerS.getFavorite() == 0){
                    beerS.setFavorite(1);
                    SugarRecord.save(beerS);
                    Log.i("id salvo",SugarRecord.findById(Beer.class, beerS.getId()).getFavorite()+")");
                }else{
                    beerS.setFavorite(0);
                    SugarRecord.delete(beerS);
                }
                beerList.set(position,beerS);

                adapter.notifyItemChanged(position);
            }
        });
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        getBeers();

        bt_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBeers();
            }
        });


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int total = layoutManager.getItemCount();
                int currentLastItem = linearLayoutManager.findLastVisibleItemPosition();
                if (currentLastItem == total - 1) {
                    progressBarBottom.setVisibility(View.VISIBLE);
                    if(page != 0 && getControler == false) {
                        getControler = true;
                        recyclerView.post(() -> {
                            getBeerPage();
                        });

                    }
                }
            }
        });

    }

    private void getBeerPage(){
        Call<List<Beer>> call = new RetrofitConfig().getBeerService().getBeerPage(page,10);
        call.enqueue(new Callback<List<Beer>>() {
            @Override
            public void onResponse(Call<List<Beer>> call, Response<List<Beer>> response) {
                List<Beer> beers = response.body();
                if(beers != null || beers.size() != 0) {
                    for (int i = 0; i < beers.size(); i++) {
                        Beer item = beers.get(i);
                        Beer temp = SugarRecord.findById(Beer.class, item.getId());
                        if(temp == null){
                            item.setFavorite(0);
                        }else{
                            item.setFavorite(1);
                        }
                        beerList.add(new Beer(item.getId(),item.getName(),item.getTagline(),item.getImage_url(),item.getDescription(),item.getFavorite()));
                        adapter.notifyItemInserted(beerList.size()-1);

                    }
                    page++;
                    getControler = false;
                    progressBarBottom.setVisibility(View.GONE);
                    //adapter.notifyDataSetChanged();
                }else{
                    page = 0;
                    progressBarBottom.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<Beer>> call, Throwable t) {
                progressBarBottom.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Falha ao requisitar novas informações",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getBeearName(String name){
        progressBarTop.setVisibility(View.VISIBLE);
        Call<List<Beer>> call = new RetrofitConfig().getBeerService().getBeerName(name);
        call.enqueue(new Callback<List<Beer>>() {
            @Override
            public void onResponse(Call<List<Beer>> call, Response<List<Beer>> response) {
                List<Beer> beers = response.body();
                if(beers != null || beers.size() != 0) {
                    for (int i = 0; i < beers.size(); i++) {
                        Beer item = beers.get(i);
                        Beer temp = SugarRecord.findById(Beer.class, item.getId());
                        if(temp == null){
                            item.setFavorite(0);
                        }else{
                            item.setFavorite(1);
                        }
                        beerList.add(new Beer(item.getId(),item.getName(),item.getTagline(),item.getImage_url(),item.getDescription(),item.getFavorite()));
                        adapter.notifyItemInserted(beerList.size()-1);

                    }

                    //adapter.notifyDataSetChanged();
                }else{
                    progressBarTop.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<Beer>> call, Throwable t) {
                progressBarBottom.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Falha ao requisitar novas informações",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Buscar");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length() >= 3){
                    beerList.clear();
                    adapter.notifyDataSetChanged();
                    getBeearName(newText);
                }else{
                    beerList.clear();
                    adapter.notifyDataSetChanged();
                    getBeers();
                }
                return true;
            }

        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
