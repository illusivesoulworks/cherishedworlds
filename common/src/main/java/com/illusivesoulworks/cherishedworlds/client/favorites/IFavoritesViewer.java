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

package com.illusivesoulworks.cherishedworlds.client.favorites;

import com.illusivesoulworks.cherishedworlds.CherishedWorldsConstants;
import com.illusivesoulworks.cherishedworlds.integration.ViewerIntegration;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public interface IFavoritesViewer<T extends Screen> {

  ResourceLocation STAR_ICON =
      new ResourceLocation(CherishedWorldsConstants.MOD_ID, "textures/gui/staricon.png");
  ResourceLocation EMPTY_STAR_ICON =
      new ResourceLocation(CherishedWorldsConstants.MOD_ID, "textures/gui/emptystaricon.png");

  void init(T screen);

  void draw(int mouseX, int mouseY, GuiGraphics guiGraphics, T screen);

  void click(int mouseX, int mouseY, T screen);

  void clicked(T screen);

  int getHorizontalOffset();

  default void drawIcon(int mouseX, int mouseY, GuiGraphics guiGraphics, T screen, int index,
                        boolean isFavorite, int topOffset, double scrollAmount, int bottom) {
    ResourceLocation icon = isFavorite ? STAR_ICON : EMPTY_STAR_ICON;
    int topOffsetMod = 15;
    int height = 36;
    Pair<Integer, Integer> override = ViewerIntegration.getOverride(height);

    if (override != null) {
      topOffsetMod = override.getFirst();
      height = override.getSecond();
    }
    int top = (int) (topOffset + topOffsetMod + height * index - scrollAmount);
    int x = screen.width / 2 - getHorizontalOffset();

    if (top < (bottom - 8) && top > topOffset) {
      guiGraphics.blit(icon, x, top, 0, 0, 9, 9, 9, 9);
    }

    if (mouseY >= top && mouseY <= (top + 9) && mouseX >= x && mouseX <= (x + 9)) {
      MutableComponent component = Component.translatable(
          "selectWorld." + CherishedWorldsConstants.MOD_ID + "." +
              (isFavorite ? "unfavorite" : "favorite"));
      guiGraphics.renderTooltip(Minecraft.getInstance().font, component, mouseX, mouseY);
    }
  }
}
