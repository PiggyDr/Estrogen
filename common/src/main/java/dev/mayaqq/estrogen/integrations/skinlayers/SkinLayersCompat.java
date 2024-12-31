package dev.mayaqq.estrogen.integrations.skinlayers;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.tr7zw.skinlayers.api.LayerFeatureTransformerAPI;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;

/**
 * Compact class for 3d Skin layers mod
 * @Modid 3dskinlayers
 */
public class SkinLayersCompat {
    public static void getBoob(AbstractClientPlayer player, PoseStack poseStack, ModelPart modelPart) {
        // Yeah this is not it, I have no idea what I'm doing
        LayerFeatureTransformerAPI.getTransformer().transform(player, poseStack, modelPart);
    }
}
