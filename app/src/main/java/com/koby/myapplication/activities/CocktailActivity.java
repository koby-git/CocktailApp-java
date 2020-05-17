package com.koby.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.koby.myapplication.R;
import com.koby.myapplication.model.Cocktail;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CocktailActivity extends AppCompatActivity {

    public static final String COCKTAIL = "cocktail";

    @BindView(R.id.cocktail_image)
    ImageView cocktailImage;
    @BindView(R.id.cocktail_name)
    TextView cocktailName;
    @BindView(R.id.cocktail_instruction)
    TextView cocktailInstruction;
    @BindView(R.id.cocktail_glass)
    TextView cocktailGlass;
    @BindView(R.id.cocktail_tags)
    TextView cocktailTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cocktail);

        ButterKnife.bind(this);

        Cocktail cocktail = (Cocktail) getIntent().getSerializableExtra(COCKTAIL);

        Glide.with(this)
                .load(Uri.parse(cocktail.getImageUri()))
                .centerCrop()
                .into(cocktailImage);

        cocktailName.setText(cocktail.getName());

        cocktailInstruction.setText(cocktail.getInstruction());

        cocktailGlass.setText("Glass: " + cocktail.getGlass());

        cocktailTags.setText("Tags: " + cocktail.getTags());

    }
}
