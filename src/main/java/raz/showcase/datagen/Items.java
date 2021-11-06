package raz.showcase.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import raz.showcase.Showcase;
import raz.showcase.setup.Registration;

public class Items extends ItemModelProvider {
    public Items(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Showcase.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent(Registration.DISPLAY_CASE_ITEM.get().getRegistryName().getPath(), new ResourceLocation(Showcase.MOD_ID, "block/display_case"));
        singleTexture(Registration.DISPLAY_LINKING_TOOL_ITEM.get().getRegistryName().getPath(), new ResourceLocation("item/handheld"), "layer0", new ResourceLocation("item/stick"));
        withExistingParent(Registration.CURATORS_WORKBENCH_ITEM.get().getRegistryName().getPath(), new ResourceLocation(Showcase.MOD_ID, "block/curators_workbench"));
        withExistingParent(Registration.WALL_MOUNT_ITEM.get().getRegistryName().getPath(), new ResourceLocation(Showcase.MOD_ID, "block/wall_mount"));
        singleTexture(Registration.SHEEP_MOB_TROPHY_ITEM.get().getRegistryName().getPath(), new ResourceLocation("item/handheld"), "layer0", new ResourceLocation("item/mutton"));
        singleTexture(Registration.PIG_MOB_TROPHY_ITEM.get().getRegistryName().getPath(), new ResourceLocation("item/handheld"), "layer0", new ResourceLocation("item/porkchop"));

    }
}
