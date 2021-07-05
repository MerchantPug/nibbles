package io.github.merchantpug.nibbles.access;

import net.minecraft.item.FoodComponent;

public interface ItemStackAccess {

    boolean isItemStackFood();

    FoodComponent getItemStackFoodComponent();

    void setItemStackFoodComponent(FoodComponent stackFoodComponent);
}
