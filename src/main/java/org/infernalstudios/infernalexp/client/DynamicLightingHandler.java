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

package org.infernalstudios.infernalexp.client;

import org.infernalstudios.infernalexp.access.AbstractArrowEntityAccess;
import org.infernalstudios.infernalexp.config.InfernalExpansionConfig;
import org.infernalstudios.infernalexp.init.IEEffects;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.BlockPos;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@OnlyIn(Dist.CLIENT)
public class DynamicLightingHandler {
    private static final Minecraft MinecraftInstance = Minecraft.getInstance();
    public static final Map<BlockPos, LightData> LIGHT_SOURCES = new ConcurrentHashMap<>();

    public static void tick(LivingEntity entity) {
        if (entity != null && MinecraftInstance.player != null && MinecraftInstance.player.ticksExisted % (int) InfernalExpansionConfig.ClientConfig.LUMINOUS_REFRESH_DELAY.getDouble() == 0) {
            if (shouldGlow(entity)) {
                LIGHT_SOURCES.put(entity.getPosition(), new LightData(getTimeAmplifier(entity)));
            }
            if (entity == MinecraftInstance.player) {
                LIGHT_SOURCES.forEach((pos, data) -> {
                    if (data.time == 0) {
                        data.shouldKeep = false;
                    }
                    if (data.time == 20 * data.amplifier || !data.shouldKeep) {
                        MinecraftInstance.world.getChunkProvider().getLightManager().checkBlock(pos);
                    }
                    data.time -= (int) InfernalExpansionConfig.ClientConfig.LUMINOUS_REFRESH_DELAY.getDouble();
                });
                LIGHT_SOURCES.entrySet().removeIf(entry -> !entry.getValue().shouldKeep);
            }
        }
    }

    public static void tick(AbstractArrowEntity entity) {
        if (entity != null && MinecraftInstance.player != null && MinecraftInstance.player.ticksExisted % (int) InfernalExpansionConfig.ClientConfig.LUMINOUS_REFRESH_DELAY.getDouble() == 0) {
            if (shouldGlow(entity)) {
                LIGHT_SOURCES.put(entity.getPosition(), new LightData(0.5));
            }
        }
    }

    public static int getTimeAmplifier(LivingEntity entity) {
        EffectInstance luminousEffect = entity.getActivePotionEffect(IEEffects.LUMINOUS.get());
        if (luminousEffect != null) {
            return luminousEffect.getAmplifier() == 0 ? 1 : 2;
        }
        return 1;
    }

    public static boolean shouldGlow(AbstractArrowEntity entity) {
        return ((AbstractArrowEntityAccess) entity).getGlow();
    }

    public static boolean shouldGlow(LivingEntity entity) {
        return entity.isPotionActive(IEEffects.LUMINOUS.get());
    }

    public static class LightData {
        public boolean shouldKeep = true;
        public double time;
        public double amplifier;

        public LightData(double amplifier) {
            this.amplifier = amplifier;
            this.time = 20 * amplifier;
        }
    }
}
