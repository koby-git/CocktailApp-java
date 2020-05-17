package com.koby.myapplication.fragments;

import android.util.Log;
import android.view.View;


public class PopularFragment extends BaseFragment {

    private static final String TAG = "PopularFragment";

    @Override
    public void setObserver() {

        cocktailViewModel.getPopularCocktails().
                observe(getViewLifecycleOwner(), listResource -> {

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
    }
}
