package org.gtlcore.gtlcore.client.renderer.machine;

import org.gtlcore.gtlcore.GTLCore;
import org.gtlcore.gtlcore.common.machine.multiblock.part.BallHatchPartMachine;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.client.renderer.machine.TieredHullMachineRenderer;

import com.lowdragmc.lowdraglib.client.bakedpipeline.FaceQuad;
import com.lowdragmc.lowdraglib.client.model.ModelFactory;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class BallHatchRenderer extends TieredHullMachineRenderer {

    public static final ResourceLocation BALL_HATCH_OVERLAY = GTLCore.id("block/overlay/machine/ball_hatch");
    public static final ResourceLocation IDLE = GTLCore.id("block/overlay/machine/ball_hatch_idle");
    public static final ResourceLocation SPINNING = GTLCore.id("block/overlay/machine/ball_hatch_spinning");
    public static final AABB AABB = new AABB(-1, -1, -0.01, 2, 2, 1.01);

    public BallHatchRenderer() {
        super(GTValues.IV, GTCEu.id("block/machine/hull_machine"));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderMachine(List<BakedQuad> quads, MachineDefinition definition, @Nullable MetaMachine machine, Direction frontFacing, @Nullable Direction side, RandomSource rand, @Nullable Direction modelFacing, ModelState modelState) {
        super.renderMachine(quads, definition, machine, frontFacing, side, rand, modelFacing, modelState);
        if (side == frontFacing && modelFacing != null) {
            quads.add(FaceQuad.bakeFace(modelFacing, ModelFactory.getBlockSprite(BALL_HATCH_OVERLAY), modelState));
            if (machine instanceof BallHatchPartMachine ballHatchMachine) {
                if (ballHatchMachine.isFormed()) {
                    if (ballHatchMachine.isWorking()) {
                        quads.add(FaceQuad.bakeFace(AABB, modelFacing, ModelFactory.getBlockSprite(SPINNING), modelState, 2, 0, true, true));
                    } else {
                        quads.add(FaceQuad.bakeFace(AABB, modelFacing, ModelFactory.getBlockSprite(IDLE), modelState, 2, 0, true, true));
                    }
                }
            }
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onPrepareTextureAtlas(ResourceLocation atlasName, Consumer<ResourceLocation> register) {
        super.onPrepareTextureAtlas(atlasName, register);
        if (atlasName.equals(InventoryMenu.BLOCK_ATLAS)) {
            register.accept(IDLE);
            register.accept(SPINNING);
            register.accept(BALL_HATCH_OVERLAY);
        }
    }
}
