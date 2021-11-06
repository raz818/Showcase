package raz.showcase.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IDataProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import raz.showcase.Showcase;
import raz.showcase.setup.Registration;

public class BlockStates extends BlockStateProvider {

    public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Showcase.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ResourceLocation txt = new ResourceLocation(Showcase.MOD_ID, "block/display_case");
        simpleBlock(Registration.DISPLAY_CASE.get(), models().cubeAll("display_case", new ResourceLocation("block/glass")));
        simpleBlock(Registration.CURATORS_WORKBENCH.get(), models().cubeAll("curators_workbench", new ResourceLocation("block/command_block_back")));
        simpleBlock(Registration.WALL_MOUNT.get(), models().getBuilder("wall_mount").texture("particle", new ResourceLocation("block/oak_planks")));
    }
}
