package raz.showcase.setup;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import raz.showcase.Showcase;
import raz.showcase.blocks.curatorsworkbench.CuratorsWorkbenchContainer;
import raz.showcase.blocks.curatorsworkbench.CuratorsWorkbenchScreen;
import raz.showcase.blocks.wallmount.WallMountTileEntity;
import raz.showcase.client.DisplayCaseTileEntityRenderer;
import raz.showcase.client.WallMountTileEntityRenderer;
import raz.showcase.gui.DisplayCaseScreen;

@Mod.EventBusSubscriber(modid = Showcase.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    public static void init(final FMLClientSetupEvent event) {
        ScreenManager.registerFactory(Registration.DISPLAY_CASE_CONTAINER.get(), DisplayCaseScreen::new);
        ScreenManager.registerFactory(Registration.CURATORS_WORKBENCH_CONTAINER.get(), CuratorsWorkbenchScreen::new);

        RenderTypeLookup.setRenderLayer(Registration.DISPLAY_CASE.get(), RenderType.getCutout());

        DisplayCaseTileEntityRenderer.register();

        RenderTypeLookup.setRenderLayer(Registration.WALL_MOUNT.get(), RenderType.getCutout());
        WallMountTileEntityRenderer.register();
    }
}
