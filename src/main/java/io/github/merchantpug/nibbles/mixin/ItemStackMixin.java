package io.github.merchantpug.nibbles.mixin;

import io.github.merchantpug.nibbles.Nibbles;
import io.github.merchantpug.nibbles.access.ItemStackAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.text.Text;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemStackAccess {

    @Shadow public abstract Item getItem();

    @Unique
    protected FoodComponent nibbles_stackFoodComponent;

    @Unique
    private FoodComponent nibbles_stackFoodComponentShouldBe;

    @Unique
    private Predicate<LivingEntity> nibbles_entityPredicate;

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
            cir.setReturnValue(user.eatFood(world, (ItemStack)(Object)this));
        }
    }

    @Inject(method = "getUseAction", at = @At("HEAD"), cancellable = true)
    private void getUseAction(CallbackInfoReturnable<UseAction> cir) {
        if (this.isItemStackFood()) {
            cir.setReturnValue(UseAction.EAT);
        }
    }

    @Inject(method = "getMaxUseTime", at = @At("HEAD"), cancellable = true)
    private void getMaxUseTime(CallbackInfoReturnable<Integer> cir) {
        if (isItemStackFood()) {
            cir.setReturnValue(this.getItemStackFoodComponent().isSnack() ? 16 : 32);
        }
    }

    @Inject(method = "inventoryTick", at = @At("HEAD"), cancellable = true)
    private void inventoryTick(World world, Entity entity, int slot, boolean selected, CallbackInfo ci) {
        if (this.getItem() != null && entity instanceof LivingEntity) {
            if (!(nibbles_stackFoodComponent == nibbles_stackFoodComponentShouldBe)) {
                if (nibbles_entityPredicate == null) {
                    this.nibbles_stackFoodComponent = this.nibbles_stackFoodComponentShouldBe;
                } else {
                    if (this.nibbles_entityPredicate.test((LivingEntity)entity)) {
                        this.nibbles_stackFoodComponent = this.nibbles_stackFoodComponentShouldBe;
                    } else {
                        this.nibbles_stackFoodComponent = null;
                    }
                }
            }
        }
    }

    @Inject(method = "isFood", at = @At("HEAD"), cancellable = true)
    private void isFood(CallbackInfoReturnable<Boolean> cir) {
        if (this.isItemStackFood()) {
            cir.setReturnValue(true);
        }
    }

    @Override
    public FoodComponent getItemStackFoodComponent() {
        return this.nibbles_stackFoodComponent;
    }

    @Override
    public void setItemStackFoodComponent(FoodComponent stackFoodComponent) {
        this.nibbles_stackFoodComponentShouldBe = stackFoodComponent;
    }

    @Override
    public boolean isItemStackFood() {
        return this.nibbles_stackFoodComponent != null;
    }

    @Override
    public void setEntityPredicate(Predicate<LivingEntity> entityPredicate) {
        this.nibbles_entityPredicate = entityPredicate;
    }
}
