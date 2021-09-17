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
import net.minecraft.client.multiplayer.ServerList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.cherishedworlds.CherishedWorldsMod;
import top.theillusivec4.cherishedworlds.mixin.MixinHooks;

@Mixin(ServerSelectionList.OnlineServerEntry.class)
public class ServerSelectionListNormalEntryMixin {

  @Shadow
  @Final
  private JoinMultiplayerScreen screen;

  @Inject(at = @At("HEAD"), method = "swap", cancellable = true)
  private void cherishedworld$swapServers(int pos1, int pos2, CallbackInfo ci) {

    if (MixinHooks.isNotValidSwap(this.screen.getServers(), pos1, pos2)) {
      CherishedWorldsMod.LOGGER.info("invalid swap");
      ci.cancel();
    }
  }
}
