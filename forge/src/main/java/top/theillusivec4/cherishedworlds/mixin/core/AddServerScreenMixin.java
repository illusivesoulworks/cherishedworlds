/*
 * Copyright (C) 2018-2021  C4
 *
 * This file is part of Cherished Worlds, a mod made for Minecraft.
 *
 * Cherished Worlds is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or any later version.
 *
 * Cherished Worlds is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with Cherished
 * Worlds.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.theillusivec4.cherishedworlds.mixin.core;

import net.minecraft.client.gui.screens.EditServerScreen;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.multiplayer.ServerData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.cherishedworlds.client.favorites.FavoritesList;

@Mixin(EditServerScreen.class)
public class AddServerScreenMixin {

  @Shadow
  @Final
  private ServerData serverData;
  @Shadow
  private EditBox ipEdit;
  @Shadow
  private EditBox nameEdit;

  @Inject(at = @At("HEAD"), method = "onButtonServerAddPressed")
  private void cherishedworlds$onButtonServerAddPressed(CallbackInfo ci) {
    String key = this.serverData.name + this.serverData.ip;

    if (FavoritesList.contains(key)) {
      FavoritesList.remove(key);
      FavoritesList.add(this.nameEdit.getValue() + this.ipEdit.getValue());
    }
  }
}
