package com.illusivesoulworks.cherishedworlds;

import com.illusivesoulworks.spectrelib.config.SpectreConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class CherishedWorldsConfig {

  public static final SpectreConfigSpec CLIENT_SPEC;
  public static final Client CLIENT;
  private static final String CONFIG_PREFIX = "gui." + CherishedWorldsConstants.MOD_ID + ".config.";

  static {
    Pair<Client, SpectreConfigSpec> specPair2 = new SpectreConfigSpec.Builder()
        .configure(Client::new);
    CLIENT_SPEC = specPair2.getRight();
    CLIENT = specPair2.getLeft();
  }

  public static class Client {

    public final SpectreConfigSpec.EnumValue<BackupType> backupWorldType;

    public Client(SpectreConfigSpec.Builder builder) {

      backupWorldType = builder
          .comment(
              "Determines which worlds prompt for a backup when loaded with a newer Minecraft version.")
          .translation(CONFIG_PREFIX + "backupWorldType")
          .defineEnum("backupWorldType", BackupType.ALL);
    }
  }

  public enum BackupType {
    ALL,
    FAVORITED,
    NONE
  }
}
