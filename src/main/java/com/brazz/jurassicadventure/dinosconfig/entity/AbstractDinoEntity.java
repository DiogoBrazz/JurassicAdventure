package com.brazz.jurassicadventure.dinosconfig.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.entity.ai.attributes.Attributes;

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

import com.brazz.jurassicadventure.ModItems;
import com.brazz.jurassicadventure.util.DinoTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractDinoEntity extends Animal implements GeoEntity, IGrowingEntity {

    // --- 1. LÓGICA DE DADOS E CRESCIMENTO (COMUM A TODOS) ---
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final EntityDataAccessor<Integer> DATA_AGE = SynchedEntityData.defineId(AbstractDinoEntity.class, EntityDataSerializers.INT);
    private int growthDelay = 0;

    protected AbstractDinoEntity(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    // --- MÉTODOS ABSTRATOS (CADA DINO VAI FORNECER SEUS VALORES) ---
    public abstract int getMaxGrowthAge();
    protected abstract float getBabyWidth();
    protected abstract float getBabyHeight();
    protected abstract float getAdultWidth();
    protected abstract float getAdultHeight();

    public abstract double getBaseHealth(); // Vida inicial do bebê
    public abstract double getMaxHealthAttribute(); // Vida final do adulto
    public abstract double getBaseAttackDamage(); // Dano inicial do bebê
    public abstract double getMaxAttackDamage(); // Dano final do adulto

    // --- 2. MÉTODOS UNIVERSAIS DE CRESCIMENTO E HITBOX ---
    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        float growthProgress = (float)this.getDinoAge() / (float)this.getMaxGrowthAge();
        float currentWidth = getBabyWidth() + (getAdultWidth() - getBabyWidth()) * growthProgress;
        float currentHeight = getBabyHeight() + (getAdultHeight() - getBabyHeight()) * growthProgress;
        return EntityDimensions.scalable(currentWidth, currentHeight);
    }

    public int getDinoAge() {
        return this.entityData.get(DATA_AGE);
    }

    public void setDinoAge(int age) {
        this.entityData.set(DATA_AGE, Math.min(age, this.getMaxGrowthAge()));
    }

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
        // 1. ANTES de mudar qualquer coisa, anota a vida atual do dinossauro.
        float currentHealth = this.getHealth();

        // 2. Calcula a porcentagem de crescimento
        double growthPercent = (double)this.getDinoAge() / (double)this.getMaxGrowthAge();

        // 3. Calcula os novos valores de vida e dano usando os métodos abstratos
        double newMaxHealth = this.getBaseHealth() + (this.getMaxHealthAttribute() - this.getBaseHealth()) * growthPercent;
        double newAttackDamage = this.getBaseAttackDamage() + (this.getMaxAttackDamage() - this.getBaseAttackDamage()) * growthPercent;

        // 4. Aplica os novos valores. É AQUI que a "cura" indesejada acontece.
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(newMaxHealth);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(newAttackDamage);

        // 5. A CORREÇÃO: Força a vida a voltar para o valor que ela tinha antes da atualização.
        // O Minecraft garante que a vida não ultrapasse o novo máximo, então é seguro.
        this.setHealth(currentHealth);
    }

    // --- 3. LÓGICA UNIVERSAL DE SPAWN E DADOS ---
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnGroupData, @Nullable CompoundTag pDataTag) {
        if (pDataTag != null && pDataTag.contains("IsIncubatorBaby") && pDataTag.getBoolean("IsIncubatorBaby")) {
            this.setDinoAge(0);
        } else if (pReason == MobSpawnType.BREEDING) {
            this.setDinoAge(0);
        } else {
            this.setDinoAge(this.getMaxGrowthAge());
        }
        // this.updateAttributes(); // Opcional
        this.refreshDimensions();
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnGroupData, pDataTag);
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

    @Override
    public boolean isBaby() {
        return this.getDinoAge() < this.getMaxGrowthAge();
    }


    // --- 4. LÓGICA UNIVERSAL DA GECKOLIB ---
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    // Animações básicas que a maioria dos dinos terá
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

     // --- 5. LÓGICA UNIVERSAL DO BREED ---

     @Override
    public boolean canFallInLove() {
        // A condição para poder acasalar é:
        // 1. NÃO PODE ser um filhote (!this.isBaby())
        // 2. NÃO PODE já estar no cooldown de acasalamento (!this.isInLove())
        return !this.isBaby() && !this.isInLove();
    }

    @Override
    public void spawnChildFromBreeding(ServerLevel pLevel, Animal pOtherParent) {
        // 1. Pega o ID de registro do dinossauro atual (ex: "jurassicadventure:rex")
        ResourceLocation dinoID = EntityType.getKey(this.getType());

        // 2. Verifica se o dinossauro está na lista de Ovíparos
        if (DinoTypes.OVIPAROUS_DINOS.contains(dinoID)) {
            // --- LÓGICA PARA BOTAR OVO ---

            // a. Reseta o cooldown de acasalamento dos pais (lógica padrão do Minecraft)
            this.setAge(6000);
            pOtherParent.setAge(6000);
            this.resetLove();
            pOtherParent.resetLove();
            pLevel.broadcastEntityEvent(this, (byte)18); // Partícula de coração

            // b. Cria o item do ovo
            ItemStack eggItemStack = new ItemStack(ModItems.DINOSAUR_EGG.get()); // Certifique-se que o nome do seu item de ovo está correto

            // c. Cria e adiciona a NBT Tag com o ID do dinossauro
            CompoundTag nbtData = new CompoundTag();
            nbtData.putString("dino_type", dinoID.toString());
            eggItemStack.setTag(nbtData);
            
            // d. Cria a entidade do item a ser dropada no mundo
            ItemEntity eggEntity = new ItemEntity(pLevel, this.getX(), this.getY(), this.getZ(), eggItemStack);
            eggEntity.setPickUpDelay(10); // Pequeno delay para o jogador poder pegar

            // e. Dropa o ovo no mundo
            pLevel.addFreshEntity(eggEntity);

        } 
        // 3. Se não for ovíparo, verifica se é um mamífero
        else if (DinoTypes.MAMIFEROS_DINOS.contains(dinoID)) {
            // --- LÓGICA PARA NASCIMENTO VIVO (PADRÃO) ---
            // Aqui usamos a lógica original do Minecraft
            super.spawnChildFromBreeding(pLevel, pOtherParent);
            
        } 
        // 4. Caso de segurança: se não estiver em nenhuma lista, usa o padrão
        else {
            super.spawnChildFromBreeding(pLevel, pOtherParent);
        }
    }
}