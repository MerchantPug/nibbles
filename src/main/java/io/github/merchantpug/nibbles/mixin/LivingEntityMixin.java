package io.github.merchantpug.nibbles.mixin;

import com.mojang.datafixers.util.Pair;
import io.github.merchantpug.nibbles.access.ItemStackAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;
import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity  {
    @Shadow
    abstract void applyFoodEffects(ItemStack stack, World world, LivingEntity targetEntity);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "eatFood", at = @At("HEAD"), cancellable = true)
    private void eatFood(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        if (stack.isFood()) {
            world.emitGameEvent(this, GameEvent.EAT, this.getCameraBlockPos());
            world.playSound(null, this.getX(), this.getY(), this.getZ(), ((LivingEntity)(Object)this).getEatSound(stack), SoundCategory.NEUTRAL, 1.0F, 1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.4F);
            this.applyFoodEffects(stack, world, (LivingEntity)(Object)this);
            if (!((LivingEntity)(Object)this instanceof PlayerEntity) || !((PlayerEntity)(Object)this).getAbilities().creativeMode) {
                stack.decrement(1);
            }

            this.emitGameEvent(GameEvent.EAT);
        }

        cir.setReturnValue(stack);
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
