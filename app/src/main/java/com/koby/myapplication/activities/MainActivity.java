package com.koby.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.koby.myapplication.fragments.FavoriteFragment;
import com.koby.myapplication.fragments.PopularFragment;
import com.koby.myapplication.fragments.SearchFragment;
import com.koby.myapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_bottom_navigation)
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()){
                case R.id.home_action:
                    selectedFragment = new PopularFragment();
                    break;
                case R.id.favorite_action:
                    selectedFragment = new FavoriteFragment();
                    break;
                case R.id.search_action:
                    selectedFragment = new SearchFragment();
                    break;

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container,
                    selectedFragment).commit();

            return true;
        });

        bottomNavigationView.setSelectedItemId(R.id.home_action);

    }

}
