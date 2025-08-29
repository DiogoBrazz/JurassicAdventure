package com.brazz.jurassicadventure.network.packets;

import com.brazz.jurassicadventure.machines.incubator.IncubatorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class HatchEggPacket {
    private final BlockPos pos;

    public HatchEggPacket(BlockPos pos) {
        this.pos = pos;
    }

    public HatchEggPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                BlockEntity blockEntity = player.level().getBlockEntity(pos);
                if (blockEntity instanceof IncubatorBlockEntity incubator) {
                    incubator.hatch(); // Chama o método para chocar o ovo no servidor
                }
            }
        });
        return true;
    }
}