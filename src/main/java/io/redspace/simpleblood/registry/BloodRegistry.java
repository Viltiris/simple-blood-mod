package io.redspace.simpleblood.registry;

import io.redspace.simpleblood.data.BloodConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BloodRegistry {
    private static final List<BloodConfig> BLOOD_CONFIGS = new ArrayList<>();

    public static void register(BloodConfig config) {
        BLOOD_CONFIGS.add(config);
    }

    public static void load(List<BloodConfig> configs) {
        BLOOD_CONFIGS.clear();
        BLOOD_CONFIGS.addAll(configs);
    }

    public static List<BloodConfig> getAll() {
        return Collections.unmodifiableList(BLOOD_CONFIGS);
    }
}
