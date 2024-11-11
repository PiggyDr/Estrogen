package dev.mayaqq.estrogen.fabric.integrations.catalogue;

import dev.mayaqq.estrogen.Estrogen;
import dev.mayaqq.estrogen.client.config.EstrogenConfigScreen;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.gui.screens.Screen;

public class CatalogueCompat {
    public static Screen createConfigScreen(Screen currentScreen, ModContainer container) {
        return new EstrogenConfigScreen(currentScreen, Estrogen.MOD_ID);
    }
}
