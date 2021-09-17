/*
 * Copyright (C) 2018-2021  C4
 *
 * This file is part of Cherished Worlds, a mod made for Minecraft.
 *
 * Cherished Worlds is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or any later version.
 *
 * Cherished Worlds is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with Cherished
 * Worlds.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.theillusivec4.cherishedworlds.client.favorites;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.client.event.GuiScreenEvent;
import top.theillusivec4.cherishedworlds.CherishedWorldsMod;

public interface IFavoritesManager<T extends Screen> {

  ResourceLocation STAR_ICON =
      new ResourceLocation(CherishedWorldsMod.MOD_ID, "textures/gui/staricon.png");
  ResourceLocation EMPTY_STAR_ICON =
      new ResourceLocation(CherishedWorldsMod.MOD_ID, "textures/gui/emptystaricon.png");

  void init(T screen);

  void draw(GuiScreenEvent.DrawScreenEvent.Post evt, T screen);

  void click(GuiScreenEvent.MouseClickedEvent.Pre evt, T screen);

  void clicked(T screen);

  int getOffset();

  default void drawIcon(GuiScreenEvent.DrawScreenEvent.Post evt, Screen gui, int index,
                        boolean isFavorite, int topOffset, double scrollAmount, int bottom) {
    ResourceLocation icon = isFavorite ? STAR_ICON : EMPTY_STAR_ICON;
    int top = (int) (topOffset + 15 + 36 * index - scrollAmount);
    int x = evt.getGui().width / 2 - getOffset();

    if (top < (bottom - 8) && top > topOffset) {
      RenderSystem.setShaderTexture(0, icon);
      GuiComponent.blit(evt.getMatrixStack(), x, top, 0, 0, 9, 9, 9, 9);
    }
    int mouseX = evt.getMouseX();
    int mouseY = evt.getMouseY();

    if (mouseY >= top && mouseY <= (top + 9) && mouseX >= x && mouseX <= (x + 9)) {
      TranslatableComponent component = new TranslatableComponent(
          "selectWorld." + CherishedWorldsMod.MOD_ID + "." + (isFavorite ? "unfavorite"
              : "favorite"));
      gui.renderTooltip(evt.getMatrixStack(), component, mouseX, mouseY);
    }
  }
}
