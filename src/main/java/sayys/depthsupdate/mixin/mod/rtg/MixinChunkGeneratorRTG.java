package sayys.depthsupdate.mixin.mod.rtg;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.structure.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rtg.world.gen.ChunkGeneratorRTG;
import sayys.depthsupdate.DepthsUpdateConfig;
import sayys.depthsupdate.core.DeepFill;
import sayys.depthsupdate.core.HeightContext;
import sayys.depthsupdate.core.HeightManager;
import sayys.depthsupdate.util.BlockUtils;

import java.util.Random;

@Mixin(ChunkGeneratorRTG.class)
public abstract class MixinChunkGeneratorRTG {

    @Shadow @Final private World world;
    @Shadow private Random rand;

    @Inject(method = "generateTerrain", at = @At("TAIL"))
    private void depthsupdate$fillDeepUnderground(ChunkPrimer primer, float[] noise, CallbackInfo ci) {
        final boolean isNotExtended = !HeightManager.isExtended(this.world) || HeightManager.get(this.world).minY() >= 0;
        if (isNotExtended) {
            return;
        }
        HeightContext ctx = HeightManager.get(this.world);
        int minY = ctx.minY();
        IBlockState stone = Blocks.STONE.getDefaultState();
        IBlockState deepslate = BlockUtils.getDeepslateBlockState();

        IBlockState bedrock = Blocks.BEDROCK.getDefaultState();
        int fillMaxY = Math.max(0, DepthsUpdateConfig.deepslateMaxY);

        for (int bx = 0; bx < 16; bx++) {
            for (int bz = 0; bz < 16; bz++) {
                for (int by = minY; by <= fillMaxY; by++) {
                    IBlockState banded = DeepFill.bandAt(by, minY, this.rand, bedrock, deepslate, stone);

                    if (banded != null) {
                        primer.setBlockState(bx, by, bz, banded);
                    }
                }
            }
        }
    }
}
