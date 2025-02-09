package dev.mayaqq.estrogen.integrations.cobblemon;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Gender;
import net.minecraft.world.entity.Entity;

public class CobblemonCompat {
    public static void toFemale(Entity entity) {
        if (entity instanceof PokemonEntity pokemonEntity) {
            pokemonEntity.getPokemon().setGender(Gender.FEMALE);
        }
    }

    public static boolean changeGender(Entity entity) {
        if (entity instanceof PokemonEntity pokemonEntity) {
            switch (pokemonEntity.getPokemon().getGender()) {
                case MALE -> {
                    pokemonEntity.getPokemon().setGender(Gender.FEMALE);
                    return true;
                }
                case FEMALE -> {
                    pokemonEntity.getPokemon().setGender(Gender.MALE);
                    return true;
                }
                default -> {
                    return false;
                }
            }
        }
        return false;
    }
}
