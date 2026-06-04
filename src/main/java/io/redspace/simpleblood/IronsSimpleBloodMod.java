package io.redspace.simpleblood;

import com.mojang.logging.LogUtils;
import io.redspace.simpleblood.registry.ParticleRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import io.redspace.simpleblood.client.ClientEvents;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

@Mod(IronsSimpleBloodMod.MODID)
public class IronsSimpleBloodMod {
    public static final float NEW_POWER_PER_LEVEL = .5f * 3;
    public static final float DAMAGE_PER_PIERCING = 1f;

    public static final String MODID = "simpleblood";
    public static final Logger LOGGER = LogUtils.getLogger();

    public IronsSimpleBloodMod(IEventBus modEventBus) {
        modEventBus.addListener(this::commonSetup);
        ParticleRegistry.register(modEventBus);
        if (FMLEnvironment.dist.isClient()) {
            modEventBus.addListener(ClientEvents::registerParticles);
        }
        NeoForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }
}
