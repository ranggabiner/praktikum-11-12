package com.binerlabs.praktikum11_12;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.binerlabs.praktikum11_12.model.Recipe;
import com.google.android.material.appbar.MaterialToolbar;

public class RecipeDetailActivity extends AppCompatActivity {
    private static final String EXTRA_TITLE = "extra_title";
    private static final String EXTRA_INSTRUCTIONS = "extra_instructions";
    private static final String EXTRA_IMAGE_URL = "extra_image_url";

    public static Intent newIntent(Context context, Recipe recipe) {
        Intent intent = new Intent(context, RecipeDetailActivity.class);
        intent.putExtra(EXTRA_TITLE, recipe.getTitle());
        intent.putExtra(EXTRA_INSTRUCTIONS, recipe.getInstructions());
        intent.putExtra(EXTRA_IMAGE_URL, recipe.getImageUrl());
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        String title = getIntent().getStringExtra(EXTRA_TITLE);
        String instructions = getIntent().getStringExtra(EXTRA_INSTRUCTIONS);
        String imageUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);

        MaterialToolbar toolbar = findViewById(R.id.toolbarDetail);
        ImageView imageRecipeDetail = findViewById(R.id.imageRecipeDetail);
        TextView textRecipeDetailTitle = findViewById(R.id.textRecipeDetailTitle);
        TextView textRecipeInstructions = findViewById(R.id.textRecipeInstructions);

        if (TextUtils.isEmpty(title)) {
            title = getString(R.string.app_name);
        }

        toolbar.setTitle(title);
        toolbar.setNavigationOnClickListener(view -> finish());
        textRecipeDetailTitle.setText(title);
        textRecipeInstructions.setText(TextUtils.isEmpty(instructions)
                ? getString(R.string.instructions_empty)
                : instructions);

        Glide.with(this)
                .load(imageUrl)
                .centerCrop()
                .placeholder(R.drawable.bg_image_placeholder)
                .error(R.drawable.bg_image_placeholder)
                .into(imageRecipeDetail);
    }
}
