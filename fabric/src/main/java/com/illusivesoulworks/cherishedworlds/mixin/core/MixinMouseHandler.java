/*
 * Copyright (C) 2018-2022 Illusive Soulworks
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

import com.illusivesoulworks.cherishedworlds.client.ScreenEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MixinMouseHandler {

  @Inject(at = @At("HEAD"), method = "method_1611([ZLnet/minecraft/client/gui/screens/Screen;DDI)V")
  private static void cherishedworlds$preMouseClick(boolean[] unused, Screen screen, double mouseX,
                                                    double mouseY,
                                                    int button, CallbackInfo cb) {
    ScreenEvents.onMouseClick((int) mouseX, (int) mouseY, screen);
  }

  @Inject(at = @At("TAIL"), method = "method_1611([ZLnet/minecraft/client/gui/screens/Screen;DDI)V")
  private static void cherishedworlds$postMouseClick(CallbackInfo cb) {
    ScreenEvents.onMouseClicked(Minecraft.getInstance().screen);
  }
}
