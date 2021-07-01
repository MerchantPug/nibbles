package io.github.merchantpug.nibbles.access;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.FoodComponent;

import java.util.function.Predicate;

public interface ItemStackAccess {

    boolean isItemStackFood();

    FoodComponent getItemStackFoodComponent();

    void setItemStackFoodComponent(FoodComponent stackFoodComponent);

    void setEntityPredicate(Predicate<LivingEntity> entityPredicate);
}
