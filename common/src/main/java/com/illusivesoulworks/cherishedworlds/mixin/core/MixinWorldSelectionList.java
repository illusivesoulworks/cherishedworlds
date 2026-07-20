/*
 * Copyright (C) 2018-2026 Illusive Soulworks
 *
 * Cherished Worlds is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * Cherished Worlds is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Cherished Worlds.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.illusivesoulworks.cherishedworlds.mixin.core;

import com.illusivesoulworks.cherishedworlds.mixin.CherishedWorldsMixinHooks;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import net.minecraft.world.level.storage.LevelSummary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldSelectionList.class)
public abstract class MixinWorldSelectionList extends
    ObjectSelectionList<WorldSelectionList.Entry> {

  public MixinWorldSelectionList(Minecraft minecraft, int i, int j, int k, int l) {
    super(minecraft, i, j, k, l);
  }

  @SuppressWarnings("ConstantConditions")
  @Inject(at = @At(value = "INVOKE", target = "net/minecraft/client/gui/screens/worldselection/WorldSelectionList.setSelected(Lnet/minecraft/client/gui/screens/worldselection/WorldSelectionList$Entry;)V"), method = "fillLevels(Ljava/lang/String;Ljava/util/List;)V")
  private void cherishedworlds$sortLevels(String filter, List<LevelSummary> levels,
                                          CallbackInfo ci) {
    this.sort(CherishedWorldsMixinHooks.getLevelComparator());
  }
}
