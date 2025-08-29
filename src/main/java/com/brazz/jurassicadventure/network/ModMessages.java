package com.brazz.jurassicadventure.network;

import com.brazz.jurassicadventure.JurassicAdventure;
import com.brazz.jurassicadventure.network.packets.HatchEggPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    private static SimpleChannel INSTANCE;
    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(JurassicAdventure.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        // Registra nossa mensagem
        net.messageBuilder(HatchEggPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(HatchEggPacket::new)
                .encoder(HatchEggPacket::toBytes)
                .consumerMainThread(HatchEggPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.send(PacketDistributor.SERVER.noArg(), message);
    }
}