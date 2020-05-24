package com.koby.myapplication.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.koby.myapplication.R;
import com.koby.myapplication.activities.CocktailActivity;
import com.koby.myapplication.model.Cocktail;
import com.koby.myapplication.view.adapter.CocktailAdapter;
import com.koby.myapplication.viewmodel.CocktailViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.koby.myapplication.activities.CocktailActivity.COCKTAIL;

public abstract class BaseFragment extends Fragment {

    @BindView(R.id.fragment_recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.fragment_progressbar)
    ProgressBar progressBar;

    CocktailViewModel cocktailViewModel;
    List<Cocktail> cocktails;
    CocktailAdapter adapter;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cocktails_list_fragment, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        unbinder = ButterKnife.bind(this,view);

        cocktailViewModel = ViewModelProviders.of(requireActivity()).get(CocktailViewModel.class);

        setCocktailRecyclerView();
        setObserver();
    }

    //Set each fragment observer
    public abstract void setObserver();

    //Set the recyclerView and listeners
    private void setCocktailRecyclerView() {
        cocktails = new ArrayList<>();
        adapter = new CocktailAdapter(cocktails, getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        //Press on star to save cocktail
        adapter.setOnStarClickListener(cocktail -> {
            cocktail.setFavorite(true);
            cocktailViewModel.update(cocktail);
        });

        //Press on cocktail to start cocktail activity
        adapter.setOnCocktailClickListener(cocktail -> {
            Intent intent = new Intent(getActivity(), CocktailActivity.class);
            intent.putExtra(COCKTAIL,cocktail);
            startActivity(intent);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
