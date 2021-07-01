package io.github.merchantpug.nibbles;

import io.github.merchantpug.nibbles.access.ItemStackAccess;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

public class ItemStackFoodComponentAPI {
    public static void addFoodComponent(Predicate<ItemStack> stackPredicate, ItemStack stack, FoodComponent foodComponent) {
        if (stackPredicate.test(stack)) {
            ((ItemStackAccess)(Object)stack).setItemStackFoodComponent(foodComponent);
        }
    }

    public static void addFoodComponent(Predicate<ItemStack> stackPredicate, Predicate<LivingEntity> entityPredicate, ItemStack stack, FoodComponent foodComponent) {
        if (stackPredicate.test(stack)) {
            ((ItemStackAccess)(Object)stack).setItemStackFoodComponent(foodComponent);
            ((ItemStackAccess)(Object)stack).setEntityPredicate(entityPredicate);
        }
    }
}
