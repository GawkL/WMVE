package ru.tempelstudio.WMVE.custom.Render;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.Identifier;
import ru.tempelstudio.WMVE.WMVE;

public final class WMVERenderPipelines {
    public static final RenderPipeline DOUBLE_SIDED_TRANSLUCENT_PARTICLE =
            RenderPipelines.register(
                    RenderPipeline.builder(RenderPipelines.PARTICLE_SNIPPET)
                            .withLocation(
                                    Identifier.fromNamespaceAndPath(
                                            WMVE.MOD_ID,
                                            "pipeline/double_sided_translucent_particle"
                                    )
                            )
                            .withColorTargetState(
                                    new ColorTargetState(BlendFunction.TRANSLUCENT)
                            )
                            .withDepthStencilState(
                                    new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, false)
                            )
                            .withCull(false)
                            .build()
            );
}
