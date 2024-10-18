package ru.dumpcave.freezing.util;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import ru.dumpcave.freezing.Freezing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class YamlDelete {
    private static Freezing freezing;
    private static String path = freezing.getFilePath() + "players_in_freeze.yml";

    public void deleteName(String name) {
//        System.out.println(path);
//        try {
//            Yaml yaml = new Yaml(new Constructor(Map.class));
//            FileInputStream fileInputStream = new FileInputStream(new File(path));
//            Map<String, Object> data = yaml.load(fileInputStream);
//            if (data.containsKey(name))
//                data.remove(name);
//            else
//                System.out.println("В файле нет элемента " + name);
//            FileWriter writer = new FileWriter(path);
//            yaml.dump(data, writer);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}