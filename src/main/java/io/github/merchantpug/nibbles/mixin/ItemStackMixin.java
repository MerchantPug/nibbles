package io.github.merchantpug.nibbles.mixin;

import io.github.merchantpug.nibbles.access.ItemStackAccess;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemStackAccess {

    @Shadow public abstract Item getItem();

    @Unique
    protected FoodComponent nibbles_stackFoodComponent;
    @Unique
    protected UseAction nibbles_useAction;
    @Unique
    protected ItemStack nibbles_returnStack;
    @Unique
    protected SoundEvent nibbles_eatSound;

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (this.isItemStackFood()) {
            ItemStack itemStack = user.getStackInHand(hand);
            if (user.canConsume(this.getItemStackFoodComponent().isAlwaysEdible())) {
                user.setCurrentHand(hand);
                cir.setReturnValue(TypedActionResult.consume(itemStack));
                if (this.getItem() instanceof BucketItem) {
                    BlockHitResult blockHitResult = ItemAccessor.callRaycast(world, user, ((BucketItemAccessor)this.getItem()).getFluid() == Fluids.EMPTY ? RaycastContext.FluidHandling.SOURCE_ONLY : RaycastContext.FluidHandling.NONE);
                    if (blockHitResult.getType() == HitResult.Type.BLOCK) {
                        cir.setReturnValue(((ItemStack)(Object)this).getItem().use(world, user, hand));
                    }
                }
            } else {
                cir.setReturnValue(((ItemStack)(Object)this).getItem().use(world, user, hand));
            }
        }
    }

    @Inject(method = "finishUsing", at = @At("HEAD"), cancellable = true)
    private void finishUsing(World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        if (this.isItemStackFood()) {
            ItemStack itemStack = user.eatFood(world, (ItemStack)(Object)this);
            if (this.getReturnStack() != null) {
                cir.setReturnValue(user instanceof PlayerEntity && ((PlayerEntity)user).getAbilities().creativeMode ? itemStack : this.getReturnStack());
            } else {
                cir.setReturnValue(itemStack);
            }
        }
    }

    @Inject(method = "getUseAction", at = @At("HEAD"), cancellable = true)
    private void getUseAction(CallbackInfoReturnable<UseAction> cir) {
        if (this.isItemStackFood()) {
            cir.setReturnValue(Objects.requireNonNullElse(this.getFoodUseAction(), UseAction.EAT));
        }
    }

    @Inject(method = "getMaxUseTime", at = @At("HEAD"), cancellable = true)
    private void getMaxUseTime(CallbackInfoReturnable<Integer> cir) {
        if (isItemStackFood()) {
            cir.setReturnValue(this.getItemStackFoodComponent().isSnack() ? 16 : 32);
        }
    }

    @Inject(method = "isFood", at = @At("HEAD"), cancellable = true)
    private void isFood(CallbackInfoReturnable<Boolean> cir) {
        if (this.isItemStackFood()) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getDrinkSound", at = @At("HEAD"), cancellable = true)
    private void getDrinkSound(CallbackInfoReturnable<SoundEvent> cir) {
        if (this.getStackEatSound() != null) {
            cir.setReturnValue(this.getStackEatSound());
        }
    }

    @Inject(method = "getEatSound", at = @At("HEAD"), cancellable = true)
    private void getEatSound(CallbackInfoReturnable<SoundEvent> cir) {
        if (this.getStackEatSound() != null) {
            cir.setReturnValue(this.getStackEatSound());
        }
    }

    @Override
    public FoodComponent getItemStackFoodComponent() {
        return this.nibbles_stackFoodComponent;
    }

    @Override
    public void setItemStackFoodComponent(FoodComponent stackFoodComponent) {
        this.nibbles_stackFoodComponent = stackFoodComponent;
    }

    @Override
    public boolean isItemStackFood() {
        return this.nibbles_stackFoodComponent != null;
    }

    @Override
    public UseAction getFoodUseAction() {
        return this.nibbles_useAction;
    }

    @Override
    public void setFoodUseAction(UseAction useAction) {
        if (useAction == UseAction.EAT || useAction == UseAction.DRINK) {
            this.nibbles_useAction = useAction;
        }
    }

    @Override
    public ItemStack getReturnStack() {
        return this.nibbles_returnStack;
    }

    @Override
    public void setReturnStack(ItemStack stack) {
        this.nibbles_returnStack = stack;
    }

    @Override
    public SoundEvent getStackEatSound() {
        return this.nibbles_eatSound;
    }

    @Override
    public void setStackEatSound(SoundEvent sound) {
        this.nibbles_eatSound = sound;
    }
}
