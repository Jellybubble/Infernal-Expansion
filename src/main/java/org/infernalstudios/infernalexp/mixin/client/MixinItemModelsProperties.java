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

package org.infernalstudios.infernalexp.mixin.client;

import org.infernalstudios.infernalexp.init.IEBiomes;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

@Mixin(targets = "net.minecraft.item.ItemModelsProperties$1")
public class MixinItemModelsProperties {

    @ModifyVariable(method = "call", at = @At(value = "STORE", ordinal = 1 /* this ordinal is when its set to Math.random(), the second time d0 is set to something */), ordinal = 0 /* this ordinal means the first double variable */)
    private double IE_daytimeInGSC(double in, ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity) {
        ClientWorld clientWorld = world;
        if (entity == null) {
            return in;
        }
        if (world == null && entity.world instanceof ClientWorld) {
            clientWorld = (ClientWorld) entity.getEntityWorld();
        }

        Optional<RegistryKey<Biome>> biomeKey = clientWorld.func_242406_i(entity.getPosition());
        RegistryKey<Biome> gscKey = RegistryKey.getOrCreateKey(Registry.BIOME_KEY, IEBiomes.GLOWSTONE_CANYON.getId());

        if (biomeKey.isPresent() && biomeKey.get().equals(gscKey)) {
            return MathHelper.nextDouble(new Random(), 0.95, 1.05) % 1;
        }
        return in;
    }
}
