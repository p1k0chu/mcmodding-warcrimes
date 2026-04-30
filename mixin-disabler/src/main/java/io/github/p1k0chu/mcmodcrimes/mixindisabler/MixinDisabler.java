package io.github.p1k0chu.mcmodcrimes.mixindisabler;

import io.github.p1k0chu.mcmodcrimes.common.CommonObjs;
import io.github.p1k0chu.mcmodcrimes.common.FieldStealer;

import java.lang.reflect.Field;

public final class MixinDisabler {
    private static final Field TRANSFORM_INITIALIZED = FieldStealer.findField(CommonObjs.knotCLI().getClass(), "transformInitialized");

    private MixinDisabler() {
    }

    public static void withoutMixins(Runnable cb) {
        setMixins(false);
        try {
            cb.run();
        } finally {
            setMixins(true);
        }
    }

    private static void setMixins(boolean enabled) {
        /*
         * The KnotClassDelegate will refuse to mixin-transform
         * classes when transform is not initialized. But since it
         * simply returns the bytecode as-is, it's perfect.
         *
         */

        try {
            TRANSFORM_INITIALIZED.set(CommonObjs.knotCLI(), enabled);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
