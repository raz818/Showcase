package raz.showcase.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import raz.showcase.blocks.displaycase.DisplayCaseBlock;
import raz.showcase.blocks.displaycase.DisplayCaseTileEntity;
import raz.showcase.items.MobTrophyItem;
import raz.showcase.setup.Registration;

public class DisplayCaseTileEntityRenderer extends TileEntityRenderer<DisplayCaseTileEntity> {

    private Minecraft mc = Minecraft.getInstance();
    private final EntityRendererManager entityRenderer;
    private SheepEntity sheepEntity;

    public DisplayCaseTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
        entityRenderer = Minecraft.getInstance().getRenderManager();
    }

    @Override
    public void render(DisplayCaseTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (tileEntityIn.isRemoved()) {
            return;
        }
        if (tileEntityIn.getDisplayItem().equals(ItemStack.EMPTY)) {
            return;
        }

        Direction dir = tileEntityIn.getBlockState().get(DisplayCaseBlock.FACING);

        ClientPlayerEntity player = mc.player;
        int lightLevel = getLightLevel(tileEntityIn.getWorld(), tileEntityIn.getPos().up());

        if (tileEntityIn.displayItemIsMobTrophy()){
            Entity entity = tileEntityIn.getCachedEntity();
            matrixStackIn.push();
            matrixStackIn.translate(0.5D, 0.0D, 0.5D);
            matrixStackIn.rotate(dir.getRotation());
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-90));

            entity.setRotationYawHead(0f);
            Minecraft.getInstance().getRenderManager().renderEntityStatic(entity, 0D, 0D, 0D, 0f, partialTicks, matrixStackIn, bufferIn, combinedLightIn);

            matrixStackIn.pop();
        }
        else {

            matrixStackIn.push();
            matrixStackIn.translate(0d, 0.35d, 0d);
            //matrixStackIn.rotate(dir.getRotation());
            matrixStackIn.scale(0.75f, 0.75f, 0.75f);
            mc.getBlockRendererDispatcher().renderBlock(Blocks.OAK_PRESSURE_PLATE.getDefaultState(),matrixStackIn,bufferIn,combinedLightIn,combinedOverlayIn, EmptyModelData.INSTANCE);
            matrixStackIn.pop();
            renderItem(tileEntityIn.getDisplayItem(), new double[] {0.5d, 0.5d, 0.5d}, Vector3f.YP.rotationDegrees(-dir.getHorizontalAngle()+180f), matrixStackIn, bufferIn, partialTicks, combinedOverlayIn, lightLevel, 0.75f);

        }

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
        ClientRegistry.bindTileEntityRenderer(Registration.DISPLAY_CASE_TILE_ENTITY.get(), DisplayCaseTileEntityRenderer::new);
    }
}
