package com.koby.myapplication.fragments;

import android.util.Log;
import android.view.View;

public class FavoriteFragment extends BaseFragment {

    private static final String TAG = "FavoriteFragment";

    @Override
    public void setObserver() {
        cocktailViewModel.getFavoriteCocktails().
                observe(getViewLifecycleOwner(), listResource -> {
                    if (listResource != null) {
                        switch (listResource.status) {
                            case SUCCESS:
                                progressBar.setVisibility(View.INVISIBLE);
                                adapter.setCocktails(listResource.data);
                                break;
                            case LOADING:
                                progressBar.setVisibility(View.VISIBLE);
                                break;
                            case ERROR:
                                progressBar.setVisibility(View.INVISIBLE);
                                Log.d(TAG, "setObserver: ERROR - " + listResource.message);
                        }
                    }
                });
    }
}
