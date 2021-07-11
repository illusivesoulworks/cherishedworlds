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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.FatalErrorScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.level.storage.LevelStorageException;
import net.minecraft.world.level.storage.LevelSummary;
import top.theillusivec4.cherishedworlds.CherishedWorldsMod;
import top.theillusivec4.cherishedworlds.mixin.AccessorEntryListWidget;
import top.theillusivec4.cherishedworlds.mixin.AccessorSelectWorldScreen;
import top.theillusivec4.cherishedworlds.mixin.AccessorWorldListEntry;

public class FavoriteWorlds implements FavoritesManager<SelectWorldScreen> {

  public static FavoriteWorlds INSTANCE = new FavoriteWorlds();

  @Override
  public void init(SelectWorldScreen screen) {
    AccessorSelectWorldScreen accessor = ((AccessorSelectWorldScreen) screen);
    WorldListWidget selectionList = accessor.getLevelList();
    TextFieldWidget textField = accessor.getSearchBox();

    if (selectionList != null) {
      FavoritesList.load();
      textField.setChangedListener((s) -> refreshList(selectionList, () -> s));
      refreshList(selectionList);
    }
  }

  @Override
  public void render(SelectWorldScreen screen, MatrixStack matrices, int mouseX, int mouseY) {
    AccessorSelectWorldScreen accessor = ((AccessorSelectWorldScreen) screen);
    WorldListWidget selectionList = accessor.getLevelList();

    if (selectionList != null) {

      for (int i = 0; i < selectionList.children().size(); i++) {
        WorldListWidget.Entry entry = selectionList.children().get(i);

        if (entry != null) {
          AccessorWorldListEntry accessorEntry = ((AccessorWorldListEntry) (Object) entry);
          LevelSummary summary = accessorEntry.getLevel();

          if (summary != null) {
            AccessorEntryListWidget accessorWidget = ((AccessorEntryListWidget) selectionList);
            boolean isFavorite = FavoritesList.contains(summary.getName());
            renderIcon(screen, matrices, mouseX, mouseY, selectionList.getScrollAmount(), i,
                accessorWidget.getTop(), accessorWidget.getBottom(), isFavorite);
          }
        }
      }
    }
  }

  @Override
  public void click(SelectWorldScreen screen, double mouseX, double mouseY) {
    AccessorSelectWorldScreen accessor = (AccessorSelectWorldScreen) screen;
    WorldListWidget worldList = accessor.getLevelList();

    if (worldList != null) {

      for (int i = 0; i < worldList.children().size(); i++) {
        WorldListWidget.Entry entry = worldList.children().get(i);

        if (entry != null) {
          LevelSummary summary = ((AccessorWorldListEntry) (Object) entry).getLevel();

          if (summary != null) {
            boolean isFavorite = FavoritesList.contains(summary.getName());
            int top = (int) (((AccessorEntryListWidget) worldList).getTop() + 15 + 36 * i -
                worldList.getScrollAmount());
            int x = screen.width / 2 - getOffset();

            if (mouseY >= top && mouseY <= (top + 9) && mouseX >= x && mouseX <= (x + 9)) {
              String s = summary.getName();

              if (isFavorite) {
                FavoritesList.remove(s);
              } else {
                FavoritesList.add(s);
              }
              FavoritesList.save();
              refreshList(worldList);
              return;
            }
          }
        }
      }
    }
  }

  @Override
  public void clicked(SelectWorldScreen screen) {
    AccessorSelectWorldScreen accessor = ((AccessorSelectWorldScreen) screen);
    WorldListWidget selectionList = accessor.getLevelList();

    if (selectionList != null) {
      WorldListWidget.Entry entry = selectionList.getSelectedOrNull();

      if (entry != null) {
        ButtonWidget deleteButton = accessor.getDeleteButton();
        disableDeletingFavorites(entry, deleteButton);
      }
    }
  }

  @Override
  public int getOffset() {
    return 148;
  }

  private static void refreshList(WorldListWidget worldList) {
    refreshList(worldList, null);
  }

  private static void refreshList(WorldListWidget worldList, Supplier<String> supplier) {
    MinecraftClient mc = MinecraftClient.getInstance();
    LevelStorage saveformat = mc.getLevelStorage();
    List<LevelSummary> list;

    try {
      list = saveformat.getLevelList();
    } catch (LevelStorageException saveexception) {
      CherishedWorldsMod.LOGGER.error("Couldn't load level list", saveexception);
      mc.setScreen(new FatalErrorScreen(new TranslatableText("selectWorld.unable_to_load"),
          new LiteralText(saveexception.getMessage())));
      return;
    }

    List<WorldListWidget.Entry> entries = worldList.children();
    entries.clear();
    Iterator<LevelSummary> iter = list.listIterator();
    List<LevelSummary> favorites = new ArrayList<>();

    while (iter.hasNext()) {
      LevelSummary summ = iter.next();

      if (FavoritesList.contains(summ.getName())) {
        favorites.add(summ);
        iter.remove();
      }
    }
    Collections.sort(favorites);
    Collections.sort(list);
    String s = supplier == null ? "" : supplier.get().toLowerCase(Locale.ROOT);

    for (LevelSummary worldsummary : favorites) {

      if (s.isEmpty() || worldsummary.getDisplayName().toLowerCase(Locale.ROOT).contains(s)
          || worldsummary.getName().toLowerCase(Locale.ROOT).contains(s)) {
        entries.add(worldList.new Entry(worldList, worldsummary));
      }
    }

    for (LevelSummary worldsummary : list) {

      if (s.isEmpty() || worldsummary.getDisplayName().toLowerCase(Locale.ROOT).contains(s)
          || worldsummary.getName().toLowerCase(Locale.ROOT).contains(s)) {
        entries.add(worldList.new Entry(worldList, worldsummary));
      }
    }

    WorldListWidget.Entry entry = worldList.getSelectedOrNull();

    if (entry != null) {
      ButtonWidget deleteButton =
          ((AccessorSelectWorldScreen) worldList.getParent()).getDeleteButton();
      disableDeletingFavorites(entry, deleteButton);
    }
  }

  private static void disableDeletingFavorites(WorldListWidget.Entry entry,
                                               ButtonWidget deleteButton) {
    LevelSummary summary = ((AccessorWorldListEntry) (Object) entry).getLevel();
    boolean isFavorite = summary != null && FavoritesList.contains(summary.getName());
    deleteButton.active = !isFavorite;
  }
}
