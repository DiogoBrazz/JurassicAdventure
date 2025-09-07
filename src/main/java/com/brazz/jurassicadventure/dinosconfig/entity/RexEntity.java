package com.brazz.jurassicadventure.dinosconfig.entity;

import com.brazz.jurassicadventure.ModEntities; // <-- IMPORTANTE: Adicione esta importação
import net.minecraft.world.item.Items;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class RexEntity extends Animal implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final EntityDataAccessor<Integer> DATA_AGE =
            SynchedEntityData.defineId(RexEntity.class, EntityDataSerializers.INT);

    public static final int MAX_AGE = 400; // 20 ticks * 60 seg * 20 min = 24000
    private int growthDelay = 0;

    public RexEntity(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    // --- NOVO MÉTODO: finalizeSpawn ---
    // Este método é chamado UMA VEZ quando a entidade é criada no mundo.
    // É o lugar perfeito para definir sua idade inicial.
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnGroupData, @Nullable CompoundTag pDataTag) {
        
        // CONDIÇÃO 1: Nasceu da Incubadora
        // Verificamos se a tag "IsIncubatorBaby" que você enviou existe.
        if (pDataTag != null && pDataTag.contains("IsIncubatorBaby") && pDataTag.getBoolean("IsIncubatorBaby")) {
            System.out.println("REX: Recebi a ordem 'IsIncubatorBaby: true'. Nascendo como filhote.");
            this.setDinoAge(0); // Nasce com idade 0 (bebê)
        }
        // CONDIÇÃO 2: Nasceu por Reprodução (Breeding)
        else if (pReason == MobSpawnType.BREEDING) {
            System.out.println("REX: Nascendo de reprodução como filhote.");
            this.setDinoAge(0); // Nasce com idade 0 (bebê)
        }
        // CONDIÇÃO 3 (PADRÃO): Spawn Egg, Comando /summon, etc.
        else {
            System.out.println("REX: Spawn padrão (ovo/comando). Nascendo como adulto.");
            this.setDinoAge(MAX_AGE); // Nasce com idade máxima (adulto)
        }

        // Atualiza os atributos e a hitbox imediatamente após definir a idade
        this.updateAttributes();
        this.refreshDimensions();

        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnGroupData, pDataTag);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 15.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        // Prioridade 2: Reprodução. O "1.0D" é a velocidade com que ele se move para o parceiro.
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D)); 
        // Prioridade 3: Seguir o jogador com carne. O "1.2D" é a velocidade.
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.2D, Ingredient.of(Items.BEEF), false));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
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

    public int getDinoAge() {
        return this.entityData.get(DATA_AGE);
    }

    public void setDinoAge(int age) {
        // Garante que a idade nunca passe do máximo
        this.entityData.set(DATA_AGE, Math.min(age, MAX_AGE));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide()) {
            this.growthDelay++;
            if (this.growthDelay > 20) {
                this.growthDelay = 0;
                int currentAge = this.getDinoAge();
                if (currentAge < MAX_AGE) {
                    this.setDinoAge(currentAge + 20);
                    updateAttributes();
                }
            }
        }
        this.refreshDimensions();
    }

    public float getScale() {
        return 0.05F + ((float)this.getDinoAge() / (float)MAX_AGE) * 0.95F;
    }

    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        // 1. Pega a escala visual calculada (que pode ser a minúscula 0.05F)
        float visualScale = this.getScale();

        // 2. Define uma escala MÍNIMA segura APENAS para a hitbox. 0.1F (10%) é um bom começo.
        float physicalScale = Math.max(visualScale, 1.0F);

        // 3. Aplica essa escala física segura à hitbox.
        return super.getDimensions(pPose).scale(physicalScale);
    }

    private void updateAttributes() {
        double scalePercent = (double)this.getDinoAge() / MAX_AGE;
        // Vida base de 20 (filhote) indo até 100 (adulto)
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0D + (scalePercent * 80.0D));
        // Dano base de 3 (filhote) indo até 15 (adulto)
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(3.0D + (scalePercent * 12.0D));

        // Se a entidade já tiver vida, ajusta a vida atual para não morrer ou se curar totalmente ao crescer
        if(this.getHealth() > this.getMaxHealth()){
            this.setHealth(this.getMaxHealth());
        }
    }

    // --- IMPLEMENTAÇÃO DA REPRODUÇÃO ---
    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        // Cria um novo RexEntity. O jogo automaticamente chamará finalizeSpawn nele com o motivo "BREEDING".
        return ModEntities.REX.get().create(pLevel);
    }
    
    @Override
    public boolean isBaby() {
        // Continua útil para checagens do jogo. Consideramos "bebê" até atingir a idade máxima.
        return this.getDinoAge() < MAX_AGE;
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        // Retorna 'true' se o item for carne de vaca crua (beef).
        return pStack.is(Items.BEEF);
    }

    // --- LÓGICA DA GECKOLIB (sem alterações) ---
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private <E extends GeoEntity> PlayState predicate(AnimationState<E> event) {
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