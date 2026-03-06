package com.illusivesoulworks.cherishedworlds;

import com.illusivesoulworks.spectrelib.config.SpectreConfigInitializer;

public class CherishedWorldsConfigInitializer implements SpectreConfigInitializer {

  @Override
  public void onInitializeConfig() {
    CherishedWorldsCommonMod.setupConfig();
  }
}
