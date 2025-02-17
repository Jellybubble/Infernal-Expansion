/*
 * Copyright 2022 Infernal Studios
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.infernalstudios.infernalexp.client.entity.model;
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import org.infernalstudios.infernalexp.entities.ShroomloinEntity;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class ShroomloinModel<T extends ShroomloinEntity> extends EntityModel<T> {
    private final ModelRenderer all;
    private final ModelRenderer body;
    private final ModelRenderer hat;
    private final ModelRenderer rightleg;
    private final ModelRenderer leftleg;

    public ShroomloinModel() {
        textureWidth = 64;
        textureHeight = 64;

        textureWidth = 64;
        textureHeight = 64;

        all = new ModelRenderer(this);
        all.setRotationPoint(0.0F, 24.0F, 0.0F);


        body = new ModelRenderer(this);
        body.setRotationPoint(0.0F, -4.0F, 0.0F);
        all.addChild(body);
        body.setTextureOffset(1, 23).addBox(-5.0F, -6.0F, -5.0F, 10.0F, 6.0F, 9.0F, 0.0F, false);

        hat = new ModelRenderer(this);
        hat.setRotationPoint(0.0F, -4.0F, -0.5F);
        body.addChild(hat);
        hat.setTextureOffset(0, 0).addBox(-8.0F, -6.0F, -8.0F, 16.0F, 6.0F, 16.0F, 0.0F, false);

        rightleg = new ModelRenderer(this);
        rightleg.setRotationPoint(-2.5F, -4.0F, -0.5F);
        all.addChild(rightleg);
        rightleg.setTextureOffset(0, 8).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 5.0F, 3.0F, 0.0F, true);

        leftleg = new ModelRenderer(this);
        leftleg.setRotationPoint(2.5F, -4.0F, -0.5F);
        all.addChild(leftleg);
        leftleg.setTextureOffset(0, 0).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 5.0F, 3.0F, 0.0F, false);
    }

    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.leftleg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.rightleg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.hat.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 0.5F * limbSwingAmount;
        this.body.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 0.25F * limbSwingAmount;
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        all.render(matrixStack, buffer, packedLight, packedOverlay);
		/*
		body.render(matrixStack, buffer, packedLight, packedOverlay);
		hat.render(matrixStack, buffer, packedLight, packedOverlay);
		leftleg.render(matrixStack, buffer, packedLight, packedOverlay);
		rightleg.render(matrixStack, buffer, packedLight, packedOverlay);
		*/
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
