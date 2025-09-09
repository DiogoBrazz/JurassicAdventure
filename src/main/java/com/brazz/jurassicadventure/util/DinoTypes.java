package com.brazz.jurassicadventure.util;

import net.minecraft.resources.ResourceLocation;
import java.util.List;

import com.brazz.jurassicadventure.JurassicAdventure; 

public class DinoTypes {
    // Carnivoros terrestres
    @SuppressWarnings("removal")
public static final ResourceLocation REX = new ResourceLocation(JurassicAdventure.MODID, "rex");
    @SuppressWarnings("removal")
public static final ResourceLocation VELOCIRAPTOR = new ResourceLocation(JurassicAdventure.MODID, "velociraptor");
    @SuppressWarnings("removal")
public static final ResourceLocation CARNOTAURUS = new ResourceLocation(JurassicAdventure.MODID, "carnotaurus");
    @SuppressWarnings("removal")
public static final ResourceLocation SPINOSAURUS = new ResourceLocation(JurassicAdventure.MODID, "spinosaurus");
    @SuppressWarnings("removal")
public static final ResourceLocation ALLOSAURUS = new ResourceLocation(JurassicAdventure.MODID, "allosaurus");
    @SuppressWarnings("removal")
public static final ResourceLocation YUTYRANNUS = new ResourceLocation(JurassicAdventure.MODID, "yutyrannus");
    @SuppressWarnings("removal")
public static final ResourceLocation DILOPHOSAURUS = new ResourceLocation(JurassicAdventure.MODID, "dilophosaurus");
    @SuppressWarnings("removal")
public static final ResourceLocation MEGALANIA = new ResourceLocation(JurassicAdventure.MODID, "megalania");
    @SuppressWarnings("removal")
public static final ResourceLocation DEINONYCHUS = new ResourceLocation(JurassicAdventure.MODID, "deinonychus");
    
    
    //Herbivoros terrestres
    @SuppressWarnings("removal")
public static final ResourceLocation TRICERATOPS = new ResourceLocation(JurassicAdventure.MODID, "triceratops");
    @SuppressWarnings("removal")
public static final ResourceLocation PARASAUROLOPHUS = new ResourceLocation(JurassicAdventure.MODID, "parasaurolophus");
    @SuppressWarnings("removal")
public static final ResourceLocation STEGOSAURUS = new ResourceLocation(JurassicAdventure.MODID, "stegosaurus");
    @SuppressWarnings("removal")
public static final ResourceLocation ANKYLOSAURUS = new ResourceLocation(JurassicAdventure.MODID, "ankylosaurus");
    @SuppressWarnings("removal")
public static final ResourceLocation GALLIMIMUS = new ResourceLocation(JurassicAdventure.MODID, "gallimimus");
    @SuppressWarnings("removal")
public static final ResourceLocation PACHYCEPHALOSAURUS = new ResourceLocation(JurassicAdventure.MODID, "pachycephalosaurus");
    @SuppressWarnings("removal")
public static final ResourceLocation BRACHIOSAURUS = new ResourceLocation(JurassicAdventure.MODID, "brachiosaurus");
    @SuppressWarnings("removal")
public static final ResourceLocation DIPLODOCUS = new ResourceLocation(JurassicAdventure.MODID, "diplodocus");


//Aquáticos
    @SuppressWarnings("removal")
public static final ResourceLocation MOSASSAURO = new ResourceLocation(JurassicAdventure.MODID, "mosassauro");

    @SuppressWarnings("removal")
public static final ResourceLocation MAMUTE = new ResourceLocation(JurassicAdventure.MODID, "mamute");
    // --- LISTA CENTRAL ---
    // Esta lista contém todas as constantes de dinossauros que definimos acima.
    public static final List<ResourceLocation> ALL_DINOS = List.of(
            REX, TRICERATOPS, VELOCIRAPTOR, CARNOTAURUS, SPINOSAURUS, ALLOSAURUS, YUTYRANNUS, DILOPHOSAURUS, MEGALANIA, DEINONYCHUS,
            PARASAUROLOPHUS, STEGOSAURUS, ANKYLOSAURUS, GALLIMIMUS, PACHYCEPHALOSAURUS, BRACHIOSAURUS, DIPLODOCUS, MOSASSAURO, MAMUTE
            // Se você adicionar um novo dino acima, adicione-o aqui também!
    );

    public static final List<ResourceLocation> OVIPAROUS_DINOS = List.of(
            REX, TRICERATOPS, VELOCIRAPTOR, CARNOTAURUS, SPINOSAURUS, ALLOSAURUS, YUTYRANNUS, DILOPHOSAURUS, MEGALANIA, DEINONYCHUS,
            PARASAUROLOPHUS, STEGOSAURUS, ANKYLOSAURUS, GALLIMIMUS, PACHYCEPHALOSAURUS, BRACHIOSAURUS, DIPLODOCUS, MOSASSAURO
    );

    public static final List<ResourceLocation> MAMIFEROS_DINOS = List.of(
            MAMUTE
    );
}