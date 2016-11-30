package com.github.maxstupo.landofsquares.crafting;

import java.util.Arrays;

import com.github.maxstupo.flatengine.gui.IListItem;
import com.github.maxstupo.landofsquares.item.ItemStack;

/**
 * @author Maxstupo
 *
 */
public class Recipe implements IListItem {

    private final ItemStack result;
    private final ItemStack[] requiredItems;

    public Recipe(ItemStack result, ItemStack[] requiredItems) {
        this.result = result;
        this.requiredItems = requiredItems;
    }

    public ItemStack getResult() {
        return result;
    }

    public ItemStack[] getRequiredItems() {
        return requiredItems;
    }

    @Override
    public String getListItemText() {
        return result.stackSize + "x " + result.getCorrectName();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(requiredItems);
        result = prime * result + ((this.result == null) ? 0 : this.result.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Recipe other = (Recipe) obj;
        if (!Arrays.equals(requiredItems, other.requiredItems))
            return false;
        if (result == null) {
            if (other.result != null)
                return false;
        } else if (!result.equals(other.result))
            return false;
        return true;
    }

}
