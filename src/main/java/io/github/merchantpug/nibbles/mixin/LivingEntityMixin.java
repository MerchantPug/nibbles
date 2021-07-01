package io.github.merchantpug.nibbles.mixin;

import com.mojang.datafixers.util.Pair;
import io.github.merchantpug.nibbles.access.ItemStackAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity  {

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "applyFoodEffects", at = @At("HEAD"))
    private void applyFoodEffects(ItemStack stack, World world, LivingEntity targetEntity, CallbackInfo ci) {
        if (((ItemStackAccess)(Object)stack).isItemStackFood()) {
            List<Pair<StatusEffectInstance, Float>> list = ((ItemStackAccess)(Object)stack).getItemStackFoodComponent().getStatusEffects();
            Iterator var6 = list.iterator();

            while(var6.hasNext()) {
                Pair<StatusEffectInstance, Float> pair = (Pair)var6.next();
                if (!world.isClient && pair.getFirst() != null && world.random.nextFloat() < pair.getSecond()) {
                    targetEntity.addStatusEffect(new StatusEffectInstance(pair.getFirst()));
                }
            }
        }
    }
}
