package com.illusivesoulworks.cherishedworlds.integration;

public class FancyMenuIntegration {

  public static boolean isNavigating() {
    try {
      Class<?> customizationOverlayClass =
          Class.forName("de.keksuccino.fancymenu.customization.overlay.CustomizationOverlay");
      Object menuBar = customizationOverlayClass.getMethod("getCurrentMenuBarInstance")
          .invoke(null);

      if (menuBar == null) {
        return false;
      }
      Object result = menuBar.getClass().getMethod("isEntryContextMenuOpen").invoke(menuBar);
      return result instanceof Boolean && (Boolean) result;
    } catch (ReflectiveOperationException | LinkageError ex) {
      return false;
    }
  }
}
