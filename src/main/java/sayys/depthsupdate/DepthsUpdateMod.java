package sayys.depthsupdate;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jspecify.annotations.NonNull;

import sayys.depthsupdate.client.AssetHandler;
import sayys.depthsupdate.command.ScanCommand;
import sayys.depthsupdate.compat.FluidloggedCompat;
import sayys.depthsupdate.proxy.IProxy;
import sayys.depthsupdate.registry.RegistryHandler;
import sayys.depthsupdate.world.generation.AmethystGeodeGenerator;
import sayys.depthsupdate.world.generation.DripstoneCavesGenerator;
import sayys.depthsupdate.world.generation.LushCavesGenerator;

@Mod(
    modid = DepthsUpdateMod.MOD_ID,
    name = DepthsUpdateMod.MOD_NAME,
    version = DepthsUpdateMod.VERSION,
    dependencies = "after:" + FluidloggedCompat.MOD_ID
)
public class DepthsUpdateMod {
    public static final Logger LOGGER = LogManager.getLogger(DepthsUpdateMod.MOD_NAME);
    public static final String MOD_ID = "depthsupdate";
    public static final String MOD_NAME = "Depths Update Lite";
    public static final String VERSION = "0.1.13";

    @SidedProxy(
        modId = DepthsUpdateMod.MOD_ID,
        clientSide = "sayys.depthsupdate.proxy.ClientProxy",
        serverSide = "sayys.depthsupdate.proxy.CommonProxy"
    )
    public static IProxy proxy;

    /**
     * <a href="https://cleanroommc.com/wiki/forge-mod-development/event#overview">
     * Take a look at how many FMLStateEvents you can listen to via
     * the @Mod.EventHandler annotation here
     * </a>
     */
    @Mod.EventHandler
    @SideOnly(Side.CLIENT)
    public void construct(@NonNull FMLConstructionEvent event) {
        AssetHandler.setup();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        RegistryHandler.init();

        LushCavesGenerator.register();
        DripstoneCavesGenerator.register();
        AmethystGeodeGenerator.register();
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new ScanCommand());
    }
}
