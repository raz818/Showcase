package raz.showcase.setup;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import raz.showcase.Showcase;

@Mod.EventBusSubscriber(modid = Showcase.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModSetup {

    public static final ItemGroup ITEM_GROUP = new ItemGroup("showcase") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Registration.DISPLAY_CASE.get());
        }
    };

    public static void init(final FMLCommonSetupEvent event) {

    }
}
