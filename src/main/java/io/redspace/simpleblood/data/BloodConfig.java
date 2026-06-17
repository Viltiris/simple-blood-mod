package io.redspace.simpleblood.data;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.function.Function;

public record BloodConfig(TagKey<EntityType<?>> entityTag, int color, float minDamage, float damageIntensityMultiplier,
                          float baseSpeed, float speedPerParticle, boolean isGraphic) {
    private static final Codec<Integer> HEX_COLOR_CODEC = Codec.STRING.flatXmap(
            str -> {
                String hex = str.startsWith("#") ? str.substring(1) : str;
                if (hex.length() != 6 && hex.length() != 8) {
                    return DataResult.error(() -> "Hex color must be 6 or 8 digits: " + str);
                }
                try {
                    long value = Long.parseLong(hex, 16);
                    if (hex.length() == 6) {
                        value = 0xFF000000L | value;
                    }
                    return DataResult.success((int) value);
                } catch (NumberFormatException exception) {
                    return DataResult.error(() -> "Invalid hex color: " + str);
                }
            },
            color -> DataResult.success(String.format("#%06X", color & 0xFFFFFF))
    );

    public static final Codec<Integer> COLOR_CODEC = Codec.either(
            Codec.INT,
            HEX_COLOR_CODEC
    ).xmap(
            either -> either.map(Function.identity(), Function.identity()),
            Either::left
    );

    public static final Codec<BloodConfig> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    TagKey.hashedCodec(Registries.ENTITY_TYPE).fieldOf("entity_tag").forGetter(BloodConfig::entityTag),
                    COLOR_CODEC.fieldOf("color").forGetter(BloodConfig::color),
                    Codec.FLOAT.optionalFieldOf("min_damage", 3.0f).forGetter(BloodConfig::minDamage),
                    Codec.FLOAT.optionalFieldOf("damage_intensity_multiplier", 1.0f / 12f).forGetter(BloodConfig::damageIntensityMultiplier),
                    Codec.FLOAT.optionalFieldOf("base_speed", 9.0f).forGetter(BloodConfig::baseSpeed),
                    Codec.FLOAT.optionalFieldOf("speed_per_particle", 0.5f).forGetter(BloodConfig::speedPerParticle),
                    Codec.BOOL.optionalFieldOf("is_graphic", true).forGetter(BloodConfig::isGraphic)
            ).apply(instance, BloodConfig::new)
    );

    public float maxDamage() {
        return 1f / damageIntensityMultiplier;
    }

    public float scaledBaseSpeed() {
        return baseSpeed * 0.01f;
    }

    public float scaledSpeedPerParticle() {
        return speedPerParticle * 0.01f;
    }
}
