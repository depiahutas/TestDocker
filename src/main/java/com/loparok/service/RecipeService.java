package com.loparok.service;

import com.loparok.model.Recipe;
import com.loparok.model.User;

import java.util.List;

public interface RecipeService {

    public Recipe createCecipe(Recipe recipe, User user);

    public Recipe findRecipeById(Long id) throws Exception;

    public void deleteRecipe(Long id) throws Exception;

    public Recipe updateRecipe(Recipe recipe, Long id) throws Exception;

    public List<Recipe> findAllRecipe();

    public Recipe likeRecipe(Long recipeId, User user) throws  Exception;
}
