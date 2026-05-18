package com.binerlabs.praktikum11_12;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.binerlabs.praktikum11_12.adapter.RecipeAdapter;
import com.binerlabs.praktikum11_12.model.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String API_URL = "https://www.themealdb.com/api/json/v1/1/search.php?s=";
    private static final String REQUEST_TAG = "recipe_list_request";

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerRecipes;
    private LinearLayout layoutLoading;
    private LinearLayout layoutMessage;
    private TextView textMessage;
    private Button buttonRetry;
    private RecipeAdapter recipeAdapter;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerRecipes = findViewById(R.id.recyclerRecipes);
        layoutLoading = findViewById(R.id.layoutLoading);
        layoutMessage = findViewById(R.id.layoutMessage);
        textMessage = findViewById(R.id.textMessage);
        buttonRetry = findViewById(R.id.buttonRetry);

        requestQueue = Volley.newRequestQueue(this);
        recipeAdapter = new RecipeAdapter(recipe ->
                startActivity(RecipeDetailActivity.newIntent(MainActivity.this, recipe)));

        recyclerRecipes.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerRecipes.setHasFixedSize(true);
        recyclerRecipes.setAdapter(recipeAdapter);

        swipeRefreshLayout.setColorSchemeResources(R.color.purple_app_bar);
        swipeRefreshLayout.setOnRefreshListener(() -> fetchRecipes(true));
        buttonRetry.setOnClickListener(view -> fetchRecipes(false));

        fetchRecipes(false);
    }

    private void fetchRecipes(boolean fromRefresh) {
        if (fromRefresh) {
            swipeRefreshLayout.setRefreshing(true);
        } else {
            showLoading();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                API_URL,
                null,
                response -> handleSuccess(response),
                error -> handleError(fromRefresh)
        );
        request.setTag(REQUEST_TAG);
        requestQueue.add(request);
    }

    private void handleSuccess(JSONObject response) {
        swipeRefreshLayout.setRefreshing(false);

        try {
            List<Recipe> recipes = parseRecipes(response);
            recipeAdapter.setRecipes(recipes);

            if (recipes.isEmpty()) {
                showEmpty();
            } else {
                showContent();
            }
        } catch (JSONException exception) {
            showError();
        }
    }

    private void handleError(boolean fromRefresh) {
        swipeRefreshLayout.setRefreshing(false);

        if (fromRefresh && recipeAdapter.getItemCount() > 0) {
            showContent();
            Toast.makeText(this, R.string.error_recipes, Toast.LENGTH_SHORT).show();
            return;
        }

        showError();
    }

    private List<Recipe> parseRecipes(JSONObject response) throws JSONException {
        List<Recipe> recipes = new ArrayList<>();
        JSONArray meals = response.optJSONArray("meals");
        if (meals == null) {
            return recipes;
        }

        for (int index = 0; index < meals.length(); index++) {
            recipes.add(Recipe.fromJson(meals.getJSONObject(index)));
        }

        return recipes;
    }

    private void showLoading() {
        recyclerRecipes.setVisibility(View.GONE);
        layoutMessage.setVisibility(View.GONE);
        layoutLoading.setVisibility(View.VISIBLE);
    }

    private void showContent() {
        layoutLoading.setVisibility(View.GONE);
        layoutMessage.setVisibility(View.GONE);
        recyclerRecipes.setVisibility(View.VISIBLE);
    }

    private void showEmpty() {
        showMessage(getString(R.string.empty_recipes), false);
    }

    private void showError() {
        showMessage(getString(R.string.error_recipes), true);
    }

    private void showMessage(String message, boolean canRetry) {
        layoutLoading.setVisibility(View.GONE);
        recyclerRecipes.setVisibility(View.GONE);
        layoutMessage.setVisibility(View.VISIBLE);
        textMessage.setText(message);
        buttonRetry.setVisibility(canRetry ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(REQUEST_TAG);
        }
    }
}
