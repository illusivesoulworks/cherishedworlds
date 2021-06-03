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

package top.theillusivec4.cherishedworlds.client;

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
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.storage.SaveFormat;
import net.minecraft.world.storage.WorldSummary;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.cherishedworlds.CherishedWorldsMod;

@SuppressWarnings("unused")
public class GuiEventsListener {

  private static final ResourceLocation STAR_ICON = new ResourceLocation(CherishedWorldsMod.MODID,
      "textures/gui/staricon.png");
  private static final ResourceLocation EMPTY_STAR_ICON = new ResourceLocation(
      CherishedWorldsMod.MODID, "textures/gui/emptystaricon.png");

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
      CherishedWorldsMod.LOGGER.error("Couldn't load level list", anvilconverterexception);
      mc.displayGuiScreen(
          new ErrorScreen(new TranslationTextComponent("selectWorld.unable_to_load"),
              new StringTextComponent(anvilconverterexception.getMessage())));
      return;
    }
    List<WorldSelectionList.Entry> entries = listWorldSelection.getEventListeners();
    entries.clear();
    Iterator<WorldSummary> iter = list.listIterator();
    List<WorldSummary> favorites = Lists.newArrayList();

    while (iter.hasNext()) {
      WorldSummary summ = iter.next();

      if (FavoriteWorlds.contains(summ.getFileName())) {
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
        entries.add(listWorldSelection.new Entry(listWorldSelection, worldsummary));
      }
    }

    for (WorldSummary worldsummary : list) {

      if (s.isEmpty() || worldsummary.getDisplayName().toLowerCase(Locale.ROOT).contains(s)
          || worldsummary.getFileName().toLowerCase(Locale.ROOT).contains(s)) {
        entries.add(listWorldSelection.new Entry(listWorldSelection, worldsummary));
      }
    }
    WorldSelectionList.Entry entry = listWorldSelection.getSelected();

    if (entry != null) {
      Button deleteButton = Reflector
          .getDeleteButton(listWorldSelection.getGuiWorldSelection());
      disableDeletingFavorites(entry, deleteButton);
    }
  }

  private static void disableDeletingFavorites(Entry entry, Button deleteButton) {
    WorldSummary summary = Reflector.getWorldSummary(entry);
    boolean isFavorite = summary != null && FavoriteWorlds.contains(summary.getFileName());
    deleteButton.active = !isFavorite;
  }

  @SubscribeEvent
  public void onGuiDrawScreen(GuiScreenEvent.DrawScreenEvent.Post evt) {
    Screen gui = evt.getGui();

    if (gui instanceof WorldSelectionScreen) {
      WorldSelectionScreen worldSelect = (WorldSelectionScreen) gui;
      WorldSelectionList selectionList = Reflector.getSelectionList(worldSelect);

      if (selectionList != null) {

        for (int i = 0; i < selectionList.getEventListeners().size(); i++) {
          WorldSelectionList.Entry entry = selectionList.getEventListeners().get(i);

          if (entry != null) {
            WorldSummary summary = Reflector.getWorldSummary(entry);

            if (summary != null) {
              boolean isFavorite = FavoriteWorlds.contains(summary.getFileName());
              ResourceLocation icon = isFavorite ? STAR_ICON : EMPTY_STAR_ICON;
              int top = (int) (selectionList.getTop() + 15 + 36 * i - selectionList
                  .getScrollAmount());
              int x = evt.getGui().width / 2 - 148;

              if (top < (selectionList.getBottom() - 8) && top > selectionList.getTop()) {
                Minecraft.getInstance().getTextureManager().bindTexture(icon);
                AbstractGui.blit(evt.getMatrixStack(), x, top, 0, 0, 9, 9, 9, 9);
              }
              int mouseX = evt.getMouseX();
              int mouseY = evt.getMouseY();

              if (mouseY >= top && mouseY <= (top + 9) && mouseX >= x && mouseX <= (x + 9)) {
                TranslationTextComponent component = new TranslationTextComponent(
                    "selectWorld." + CherishedWorldsMod.MODID + "." + (isFavorite ? "unfavorite"
                        : "favorite"));
                gui.renderTooltip(evt.getMatrixStack(), component, mouseX, mouseY);
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
      WorldSelectionList selectionList = Reflector.getSelectionList(worldSelect);

      if (selectionList != null) {

        for (int i = 0; i < selectionList.getEventListeners().size(); i++) {
          WorldSelectionList.Entry entry = selectionList.getEventListeners().get(i);

          if (entry != null) {
            WorldSummary summary = Reflector.getWorldSummary(entry);

            if (summary != null) {
              boolean isFavorite = FavoriteWorlds.contains(summary.getFileName());
              int top = (int) (selectionList.getTop() + 15 + 36 * i - selectionList
                  .getScrollAmount());
              int x = evt.getGui().width / 2 - 148;
              double mouseX = evt.getMouseX();
              double mouseY = evt.getMouseY();

              if (mouseY >= top && mouseY <= (top + 9) && mouseX >= x && mouseX <= (x + 9)) {
                String s = summary.getFileName();

                if (isFavorite) {
                  FavoriteWorlds.remove(s);
                } else {
                  FavoriteWorlds.add(s);
                }
                FavoriteWorlds.save();
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
      WorldSelectionList selectionList = Reflector.getSelectionList(worldSelect);

      if (selectionList != null) {
        WorldSelectionList.Entry entry = selectionList.getSelected();

        if (entry != null) {
          Button deleteButton = Reflector.getDeleteButton(worldSelect);
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
      WorldSelectionList selectionList = Reflector.getSelectionList(worldSelect);
      TextFieldWidget textField = Reflector.getTextField(worldSelect);

      if (selectionList != null) {
        FavoriteWorlds.load();
        textField.setResponder((s) -> refreshList(selectionList, () -> s));
        refreshList(selectionList);
      }
    }
  }
}
