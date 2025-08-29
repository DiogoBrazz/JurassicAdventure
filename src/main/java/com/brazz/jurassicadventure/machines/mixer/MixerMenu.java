package com.brazz.jurassicadventure.machines.mixer;

import com.brazz.jurassicadventure.ModBlocks;
import com.brazz.jurassicadventure.ModItems;
import com.brazz.jurassicadventure.ModMenuTypes;
import com.brazz.jurassicadventure.ModTags; // << IMPORT ADICIONADO
import com.brazz.jurassicadventure.machines.allsettingsmachine.AllSettingsMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class MixerMenu extends AllSettingsMenu {

    public MixerMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(4));
    }

    // Construtor principal (Servidor)
    public MixerMenu(int pContainerId, Inventory inv, BlockEntity entity, ContainerData data) {
        // << CORRIGIDO: O número de slots (4) agora bate com o getMachineInventorySlotCount
        // E os valores da GUI são passados corretamente.
        super(ModMenuTypes.MIXER_MENU.get(), pContainerId, inv, entity, data, 4, 53, 42);

        // Chama os métodos da classe pai para adicionar o inventário do jogador
        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        // --- Configuração dos Slots ---
        this.addSlot(new SlotItemHandler(((MixerBlockEntity)entity).getItemHandler(), 0, 11, 30) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                // Usa o nosso novo Item Tag
                return stack.is(ModTags.Items.NATURAL_FOODS);
            }
        });

        this.addSlot(new SlotItemHandler(((MixerBlockEntity)entity).getItemHandler(), 1, 51, 30) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.getItem() == ModItems.SYRINGE.get();
            }
        });
        
        // Slot do balde de leite que também aceita um balde vazio para ser retirado
        this.addSlot(new SlotItemHandler(((MixerBlockEntity)entity).getItemHandler(), 2, 81, 30) {
             @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.getItem() == Items.MILK_BUCKET;
            }
        });

        this.addSlot(new OutputSlot(((MixerBlockEntity)entity).getItemHandler(), 3, 132, 43));
    }

    @Override
    protected int getMachineInventorySlotCount() {
        return 4; // Total de slots da máquina
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                pPlayer, ModBlocks.MIXER.get());
    }
}