package io.github.p1k0chu.idkbroimtiredandidontwanttocomeupwithpackagesrightnow.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import io.github.p1k0chu.idkbroimtiredandidontwanttocomeupwithpackagesrightnow.Main;
import net.minecraft.server.MinecraftServer;

@Mixin(MinecraftServer.class)
public abstract class AbstractExampleMixin {
    @Inject(at = @At("HEAD"), method = "loadLevel")
    public void init(CallbackInfo info) {
        // This code is injected into the start of MinecraftServer.loadLevel()V
        Main.LOGGER.info("AbstractExampleMixin ran! CallbackInfo = " + info);
    }
    
    @Unique
    public int sub(int l, int r) {
        return l - r;
    }
}
