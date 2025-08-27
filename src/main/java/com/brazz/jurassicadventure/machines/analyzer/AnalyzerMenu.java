package com.brazz.jurassicadventure.machines.analyzer;

import org.jetbrains.annotations.NotNull;
import com.brazz.jurassicadventure.ModBlocks;
import com.brazz.jurassicadventure.ModItems;
import com.brazz.jurassicadventure.ModMenuTypes;
import com.brazz.jurassicadventure.ModTags;
import com.brazz.jurassicadventure.machines.allsettingsmachine.AllSettingsMenu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;

public class AnalyzerMenu extends AllSettingsMenu {
    public final AnalyzerBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    // Construtor para o lado do cliente
    public AnalyzerMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        // Agora, ele também cria um ContainerData para corresponder ao construtor
        // principal.
        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()),
                new SimpleContainerData(2));
    }

    @Override
    protected int getMachineInventorySlotCount() {
        return 8; //IMPORTANTE, COLOCAR O NUMERO DE SLOTS DA MÁQUINA
    }

    // Construtor para o lado do servidor
    public AnalyzerMenu(int pContainerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.ANALYZER_MENU.get(), pContainerId);
        checkContainerSize(inv, this.getMachineInventorySlotCount());
        this.blockEntity = (AnalyzerBlockEntity) entity;
        this.level = inv.player.level();
        this.data = data;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        // --- Configuração dos Slots ---
        this.addSlot(new SlotItemHandler(this.blockEntity.getItemHandler(), 0, 80, 11) {
        @Override
        public boolean mayPlace(@NotNull ItemStack stack) {
            return stack.is(ModTags.Items.ANALYZABLE_ITEMS);
            }
        });

        // Adiciona o slot de FERRAMENTA para a Seringa (com restrição)
        this.addSlot(new SlotItemHandler(this.blockEntity.getItemHandler(), 1, 144, 11) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.getItem() == ModItems.SYRINGE.get();
            }
        });

        // Slots de Saída (agora usam a nossa classe OutputSlot)
        this.addSlot(new OutputSlot(this.blockEntity.getItemHandler(), 2, 25, 58));
        this.addSlot(new OutputSlot(this.blockEntity.getItemHandler(), 3, 47, 58));
        this.addSlot(new OutputSlot(this.blockEntity.getItemHandler(), 4, 69, 58));
        this.addSlot(new OutputSlot(this.blockEntity.getItemHandler(), 5, 91, 58));
        this.addSlot(new OutputSlot(this.blockEntity.getItemHandler(), 6, 113, 58));
        this.addSlot(new OutputSlot(this.blockEntity.getItemHandler(), 7, 135, 58));

        addDataSlots(data);
    }

    // Métodos para a barra de progresso
    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public int getScaledProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);
        int progressArrowSize = 28; // Mude este valor para a largura da sua seta de progresso em pixels
        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                pPlayer, ModBlocks.ANALYZER.get());
    }
}