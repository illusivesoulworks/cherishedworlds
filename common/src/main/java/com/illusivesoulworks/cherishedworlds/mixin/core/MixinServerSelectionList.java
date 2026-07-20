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
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.server.LanServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerSelectionList.class)
public abstract class MixinServerSelectionList {

  @Shadow
  @Final
  private List<ServerSelectionList.OnlineServerEntry> onlineServers;

  @Shadow
  @Final
  private List<ServerSelectionList.NetworkServerEntry> networkServers;

  @SuppressWarnings("ConstantConditions")
  @Inject(at = @At(value = "INVOKE", target = "net/minecraft/client/gui/screens/multiplayer/ServerSelectionList.refreshEntries()V"), method = "updateOnlineServers")
  private void cherishedworlds$updateOnlineServers(ServerList serverList, CallbackInfo ci) {
    CherishedWorldsMixinHooks.updateOnlineServers(serverList, this.onlineServers);
  }

  @SuppressWarnings("ConstantConditions")
  @Inject(at = @At(value = "INVOKE", target = "net/minecraft/client/gui/screens/multiplayer/ServerSelectionList.refreshEntries()V"), method = "updateNetworkServers")
  private void cherishedworlds$updateNetworkServers(List<LanServer> serverList, CallbackInfo ci) {
    CherishedWorldsMixinHooks.updateNetworkServers(serverList, this.networkServers);
  }
}
