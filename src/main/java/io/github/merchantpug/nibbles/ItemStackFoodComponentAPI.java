package io.github.merchantpug.nibbles;

import io.github.merchantpug.nibbles.access.ItemStackAccess;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;

public class ItemStackFoodComponentAPI {
    public static void addFoodComponent(ItemStack stack, FoodComponent foodComponent) {
        ((ItemStackAccess)(Object)stack).setItemStackFoodComponent(foodComponent);
    }

    public static void removeFoodComponent(ItemStack stack) {
        ((ItemStackAccess)(Object)stack).setItemStackFoodComponent(null);
    }
}
