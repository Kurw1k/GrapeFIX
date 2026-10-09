package org.Kurw1k.grapefix;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@Mod(Grapefix.MODID)
public class Grapefix {
    public static final String MODID = "grapefix";

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final Supplier<CropBlock> GRAPE_CROP = BLOCKS.register("grape_crop",
            () -> new GrapeCropBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .noCollission()
                    .noOcclusion()
                    .randomTicks()
                    .destroyTime(0.0f)
                    .sound(SoundType.CROP))
    );

    public static final Supplier<Item> GRAPE = ITEMS.register("grape",
            () -> new ItemNameBlockItem(GRAPE_CROP.get(), new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(2)
                            .saturationModifier(0.4f)
                            .build())
            )
    );

    public Grapefix(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);

        modEventBus.addListener(this::onClientSetup);
        modEventBus.addListener(this::addCreative);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        // Проверяем, что это креативная вкладка из мода Create: Alcoholic Beverages
        if (event.getTabKey().location().equals(ResourceLocation.fromNamespaceAndPath("create_alcoholic_beverages", "alcoholic_beverages"))) {
            event.accept(GRAPE.get());
        }
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(GRAPE_CROP.get(), RenderType.cutout());
        });
    }
}