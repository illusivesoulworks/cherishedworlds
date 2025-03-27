package com.illusivesoulworks.cherishedworlds.mixin.core;

import com.illusivesoulworks.cherishedworlds.client.ScreenEvents;
import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.WorldData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldOpenFlows.class)
public class MixinWorldOpenFlows {

  @Inject(
      at = @At("HEAD"),
      method = "createLevelFromExistingSettings")
  private void cherishedworlds$createLevelFromExistingSettings(
      LevelStorageSource.LevelStorageAccess levelStorageAccess, ReloadableServerResources unused,
      LayeredRegistryAccess<RegistryLayer> unused1, WorldData unused2, CallbackInfo ci) {
    ScreenEvents.onCreateNewWorld(levelStorageAccess.getLevelId());
  }
}
