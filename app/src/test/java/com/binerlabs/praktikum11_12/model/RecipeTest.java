package com.binerlabs.praktikum11_12.model;

import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RecipeTest {

    @Test
    public void fromJson_mapsMealDbFields() throws Exception {
        JSONObject mealJson = new JSONObject()
                .put("idMeal", "53049")
                .put("strMeal", "Apam balik")
                .put("strCategory", "Dessert")
                .put("strInstructions", "Mix milk, oil and egg together.")
                .put("strMealThumb", "https://www.themealdb.com/images/media/meals/adxcbq1619787919.jpg");

        Recipe recipe = Recipe.fromJson(mealJson);

        assertEquals("53049", recipe.getId());
        assertEquals("Apam balik", recipe.getTitle());
        assertEquals("Dessert", recipe.getCategory());
        assertEquals("Mix milk, oil and egg together.", recipe.getInstructions());
        assertEquals("https://www.themealdb.com/images/media/meals/adxcbq1619787919.jpg", recipe.getImageUrl());
    }

    @Test
    public void fromJson_usesEmptyStringsForMissingFields() throws Exception {
        Recipe recipe = Recipe.fromJson(new JSONObject());

        assertEquals("", recipe.getId());
        assertEquals("", recipe.getTitle());
        assertEquals("", recipe.getCategory());
        assertEquals("", recipe.getInstructions());
        assertEquals("", recipe.getImageUrl());
    }
}
