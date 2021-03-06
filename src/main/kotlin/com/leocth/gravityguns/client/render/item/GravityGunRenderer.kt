package com.leocth.gravityguns.client.render.item

import com.leocth.gravityguns.GravityGuns
import com.leocth.gravityguns.item.GravityGunItem
import com.leocth.gravityguns.util.ext.frame
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtHelper
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Quaternion
import software.bernie.geckolib3.model.AnimatedGeoModel
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer

@Environment(EnvType.CLIENT)
class GravityGunRenderer: GeoItemRenderer<GravityGunItem>(GravityGunModel)

@Environment(EnvType.CLIENT)
object GravityGunModel: AnimatedGeoModel<GravityGunItem>() {
    override fun getModelLocation(item: GravityGunItem) = GravityGuns.id("geo/item/gravity_gun.geo.json")

    override fun getTextureLocation(item: GravityGunItem) = GravityGuns.id("textures/item/gravity_gun.png")

    override fun getAnimationFileLocation(item: GravityGunItem): Identifier = GravityGuns.id("animations/item/gravity_gun.animation.json")
}