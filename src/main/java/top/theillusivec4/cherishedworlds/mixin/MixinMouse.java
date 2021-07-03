/*
 * Copyright (c) 2018-2020 C4
 *
 * This file is part of Cherished Worlds, a mod made for Minecraft.
 *
 * Cherished Worlds is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Cherished Worlds is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Cherished Worlds.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.theillusivec4.cherishedworlds.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.cherishedworlds.client.FavoriteServers;
import top.theillusivec4.cherishedworlds.client.FavoriteWorlds;

@Mixin(Mouse.class)
public class MixinMouse {

  @Inject(at = @At("HEAD"), method = "method_1611([ZLnet/minecraft/client/gui/screen/Screen;DDI)V")
  private static void cherishedworlds$preMouseClick(boolean[] unused, Screen screen, double mouseX, double mouseY,
                                            int button, CallbackInfo cb) {

    if (screen instanceof SelectWorldScreen) {
      FavoriteWorlds.INSTANCE.click((SelectWorldScreen) screen, mouseX, mouseY);
    } else if (screen instanceof MultiplayerScreen) {
      FavoriteServers.INSTANCE.click((MultiplayerScreen) screen, mouseX, mouseY);
    }
  }

  @Inject(at = @At("TAIL"), method = "method_1611([ZLnet/minecraft/client/gui/screen/Screen;DDI)V")
  private static void cherishedworlds$postMouseClick(CallbackInfo cb) {
    Screen screen = MinecraftClient.getInstance().currentScreen;

    if (screen instanceof SelectWorldScreen) {
      FavoriteWorlds.INSTANCE.clicked((SelectWorldScreen) screen);
    } else if (screen instanceof MultiplayerScreen) {
      FavoriteServers.INSTANCE.clicked((MultiplayerScreen) screen);
    }
  }
}
