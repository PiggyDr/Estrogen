package dev.mayaqq.estrogen.resources;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import dev.mayaqq.estrogen.Estrogen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class EstrogenSplashLoader extends SimpleJsonResourceReloadListener {

    public static final EstrogenSplashLoader INSTANCE = new EstrogenSplashLoader(new Gson(), "splashes");

    public List<Component> splashes = new ArrayList<>();

    public EstrogenSplashLoader(Gson gson, String directory) {
        super(gson, directory);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, @NotNull ResourceManager manager, @NotNull ProfilerFiller profiler) {
        this.splashes.clear();
        object.forEach((key, value) -> {
            if (key.getNamespace().equals("estrogen") && key.getPath().equals("splashes")) {
                value.getAsJsonObject().getAsJsonArray("splashes").forEach(element -> {
                    if (element.isJsonObject()) {
                        try {
                            String formatted = parse(element.getAsJsonObject().toString());
                            this.splashes.add(Component.Serializer.fromJsonLenient(formatted));
                        } catch (Exception e) {
                            Estrogen.LOGGER.warn("Failed to parse splash text", e);
                        }
                    } else {
                        this.splashes.add(Component.literal(parse(element.getAsString())));
                    }
                });
            }
        });
    }

    private static String parse(String input) {
        input = replaceVariable(input, "percent", new Random().nextInt(101) + "%");
        input = replaceVariable(input, "username", Minecraft.getInstance().getUser().getName());
        return input;
    }

    private static String replaceVariable(String input, String name, String value) {
        String variable = "%" + name + "%";
        return input.replaceAll("(^|[^\\\\])" + variable, "$1" + value).replaceAll("\\\\" + variable, variable);
    }
}
