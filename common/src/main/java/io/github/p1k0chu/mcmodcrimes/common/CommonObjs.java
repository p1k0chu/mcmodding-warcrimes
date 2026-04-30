package io.github.p1k0chu.mcmodcrimes.common;

import net.fabricmc.loader.impl.launch.FabricLauncherBase;

import static io.github.p1k0chu.mcmodcrimes.common.FieldStealer.getField;

public final class CommonObjs {
    private static final Object KNOT_CLI = getField(FabricLauncherBase.getLauncher(), "classLoader");

    private CommonObjs() {
    }

    public static Object knotCLI() {
        return KNOT_CLI;
    }
}
