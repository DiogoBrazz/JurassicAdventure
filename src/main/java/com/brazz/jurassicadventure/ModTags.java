package com.brazz.jurassicadventure;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {
    public static class Items {
        // Cria uma referência à nossa tag para ser usada no código Java
        public static final TagKey<Item> ANALYZABLE_ITEMS = tag("analyzable_items");
        public static final TagKey<Item> DINO_DNAS = tag("dino_dnas");
        public static final TagKey<Item> MOB_DNAS = tag("mob_dnas");

        @SuppressWarnings("removal")
        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(JurassicAdventure.MODID, name));
        }
    }
}