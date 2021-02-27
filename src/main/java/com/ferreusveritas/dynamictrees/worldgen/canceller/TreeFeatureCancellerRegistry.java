package com.ferreusveritas.dynamictrees.worldgen.canceller;

import com.ferreusveritas.dynamictrees.DynamicTrees;
import com.ferreusveritas.dynamictrees.api.worldgen.ITreeFeatureCanceller;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nullable;
import java.util.HashMap;

/**
 * This class contains the main registry for {@link ITreeFeatureCanceller}. {@link TreeFeatureCancellerRegistryEvent}
 * can be used to grab the instance and register any extra cancellers when it's set up. The registry is only
 * initialised once the first time {@link BiomeLoadingEvent} is fired.
 *
 * @author Harley O'Connor
 */
public final class TreeFeatureCancellerRegistry {

    // Default canceller identifiers.
    public static final String TREE_CANCELLER = "tree";
    public static final String FUNGUS_CANCELLER = "fungus";
    public static final String MUSHROOM_CANCELLER = "mushroom";

    private static TreeFeatureCancellerRegistry INSTANCE = null;

    private final HashMap<String, ITreeFeatureCanceller> cancellers = new HashMap<>();

    public static void registerCancellers () {
        if (INSTANCE != null)
            return;

        INSTANCE = new TreeFeatureCancellerRegistry();

        // Creates a feature canceller registry event which add-ons can use to register their own cancellers.
        final TreeFeatureCancellerRegistryEvent registryEvent = new TreeFeatureCancellerRegistryEvent(INSTANCE);

        MinecraftForge.EVENT_BUS.post(registryEvent);
    }

    public void register (final String name, final ITreeFeatureCanceller treeFeatureCanceller) {
        if (this.cancellers.containsKey(name)) {
            LogManager.getLogger().error("Tried to register existing tree feature canceller \"" + name + "\"."); return;
        }

        this.cancellers.put(name, treeFeatureCanceller);
    }

    @Nullable
    public static ITreeFeatureCanceller getFeatureCanceller (final String name) {
        if (INSTANCE == null)
            return null;

        return INSTANCE.cancellers.getOrDefault(name, null);
    }

    public static class TreeFeatureCancellerRegistryEvent extends Event {

        private final TreeFeatureCancellerRegistry featureCancellerRegistry;

        public TreeFeatureCancellerRegistryEvent(final TreeFeatureCancellerRegistry featureCancellerRegistry) {
            this.featureCancellerRegistry = featureCancellerRegistry;
        }

        public TreeFeatureCancellerRegistry getFeatureCancellerRegistry() {
            return featureCancellerRegistry;
        }

    }

}