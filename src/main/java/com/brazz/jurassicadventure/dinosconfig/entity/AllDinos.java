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

    // --- NOVO SISTEMA DE IDADE ---
    public enum AgeStage { BEBE, JUVENIL, ADULTO }

    // Idade em que um bebê se torna juvenil (em ticks negativos). Padrão do Minecraft é 20 min.
    public static final int BABY_TO_JUVENILE_AGE = -24000; 
    // Idade em que um juvenil se torna adulto (0).
    public static final int JUVENILE_TO_ADULT_AGE = 0;

    protected AllDinos(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    
    // << REMOVIDO: Não precisamos mais salvar/carregar 'ageInTicks', a classe Animal já faz isso com a sua idade interna. >>

    @Override
    public void aiStep() {
        // << ALTERADO: A lógica agora está muito mais simples >>
        AgeStage previousStage = getAgeStage();
        
        // A chamada super.aiStep() JÁ CUIDA de incrementar a idade do animal a cada tick!
        super.aiStep();

        if (!this.level().isClientSide()) {
            // Se o estágio mudou (ex: de BEBE para JUVENIL)...
            if (getAgeStage() != previousStage) {
                // ...atualiza tudo.
                this.refreshDimensions();
                this.updateAttributesForAge();
            }
        }
    }
    
    protected abstract void updateAttributesForAge();

    // << ALTERADO: Agora usa a idade do Minecraft >>
    // O método isBaby() da classe Animal já funciona perfeitamente (verifica se a idade é < 0)
    
    // Getter para a "lupa" poder usar no futuro
    public AgeStage getAgeStage() {
        int age = this.getAge(); // Usa o método getAge() do Minecraft
        if (age < JUVENILE_TO_ADULT_AGE) return AgeStage.BEBE; // A idade é negativa
        // Você pode adicionar uma lógica para JUVENIL aqui se quiser, mas por agora vamos simplificar
        return AgeStage.ADULTO;
    }
    
    // --- LÓGICA DO GECKOLIB (continua a mesma) ---
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {}
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}