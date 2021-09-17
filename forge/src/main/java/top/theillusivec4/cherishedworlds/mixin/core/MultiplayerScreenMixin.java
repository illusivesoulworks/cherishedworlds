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

import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.multiplayer.ServerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.cherishedworlds.client.favorites.FavoritesList;

@Mixin(JoinMultiplayerScreen.class)
public class MultiplayerScreenMixin {

  @Shadow
  protected ServerSelectionList serverSelectionList;

  @Shadow
  private Button deleteButton;

  @Inject(at = @At("TAIL"), method = "onSelectedChange")
  private void _cherishedworlds_initButtons(CallbackInfo ci) {
    ServerSelectionList.Entry entry = this.serverSelectionList.getSelected();

    if (entry instanceof ServerSelectionList.OnlineServerEntry) {
      ServerData data = ((ServerSelectionList.OnlineServerEntry) entry).getServerData();
      this.deleteButton.active = !FavoritesList.contains(data.name + data.ip);
    }
  }
}
