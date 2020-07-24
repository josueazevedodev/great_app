package com.appgreat.beerapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.appgreat.beerapp.Model.Beer;
import com.appgreat.beerapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BeerAdapter extends RecyclerView.Adapter<BeerAdapter.ResultViewHolder> {

    private List<Beer> beers;
    private Context context;
    private BeerAdapter.OnItemClickListener listener;

    public BeerAdapter(List<Beer> beers, Context context, BeerAdapter.OnItemClickListener listener) {
        this.beers = beers;
        this.context = context;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Beer item);
        void onItemFavorite(Beer item, int position);
    }


    @Override
    public ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.from(parent.getContext()).inflate(R.layout.beer_list_item,parent,false);
        return new BeerAdapter.ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ResultViewHolder holder, int position) {
        Beer beer = beers.get(position);
        holder.bind(beers.get(position), listener, position);
        holder.tx_name.setText(beer.getName());
        holder.tx_tagline.setText(beer.getTagline());
        Picasso.get().load(beer.getImage_url()).into(holder.img_beer);

        if(beer.getFavorite() == 1){
            holder.img_favorite.setImageResource(R.drawable.staron);
        }else{
            holder.img_favorite.setImageResource(R.drawable.staroff);
        }

    }

    @Override
    public int getItemCount() {
        return beers.size();
    }

    class ResultViewHolder extends RecyclerView.ViewHolder{

        public TextView tx_name;
        public TextView tx_tagline;
        public ImageView img_beer;
        public ImageView img_favorite;

        public ResultViewHolder(View itemView) {
            super(itemView);

            tx_name = itemView.findViewById(R.id.beer_list_tx_name);
            tx_tagline = itemView.findViewById(R.id.beer_list_tx_tagline);
            img_beer = itemView.findViewById(R.id.beer_list_img_beer);
            img_favorite = itemView.findViewById(R.id.beer_listn_bt_favorite);


        }

        public void bind(final Beer item, final OnItemClickListener listener, int position) {
            itemView.setOnClickListener(v -> {listener.onItemClick(item);});

            img_favorite.setOnClickListener(v -> listener.onItemFavorite(item, position));
        }

    }

}
