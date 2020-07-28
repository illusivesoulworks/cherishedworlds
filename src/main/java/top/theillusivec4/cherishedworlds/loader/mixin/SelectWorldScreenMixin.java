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

package top.theillusivec4.cherishedworlds.loader.mixin;

import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.cherishedworlds.core.WorldScreenHooks;

@Mixin(SelectWorldScreen.class)
public class SelectWorldScreenMixin {

  @SuppressWarnings("ConstantConditions")
  @Inject(at = @At("TAIL"), method = "init")
  public void _cherishedworlds_init(CallbackInfo cb) {
    WorldScreenHooks.init((SelectWorldScreen) (Object) this);
  }

  @SuppressWarnings("ConstantConditions")
  @Inject(at = @At("TAIL"), method = "render")
  public void _cherishedworlds_render(MatrixStack matrices, int mouseX, int mouseY, float delta,
      CallbackInfo cb) {
    WorldScreenHooks.render((SelectWorldScreen) (Object) this, matrices, mouseX, mouseY);
  }
}
