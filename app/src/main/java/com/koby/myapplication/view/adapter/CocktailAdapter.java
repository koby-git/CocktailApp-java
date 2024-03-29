package com.koby.myapplication.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.koby.myapplication.model.Cocktail;
import com.koby.myapplication.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CocktailAdapter extends RecyclerView.Adapter<CocktailAdapter.CocktailHolder> {

    //Vars
    private List<Cocktail> cocktails;
    private Context context;
    private OnStarClickListener starClickListener;
    private OnCocktailClickListener cocktailClickListener;

    //Star click listener
    public void setOnStarClickListener(OnStarClickListener starClickListener){
        this.starClickListener = starClickListener;
    }
    public interface OnStarClickListener{
        void onStarClick(Cocktail cocktail);
    }

    //Cocktail click listener
    public void setOnCocktailClickListener(OnCocktailClickListener cocktailClickListener){
        this.cocktailClickListener = cocktailClickListener;
    }
    public interface OnCocktailClickListener{
        void onCocktailClick(Cocktail cocktail);
    }

    //Constructor
    public CocktailAdapter(List<Cocktail> cocktails, Context context) {
        this.cocktails = cocktails;
        this.context = context;
    }

    @NonNull
    @Override
    public CocktailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cocktail_item, parent, false);

        return new CocktailHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CocktailHolder holder, int position) {

        //Set name
        holder.cocktailName.setText(cocktails.get(position).getName());

        //Set star
        if (cocktails.get(position).isFavorite()){
            holder.cocktailStar.setImageDrawable(context.getDrawable(R.drawable.ic_star_hollow_gold_32dp));
        }else {
            holder.cocktailStar.setImageDrawable(context.getDrawable(R.drawable.ic_star_hollow_black_32dp));
        }

        //Set image
        Glide.with(context)
                .load(cocktails.get(position).getImageUri())
                .centerCrop()
                .into(holder.cocktailImage);
    }

    @Override
    public int getItemCount() {
        if (cocktails != null) {
            return cocktails.size();
        }
        return 0;
    }

    public void setCocktails(List<Cocktail> cocktails){
        this.cocktails = cocktails;
        notifyDataSetChanged();
    }

    public class CocktailHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.cocktail_item_name)
        TextView cocktailName;

        @BindView(R.id.cocktail_item_image)
        ImageView cocktailImage;

        @BindView(R.id.cocktail_item_star)
        ImageView cocktailStar;

        public CocktailHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);

            cocktailStar.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && starClickListener != null) {
                    starClickListener.onStarClick(cocktails.get(position));
                }
            });

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && starClickListener != null) {
                    cocktailClickListener.onCocktailClick(cocktails.get(position));
                }
            });

        }
    }

}
