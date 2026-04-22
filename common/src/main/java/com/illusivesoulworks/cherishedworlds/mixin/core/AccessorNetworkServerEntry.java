package com.illusivesoulworks.cherishedworlds.mixin.core;

import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.server.LanServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerSelectionList.NetworkServerEntry.class)
public interface AccessorNetworkServerEntry {

  @Accessor
  LanServer getServerData();
}
