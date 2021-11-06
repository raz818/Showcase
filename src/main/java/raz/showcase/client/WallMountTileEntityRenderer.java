package raz.showcase.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.WoodType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import raz.showcase.blocks.wallmount.WallMountBlock;
import raz.showcase.blocks.wallmount.WallMountTileEntity;
import raz.showcase.setup.Registration;

public class WallMountTileEntityRenderer extends TileEntityRenderer<WallMountTileEntity> {

    private Minecraft mc = Minecraft.getInstance();
    private final WallMountTileEntityRenderer.WallMountModel model = new WallMountModel();

    public WallMountTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(WallMountTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        BlockState blockState = tileEntityIn.getBlockState();
        int lightLevel = getLightLevel(tileEntityIn.getWorld(), tileEntityIn.getPos().up());
        matrixStackIn.push();
        matrixStackIn.translate(0.5D, 0.5D, 0.5D);
        float f4 = -blockState.get(WallMountBlock.FACING).getHorizontalAngle();
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(f4));
        matrixStackIn.translate(0.0D, -0.3125D, -0.4375D);

        matrixStackIn.push();
        matrixStackIn.scale(0.6666667F, -0.6666667F, -0.6666667F);
        RenderMaterial renderMaterial = Atlases.SIGN_MATERIALS.get(WoodType.OAK);
        IVertexBuilder ivertexbuilder = renderMaterial.getBuffer(bufferIn, this.model::getRenderType);
        this.model.board.render(matrixStackIn, ivertexbuilder, lightLevel, combinedOverlayIn);
        matrixStackIn.pop();

        if (!tileEntityIn.isEmpty()) {
            renderItem(tileEntityIn.getDisplayItem(), new double[] {0d, 0.3125d, 0.0625d}, Vector3f.YP.rotationDegrees(180f), matrixStackIn, bufferIn, partialTicks, combinedOverlayIn, lightLevel, 0.75f);
        }

        matrixStackIn.pop();

    }

    private void renderItem(ItemStack stack, double[] translation, Quaternion rotation, MatrixStack matrixStack,
                            IRenderTypeBuffer buffer, float partialTicks, int combinedOverlay, int lightLevel, float scale) {
        matrixStack.push();
        matrixStack.translate(translation[0], translation[1], translation[2]);
        matrixStack.rotate(rotation);
        matrixStack.scale(scale, scale, scale);

        IBakedModel model = mc.getItemRenderer().getItemModelWithOverrides(stack, null, null);
        mc.getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.FIXED, true, matrixStack, buffer, lightLevel, combinedOverlay, model);
        matrixStack.pop();
    }

    private int getLightLevel(World world, BlockPos pos) {
        int bLight = world.getLightFor(LightType.BLOCK, pos);
        int sLight = world.getLightFor(LightType.SKY, pos);
        return LightTexture.packLight(bLight, sLight);
    }

    public static void register() {
        ClientRegistry.bindTileEntityRenderer(Registration.WALL_MOUNT_TILE_ENTITY.get(), WallMountTileEntityRenderer::new);
    }

    @OnlyIn(Dist.CLIENT)
    public static final class WallMountModel extends Model {
        public final ModelRenderer board = new ModelRenderer(64, 32, 0, 0);

        public WallMountModel() {
            super(RenderType::getEntityCutoutNoCull);
            this.board.addBox(-12.0F, -14.0F, -1.0F, 24.0F, 12.0F, 2.0F, 0.0F);
        }

        public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
            this.board.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        }
    }
}
