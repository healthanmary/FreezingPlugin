package ru.dumpcave.freezing;

import lombok.experimental.UtilityClass;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

@UtilityClass
public class Config {
    public static void load(FileConfiguration file) {
        final var cordSection = file.getConfigurationSection("freezeingpos");
        if (cordSection == null) {
            System.out.println("нет сеекции");
            throw new IllegalStateException("Error with freezeingpos");
        }
        parseCords(cordSection);
    }
    public static void parseCords(ConfigurationSection section) {
        Cords.xCord = section.getInt("x");
        Cords.yCord = section.getInt("y");
        Cords.zCord = section.getInt("z");
        Cords.worldname = section.getString("worldname");
}
    @UtilityClass
    public static class Cords {
        public static double xCord;
        public static double yCord;
        public static double zCord;
        public static String worldname;
    }
}
