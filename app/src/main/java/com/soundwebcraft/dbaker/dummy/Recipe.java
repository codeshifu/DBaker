package com.soundwebcraft.dbaker.dummy;

import com.soundwebcraft.dbaker.R;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private String name;
    private int resourceId;
    private int step;

    public Recipe() {}

    public Recipe(String name, int resourceId, int step) {
        this.name = name;
        this.resourceId = resourceId;
        this.step = step;
    }

    public String getName() {
        return name;
    }

    public int getResourceId() {
        return resourceId;
    }

    public static List<Recipe> getRecipes () {
        List<Recipe> recipes = new ArrayList<>();

        ArrayList<Integer> resources = new ArrayList<>();
        resources.add(R.drawable.nutellapie);
        resources.add(R.drawable.brownies);
        resources.add(R.drawable.yellowcake);
        resources.add(R.drawable.cheesecake);

        for (int i = 0; i < 4; i++) {
            int step = (int) (Math.random() * 12 + 1);
            recipes.add(new Recipe("Recipe " + i, resources.get(i), step));
        }

        return recipes;
    }

    public int getStep() {
        return step;
    }
}
