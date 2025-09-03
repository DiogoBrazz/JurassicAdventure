package com.brazz.jurassicadventure.dinosconfig.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl; // << IMPORT NOVO
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation; // << IMPORT NOVO
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation; // << IMPORT NOVO
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3; // << IMPORT NOVO

// --- GeckoLib ---
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.object.PlayState;

public class MosassauroEntity extends WaterAnimal implements GeoEntity {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public MosassauroEntity(EntityType<? extends WaterAnimal> type, Level level) {
        super(type, level);
        // << ADICIONADO: Define um controlador de movimento para nado inteligente, como o de um golfinho >>
        this.moveControl = new MosassauroMoveControl(this);
    }

    /**
     * << NOVO: Cria um sistema de navegação que entende a água. >>
     * Isso impede que ele tente "andar" no fundo do oceano.
     */
    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        return new WaterBoundPathNavigation(this, pLevel);
    }
    
    // ---------- GECKOLIB ----------
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private PlayState predicate(AnimationState state) {
        // ATENÇÃO: Os nomes "swim" e "idle" devem ser os mesmos do seu arquivo de animação .json
        if (state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().then("walk", Animation.LoopType.LOOP));
        } else {
            state.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    // ---------- AI GOALS ----------
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(1, new RandomSwimmingGoal(this, 1.0D, 40));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.4D, true));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.1D, Ingredient.of(Items.BEEF), false));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Animal.class, true));
    }

    // ---------- ATRIBUTOS ----------
    public static AttributeSupplier.Builder createAttributes() {
        return WaterAnimal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 120.0D)
                .add(Attributes.MOVEMENT_SPEED, 1.0D)
                .add(Attributes.ATTACK_DAMAGE, 20.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 2.0D)
                .add(Attributes.FOLLOW_RANGE, 30.0D);
    }

    // ---------- RESPIRAÇÃO / FLUIDO ----------
    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }
    
    // ---------- CLASSE INTERNA PARA CONTROLE DE MOVIMENTO AQUÁTICO ----------
    /**
     * Esta classe interna customiza como o Mosassauro se move na água.
     * A lógica aqui é muito similar à do golfinho, permitindo um nado suave em 3D.
     */
    static class MosassauroMoveControl extends MoveControl {
        private final MosassauroEntity mosassauro;

        public MosassauroMoveControl(MosassauroEntity mosassauro) {
            super(mosassauro);
            this.mosassauro = mosassauro;
        }

        @Override
        public void tick() {
            if (this.mosassauro.isInWater()) {
                // Se estiver na água, aplica uma pequena força para cima para evitar que afunde
                this.mosassauro.setDeltaMovement(this.mosassauro.getDeltaMovement().add(0.0D, 0.005D, 0.0D));
            }

            if (this.operation == MoveControl.Operation.MOVE_TO && !this.mosassauro.getNavigation().isDone()) {
                double xDiff = this.wantedX - this.mosassauro.getX();
                double yDiff = this.wantedY - this.mosassauro.getY();
                double zDiff = this.wantedZ - this.mosassauro.getZ();
                double distance = Math.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
                yDiff /= distance;

                float angle = (float)(Math.atan2(zDiff, xDiff) * (double)(180F / (float)Math.PI)) - 90.0F;
                this.mosassauro.setYRot(this.rotlerp(this.mosassauro.getYRot(), angle, 90.0F));
                this.mosassauro.yBodyRot = this.mosassauro.getYRot();

                float speed = (float)(this.speedModifier * this.mosassauro.getAttributeValue(Attributes.MOVEMENT_SPEED));
                this.mosassauro.setSpeed(speed * 0.8F); // Ajuste a agilidade geral aqui

                // Movimento suave para cima e para baixo
                this.mosassauro.setDeltaMovement(this.mosassauro.getDeltaMovement().add(0.0D, (double)speed * yDiff * 0.1D, 0.0D));
            } else {
                this.mosassauro.setSpeed(0.0F);
            }
        }
    }
}