package com.binerlabs.praktikum11_12.model;

import org.json.JSONObject;

public class Recipe {
    private final String id;
    private final String title;
    private final String category;
    private final String instructions;
    private final String imageUrl;

    public Recipe(String id, String title, String category, String instructions, String imageUrl) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.instructions = instructions;
        this.imageUrl = imageUrl;
    }

    public static Recipe fromJson(JSONObject jsonObject) {
        return new Recipe(
                jsonObject.optString("idMeal"),
                jsonObject.optString("strMeal"),
                jsonObject.optString("strCategory"),
                jsonObject.optString("strInstructions"),
                jsonObject.optString("strMealThumb")
        );
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
