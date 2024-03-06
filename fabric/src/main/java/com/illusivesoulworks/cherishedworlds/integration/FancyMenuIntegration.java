package com.illusivesoulworks.cherishedworlds.integration;

import de.keksuccino.fancymenu.customization.overlay.CustomizationOverlay;
import de.keksuccino.fancymenu.customization.overlay.CustomizationOverlayMenuBar;

public class FancyMenuIntegration {

  public static boolean isNavigating() {
    CustomizationOverlayMenuBar menuBar = CustomizationOverlay.getCurrentMenuBarInstance();
    return menuBar != null && menuBar.isEntryContextMenuOpen();
  }
}
