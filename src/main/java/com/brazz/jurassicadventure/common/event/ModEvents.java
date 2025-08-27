package com.brazz.jurassicadventure.common.event;

import com.brazz.jurassicadventure.JurassicAdventure;
import com.brazz.jurassicadventure.ModItems;
import com.brazz.jurassicadventure.common.capability.PregnancyProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = JurassicAdventure.MODID)
public class ModEvents {

    /**
     * Este evento é chamado sempre que uma entidade é criada no mundo.
     * Nós usamo-lo para "prender" a nossa "mochila de dados" de gestação
     * a cada Galinha e Vaca.
     */
    @SuppressWarnings("removal")
    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        // Verifica se a entidade é uma Galinha ou uma Vaca
        if (event.getObject() instanceof Chicken || event.getObject() instanceof Cow) {
            // Se for, anexa a nossa "mochila" (PregnancyProvider) a ela.
            event.addCapability(new ResourceLocation(JurassicAdventure.MODID, "pregnancy"), new PregnancyProvider());
        }
    }

    /**
     * Este evento é o nosso "relógio". Ele é chamado 20 vezes por segundo para cada
     * entidade viva no mundo. Nós usamo-lo para contar o tempo da gestação.
     */
    @SuppressWarnings("removal")
    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        // Garante que a lógica só corre no lado do servidor, que é quem controla o jogo.
        if (event.getEntity().level().isClientSide()) {
            return;
        }

        // Tenta pegar na "mochila" de gestação da entidade.
        event.getEntity().getCapability(PregnancyProvider.PREGNANCY_CAPABILITY).ifPresent(pregnancy -> {
            // Se a entidade estiver grávida...
            if (pregnancy.isPregnant()) {
                // ...diminui o temporizador.
                pregnancy.tickDown();

                // Se o temporizador chegou a zero...
                if (pregnancy.getGestationTicks() <= 0) {
                    LivingEntity parent = event.getEntity();
                    ResourceLocation dinoType = pregnancy.getDinoType();
                    boolean isOviparous = pregnancy.isOviparous();

                    // CASO 1: A CRIATURA É OVÍPARA (põe um ovo)
                    if (isOviparous) {
                        // Cria o nosso item de ovo genérico
                        ItemStack dinoEgg = new ItemStack(ModItems.DINOSAUR_EGG.get());
                        
                        // Adiciona as etiquetas NBT ao ovo para que ele "saiba" de quem ele é
                        dinoEgg.getOrCreateTag().putString("dino_type", dinoType.toString());
                        dinoEgg.getOrCreateTag().putBoolean("is_oviparous", true);
                        
                        // Dropa o ovo no mundo, na posição da galinha
                        parent.spawnAtLocation(dinoEgg);
                    }
                    
                    // CASO 2: A CRIATURA É MAMÍFERO (parto)
                    else {
                        // Encontra o tipo de entidade do nosso mamífero pré-histórico
                        EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(dinoType);
                        
                        if (entityType != null) {
                            // Cria a entidade bebé no mundo
                            Entity babyDino = entityType.create(parent.level());
                            if (babyDino instanceof AgeableMob) {
                                ((AgeableMob) babyDino).setBaby(true); // Garante que nasce como filhote
                            }
                            babyDino.setPos(parent.getX(), parent.getY(), parent.getZ());
                            parent.level().addFreshEntity(babyDino);
                        }
                    }

                    // "Esvazia" a mochila, terminando a gestação.
                    pregnancy.setPregnant(new ResourceLocation("empty"), 0, false);
                }
            }
        });
    }
}