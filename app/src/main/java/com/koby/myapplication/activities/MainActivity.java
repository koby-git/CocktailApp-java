package com.koby.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Insert;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.koby.myapplication.util.Resource;
import com.koby.myapplication.viewmodel.CocktailViewModel;
import com.koby.myapplication.R;
import com.koby.myapplication.model.Cocktail;
import com.koby.myapplication.view.adapter.CocktailAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.koby.myapplication.activities.CocktailActivity.COCKTAIL;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_progressbar)
    ProgressBar progressBar;

    private static final String TAG = "MainActivity";
    CocktailViewModel cocktailViewModel;
    List<Cocktail> cocktails;
    CocktailAdapter adapter;
    RecyclerView recyclerView;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        cocktailViewModel = ViewModelProviders.of(MainActivity.this).get(CocktailViewModel.class);

        BottomNavigationView bottomNavigationView = findViewById(R.id.main_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home_action:
                    setToolbar(R.id.home_action);
                    cocktailViewModel.getPopularCocktails()
                            .observe(MainActivity.this, cocktails ->
                                    adapter.setCocktails(
                                            cocktails.data));
                    break;
                case R.id.favorite_action:
                    setToolbar(R.id.favorite_action);
                        cocktailViewModel.getFavoriteCocktails()
                                .observe(MainActivity.this, cocktails ->
                                        adapter.setCocktails(
                                                cocktails.data));
            }

            return true;
        });

        bottomNavigationView.setSelectedItemId(R.id.home_action);

        recyclerView = findViewById(R.id.main_recyclerview);
        setCocktailRecyclerView();
    }

    private void setToolbar(int action) {
        if(action == R.id.home_action){
            getSupportActionBar().setTitle(R.string.popular_cocktails);
        }else if(action == R.id.favorite_action){
            getSupportActionBar().setTitle(R.string.favorite_cocktails);
        }else if (action == R.id.search_action){
            getSupportActionBar().setTitle(R.string.search_cocktails);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //Inflate menu
        getMenuInflater().inflate(R.menu.main_menu,menu);

        searchView = (SearchView) menu.findItem(R.id.main_menu_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                cocktailViewModel.getFavoriteCocktails().removeObservers(MainActivity.this);
                cocktailViewModel.getSearchedCocktail(s)
                        .observe(MainActivity.this, listResource -> {
                    adapter.setCocktails(listResource.data);

                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    private void setCocktailRecyclerView() {
        cocktails = new ArrayList<>();
        adapter = new CocktailAdapter(cocktails, getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        //Press on star to save cocktail
        adapter.setOnStarClickListener(cocktail -> {
            cocktail.setFavorite(true);
            cocktailViewModel.update(cocktail);
        });

        //Press on cocktail to start cocktail activity
        adapter.setOnCocktailClickListener(cocktail -> {
            Intent intent = new Intent(MainActivity.this,CocktailActivity.class);
            intent.putExtra(COCKTAIL,cocktail);
            startActivity(intent);
        });
    }
}
