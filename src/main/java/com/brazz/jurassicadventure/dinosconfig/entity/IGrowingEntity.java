package com.brazz.jurassicadventure.dinosconfig.entity;

public interface IGrowingEntity {
    // Qualquer classe que assinar este contrato DEVE ter estes dois métodos.
    int getDinoAge();
    int getMaxGrowthAge();
}