package io.github.p1k0chu.mcmodcrimes.asmmixin;

import io.github.p1k0chu.mcmodcrimes.common.CommonObjs;
import io.github.p1k0chu.mcmodcrimes.common.FieldStealer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.impl.game.GameProvider;
import net.minecraft.core.HolderLookup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.extensibility.IMixinConfig;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.transformer.IMixinTransformer;

import java.io.IOException;
import java.lang.classfile.Annotation;
import java.lang.classfile.AnnotationElement;
import java.lang.classfile.AnnotationValue;
import java.lang.classfile.ClassFile;
import java.lang.classfile.attribute.RuntimeInvisibleAnnotationsAttribute;
import java.lang.classfile.attribute.RuntimeVisibleAnnotationsAttribute;
import java.lang.constant.ClassDesc;
import java.lang.constant.ConstantDescs;
import java.lang.constant.MethodTypeDesc;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Main implements ModInitializer {
    public static final String MOD_ID = "asm-mixin";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static final ClassDesc DESC = Main.class.describeConstable().orElseThrow();

    @Override
    public void onInitialize() {
        // This should've never been info level
        LOGGER.info("Hello Fabric world!");

        /*
         * Since the mixin isn't in classpath, and loading it makes no
         * sense, we take one of the secret maps of class name ->
         * bytecode, and write our mixin there.
         * 
         */

        // Grand Theft Reflection
        GameProvider provider = FieldStealer.getField(CommonObjs.knotCLI(), "provider");
        Object transformer = FieldStealer.getField(provider, "transformer");
        Map<String, byte[]> classMap = FieldStealer.getField(transformer, "patchedClasses");

        classMap.put(Main.class.getPackageName() + ".mixin.GeneratedMixin", makeMixinClass());

        /*
         * Now register the mixin in the config like it was always
         * there
         *
         */
        
        var mixinTransformer = (IMixinTransformer) MixinEnvironment.getCurrentEnvironment().getActiveTransformer();
        var extensions = mixinTransformer.getExtensions();
        IMixinConfig myConfig = findMyConfig(mixinTransformer);
        try {
            Method m = myConfig.getClass().getDeclaredMethod("prepareMixins", String.class, List.class, boolean.class, extensions.getClass());
            m.setAccessible(true);
            m.invoke(myConfig, "mixins", List.of("GeneratedMixin"), true, extensions);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private static IMixinConfig findMyConfig(Object mixinTransformer) {
        Object processor = FieldStealer.getField(mixinTransformer, "processor");
        for (var config : FieldStealer.<List<IMixinConfig>>getField(processor, "configs")) {
            if (config.getName().equals("asm-mixin.mixins.json")) {
                return config;
            }
        }
        throw new RuntimeException("couldn't find own mixin config.");
    }

    private static byte[] makeMixinClass() {
        /*
         * This is hell and i do not with to write that again.
         *
         * This mixin mixins into ServerAdvancementManager's <init>
         * and says hi.
         *
         */
        
        return ClassFile.of()
            .build(ClassDesc.of(Main.class.getPackageName() + ".mixin", "GeneratedMixin"),
                builder -> builder
                    .with(
                        RuntimeInvisibleAnnotationsAttribute.of(
                            Annotation.of(
                                Mixin.class.describeConstable().orElseThrow(),
                                AnnotationElement.of("value", AnnotationValue.ofArray(AnnotationValue.ofClass(ClassDesc.of("net.minecraft.server.ServerAdvancementManager"))))
                            )
                        )
                    )
                    .withMethod(
                        "init",
                        MethodTypeDesc.of(
                            ConstantDescs.CD_void,
                            HolderLookup.Provider.class.describeConstable().orElseThrow(),
                            CallbackInfo.class.describeConstable().orElseThrow()
                        ),
                        Modifier.PRIVATE | Modifier.STATIC,
                        method -> method
                            .with(
                                RuntimeVisibleAnnotationsAttribute.of(
                                    Annotation.of(
                                        Inject.class.describeConstable().orElseThrow(),
                                        AnnotationElement.of(
                                            "method",
                                            AnnotationValue.ofArray(
                                                AnnotationValue.ofString("Lnet/minecraft/server/ServerAdvancementManager;<init>(Lnet/minecraft/core/HolderLookup$Provider;)V")
                                            )
                                        ),
                                        AnnotationElement.of(
                                            "at",
                                            AnnotationValue.ofArray(
                                                AnnotationValue.ofAnnotation(
                                                    Annotation.of(
                                                        At.class.describeConstable().orElseThrow(),
                                                        AnnotationElement.of("value", AnnotationValue.ofString("HEAD"))
                                                    )
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                            .withCode(
                                code ->
                                    code.getstatic(DESC, "LOGGER", Logger.class.describeConstable().orElseThrow())
                                        .ldc("Hello from GeneratedMixin")
                                        .invokeinterface(
                                            Logger.class.describeConstable().orElseThrow(),
                                            "info",
                                            MethodTypeDesc.of(
                                                ConstantDescs.CD_void, ConstantDescs.CD_String
                                            )
                                        )
                                        .return_()
                            )
                    )
            );
    }
}
