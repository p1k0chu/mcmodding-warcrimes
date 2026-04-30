package io.github.p1k0chu.mcmodcrimes.mixindisabler;

import io.github.p1k0chu.mcmodcrimes.mixindisabler.mixin.ExampleMixin;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.impl.launch.knot.Knot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class Main implements ModInitializer {
    public static final String MOD_ID = "mixin-disabler-proof-of-concept";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // classic. cant remove this
        LOGGER.info("Hello Fabric world!");

        /*
         * First, disable mixins and load the classes
         *
         */
        MixinDisabler.withoutMixins(() -> {
            var ignore = ExampleMixin.class;
            var ignore2 = Second.class;
        });

        /*
         * After you are done loading classes you MUST re-enable
         * mixins otherwise ALL classes that load in the future will
         * NOT be transformed.
         *
         */

        ExampleMixin ex = new ExampleMixin();
        LOGGER.info("6 + 9 = {}", ex.add(6, 9));
        ex.init(new CallbackInfo("onInitialize", false));

        Second s = new Second();
        LOGGER.info("6 - 9 = {}", s.sub(6, 9));
        s.init(new CallbackInfo("onInitialize", false));
    }
}
