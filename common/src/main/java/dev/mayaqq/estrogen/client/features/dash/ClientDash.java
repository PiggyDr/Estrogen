package dev.mayaqq.estrogen.client.features.dash;

import dev.mayaqq.estrogen.client.registry.EstrogenKeybinds;
import dev.mayaqq.estrogen.config.EstrogenConfig;
import dev.mayaqq.estrogen.features.dash.CommonDash;
import dev.mayaqq.estrogen.networking.EstrogenNetworkManager;
import dev.mayaqq.estrogen.networking.messages.c2s.DashPacket;
import dev.mayaqq.estrogen.registry.EstrogenAttributes;
import dev.mayaqq.estrogen.registry.EstrogenEffects;
import dev.mayaqq.estrogen.registry.blocks.DreamBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ClientDash {

    private static final double DASH_SPEED = 1.0;
    private static final double DASH_END_SPEED = 0.4;
    private static final double HYPER_H_SPEED = 3.0;
    private static final double HYPER_V_SPEED = 0.5;
    private static final double SUPER_H_SPEED = 0.8;
    private static final double SUPER_V_SPEED = 1.0;
    private static final double BOUNCE_H_SPEED = 0.8;
    private static final double BOUNCE_V_SPEED = 1.5;

    private static boolean isOnCooldown = false;

    private static int groundCooldown = 0;
    private static int dashCooldown = 0;
    private static int dashes = 0;
    private static int dashLevel = 0;
    private static int extraParticleTicks = 0;

    private static Vec3 dashDirection = null;
    private static double dashXRot = 0.0;
    private static double dashDeltaModifier = 0.0;

    private static boolean willHyper = false;
    private static boolean willSuper = false;
    private static boolean willBounce = false;
    private static Direction bounceDirection = null;
    private static boolean doReverse = false;

    private static BlockPos lastPos = null;

    public static void tick() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        if (!player.hasEffect(EstrogenEffects.ESTROGEN_EFFECT.get())) {
            reset();
            return;
        }

        // Refresh number of dashes
        if (canRefresh(player) && groundCooldown == 0) {
            groundCooldown = 4;
            refresh(player);
        }

        groundCooldown--;
        if (groundCooldown < 0) groundCooldown = 0;

        // Hyper
        if (willHyper) {
            willHyper = false;
            Vec3 hyperMotion = new Vec3(
                    player.getLookAngle().x * HYPER_H_SPEED,
                    HYPER_V_SPEED,
                    player.getLookAngle().z * HYPER_H_SPEED
            );
            if (doReverse) {
                doReverse = false;
                hyperMotion = hyperMotion.multiply(-1, 1, -1);
            }
            player.setDeltaMovement(hyperMotion);
            dashCooldown = 0;
            extraParticleTicks = 2;
        }
        // Super
        if (willSuper) {
            willSuper = false;
            Vec3 superMotion = new Vec3(
                    player.getLookAngle().x * SUPER_H_SPEED,
                    SUPER_V_SPEED,
                    player.getLookAngle().z * SUPER_H_SPEED
            );
            if (doReverse) {
                doReverse = false;
                superMotion = superMotion.multiply(-1, 1, -1);
            }
            player.setDeltaMovement(superMotion);
            dashCooldown = 0;
            extraParticleTicks = 1;
        }
        // Wall Bounce
        if (willBounce) {
            willBounce = false;
            if (bounceDirection != null) {
                player.setDeltaMovement(bounceDirection.getStepX() * BOUNCE_H_SPEED, BOUNCE_V_SPEED, bounceDirection.getStepZ() * BOUNCE_H_SPEED);
            } else {
                player.setDeltaMovement(0, BOUNCE_V_SPEED, 0);
            }
            dashCooldown = 0;
            extraParticleTicks = 1;
        }

        // During Dash
        Dash:
        if (dashCooldown > 0) {
            dashCooldown--;
            extraParticleTicks = 0;

            // End Dash
            if (dashCooldown == 0) {
                CommonDash.removeDashing(player.getUUID());
                if (!player.isFallFlying()) {
                    player.setDeltaMovement(dashDirection.scale(DASH_END_SPEED).scale(dashDeltaModifier));
                }
                break Dash;
            }

            player.setDeltaMovement(dashDirection.scale(DASH_SPEED).scale(dashDeltaModifier));

            // Hyper and Super Detection
            Detect:
            if (Minecraft.getInstance().options.keyJump.isDown()) {
                if (Minecraft.getInstance().options.keyDown.isDown()) {
                    doReverse = true;
                }
                if (player.onGround()) {
                    if (dashXRot > 15 && dashXRot < 60) willHyper = true;
                    else if (dashXRot > 0 && dashXRot < 15) willSuper = true;
                    break Detect;
                }
                if (dashXRot < -60) {
                    for (Direction direction : Direction.Plane.HORIZONTAL) {
                        // change required distance from wall here
                        Vec3 vector = Vec3.atLowerCornerOf(direction.getNormal()).scale(0.25);
                        AABB aabb = player.getBoundingBox().expandTowards(vector);
                        if (player.level().noCollision(player, aabb)) continue;
                        bounceDirection = direction.getOpposite();
                        willBounce = true;
                        break Detect;
                    }
                }
            }

            // Dash particles
            if (player.blockPosition() != lastPos) {
                EstrogenNetworkManager.CHANNEL.sendToServer(new DashPacket(false, dashLevel));
            }
            lastPos = player.blockPosition();
        }

        isOnCooldown = dashCooldown > 0 || dashes == 0;

        if(extraParticleTicks > 0) {
            EstrogenNetworkManager.CHANNEL.sendToServer(new DashPacket(false, dashLevel));
            extraParticleTicks--;
        }

        // Here is when the dash happens
        if (EstrogenKeybinds.DASH_KEY.consumeClick() && !isOnCooldown()) {
            DreamBlock.lookAngle = null;
            CommonDash.setDashing(player.getUUID());

            // Set counter to duration of dash
            dashCooldown = 5;
            // Dash level of current dash (number of dashes at the beginning)
            dashLevel = dashes;
            // Decrement the dash counter
            dashes--;


            EstrogenNetworkManager.CHANNEL.sendToServer(new DashPacket(true, dashLevel));

            dashDirection = player.getLookAngle();
            dashXRot = player.getXRot();
            dashDeltaModifier = EstrogenConfig.server().dashDeltaModifier.get();
        }
    }

    public static void reset() {
        dashes = 0;
        dashLevel = 0;
        isOnCooldown = false;
        dashCooldown = 0;
    }

    public static void refresh(Player player) {
        dashes = (short) player.getAttributeValue(EstrogenAttributes.DASH_LEVEL.get());
    }

    private static boolean canRefresh(Player player) {
        return player.onGround() || player.level().getBlockState(player.blockPosition()).getBlock() instanceof LiquidBlock || player.onClimbable();
    }

    public static boolean isOnCooldown() {
        return isOnCooldown;
    }

    public static int getDashLevel() {
        return dashLevel;
    }
}
