package ru.dumpcave.freezing.util;

import org.yaml.snakeyaml.Yaml;
import ru.dumpcave.freezing.Freezing;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public class YamlReader {
    private final Freezing freezing;

    public YamlReader(Freezing freezing) {
        this.freezing = freezing;
    }

    public boolean isFrozen(String name) {
        File file = new File(freezing.getFilePath());
        Yaml yaml = new Yaml();
        try (InputStream inputStream = YamlReader.class.getClassLoader().getResourceAsStream(freezing.getFilePath() + "players_in_freeze.yml")) {
            if (inputStream == null) {
                System.err.println("YAML file not found.");
                return false;
            }
            List<String> data = yaml.load(inputStream);
            return data != null && data.contains(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
