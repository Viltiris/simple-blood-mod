package io.redspace.simpleblood;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

public class Utils {

    public static float damageFromPowerEnchantment(ItemStack itemStack, HolderLookup.Provider registryAccess, Holder<Enchantment> powerEnchantment) {
        var power = EnchantmentHelper.getItemEnchantmentLevel(powerEnchantment, itemStack);
        return (power > 0 ? (power + 1) * IronsSimpleBloodMod.NEW_POWER_PER_LEVEL : 0);
    }
//
//    public static float damageFromPowerEnchantment(ItemStack itemStack, HolderLookup.Provider registryAccess) {
//        return damageFromPowerEnchantment(itemStack, registryAccess, registryAccess.lookupOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.POWER));
//    }
}
