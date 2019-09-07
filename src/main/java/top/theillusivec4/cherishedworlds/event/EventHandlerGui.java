/*
 * Copyright (C) 2018-2019  C4
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

package top.theillusivec4.cherishedworlds.event;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.ErrorScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.WorldSelectionList;
import net.minecraft.client.gui.screen.WorldSelectionList.Entry;
import net.minecraft.client.gui.screen.WorldSelectionScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.storage.SaveFormat;
import net.minecraft.world.storage.WorldSummary;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.config.GuiUtils;
import top.theillusivec4.cherishedworlds.CherishedWorlds;
import top.theillusivec4.cherishedworlds.util.FavoriteWorldsList;
import top.theillusivec4.cherishedworlds.util.ReflectionAccessor;

public class EventHandlerGui {

  private static final ResourceLocation STAR_ICON = new ResourceLocation(CherishedWorlds.MODID,
      "textures/gui/staricon.png");
  private static final ResourceLocation EMPTY_STAR_ICON = new ResourceLocation(
      CherishedWorlds.MODID, "textures/gui/emptystaricon.png");

  private static void refreshList(WorldSelectionList listWorldSelection) {
    refreshList(listWorldSelection, null);
  }

  private static void refreshList(WorldSelectionList listWorldSelection,
      Supplier<String> supplier) {
    Minecraft mc = Minecraft.getInstance();
    SaveFormat saveformat = mc.getSaveLoader();
    List<WorldSummary> list;

    try {
      list = saveformat.getSaveList();
    } catch (AnvilConverterException anvilconverterexception) {
      CherishedWorlds.LOGGER.error("Couldn't load level list", anvilconverterexception);
      mc.displayGuiScreen(
          new ErrorScreen(new TranslationTextComponent("selectWorld.unable_to_load"),
              anvilconverterexception.getMessage()));
      return;
    }

    List<WorldSelectionList.Entry> entries = listWorldSelection.children();
    entries.clear();
    Iterator<WorldSummary> iter = list.listIterator();
    List<WorldSummary> favorites = Lists.newArrayList();

    while (iter.hasNext()) {
      WorldSummary summ = iter.next();

      if (FavoriteWorldsList.isFavorite(summ.getFileName())) {
        favorites.add(summ);
        iter.remove();
      }
    }
    Collections.sort(favorites);
    Collections.sort(list);
    String s = supplier == null ? "" : supplier.get().toLowerCase(Locale.ROOT);

    for (WorldSummary worldsummary : favorites) {

      if (s.isEmpty() || worldsummary.getDisplayName().toLowerCase(Locale.ROOT).contains(s)
          || worldsummary.getFileName().toLowerCase(Locale.ROOT).contains(s)) {
        entries.add(
            listWorldSelection.new Entry(listWorldSelection, worldsummary, mc.getSaveLoader()));
      }
    }

    for (WorldSummary worldsummary : list) {

      if (s.isEmpty() || worldsummary.getDisplayName().toLowerCase(Locale.ROOT).contains(s)
          || worldsummary.getFileName().toLowerCase(Locale.ROOT).contains(s)) {
        entries.add(
            listWorldSelection.new Entry(listWorldSelection, worldsummary, mc.getSaveLoader()));
      }
    }

    WorldSelectionList.Entry entry = listWorldSelection.getSelected();

    if (entry != null) {
      Button deleteButton = ReflectionAccessor.getDeleteButton(listWorldSelection.getGuiWorldSelection());
      disableDeletingFavorites(entry, deleteButton);
    }
  }

  private static void disableDeletingFavorites(Entry entry, Button deleteButton) {
    WorldSummary summary = ReflectionAccessor.getWorldSummary(entry);
    boolean isFavorite = summary != null && FavoriteWorldsList.isFavorite(summary.getFileName());
    deleteButton.active = !isFavorite;
  }

  @SubscribeEvent
  public void onGuiDrawScreen(GuiScreenEvent.DrawScreenEvent.Post evt) {
    Screen gui = evt.getGui();

    if (gui instanceof WorldSelectionScreen) {
      WorldSelectionScreen worldSelect = (WorldSelectionScreen) gui;
      WorldSelectionList selectionList = ReflectionAccessor.getSelectionList(worldSelect);

      if (selectionList != null) {

        for (int i = 0; i < selectionList.children().size(); i++) {
          WorldSelectionList.Entry entry = selectionList.children().get(i);

          if (entry != null) {
            WorldSummary summary = ReflectionAccessor.getWorldSummary(entry);

            if (summary != null) {
              boolean isFavorite = FavoriteWorldsList.isFavorite(summary.getFileName());
              ResourceLocation icon = isFavorite ? STAR_ICON : EMPTY_STAR_ICON;
              int top = (int) (selectionList.getTop() + 15 + 36 * i - selectionList
                  .getScrollAmount());
              int x = evt.getGui().width / 2 - 148;

              if (top < (selectionList.getBottom() - 8) && top > selectionList.getTop()) {
                Minecraft.getInstance().getTextureManager().bindTexture(icon);
                AbstractGui.blit(x, top, 0, 0, 9, 9, 9, 9);
              }
              int mouseX = evt.getMouseX();
              int mouseY = evt.getMouseY();

              if (mouseY >= top && mouseY <= (top + 9) && mouseX >= x && mouseX <= (x + 9)) {
                String s = new TranslationTextComponent(
                    "selectWorld." + CherishedWorlds.MODID + "." + (isFavorite ? "unfavorite"
                        : "favorite")).getFormattedText();
                GuiUtils
                    .drawHoveringText(Lists.newArrayList(s), mouseX, mouseY, gui.width, gui.height,
                        -1, Minecraft.getInstance().fontRenderer);
              }
            }
          }
        }
      }
    }
  }

  @SubscribeEvent
  public void onGuiMouseClick(GuiScreenEvent.MouseClickedEvent.Pre evt) {
    Screen gui = evt.getGui();

    if (gui instanceof WorldSelectionScreen) {
      WorldSelectionScreen worldSelect = (WorldSelectionScreen) gui;
      WorldSelectionList selectionList = ReflectionAccessor.getSelectionList(worldSelect);

      if (selectionList != null) {

        for (int i = 0; i < selectionList.children().size(); i++) {
          WorldSelectionList.Entry entry = selectionList.children().get(i);

          if (entry != null) {
            WorldSummary summary = ReflectionAccessor.getWorldSummary(entry);

            if (summary != null) {
              boolean isFavorite = FavoriteWorldsList.isFavorite(summary.getFileName());
              int top = (int) (selectionList.getTop() + 15 + 36 * i - selectionList
                  .getScrollAmount());
              int x = evt.getGui().width / 2 - 148;
              double mouseX = evt.getMouseX();
              double mouseY = evt.getMouseY();

              if (mouseY >= top && mouseY <= (top + 9) && mouseX >= x && mouseX <= (x + 9)) {
                String s = summary.getFileName();

                if (isFavorite) {
                  FavoriteWorldsList.removeFavorite(s);
                } else {
                  FavoriteWorldsList.addFavorite(s);
                }
                FavoriteWorldsList.saveFavoritesList();
                refreshList(selectionList);
                return;
              }
            }
          }
        }
      }
    }
  }

  @SubscribeEvent
  public void onGuiMouseClicked(GuiScreenEvent.MouseClickedEvent.Post evt) {
    Screen gui = evt.getGui();

    if (gui instanceof WorldSelectionScreen) {
      WorldSelectionScreen worldSelect = (WorldSelectionScreen) gui;
      WorldSelectionList selectionList = ReflectionAccessor.getSelectionList(worldSelect);

      if (selectionList != null) {
        WorldSelectionList.Entry entry = selectionList.getSelected();

        if (entry != null) {
          Button deleteButton = ReflectionAccessor.getDeleteButton(worldSelect);
          disableDeletingFavorites(entry, deleteButton);
        }
      }
    }
  }

  @SubscribeEvent
  public void onGuiInit(GuiScreenEvent.InitGuiEvent.Post evt) {
    Screen gui = evt.getGui();

    if (gui instanceof WorldSelectionScreen) {
      WorldSelectionScreen worldSelect = (WorldSelectionScreen) gui;
      WorldSelectionList selectionList = ReflectionAccessor.getSelectionList(worldSelect);
      TextFieldWidget textField = ReflectionAccessor.getTextField(worldSelect);

      if (selectionList != null) {
        FavoriteWorldsList.loadFavoritesList();
        textField.func_212954_a((s) -> refreshList(selectionList, () -> s));
        refreshList(selectionList);
      }
    }
  }
}
