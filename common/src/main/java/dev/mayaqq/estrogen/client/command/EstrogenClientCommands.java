package dev.mayaqq.estrogen.client.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import dev.mayaqq.estrogen.client.cosmetics.ui.CosmeticUI;
import net.minecraft.network.chat.Component;

public class EstrogenClientCommands {
    public static <T> void register(CommandDispatcher<T> dispatcher, ClientCommandManager<T> manager) {
        LiteralCommandNode<T> estrogen = dispatcher.register(manager.literal("estrogen").executes(context -> {return 1;}));

        LiteralCommandNode<T> cosmetics = dispatcher.register(manager.literal("cosmetics").executes(CosmeticUI::open));

        RootCommandNode<T> root = dispatcher.getRoot();

        estrogen.addChild(cosmetics);
        root.addChild(estrogen);
    }

    public interface ClientCommandManager<T> {

        LiteralArgumentBuilder<T> literal(String name);

        <I> RequiredArgumentBuilder<T, I> argument(String name, ArgumentType<I> type);

        boolean hasPermission(T source, int permissionLevel);

        void sendFailure(T source, Component component);
    }
}
