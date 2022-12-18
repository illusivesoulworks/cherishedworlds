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
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JoinMultiplayerScreen.class)
public class MixinQuiltJoinMultiplayerScreen {

  @SuppressWarnings("ConstantConditions")
  @Inject(at = @At("TAIL"), method = "init")
  public void cherishedworlds$init(CallbackInfo cb) {
    ScreenEvents.onInit(((JoinMultiplayerScreen) (Object) this));
  }

  @SuppressWarnings("ConstantConditions")
  @Inject(at = @At("TAIL"), method = "render")
  public void cherishedworlds$render(PoseStack poseStack, int mouseX, int mouseY, float delta,
                                     CallbackInfo cb) {
    ScreenEvents.onDraw(mouseX, mouseY, poseStack, ((JoinMultiplayerScreen) (Object) this));
  }
}
