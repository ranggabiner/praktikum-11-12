package com.binerlabs.praktikum11_12.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.binerlabs.praktikum11_12.R;
import com.binerlabs.praktikum11_12.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private final List<Recipe> recipes = new ArrayList<>();
    private final OnRecipeClickListener listener;

    public RecipeAdapter(OnRecipeClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.bind(recipes.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public void setRecipes(List<Recipe> newRecipes) {
        recipes.clear();
        recipes.addAll(newRecipes);
        notifyDataSetChanged();
    }

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageRecipe;
        private final TextView textTitle;
        private final TextView textCategory;

        RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            imageRecipe = itemView.findViewById(R.id.imageRecipe);
            textTitle = itemView.findViewById(R.id.textRecipeTitle);
            textCategory = itemView.findViewById(R.id.textRecipeCategory);
        }

        void bind(Recipe recipe, OnRecipeClickListener listener) {
            textTitle.setText(recipe.getTitle());
            textCategory.setText(itemView.getContext().getString(R.string.category_food));

            Glide.with(itemView)
                    .load(recipe.getImageUrl())
                    .centerCrop()
                    .placeholder(R.drawable.bg_image_placeholder)
                    .error(R.drawable.bg_image_placeholder)
                    .into(imageRecipe);

            itemView.setOnClickListener(view -> listener.onRecipeClick(recipe));
        }
    }
}
