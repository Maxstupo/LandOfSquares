package com.github.maxstupo.landofsquares.crafting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.github.maxstupo.landofsquares.core.LandOfSquares;
import com.github.maxstupo.landofsquares.item.Item;
import com.github.maxstupo.landofsquares.item.ItemStack;
import com.github.maxstupo.landofsquares.world.block.Block;

/**
 * @author Maxstupo
 *
 */
public class CraftingManager {

    private static CraftingManager instance;

    private List<Recipe> recipes = new ArrayList<>();

    private CraftingManager() {
        registerRecipes();
    }

    public void registerRecipes() {
        clearRecipes();

        addRecipe(new ItemStack(Block.plank, 4), new ItemStack(Block.log, 1));
        addRecipe(new ItemStack(Item.stick, 4), new ItemStack(Block.plank, 1));
        addRecipe(new ItemStack(Block.ladder, 4), new ItemStack(Block.plank, 1));

        addRecipe(new ItemStack(Item.tool_stone_pickaxe, 1), new ItemStack(Item.stick, 2), new ItemStack(Block.stone, 3));
        addRecipe(new ItemStack(Item.tool_stone_spade, 1), new ItemStack(Item.stick, 2), new ItemStack(Block.stone, 1));
        addRecipe(new ItemStack(Item.tool_stone_axe, 1), new ItemStack(Item.stick, 2), new ItemStack(Block.stone, 3));

        addRecipe(new ItemStack(Item.tool_iron_pickaxe, 1), new ItemStack(Item.stick, 2), new ItemStack(Item.ingot_iron, 3));
        addRecipe(new ItemStack(Item.tool_iron_spade, 1), new ItemStack(Item.stick, 2), new ItemStack(Item.ingot_iron, 1));
        addRecipe(new ItemStack(Item.tool_iron_axe, 1), new ItemStack(Item.stick, 2), new ItemStack(Item.ingot_iron, 3));

        addRecipe(new ItemStack(Item.tool_diamond_pickaxe, 1), new ItemStack(Item.stick, 2), new ItemStack(Item.diamond, 3));
        addRecipe(new ItemStack(Item.tool_diamond_spade, 1), new ItemStack(Item.stick, 2), new ItemStack(Item.diamond, 1));
        addRecipe(new ItemStack(Item.tool_diamond_axe, 1), new ItemStack(Item.stick, 2), new ItemStack(Item.diamond, 3));

        addRecipe(new ItemStack(Block.torch, 4), new ItemStack(Item.coal), new ItemStack(Item.stick));
    }

    private void addRecipe(ItemStack result, ItemStack... itemsNeeded) {
        recipes.add(new Recipe(result, itemsNeeded));
        LandOfSquares.get().getLog().debug(getClass().getSimpleName(), "Registered recipe: {0}", result.getCorrectName());
    }

    private void clearRecipes() {
        recipes.clear();
    }

    public Collection<Recipe> getRecipes() {
        return Collections.unmodifiableList(recipes);
    }

    public static CraftingManager get() {
        if (instance == null)
            instance = new CraftingManager();
        return instance;
    }
}
