package com.illusivesoulworks.cherishedworlds.mixin.core;

import com.illusivesoulworks.cherishedworlds.client.ScreenEvents;
import com.illusivesoulworks.cherishedworlds.mixin.CherishedWorldsMixinHooks;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.LevelSummary;
import net.minecraft.world.level.storage.WorldData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldOpenFlows.class)
public class MixinWorldOpenFlows {

  @WrapOperation(
      at = @At(
          value = "INVOKE",
          target = "net/minecraft/world/level/storage/LevelSummary.backupStatus ()Lnet/minecraft/world/level/storage/LevelSummary$BackupStatus;"),
      method = "openWorldCheckVersionCompatibility"
  )
  private LevelSummary.BackupStatus cherishedworlds$openWorldCheckVersionCompatibility(
      LevelSummary instance, Operation<LevelSummary.BackupStatus> original) {
    return CherishedWorldsMixinHooks.getBackupStatus(instance, original.call(instance));
  }

  @Inject(
      at = @At("HEAD"),
      method = "createLevelFromExistingSettings")
  private void cherishedworlds$createLevelFromExistingSettings(
      LevelStorageSource.LevelStorageAccess levelStorageAccess, ReloadableServerResources unused,
      LayeredRegistryAccess<RegistryLayer> unused1, WorldData unused2, CallbackInfo ci) {
    ScreenEvents.onCreateNewWorld(levelStorageAccess.getLevelId());
  }
}
