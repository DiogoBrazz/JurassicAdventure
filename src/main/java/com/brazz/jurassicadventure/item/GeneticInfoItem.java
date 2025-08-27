package com.brazz.jurassicadventure.item;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GeneticInfoItem extends Item {
    public GeneticInfoItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents,
            TooltipFlag pIsAdvanced) {
        if (pStack.hasTag()) {
            CompoundTag nbt = pStack.getTag();

            String dinoIdString = nbt.getString("dino_type");
            if (!dinoIdString.isEmpty()) {
                // Ex: "dino.jurassicadventure.t_rex"
                String translationKey = "dino." + dinoIdString.replace(":", ".");
                pTooltipComponents.add(Component.translatable(translationKey).withStyle(ChatFormatting.GREEN));

            } else if (nbt.contains("mob_type")) {
                String mobTypeString = nbt.getString("mob_type");
                if (!mobTypeString.isEmpty()) {
                    // Ex: "mob.minecraft.zombie"
                    String translationKey = "mob." + mobTypeString.replace(":", ".");
                    pTooltipComponents.add(Component.translatable(translationKey).withStyle(ChatFormatting.YELLOW));
                }
            }
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

}