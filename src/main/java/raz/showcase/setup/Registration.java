package raz.showcase.setup;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import raz.showcase.blocks.curatorsworkbench.CuratorsWorkbenchBlock;
import raz.showcase.blocks.curatorsworkbench.CuratorsWorkbenchContainer;
import raz.showcase.blocks.curatorsworkbench.CuratorsWorkbenchTileEntity;
import raz.showcase.blocks.displaycase.DisplayCaseBlock;
import raz.showcase.blocks.displaycase.DisplayCaseContainer;
import raz.showcase.blocks.displaycase.DisplayCaseTileEntity;
import raz.showcase.blocks.wallmount.WallMountBlock;
import raz.showcase.blocks.wallmount.WallMountTileEntity;
import raz.showcase.items.DisplayLinkingToolItem;
import raz.showcase.items.MobTrophyItem;

import static raz.showcase.Showcase.MOD_ID;

public class Registration {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    private static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MOD_ID);
    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MOD_ID);

    public static void init() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<DisplayCaseBlock> DISPLAY_CASE = BLOCKS.register("display_case", DisplayCaseBlock::new);
    public static final RegistryObject<Item> DISPLAY_CASE_ITEM = ITEMS.register("display_case", () -> new BlockItem(DISPLAY_CASE.get(), new Item.Properties().group(ModSetup.ITEM_GROUP)));
    public static final RegistryObject<TileEntityType<DisplayCaseTileEntity>> DISPLAY_CASE_TILE_ENTITY = TILE_ENTITIES.register("display_case", () -> TileEntityType.Builder.create(DisplayCaseTileEntity::new, DISPLAY_CASE.get()).build(null));

    public static final RegistryObject<ContainerType<DisplayCaseContainer>> DISPLAY_CASE_CONTAINER = CONTAINERS.register("display_case", () -> IForgeContainerType.create(DisplayCaseContainer::new));

    public static final RegistryObject<CuratorsWorkbenchBlock> CURATORS_WORKBENCH = BLOCKS.register("curators_workbench", CuratorsWorkbenchBlock::new);
    public static final RegistryObject<Item> CURATORS_WORKBENCH_ITEM = ITEMS.register("curators_workbench", () -> new BlockItem(CURATORS_WORKBENCH.get(), new Item.Properties().group(ModSetup.ITEM_GROUP)));
    public static final RegistryObject<TileEntityType<CuratorsWorkbenchTileEntity>> CURATORS_WORKBENCH_TILE_ENTITY = TILE_ENTITIES.register("curators_workbench", () -> TileEntityType.Builder.create(CuratorsWorkbenchTileEntity::new, CURATORS_WORKBENCH.get()).build(null));

    public static final RegistryObject<ContainerType<CuratorsWorkbenchContainer>> CURATORS_WORKBENCH_CONTAINER = CONTAINERS.register("curators_workbench", () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntityWorld();
        return new CuratorsWorkbenchContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<Item> DISPLAY_LINKING_TOOL_ITEM = ITEMS.register("display_linking_tool", () -> new DisplayLinkingToolItem(new Item.Properties().maxStackSize(1)
            .group(ModSetup.ITEM_GROUP))
    );

    public static final RegistryObject<WallMountBlock> WALL_MOUNT = BLOCKS.register("wall_mount", WallMountBlock::new);
    public static final RegistryObject<Item> WALL_MOUNT_ITEM = ITEMS.register("wall_mount", () -> new BlockItem(WALL_MOUNT.get(), new Item.Properties().group(ModSetup.ITEM_GROUP)));
    public static final RegistryObject<TileEntityType<WallMountTileEntity>> WALL_MOUNT_TILE_ENTITY = TILE_ENTITIES.register("wall_mount", () -> TileEntityType.Builder.create(WallMountTileEntity::new, WALL_MOUNT.get()).build(null));

    // MOB TROPHY ITEMS
    public static final RegistryObject<Item> SHEEP_MOB_TROPHY_ITEM = ITEMS.register("sheep_trophy", () -> new MobTrophyItem(EntityType.SHEEP, new Item.Properties().group(ModSetup.ITEM_GROUP)));
    public static final RegistryObject<Item> PIG_MOB_TROPHY_ITEM = ITEMS.register("pig_trophy", () -> new MobTrophyItem(EntityType.PIG, new Item.Properties().group(ModSetup.ITEM_GROUP)));

}
