package mod.linguardium.invmgmtcompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class JsonConfigLoader {
    public static Config config = new Config();
    static Path configPath = FabricLoader.getInstance().getConfigDir().resolve("inventory_management_compat.json");
    static Gson GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    public static void init() {
        //config.inventoryClasses.add("tech.thatgravyboat.ironchests.common.blocks.GenericChestMenu");
        try {
            if (Files.exists(configPath)) {
                config = GSON.fromJson(Files.newBufferedReader(configPath), Config.class);
            } else {
                save();
            }
        } catch(JsonIOException | IOException e) {
            e.printStackTrace();
        }
    }
    public static void save() {
        try {
            Files.writeString(configPath, GSON.toJson(config));
        }catch(IOException e) {
            System.out.println(e);
        }
    }
    public static class Config {
        public ArrayList<String> inventoryClasses = new ArrayList<>();
    }
}
