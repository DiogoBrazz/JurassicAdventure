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

    public enum AgeStage { BEBE, JUVENIL, ADULTO }
    public static final int BABY_TO_JUVENILE_AGE = -24000;
    public static final int JUVENILE_TO_ADULT_AGE = 0;

    protected AllDinos(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    
    @Override
    public void aiStep() {
        AgeStage previousStage = getAgeStage();
        super.aiStep(); // Esta linha já incrementa a idade do animal

        if (!this.level().isClientSide()) {
            // MELHORIA: Atualiza os atributos a cada 5 segundos para um crescimento de força mais suave.
            if (this.isBaby() && this.tickCount % 100 == 0) {
                this.updateAttributesForAge();
            }
            
            // Se o estágio mudou (ex: de BEBE para ADULTO), força uma atualização final.
            if (getAgeStage() != previousStage) {
                this.refreshDimensions(); // Recalcula a hitbox
                this.updateAttributesForAge();
            }
        }
    }
    
    protected abstract void updateAttributesForAge();

    public AgeStage getAgeStage() {
        // A idade de um bebê no Minecraft é negativa.
        return this.getAge() < 0 ? AgeStage.BEBE : AgeStage.ADULTO;
    }
    
    // --- LÓGICA DO GECKOLIB ---
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {}
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}