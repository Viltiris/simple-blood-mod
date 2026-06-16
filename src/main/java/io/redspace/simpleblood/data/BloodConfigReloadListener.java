package io.redspace.simpleblood.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import io.redspace.simpleblood.IronsSimpleBloodMod;
import io.redspace.simpleblood.registry.BloodRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@EventBusSubscriber
public class BloodConfigReloadListener extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    public static final String DIRECTORY = "blood_types";

    public BloodConfigReloadListener() {
        super(GSON, DIRECTORY);
    }

    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new BloodConfigReloadListener());
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager resourceManager, ProfilerFiller profiler) {
        DynamicOps<JsonElement> ops = this.makeConditionalOps();
        List<BloodConfig> configs = new ArrayList<>();

        for (Map.Entry<ResourceLocation, JsonElement> entry : resources.entrySet()) {
            BloodConfig.CODEC.parse(ops, entry.getValue())
                    .resultOrPartial(error -> IronsSimpleBloodMod.LOGGER.error("Failed to parse blood type {}: {}", entry.getKey(), error))
                    .ifPresent(configs::add);
        }

        BloodRegistry.load(configs);
        IronsSimpleBloodMod.LOGGER.info("Loaded {} blood type(s)", configs.size());
    }
}
