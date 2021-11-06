package raz.showcase;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raz.showcase.setup.ClientSetup;
import raz.showcase.setup.ModSetup;
import raz.showcase.setup.Registration;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Showcase.MOD_ID)
public class Showcase {

    public static final String MOD_ID = "showcase";
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public Showcase() {

        Registration.init();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModSetup::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
    }

}
