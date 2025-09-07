package com.brazz.jurassicadventure.dinosconfig.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class AllDinosGrowth extends Animal implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final int MAX_BABY_AGE = -24000;
    public static final int ADULT_AGE = 0;

    protected AllDinosGrowth(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    
    @Override
    public void aiStep() {
        super.aiStep();
        
        if (!this.level().isClientSide() && this.isBaby()) {
            if (this.tickCount % 20 == 0) {
                this.updateAttributesForAge();
            }
        }
    }
    
    public float getGrowthProgress() {
        if (!this.isBaby()) {
            return 1.0f;
        }
        
        int currentAge = this.getAge();
        float progress = 1.0f - ((float) currentAge / (float) MAX_BABY_AGE);
        return Math.max(0.0f, Math.min(1.0f, progress));
    }
    
    protected abstract void updateAttributesForAge();
    
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {}
    
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}