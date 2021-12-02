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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ErrorScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.storage.LevelStorageException;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.LevelSummary;
import net.minecraftforge.client.event.ScreenEvent;
import top.theillusivec4.cherishedworlds.CherishedWorldsMod;
import top.theillusivec4.cherishedworlds.mixin.core.WorldSelectionListEntryAccessor;
import top.theillusivec4.cherishedworlds.mixin.core.WorldSelectionScreenAccessor;

public class FavoriteWorlds implements IFavoritesManager<SelectWorldScreen> {

  @Override
  public void init(SelectWorldScreen screen) {
    WorldSelectionScreenAccessor accessor = (WorldSelectionScreenAccessor) screen;
    WorldSelectionList selectionList = accessor.getList();

    if (selectionList != null) {
      EditBox textField = accessor.getSearchBox();

      if (textField != null) {
        textField.setResponder((s) -> refreshList(selectionList, () -> s));
      }
      refreshList(selectionList);
    }
  }

  @SuppressWarnings("ConstantConditions")
  @Override
  public void draw(ScreenEvent.DrawScreenEvent.Post evt, SelectWorldScreen screen) {
    WorldSelectionScreenAccessor accessor = (WorldSelectionScreenAccessor) screen;
    WorldSelectionList selectionList = accessor.getList();

    if (selectionList != null) {

      for (int i = 0; i < selectionList.children().size(); i++) {
        WorldSelectionList.WorldListEntry entry = selectionList.children().get(i);

        if (entry != null) {
          WorldSelectionListEntryAccessor entryAccessor =
              (WorldSelectionListEntryAccessor) (Object) entry;
          LevelSummary summary = entryAccessor.getWorldSummary();

          if (summary != null) {
            boolean isFavorite = FavoritesList.contains(summary.getLevelId());
            drawIcon(evt, screen, i, isFavorite, selectionList.getTop(),
                selectionList.getScrollAmount(), selectionList.getBottom());
          }
        }
      }
    }
  }

  @SuppressWarnings("ConstantConditions")
  @Override
  public void click(ScreenEvent.MouseClickedEvent.Pre evt, SelectWorldScreen screen) {
    WorldSelectionScreenAccessor accessor = (WorldSelectionScreenAccessor) screen;
    WorldSelectionList selectionList = accessor.getList();

    if (selectionList != null) {

      for (int i = 0; i < selectionList.children().size(); i++) {
        WorldSelectionList.WorldListEntry entry = selectionList.children().get(i);

        if (entry != null) {
          WorldSelectionListEntryAccessor entryAccessor =
              (WorldSelectionListEntryAccessor) (Object) entry;
          LevelSummary summary = entryAccessor.getWorldSummary();

          if (summary != null) {
            boolean isFavorite = FavoritesList.contains(summary.getLevelId());
            int top = (int) (selectionList.getTop() + 15 + 36 * i - selectionList
                .getScrollAmount());
            int x = evt.getScreen().width / 2 - getOffset();
            double mouseX = evt.getMouseX();
            double mouseY = evt.getMouseY();

            if (mouseY >= top && mouseY <= (top + 9) && mouseX >= x && mouseX <= (x + 9)) {
              String s = summary.getLevelId();

              if (isFavorite) {
                FavoritesList.remove(s);
              } else {
                FavoritesList.add(s);
              }
              FavoritesList.save();
              refreshList(selectionList);
              return;
            }
          }
        }
      }
    }
  }

  @SuppressWarnings("ConstantConditions")
  @Override
  public void clicked(SelectWorldScreen screen) {
    WorldSelectionScreenAccessor accessor = (WorldSelectionScreenAccessor) screen;
    WorldSelectionList selectionList = accessor.getList();

    if (selectionList != null) {
      WorldSelectionList.WorldListEntry entry = selectionList.getSelected();

      if (entry != null) {
        WorldSelectionListEntryAccessor entryAccessor =
            (WorldSelectionListEntryAccessor) (Object) entry;
        LevelSummary summary = entryAccessor.getWorldSummary();
        Button deleteButton = accessor.getDeleteButton();

        if (deleteButton != null && summary != null) {
          disableDeletion(summary, deleteButton);
        }
      }
    }
  }

  @Override
  public int getOffset() {
    return 148;
  }

  private static void refreshList(WorldSelectionList listWorldSelection) {
    refreshList(listWorldSelection, null);
  }

  @SuppressWarnings("ConstantConditions")
  private static void refreshList(WorldSelectionList listWorldSelection,
                                  Supplier<String> supplier) {
    Minecraft mc = Minecraft.getInstance();
    LevelStorageSource saveformat = mc.getLevelSource();
    List<LevelSummary> list;

    try {
      list = saveformat.getLevelList();
    } catch (LevelStorageException anvilconverterexception) {
      CherishedWorldsMod.LOGGER.error("Couldn't load level list", anvilconverterexception);
      mc.setScreen(
          new ErrorScreen(new TranslatableComponent("selectWorld.unable_to_load"),
              new TextComponent(anvilconverterexception.getMessage())));
      return;
    }
    List<WorldSelectionList.WorldListEntry> entries = listWorldSelection.children();
    entries.clear();
    Iterator<LevelSummary> iter = list.listIterator();
    List<LevelSummary> favorites = new ArrayList<>();

    while (iter.hasNext()) {
      LevelSummary summ = iter.next();

      if (FavoritesList.contains(summ.getLevelId())) {
        favorites.add(summ);
        iter.remove();
      }
    }
    Collections.sort(favorites);
    Collections.sort(list);
    String s = supplier == null ? "" : supplier.get().toLowerCase(Locale.ROOT);

    for (LevelSummary worldsummary : favorites) {

      if (s.isEmpty() || worldsummary.getLevelName().toLowerCase(Locale.ROOT).contains(s)
          || worldsummary.getLevelId().toLowerCase(Locale.ROOT).contains(s)) {
        entries.add(listWorldSelection.new WorldListEntry(listWorldSelection, worldsummary));
      }
    }

    for (LevelSummary worldsummary : list) {

      if (s.isEmpty() || worldsummary.getLevelName().toLowerCase(Locale.ROOT).contains(s)
          || worldsummary.getLevelId().toLowerCase(Locale.ROOT).contains(s)) {
        entries.add(listWorldSelection.new WorldListEntry(listWorldSelection, worldsummary));
      }
    }
    WorldSelectionList.WorldListEntry entry = listWorldSelection.getSelected();

    if (entry != null) {
      WorldSelectionListEntryAccessor entryAccessor =
          (WorldSelectionListEntryAccessor) (Object) entry;
      LevelSummary summary = entryAccessor.getWorldSummary();
      Button deleteButton =
          ((WorldSelectionScreenAccessor) listWorldSelection.getScreen())
              .getDeleteButton();

      if (deleteButton != null && summary != null) {
        disableDeletion(summary, deleteButton);
      }
    }
  }

  private static void disableDeletion(@Nonnull LevelSummary summary, Button deleteButton) {
    deleteButton.active = !FavoritesList.contains(summary.getLevelId());
  }
}
