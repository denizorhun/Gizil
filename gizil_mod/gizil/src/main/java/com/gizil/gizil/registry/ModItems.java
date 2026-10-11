package com.gizil.gizil.registry;


import com.gizil.gizil.item.*;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import com.gizil.gizil.item.MildExtractItem;
import com.gizil.gizil.item.StrongExtractItem;
import com.gizil.gizil.item.UnstableMixtureItem;
import com.gizil.gizil.item.AlchemyInkItem;


import com.gizil.gizil.Gizil;
import com.gizil.gizil.registry.ModBlocks;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Gizil.MODID);

    public static final RegistryObject<Item> FLOWER_BOOTS = ITEMS.register("flower_boots",
            () -> new FlowerBootsItem());

    public static final RegistryObject<Item> BEE_HELMET = ITEMS.register("bee_helmet",
            () -> new BeeHelmetItem());

    public static final RegistryObject<Item> HARMONY_DRESS = ITEMS.register("harmony_dress",
            () -> new HarmonyDressItem());

    public static final RegistryObject<Item> CRACKED_BEE_HELMET = ITEMS.register("cracked_bee_helmet",
            () -> new CrackedBeeHelmetItem());

    public static final RegistryObject<Item> CRACKED_HARMONY_DRESS = ITEMS.register("cracked_harmony_dress",
            () -> new CrackedHarmonyDressItem());

    public static final RegistryObject<Item> CRACKED_FLOWER_BOOTS = ITEMS.register("cracked_flower_boots",
            () -> new CrackedFlowerBootsItem());

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Gizil.MODID);

    public static final RegistryObject<Item> MILD_EXTRACT = ITEMS.register("mild_extract",
            () -> new MildExtractItem());

    public static final RegistryObject<Item> STRONG_EXTRACT = ITEMS.register("strong_extract",
            () -> new StrongExtractItem());

    public static final RegistryObject<Item> UNSTABLE_MIXTURE = ITEMS.register("unstable_mixture",
            () -> new UnstableMixtureItem());


    public static final RegistryObject<Item> ALCHEMY_INK = ITEMS.register("alchemy_ink",
            () -> new AlchemyInkItem());

    public static final RegistryObject<CreativeModeTab> GIZIL_TAB = CREATIVE_TABS.register("gizil_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.literal("Gizil")
                            .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x88E57A))))
                    .icon(() -> FLOWER_BOOTS.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(FLOWER_BOOTS.get());
                        output.accept(BEE_HELMET.get());
                        output.accept(HARMONY_DRESS.get());
                        output.accept(CRACKED_BEE_HELMET.get());
                        output.accept(CRACKED_HARMONY_DRESS.get());
                        output.accept(CRACKED_FLOWER_BOOTS.get());
                        output.accept(ModBlocks.RESEARCH_TABLE_ITEM.get());
                        output.accept(ALCHEMY_INK.get());
                        output.accept(ModBlocks.SYNTHESIS_TABLE_ITEM.get());
                        output.accept(MILD_EXTRACT.get());
                        output.accept(STRONG_EXTRACT.get());
                        output.accept(UNSTABLE_MIXTURE.get());
                        output.accept(ModBlocks.ALCHEMY_TABLE_ITEM.get());
                    })
                    .build());

}
