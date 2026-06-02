package currency;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.gson.reflect.TypeToken;

import tinyrpg.TinyRPG;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CurrencyStorage
{
    private TinyRPG env;
    private File dataFile;
    private final Gson gson;

    // Before entering this function, data folder should exist
    public CurrencyStorage(TinyRPG env)
    {
        this.env = env;
        this.gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();

        File dataFolder = new File(env.getDataFolder(), "currency");
        if (!dataFolder.exists())
            dataFolder.mkdirs();

        dataFile = new File(dataFolder, "data.json");
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();

                try (FileWriter writer = new java.io.FileWriter(dataFile)) {
                    writer.write("{}");
                }
            } catch (Exception e) {
                // Should not reach here
                env.panic("currency: failed to created data file");
            }
        }
    }

    public Map<UUID, Long> loadData()
    {
        try (FileReader reader = new FileReader(dataFile)) {
            Map<UUID, Long> currency_data = gson.fromJson(reader, new TypeToken<Map<UUID, Long>>() {
            }.getType());

            if (currency_data == null) {
                env.panic("currency: data file is empty or corrupted");

                return null;
            }

            return currency_data;
        } catch (Exception e) {
            env.panic("currency: error while reading data file. Reason: " + e.getMessage());

            return new HashMap<>();
        }
    }

    public boolean storeData(Map<UUID, Long> currency_data)
    {
        try (FileWriter writer = new FileWriter(dataFile)) {
            gson.toJson(currency_data, writer);

            return true;
        } catch (Exception e) {
            env.log("currency: error while writing data file. Reason: " + e.getMessage());

            return false;
        }
    }
}
