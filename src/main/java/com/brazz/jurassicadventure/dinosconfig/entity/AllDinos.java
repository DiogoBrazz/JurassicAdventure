package com.brazz.jurassicadventure.dinosconfig.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class AllDinos extends Animal implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public enum AgeStage { BEBE, ADULTO }
    public static final int MAX_BABY_AGE = -24000;

    protected AllDinos(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    
    // O Minecraft já salva e carrega a idade para a classe Animal, não precisamos sobrescrever.
    
    @Override
    public void aiStep() {
        AgeStage previousStage = getAgeStage();
        super.aiStep(); 

        if (!this.level().isClientSide()) {
            // Atualiza os atributos periodicamente para crescimento suave
            if (this.isBaby() && this.tickCount % 100 == 0) {
                this.updateAttributesForAge();
            }
            
            // Atualiza a hitbox quando vira adulto
            if (getAgeStage() != previousStage) {
                this.refreshDimensions();
                this.updateAttributesForAge(); // Atualização final
            }
        }
    }
    
    protected abstract void updateAttributesForAge();

    
    public AgeStage getAgeStage() {
        return this.isBaby() ? AgeStage.BEBE : AgeStage.ADULTO;
    }
    
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {}
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}