package com.illusivesoulworks.cherishedworlds.client.favorites.impl;

import com.illusivesoulworks.cherishedworlds.client.favorites.AbstractFavoritesListWidget;
import com.illusivesoulworks.cherishedworlds.mixin.core.AccessorWorldSelectionList;
import com.illusivesoulworks.cherishedworlds.mixin.core.AccessorWorldSelectionScreen;
import java.util.List;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import net.minecraft.world.level.storage.LevelSummary;

public class FavoriteWorldsWidget
    extends AbstractFavoritesListWidget<WorldSelectionList, WorldSelectionList.Entry> {

  public FavoriteWorldsWidget(SelectWorldScreen selectWorldScreen) {
    super(selectWorldScreen);
  }

  public EditBox getSearchBox() {
    return ((AccessorWorldSelectionScreen) this.parentScreen).getSearchBox();
  }

  @Override
  public WorldSelectionList getList() {
    return ((AccessorWorldSelectionScreen) this.parentScreen).getList();
  }

  @Override
  protected String getKey(WorldSelectionList.Entry entry) {
    LevelSummary summary = entry.getLevelSummary();
    return summary != null ? summary.getLevelId() : "";
  }

  @Override
  protected void updateList() {

    if (this.list != null) {
      EditBox textField = this.getSearchBox();
      String filter = "";

      if (textField != null) {
        filter = textField.getValue();
      }
      AccessorWorldSelectionList accessorWorldSelectionList =
          (AccessorWorldSelectionList) this.list;
      List<LevelSummary> levelSummaries =
          accessorWorldSelectionList.getCurrentlyDisplayedLevels();

      if (levelSummaries != null) {
        accessorWorldSelectionList.callFillLevels(filter, levelSummaries);
      }
    }
  }
}
