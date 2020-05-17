package com.koby.myapplication.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

import com.koby.myapplication.R;

public class SearchFragment extends BaseFragment {

    private static final String TAG = "SearchFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.main_menu_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                cocktailViewModel.getSearchedCocktail(s)
                        .observe(getViewLifecycleOwner(), listResource -> {
                            if (listResource != null) {
                                switch (listResource.status) {
                                    case SUCCESS:
                                        Log.d(TAG, "setObserver: SUCCESS");
                                        progressBar.setVisibility(View.INVISIBLE);
                                        adapter.setCocktails(listResource.data);
                                        break;
                                    case LOADING:
                                        progressBar.setVisibility(View.VISIBLE);
                                        Log.d(TAG, "setObserver: LOADING");
                                        break;
                                    case ERROR:
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Log.d(TAG, "setObserver: ERROR");
                                }
                            }
                        });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

    }

    @Override
    public void setObserver() { }
}
