package com.brazz.jurassicadventure.dinosconfig.entity;

// Importações do Minecraft e Forge
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

// Importações do GeckoLib
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class AbstractWaterDinoEntity extends WaterAnimal implements GeoEntity, IGrowingEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    // CORREÇÃO 1: Apontado para a classe correta
    private static final EntityDataAccessor<Integer> DATA_AGE = SynchedEntityData.defineId(AbstractWaterDinoEntity.class, EntityDataSerializers.INT);
    private int growthDelay = 0;

    protected AbstractWaterDinoEntity(EntityType<? extends WaterAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        // ADICIONADO: Ativa o controlador de movimento customizado
        this.moveControl = new WaterDinoMoveControl(this);
    }

    // --- MÉTODOS ABSTRATOS (CADA DINO DEVE IMPLEMENTAR) ---
    public abstract int getMaxGrowthAge();
    protected abstract float getBabyWidth();
    protected abstract float getBabyHeight();
    protected abstract float getAdultWidth();
    protected abstract float getAdultHeight();

    public abstract double getBaseHealth();
    public abstract double getMaxHealthAttribute();
    public abstract double getBaseAttackDamage();
    public abstract double getMaxAttackDamage();
    
    // --- LÓGICA DE SPAWN E DADOS (NBT) ---
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnGroupData, @Nullable CompoundTag pDataTag) {
        if (pDataTag != null && pDataTag.contains("IsIncubatorBaby") && pDataTag.getBoolean("IsIncubatorBaby")) {
            this.setDinoAge(0);
        } else {
            this.setDinoAge(this.getMaxGrowthAge());
        }
        this.refreshDimensions();
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnGroupData, pDataTag);
    }

    // --- PROPRIEDADES AQUÁTICAS ---
    @Override
    public boolean canBreatheUnderwater() { return true; }

    @Override
    public boolean isPushedByFluid() { return false; }


    // --- CLASSE INTERNA PARA CONTROLE DE MOVIMENTO ---
    static class WaterDinoMoveControl extends MoveControl {
        // Usa a classe abstrata genérica
        private final AbstractWaterDinoEntity dino;

        public WaterDinoMoveControl(AbstractWaterDinoEntity dino) {
            super(dino);
            this.dino = dino;
        }

        @Override
        public void tick() {
            if (this.dino.isInWater()) {
                this.dino.setDeltaMovement(this.dino.getDeltaMovement().add(0.0D, 0.005D, 0.0D));
            }

            if (this.operation == Operation.MOVE_TO && !this.dino.getNavigation().isDone()) {
                double xDiff = this.wantedX - this.dino.getX();
                double yDiff = this.wantedY - this.dino.getY();
                double zDiff = this.wantedZ - this.dino.getZ();
                double distance = Math.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
                if (distance < 1.0E-7D) { // Evita divisão por zero
                    this.mob.setZza(0.0F);
                    return;
                }
                yDiff /= distance;

                float angle = (float)(Math.atan2(zDiff, xDiff) * (180F / Math.PI)) - 90.0F;
                this.dino.setYRot(this.rotlerp(this.dino.getYRot(), angle, 90.0F));
                this.dino.yBodyRot = this.dino.getYRot();

                float speed = (float)(this.speedModifier * this.dino.getAttributeValue(Attributes.MOVEMENT_SPEED));
                this.dino.setSpeed(speed * 0.8F);

                this.dino.setDeltaMovement(this.dino.getDeltaMovement().add(0.0D, (double)speed * yDiff * 0.1D, 0.0D));
            } else {
                this.dino.setSpeed(0.0F);
            }
        }
    }

    // --- LÓGICA DE CRESCIMENTO (sem alterações) ---
    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        float growthProgress = (float)this.getDinoAge() / (float)this.getMaxGrowthAge();
        float currentWidth = getBabyWidth() + (getAdultWidth() - getBabyWidth()) * growthProgress;
        float currentHeight = getBabyHeight() + (getAdultHeight() - getBabyHeight()) * growthProgress;
        return EntityDimensions.scalable(currentWidth, currentHeight);
    }

    public int getDinoAge() { return this.entityData.get(DATA_AGE); }
    public void setDinoAge(int age) { this.entityData.set(DATA_AGE, Math.min(age, this.getMaxGrowthAge())); }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide()) {
            this.growthDelay++;
            if (this.growthDelay > 20) {
                this.growthDelay = 0;
                int currentAge = this.getDinoAge();
                if (currentAge < this.getMaxGrowthAge()) {
                    this.setDinoAge(currentAge + 20);
                    updateAttributes();
                }
            }
        }
        this.refreshDimensions();
    }

    private void updateAttributes() {
        float currentHealth = this.getHealth();
        double growthPercent = (double)this.getDinoAge() / (double)this.getMaxGrowthAge();
        double newMaxHealth = this.getBaseHealth() + (this.getMaxHealthAttribute() - this.getBaseHealth()) * growthPercent;
        double newAttackDamage = this.getBaseAttackDamage() + (this.getMaxAttackDamage() - this.getBaseAttackDamage()) * growthPercent;
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(newMaxHealth);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(newAttackDamage);
        this.setHealth(currentHealth);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_AGE, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("DinoAge", this.getDinoAge());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setDinoAge(pCompound.getInt("DinoAge"));
    }
    
    // --- LÓGICA DA GECKOLIB ---
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private PlayState predicate(AnimationState<AbstractWaterDinoEntity> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(RawAnimation.begin().then("walk", Animation.LoopType.LOOP));
        } else {
            event.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}