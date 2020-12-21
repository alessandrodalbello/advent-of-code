package org.adb.adventofcode.aoc2020;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.adb.adventofcode.Solver;
import org.adb.adventofcode.io.FileResourceReader;

class Day21 implements Solver {

    private static final String INPUT_FILENAME = "aoc_day21.txt";

    private final List<Recipe> recipes;
    private final List<String> safeIngredients;
    private final Map<String, List<Recipe>> recipesByAllergen;

    public Day21() {
        safeIngredients = new LinkedList<>();
        recipesByAllergen = new HashMap<>();

        try (FileResourceReader reader = new FileResourceReader(INPUT_FILENAME)) {
            recipes = reader.parseLines(this::parseRecipe).collect(Collectors.toList());
            for (Recipe recipe : recipes) {
                for (String allergen : recipe.allergens) {
                    recipesByAllergen.putIfAbsent(allergen, new LinkedList<>());
                    recipesByAllergen.get(allergen).add(recipe);
                }
            }
        }
    }

    private Recipe parseRecipe(String rawRecipe) {
        String[] recipeParts = rawRecipe.split(" \\(contains ");
        List<String> ingredients = parseIngredients(recipeParts[0]);
        List<String> allergens = parseAllergens(recipeParts[1]);
        return new Recipe(ingredients, allergens);
    }

    private List<String> parseIngredients(String rawIngredients) {
        return Arrays.stream(rawIngredients.split(" ")).collect(Collectors.toList());
    }

    private List<String> parseAllergens(String rawAllergens) {
        rawAllergens = rawAllergens.substring(0, rawAllergens.length() - 1);
        return Arrays.stream(rawAllergens.split(", ")).collect(Collectors.toList());
    }

    @Override
    public void solveSilver() {
        Map<String, List<Recipe>> recipesByIngredient = new HashMap<>();
        for (Recipe recipe : recipes) {
            for (String ingredient : recipe.ingredients) {
                recipesByIngredient.putIfAbsent(ingredient, new LinkedList<>());
                recipesByIngredient.get(ingredient).add(recipe);
            }
        }

        long safeIngredientsFrequency = 0;
        for (String ingredient : recipesByIngredient.keySet()) {
            List<Recipe> ingredientRecipes = recipesByIngredient.get(ingredient);
            Set<String> allergens = ingredientRecipes.stream()
                    .flatMap(recipe -> recipe.allergens.stream())
                    .collect(Collectors.toSet());
            List<String> otherAllergens = allergens.stream()
                    .flatMap(allergen -> recipesByAllergen.get(allergen).stream())
                    .filter(recipe -> !ingredientRecipes.contains(recipe))
                    .flatMap(recipe -> recipe.allergens.stream())
                    .collect(Collectors.toList());

            boolean isSafe = otherAllergens.containsAll(allergens);
            if (isSafe) {
                safeIngredientsFrequency += ingredientRecipes.size();
                safeIngredients.add(ingredient);
            }
        }
        System.out.printf("The frequency of safe ingredients is %d.%n", safeIngredientsFrequency);
    }

    @Override
    public void solveGold() {
        Map<String, Set<String>> possibleAllergensByIngredient = possibleAllergensByIngredient();
        SortedMap<String, String> ingredientByAllergen = identifyAllergens(possibleAllergensByIngredient);
        String dangerousIngredients = String.join(",", ingredientByAllergen.values());
        System.out.printf("Canonical dangerous ingredient list: %s.%n", dangerousIngredients);
    }

    private Map<String, Set<String>> possibleAllergensByIngredient() {
        Map<String, Set<String>> possibleAllergensByIngredient = new HashMap<>();
        for (Recipe recipe : recipes) {
            recipe.ingredients.removeAll(safeIngredients);
            for (String ingredient : recipe.ingredients) {
                possibleAllergensByIngredient.putIfAbsent(ingredient, new HashSet<>());
                Set<String> validAllergens = recipe.allergens.stream()
                        .filter(allergen -> recipesByAllergen.get(allergen).stream()
                                .allMatch(allergenRecipe -> allergenRecipe.ingredients.contains(ingredient)))
                        .collect(Collectors.toSet());
                possibleAllergensByIngredient.get(ingredient).addAll(validAllergens);
            }
        }
        return possibleAllergensByIngredient;
    }

    private SortedMap<String, String> identifyAllergens(Map<String, Set<String>> possibleAllergensByIngredient) {
        SortedMap<String, String> ingredientByAllergen = new TreeMap<>();
        boolean allIdentified = false;
        while (!allIdentified) {
            List<String> ingredients = possibleAllergensByIngredient.entrySet().stream()
                    .filter(entry -> entry.getValue().size() == 1)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            for (String ingredient : ingredients) {
                Set<String> ingredientAllergens = possibleAllergensByIngredient.remove(ingredient);
                String allergen = ingredientAllergens.iterator().next();
                ingredientByAllergen.putIfAbsent(allergen, ingredient);
                possibleAllergensByIngredient.values().forEach(allergens -> allergens.remove(allergen));
            }
            allIdentified = possibleAllergensByIngredient.isEmpty();
        }
        return ingredientByAllergen;
    }

    private static class Recipe {
        private final List<String> ingredients;
        private final List<String> allergens;

        private Recipe(List<String> ingredients, List<String> allergens) {
            this.ingredients = ingredients;
            this.allergens = allergens;
        }
    }
}
