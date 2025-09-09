package com.brazz.jurassicadventure.item;

import com.brazz.jurassicadventure.dinosconfig.entity.IGrowingEntity;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class LupaItem extends Item {

    public LupaItem(Properties pProperties) {
        super(pProperties);
    }

    // Este é o método que é chamado quando um jogador clica com o botão direito em uma entidade
    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {

        // A MUDANÇA: Agora verificamos se a entidade tem o "selo" IGrowingEntity
        if (pInteractionTarget instanceof IGrowingEntity dino) { // Funciona para TERRESTRES E AQUÁTICOS!

            if (pPlayer.level().isClientSide()) {
                int currentAge = dino.getDinoAge();
                int maxAge = dino.getMaxGrowthAge();

                // Evita divisão por zero se a idade máxima for 0 por algum motivo
                if (maxAge == 0) return InteractionResult.FAIL;

                float percentage = ((float) currentAge / (float) maxAge) * 100.0f;

                // Corrigido o erro de digitação para "Growth"
                Component message = Component.literal("Growth: " + (int) percentage + "%")
                                            .withStyle(ChatFormatting.GREEN);

                pPlayer.sendSystemMessage(message);
            }

            return InteractionResult.SUCCESS;
        }
        
        return InteractionResult.PASS; // PASS é melhor que super() para não interferir com outras interações
    }
}