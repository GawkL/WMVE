package ru.tempelstudio.WMVE.custom.Render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import ru.tempelstudio.WMVE.custom.particles.SlashMark;

import java.util.ArrayList;
import java.util.List;

public class SlashRenderer {

    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath("wmve", "textures/misc/slash.png");

    public static final List<SlashMark> MARKS = new ArrayList<>();

    public static void render(LevelRenderContext context) {

        PoseStack pose = context.poseStack();
        Camera camera = context.gameRenderer().getMainCamera();

        MultiBufferSource.BufferSource buffers = Minecraft.getInstance()
                .renderBuffers()
                .bufferSource();

        VertexConsumer vc = buffers.getBuffer(RenderTypes.entityTranslucent(TEXTURE));

        Vec3 cam = camera.position();

        pose.pushPose();

        for (SlashMark mark : MARKS) {

            pose.pushPose();

            pose.translate(
                    mark.pos().x - cam.x,
                    mark.pos().y - cam.y,
                    mark.pos().z - cam.z
            );

            Quaternionf rotation = rotationFromFace(mark.face());

            pose.mulPose(rotation);

            pose.mulPose(Axis.ZP.rotationDegrees(mark.angle()));

            Matrix4f mat = pose.last().pose();

            float w = mark.width();
            float h = mark.length();

            float alpha = 1f - mark.age() / (float) mark.lifetime();

            vertex(vc, mat, -w, -h, 0, 0, 1, alpha);
            vertex(vc, mat, -w,  h, 0, 0, 0, alpha);
            vertex(vc, mat,  w,  h, 0, 1, 0, alpha);
            vertex(vc, mat,  w, -h, 0, 1, 1, alpha);

            pose.popPose();
        }

        pose.popPose();

        buffers.endBatch(RenderTypes.entityTranslucent(TEXTURE));
    }

    private static void vertex(VertexConsumer vc,
                               Matrix4f mat,
                               float x,
                               float y,
                               float z,
                               float u,
                               float v,
                               float alpha) {

        vc.addVertex(mat, x, y, z)
                .setColor(1f, 1f, 1f, alpha)
                .setUv(u, v)
                .setLight(LightCoordsUtil.FULL_BRIGHT)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setNormal(0,0,1);
    }

    private static Quaternionf rotationFromFace(Direction face) {
        return switch (face) {

            case UP -> Axis.XP.rotationDegrees(-90);

            case DOWN -> Axis.XP.rotationDegrees(90);

            case NORTH -> new Quaternionf();

            case SOUTH -> Axis.YP.rotationDegrees(180);

            case EAST -> Axis.YP.rotationDegrees(90);

            case WEST -> Axis.YP.rotationDegrees(-90);
        };
    }
    public static void tick() {
        MARKS.replaceAll(mark -> new SlashMark(
                mark.pos(),
                mark.face(),
                mark.angle(),
                mark.length(),
                mark.width(),
                mark.age() + 1,
                mark.lifetime()
        ));

        MARKS.removeIf(mark -> mark.age() >= mark.lifetime());
    }
}
