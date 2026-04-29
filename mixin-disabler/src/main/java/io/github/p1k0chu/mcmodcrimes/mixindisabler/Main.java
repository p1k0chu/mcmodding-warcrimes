package io.github.p1k0chu.mcmodcrimes.mixindisabler;

import io.github.p1k0chu.mcmodcrimes.mixindisabler.mixin.ExampleMixin;
import java.lang.reflect.Field;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.impl.launch.FabricLauncherBase;
import net.fabricmc.loader.impl.launch.knot.Knot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class Main implements ModInitializer {
    public static final String MOD_ID = "mixin-disabler-proof-of-concept";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static final Knot KNOT = (Knot) FabricLauncherBase.getLauncher();
    private static final Object KNOT_CLI = findKnotClassDelegate(KNOT);

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // classic. cant remove this
        LOGGER.info("Hello Fabric world!");

        /*
         * This is how you'd access a simple non abstract mixin class
         *
         */

        setMixinsOrDie(false);
        ExampleMixin ex = new ExampleMixin();
        LOGGER.info("6 + 9 = {}", ex.add(6, 9));
        ex.init(new CallbackInfo("onInitialize", false));

        /*
         * To access an abstract mixin simply make a class that
         * extends it. But it cannot be an inner class or an anon
         * class.
         *
         */

        Second s = new Second();
        LOGGER.info("6 - 9 = {}", s.sub(6, 9));
        s.init(new CallbackInfo("onInitialize", false));


        /*
         * After you are done you MUST re-enable mixins otherwise ALL
         * classes will NOT be transformed.
         *
         */
        setMixinsOrDie(true);
    }

    /**
     * Disable mixin tranfromation in Knot class loader
     *
     */
    private static void setMixinsOrDie(boolean enabled) {
        try {
            setMixins(enabled);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static void setMixins(boolean enabled) throws ReflectiveOperationException {
        /*
         * The KnotClassDelegate will refuse to mixin-transform
         * classes when transform is not initialized. But since it
         * simply returns the bytecode as-is, its perfect.
         *
         */

        Field f = KNOT_CLI.getClass().getDeclaredField("transformInitialized");
        f.setAccessible(true);
        f.set(KNOT_CLI, enabled);
    }

    private static Object findKnotClassDelegate(Knot knot) {
        try {
            Field clField = Knot.class.getDeclaredField("classLoader");
            clField.setAccessible(true);
            return clField.get(knot);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
