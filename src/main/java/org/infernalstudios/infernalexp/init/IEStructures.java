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

package org.infernalstudios.infernalexp.init;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.infernalstudios.infernalexp.InfernalExpansion;
import org.infernalstudios.infernalexp.world.gen.structures.BastionOutpostStructure;
import org.infernalstudios.infernalexp.world.gen.structures.GlowstoneCanyonRuinStructure;
import org.infernalstudios.infernalexp.world.gen.structures.IEStructure;
import org.infernalstudios.infernalexp.world.gen.structures.SoulSandValleyRuinStructure;
import org.infernalstudios.infernalexp.world.gen.structures.StriderAltarStructure;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IEStructures {

    public static List<IEStructure<?>> structures = new ArrayList<>();


    public static final IEStructure<NoFeatureConfig> GLOWSTONE_CANYON_RUIN = registerStructure("glowstone_canyon_ruin", new GlowstoneCanyonRuinStructure(NoFeatureConfig.CODEC));
    public static final IEStructure<NoFeatureConfig> BASTION_OUTPOST = registerStructure("bastion_outpost", new BastionOutpostStructure(NoFeatureConfig.CODEC));
    public static final IEStructure<NoFeatureConfig> SOUL_SAND_VALLEY_RUIN = registerStructure("soul_sand_valley_ruin", new SoulSandValleyRuinStructure(NoFeatureConfig.CODEC));
    public static final IEStructure<NoFeatureConfig> STRIDER_ALTAR = registerStructure("strider_altar", new StriderAltarStructure(NoFeatureConfig.CODEC));

    public static <C extends IFeatureConfig, F extends IEStructure<C>> F registerStructure(String registryName, F structure) {
        ResourceLocation resourceLocation = new ResourceLocation(InfernalExpansion.MOD_ID, registryName);

        if (Registry.STRUCTURE_FEATURE.keySet().contains(resourceLocation))
            throw new IllegalStateException("Structure ID: \"" + resourceLocation.toString() + "\" is already in the registry!");

        structure.setRegistryName(resourceLocation);
        structures.add(structure);

        return structure;
    }

    public static void setupStructures() {
        structures.forEach(structure -> setupMapSpacingAndLand(structure, structure.getSeparationSettings(), structure.shouldTransformLand()));
    }

    public static <F extends Structure<?>> void setupMapSpacingAndLand(F structure, StructureSeparationSettings structureSeparationSettings, boolean transformSurroundingLand) {
        Structure.NAME_STRUCTURE_BIMAP.put(structure.getRegistryName().toString(), structure);

        if (transformSurroundingLand) {
            Structure.field_236384_t_ = ImmutableList.<Structure<?>>builder().addAll(Structure.field_236384_t_).add(structure).build();
        }

        DimensionStructuresSettings.field_236191_b_ = ImmutableMap.<Structure<?>, StructureSeparationSettings>builder().putAll(DimensionStructuresSettings.field_236191_b_).put(structure, structureSeparationSettings).build();

        WorldGenRegistries.NOISE_SETTINGS.getEntries().forEach(settings -> {
            Map<Structure<?>, StructureSeparationSettings> structureMap = settings.getValue().getStructures().func_236195_a_();

            if (structureMap instanceof ImmutableMap) {
                Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(structureMap);

                tempMap.put(structure, structureSeparationSettings);
                settings.getValue().getStructures().field_236193_d_ = tempMap;

            } else {
                structureMap.put(structure, structureSeparationSettings);
            }
        });
    }

}
