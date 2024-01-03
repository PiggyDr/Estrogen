package dev.mayaqq.estrogen.registry.common.recipes;

import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import dev.mayaqq.estrogen.platformSpecific.CentrifugingRecipeMatches;
import dev.mayaqq.estrogen.registry.common.EstrogenRecipes;
import dev.mayaqq.estrogen.registry.common.recipes.common.DataInventory;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class CentrifugingRecipe extends ProcessingRecipe<DataInventory> {
    public CentrifugingRecipe(IRecipeTypeInfo typeInfo, ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(typeInfo, params);
    }


    public CentrifugingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        this(EstrogenRecipes.CENTRIFUGING, params);
    }

    public boolean matches(DataInventory inventory, Level world) {
        return CentrifugingRecipeMatches.matches(inventory, world, this);
    }

    public ItemStack assemble(DataInventory container, RegistryAccess registryAccess) {
        return getResultItem(registryAccess);
    }

    public boolean canCraftInDimensions(int i, int j) {
        return true;
    }

    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return getRollableResults().isEmpty() ? ItemStack.EMPTY
                : getRollableResults().get(0)
                .getStack();
    }

    public ResourceLocation getId() {
        return id;
    }

    public RecipeSerializer<?> getSerializer() {
        return getTypeInfo().getSerializer();
    }

    public RecipeType<?> getType() {
        return getTypeInfo().getType();
    }

    @Override
    protected int getMaxInputCount() {
        return 0;
    }

    @Override
    protected int getMaxOutputCount() {
        return 0;
    }

    @Override
    protected int getMaxFluidInputCount() {
        return 1;
    }

    @Override
    protected int getMaxFluidOutputCount() {
        return 1;
    }
}