package com.illusivesoulworks.cherishedworlds.client.favorites;

import com.illusivesoulworks.cherishedworlds.CherishedWorldsConstants;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public abstract class AbstractFavoritesWidget extends AbstractWidget {

  public static final Identifier FAVORITED_ICON =
      Identifier.fromNamespaceAndPath(CherishedWorldsConstants.MOD_ID,
                                      "textures/gui/favorite.png");
  public static final Identifier EMPTY_ICON =
      Identifier.fromNamespaceAndPath(CherishedWorldsConstants.MOD_ID,
                                      "textures/gui/empty.png");
  public static final Identifier HOVERED_FAVORITED_ICON =
      Identifier.fromNamespaceAndPath(CherishedWorldsConstants.MOD_ID,
                                      "textures/gui/hovered_favorite.png");
  public static final Identifier HOVERED_EMPTY_ICON =
      Identifier.fromNamespaceAndPath(CherishedWorldsConstants.MOD_ID,
                                      "textures/gui/hovered_empty.png");
  public static final Identifier FOCUSED_OUTLINE =
      Identifier.fromNamespaceAndPath(CherishedWorldsConstants.MOD_ID,
                                      "textures/gui/focused_outline.png");

  public static final int TEXTURE_SIZE = 9;

  protected final Screen parentScreen;

  public AbstractFavoritesWidget(Screen parentScreen, int width, int height) {
    super(0, 0, width, height, Component.empty());
    this.parentScreen = parentScreen;
  }

  public AbstractFavoritesWidget(Screen parentScreen) {
    this(parentScreen, 0, 0);
  }

  protected boolean areCoordinatesInIcon(int iconX, int iconY, int height, double x, double y) {
    return x >= iconX && y >= iconY && x < iconX + TEXTURE_SIZE && y < iconY + height;
  }
}
