package dev.mayaqq.estrogen.registry.blockEntities;

import com.jozufozu.flywheel.backend.instancing.InstancedRenderDispatcher;
import com.simibubi.create.foundation.blockEntity.SyncedBlockEntity;
import dev.mayaqq.estrogen.registry.EstrogenTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CookieJarBlockEntity extends SyncedBlockEntity implements Container {
    private final NonNullList<ItemStack> items = NonNullList.withSize(8, ItemStack.EMPTY);

    public CookieJarBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);
    }

    /**
     * Remove and returns 1 count of the last-most item from the jar.
     * returns ItemStack.EMPTY if jar was empty
     */
    public ItemStack remove1Item() {
        for (int i = items.size() - 1; i >= 0; i--) {
            ItemStack jarItemStack = items.get(i);
            if (jarItemStack.isEmpty()) {
                continue;
            }
            ItemStack itemStack = jarItemStack.split(1);
            notifyUpdate();
            return itemStack;
        }
        return ItemStack.EMPTY;
    }

    /**
     * Adds the item stack to the jar, and returns the item stack not added to the jar.
     * @param itemStack The stack to be added to the jar.
     * @return The stack not added to the jar.
     */
    public ItemStack addItemStack(ItemStack itemStack) {
        if (!canPlaceItem(0, itemStack)) return itemStack;

        ItemStack itemStackCopy = itemStack.copy();
        int i = 0;
        for (ItemStack jarItemStack : items) {
            if (!jarItemStack.isEmpty() && !ItemStack.isSameItemSameTags(jarItemStack, itemStackCopy)) continue;

            ItemStack addToJar = itemStackCopy.split(itemStackCopy.getMaxStackSize() - jarItemStack.getCount());
            addToJar.grow(jarItemStack.getCount());
            items.set(i, addToJar);

            if (itemStackCopy.isEmpty()) break;

            i++;
        }
        notifyUpdate();
        return itemStackCopy;
    }

    /**
     * Returns number of items.
     */
    public int getCount() {
        int count = 0;
        for (ItemStack jarItemStack : items) {
            if (jarItemStack.isEmpty()) {
                continue;
            }
            count += jarItemStack.getCount();
        }
        return count;
    }

    /**
     * Gets items. For use in rendering.
     */
    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    /**
     * Returns true if the provided ItemStack matches the first item in the jar,
     * or if the jar is empty.
     */
    public boolean matchesFirstItem(ItemStack itemStack) {
        // this is because chutes do NOT obey Container.canTakeItem.
        // so they ignore FILO, which can allow for different cookies in the same jar.
        for (ItemStack jarItemStack : items) {
            if (jarItemStack.isEmpty()) {
                continue;
            }
            return ItemStack.isSameItemSameTags(jarItemStack, itemStack);
        }
        return true;
    }
    
    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        ContainerHelper.saveAllItems(compoundTag, items);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        this.items.clear();
        ContainerHelper.loadAllItems(compoundTag, items);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket packet) {
        super.onDataPacket(connection, packet);
        InstancedRenderDispatcher.enqueueUpdate(this);
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int slot) {
        if (slot >= items.size()) {
            return null;
        }
        return getItems().get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        ItemStack itemStack = ContainerHelper.removeItem(items, slot, count);
        if (!itemStack.isEmpty()) {
            notifyUpdate();
        }

        return itemStack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        // teehee! this doesn't actually test for the item type!
        // do the testing in CookieJarBlock.onUse()!
        items.set(slot, itemStack);
        if (itemStack.getCount() > this.getMaxStackSize()) {
            itemStack.setCount(this.getMaxStackSize());
        }

        notifyUpdate();
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack itemStack) {
        if (!itemStack.is(EstrogenTags.Items.COOKIES)) {
            return false;
        }
        return matchesFirstItem(itemStack);
    }

    @Override
    public void clearContent() {
        items.clear();
    }
}