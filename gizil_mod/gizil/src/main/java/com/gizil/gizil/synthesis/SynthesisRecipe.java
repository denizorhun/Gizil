package com.gizil.gizil.synthesis;

import net.minecraft.world.item.Item;

import com.gizil.gizil.registry.ModItems;

public class SynthesisRecipe {

    public enum Result {
        MILD_EXTRACT,
        STRONG_EXTRACT,
        UNSTABLE_MIXTURE
    }

    public static Result getResult(Item input1, Item input2) {
        if (!PlantChemistry.isKnownPlant(input1) || !PlantChemistry.isKnownPlant(input2)) {
            return null;
        }

        PlantCategory cat1 = PlantChemistry.getCategory(input1);
        PlantCategory cat2 = PlantChemistry.getCategory(input2);

        boolean hasToxin = cat1 == PlantCategory.TOXIN || cat2 == PlantCategory.TOXIN;
        boolean hasNeutralizer = cat1 == PlantCategory.NEUTRALIZER || cat2 == PlantCategory.NEUTRALIZER;
        boolean bothToxin = cat1 == PlantCategory.TOXIN && cat2 == PlantCategory.TOXIN;

        if (bothToxin) {
            return Result.STRONG_EXTRACT;
        }
        if (hasToxin && hasNeutralizer) {
            return Result.MILD_EXTRACT;
        }
        return Result.UNSTABLE_MIXTURE;
    }

    public static Item getResultItem(Result result) {
        return switch (result) {
            case MILD_EXTRACT -> ModItems.MILD_EXTRACT.get();
            case STRONG_EXTRACT -> ModItems.STRONG_EXTRACT.get();
            case UNSTABLE_MIXTURE -> ModItems.UNSTABLE_MIXTURE.get();
        };
    }
}