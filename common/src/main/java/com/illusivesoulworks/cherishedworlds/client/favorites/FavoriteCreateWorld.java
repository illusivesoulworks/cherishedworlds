package com.illusivesoulworks.cherishedworlds.client.favorites;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;

public class FavoriteCreateWorld implements IFavoritesViewer<CreateWorldScreen> {

  private boolean isFavorited = false;

  @Override
  public void init(CreateWorldScreen screen) {
    isFavorited = false;
  }

  @Override
  public void draw(int mouseX, int mouseY, GuiGraphicsExtractor guiGraphics,
                   CreateWorldScreen screen) {
    drawIcon(mouseX, mouseY, guiGraphics, screen, 0, isFavorited, screen.height - 35, 0,
        screen.height + 25);
  }

  @Override
  public void click(int mouseX, int mouseY, CreateWorldScreen screen) {
    int top = screen.height - 20;
    int x = screen.width / 2 - getHorizontalOffset();

    if (mouseY >= top && mouseY <= (top + 9) && mouseX >= x && mouseX <= (x + 9)) {
      isFavorited = !isFavorited;
    }
  }

  @Override
  public void clicked(CreateWorldScreen screen) {
    // NO-OP
  }

  @Override
  public int getHorizontalOffset() {
    return 170;
  }

  public void saveFavorite(String levelId) {

    if (isFavorited) {
      isFavorited = false;
      FavoritesList.add(levelId);
      FavoritesList.save();
    }
  }
}
