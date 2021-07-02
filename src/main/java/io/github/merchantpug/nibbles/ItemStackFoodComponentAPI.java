package io.github.merchantpug.nibbles;

import io.github.merchantpug.nibbles.access.ItemStackAccess;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

public class ItemStackFoodComponentAPI {
    public static void addFoodComponent(ItemStack stack, FoodComponent foodComponent) {
        ((ItemStackAccess)(Object)stack).setItemStackFoodComponent(foodComponent);
    }

    public static void addFoodComponent(Predicate<LivingEntity> entityPredicate, ItemStack stack, FoodComponent foodComponent) {
        ((ItemStackAccess)(Object)stack).setItemStackFoodComponent(foodComponent);
        ((ItemStackAccess)(Object)stack).setEntityPredicate(entityPredicate);
    }

    public static void removeFoodComponent(ItemStack stack) {
        ((ItemStackAccess)(Object)stack).setItemStackFoodComponent(null);
    }
}
