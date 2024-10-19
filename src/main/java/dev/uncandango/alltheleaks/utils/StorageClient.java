package dev.uncandango.alltheleaks.utils;

import net.minecraft.world.item.ItemStack;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public interface StorageClient {

    List<WeakReference<ItemStack>> stackWithRepresentation = new ArrayList<>();
}
