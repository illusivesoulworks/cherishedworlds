package com.illusivesoulworks.cherishedworlds.mixin.core;

import com.illusivesoulworks.cherishedworlds.client.ScreenEvents;
import com.mojang.serialization.Lifecycle;
import java.util.Optional;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.server.RegistryLayer;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.PrimaryLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(CreateWorldScreen.class)
public class MixinCreateWorldScreen {

  @Inject(at = @At(target = "net/minecraft/client/gui/screens/worldselection/CreateWorldScreen.createLevelSettings(Z)Lnet/minecraft/world/level/LevelSettings;", value = "INVOKE"), method = "createNewWorld", locals = LocalCapture.CAPTURE_FAILSOFT)
  private void cherishedworlds$createNewWorld(
      PrimaryLevelData.SpecialWorldProperty specialWorldProperty,
      LayeredRegistryAccess<RegistryLayer> layeredRegistryAccess, Lifecycle lifecycle,
      CallbackInfo ci, Optional<LevelStorageSource.LevelStorageAccess> levelStorageAccess) {
    levelStorageAccess.ifPresent(access -> ScreenEvents.onCreateNewWorld(access.getLevelId()));
  }
}
