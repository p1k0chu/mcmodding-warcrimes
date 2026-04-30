package io.github.p1k0chu.mcmodcrimes.mixindisabler;

import io.github.p1k0chu.mcmodcrimes.mixindisabler.mixin.AbstractExampleMixin;

final class Second extends AbstractExampleMixin {
    /*
     * This is here because having this class as an inner or anon
     * class would cause AbstractExampleMixin to be loaded as soon as
     * Main loads.
     *
     */
}
