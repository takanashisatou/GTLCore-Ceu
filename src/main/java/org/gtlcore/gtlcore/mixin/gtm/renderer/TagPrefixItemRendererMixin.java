package org.gtlcore.gtlcore.mixin.gtm.renderer;

import org.gtlcore.gtlcore.common.data.GTLMaterials;

import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconType;
import com.gregtechceu.gtceu.client.renderer.item.TagPrefixItemRenderer;

import net.minecraft.world.item.Item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(TagPrefixItemRenderer.class)
public class TagPrefixItemRendererMixin {

    @Unique
    private static final Set<String> SPECIAL_ICON_SETS = Set.of(
            GTLMaterials.Infinity.getMaterialIconSet().name,
            GTLMaterials.Cosmic.getMaterialIconSet().name,
            GTLMaterials.CosmicNeutronium.getMaterialIconSet().name,
            GTLMaterials.Eternity.getMaterialIconSet().name,
            GTLMaterials.Magmatter.getMaterialIconSet().name);

    @Inject(method = "create", at = @At("HEAD"), remap = false, cancellable = true)
    private static void create(Item item, MaterialIconType type, MaterialIconSet iconSet, CallbackInfo ci) {
        if (SPECIAL_ICON_SETS.contains(iconSet.name)) {
            ci.cancel();
        }
    }
}