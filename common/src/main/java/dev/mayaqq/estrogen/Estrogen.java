package dev.mayaqq.estrogen;

import dev.mayaqq.estrogen.networking.EstrogenNetworkManager;
import dev.mayaqq.estrogen.registry.*;
import earth.terrarium.botarium.util.CommonHooks;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uwu.serenity.critter.RegistryManager;

public class Estrogen {
    public static final String MOD_ID = "estrogen";

    public static final Logger LOGGER = LoggerFactory.getLogger("Estrogen");

    public static final RegistryManager REGISTRIES = RegistryManager.create(MOD_ID);

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static final String mcCapesMessage = """
            ----------------------------------------------------------------------------
            Minecraft Capes is detected! This mod currently causes some features
            of Estrogen to not work properly, before making an issue, please make sure
            to first update and disable Minecraft Capes and see if the issue persists.
            ----------------------------------------------------------------------------
            """;

    public static void init() {
        if (CommonHooks.isModLoaded("minecraftcapes")) {
            for (String line : mcCapesMessage.split("\n")) {
                LOGGER.error("[ESTROGEN] {}", line);
            }
        }
        // Init all the different classes
        EstrogenAttributes.init();
        EstrogenEntities.ENTITIES.register();
        EstrogenFluids.FLUIDS.register();
        EstrogenSounds.SOUNDS.register();
        EstrogenBlocks.BLOCKS.register();
        EstrogenBlockEntities.BLOCK_ENTITIES.register();
        EstrogenEffects.MOB_EFFECTS.register();
        EstrogenPotions.POTIONS.register();
        EstrogenEnchantments.ENCHANTMENTS.register();
        EstrogenItems.ITEMS.register();
        // Recipes need to be registered before completing the recipe registers
        EstrogenRecipes.RECIPE_TYPES.register();
        EstrogenRecipes.RECIPE_SERIALIZERS.register();
        EstrogenAdvancementCriteria.CRITERIAS.init();
        EstrogenParticles.PARTICLES.register();
        EstrogenCreativeTab.TAB.register();
        EstrogenNetworkManager.NETWORK_MANAGER.init();

        LOGGER.info("Injecting Estrogen into your veins!");
    }
}