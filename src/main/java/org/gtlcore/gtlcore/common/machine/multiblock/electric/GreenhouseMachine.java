package org.gtlcore.gtlcore.common.machine.multiblock.electric;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GreenhouseMachine extends WorkableElectricMultiblockMachine {

    public GreenhouseMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    private int SkyLight = 15;

    private void getGreenhouseLight() {
        Level level = getLevel();
        SkyLight = 15;
        int x = 0, y = 3, z = 0;
        switch (getFrontFacing()) {
            case NORTH -> z = 1;
            case SOUTH -> z = -1;
            case WEST -> x = 1;
            case EAST -> x = -1;
        }
        final BlockPos pos = getPos().offset(x, y, z);
        BlockPos[] coordinates = new BlockPos[] { pos,
                pos.offset(1, 0, 0),
                pos.offset(1, 0, 1),
                pos.offset(1, 0, -1),
                pos.offset(0, 0, 1),
                pos.offset(0, 0, -1),
                pos.offset(-1, 0, 0),
                pos.offset(-1, 0, 1),
                pos.offset(-1, 0, -1) };
        for (BlockPos i : coordinates) {
            int l = level.getBrightness(LightLayer.SKY, i) - (level.dimension() == Level.OVERWORLD ? level.getSkyDarken() : 0);
            if (l < SkyLight) {
                SkyLight = l;
            }
        }
    }

    @Override
    public boolean beforeWorking(@Nullable GTRecipe recipe) {
        getGreenhouseLight();
        if (SkyLight == 0) {
            getRecipeLogic().interruptRecipe();
            return false;
        }
        return super.beforeWorking(recipe);
    }

    @Override
    public boolean onWorking() {
        boolean value = super.onWorking();
        if (getOffsetTimer() % 20 == 0) {
            getGreenhouseLight();
            if (SkyLight == 0) {
                getRecipeLogic().setProgress(0);
            }
            if (SkyLight < 13) {
                getRecipeLogic().setProgress(getRecipeLogic().getProgress() - 10);
            }
        }
        return value;
    }

    @Override
    public void addDisplayText(@NotNull List<Component> textList) {
        super.addDisplayText(textList);
        if (!this.isFormed) return;
        if (getOffsetTimer() % 10 == 0) {
            getGreenhouseLight();
        }
        textList.add(Component.literal("当前光照：" + SkyLight));
    }
}
