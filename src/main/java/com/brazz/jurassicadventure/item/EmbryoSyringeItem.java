package com.brazz.jurassicadventure.item; // Verifique o seu package

import com.brazz.jurassicadventure.ModItems;
import com.brazz.jurassicadventure.common.capability.PregnancyProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

// A NOSSA SERINGA DE EMBRIÃO AGORA HERDA DA GeneticInfoItem!
// Isto dá-lhe automaticamente a tooltip customizada.
public class EmbryoSyringeItem extends GeneticInfoItem {
    public EmbryoSyringeItem(Properties pProperties) {
        super(pProperties);
    }

    // E aqui adicionamos a sua funcionalidade ÚNICA de clique direito.
    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
        if (!pStack.hasTag()) return InteractionResult.PASS;

        boolean isOviparous = pStack.getTag().getBoolean("is_oviparous");

        // Regra 1: Ovíparo só em Galinhas
        if (isOviparous && pInteractionTarget instanceof Chicken) {
            return startGestation(pStack, pPlayer, pInteractionTarget, pUsedHand);
        }

        // Regra 2: Mamífero só em Vacas
        if (!isOviparous && pInteractionTarget instanceof Cow) {
            return startGestation(pStack, pPlayer, pInteractionTarget, pUsedHand);
        }
        
        return InteractionResult.PASS;
    }

    // Método auxiliar para não repetir código
    @SuppressWarnings("removal")
    private InteractionResult startGestation(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
        pInteractionTarget.getCapability(PregnancyProvider.PREGNANCY_CAPABILITY).ifPresent(pregnancy -> {
            if (!pregnancy.isPregnant()) {
                String dinoTypeId = pStack.getTag().getString("dino_type");
                boolean isOviparous = pStack.getTag().getBoolean("is_oviparous");

                if (!dinoTypeId.isEmpty()) {
                    pregnancy.setPregnant(new ResourceLocation(dinoTypeId), 50, isOviparous);
                    pPlayer.setItemInHand(pUsedHand, new ItemStack(ModItems.SYRINGE.get()));
                }
            }
        });
        return InteractionResult.SUCCESS;
    }
}