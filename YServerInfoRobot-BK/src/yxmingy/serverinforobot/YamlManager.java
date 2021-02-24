package yxmingy.serverinforobot;

import com.google.common.base.Charsets;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class YamlManager {

    private JavaPlugin main;
    private String name;
    private File f;
    private YamlConfiguration cfg;

    public YamlManager(String name) {
        this(null, null, name);
    }

    public YamlManager(JavaPlugin main, File dir, String name) {
        if (dir == null) dir = main.getDataFolder();
        if (!dir.exists() || !dir.isDirectory()) dir.mkdirs();
        if (name == null) return;
        this.name = name.endsWith(".yml") ? name : name + ".yml";
        this.main = main;
        f = new File(dir, this.name);
        if (!f.exists()) loadFile();
        if (f.exists()) reload();
    }


    private void loadFile() {
        if (!f.exists()) {
            try {
                if (main != null) {
                    if (main.getResource(f.getName()) != null) {
                        printToFile(main.getResource(f.getName()), f);
                    }
                    else {
                        f.createNewFile();
                    }
                }
                else {
                    f.createNewFile();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Bukkit.broadcast("Error occurred when making config: " + name, "config.errors");
            }
        }
    }

    public void reload() {
        if (!f.exists())
            loadFile();
        cfg = YamlConfiguration.loadConfiguration(f);
        InputStream defConfigStream = main.getResource(f.getName());
        if (defConfigStream != null) {
            cfg.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
        }
    }

    public void save() {
        try {
            cfg.save(f);
            reload();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void printToFile(InputStream is, File toPrint) {
        try (FileOutputStream outputStream = new FileOutputStream(toPrint)) {
//            InputStreamReader in = new InputStreamReader(is);
//            String fullMessage = "";
//            char[] bytes = new char[12];
//            int len = 0;
//            if (is == null) return;
//            while((len = in.read(bytes)) != -1) {
//                fullMessage += new String(bytes,0,len);
//            }
//            byte[] strToBytes = fullMessage.getBytes();
            BufferedReader in = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            String read;
            StringBuffer content = new StringBuffer();
            while((read = in.readLine()) != null) {
                content.append(read + "\n");
            }
            outputStream.write(content.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getInt(String path, int def) {
        return (cfg.getInt(path, def));
    }

    public boolean getBoolean(String path, boolean def) {
        return (cfg.getBoolean(path, def));
    }

    public String getString(String path) {
        return (cfg.getString(path));
    }

    public String getString(String path, String def) {
        return (cfg.getString(path, def));
    }

    public List<String> getStringList(String path) {
        return cfg.getStringList(path);
    }

    public YamlConfiguration getConfig() {
        return cfg;
    }

    public void set(String path, Object value) {
        getConfig().set(path, value);
    }

}
