package jp.msfblue1.backpack.DataManager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by msfblue1 on 2017/11/05.
 */
public class DataManager {

    private List<Data> playerdatas = new LinkedList<>();

    public List<Data> getPlayerdatas() {
        return playerdatas;
    }

    public DataManager(String uuid){

        FileConfiguration d;
        for(File fdata : getSaveFile(uuid,null).listFiles()){
            d = YamlConfiguration.loadConfiguration(fdata);
            String Name = d.getString("inv=name","NO TITLE");
            Inventory inv = Bukkit.createInventory(null,54,Name);
            for (int i = 0; i < inv.getSize(); i++) {
                StringBuilder path = new StringBuilder();
                inv.setItem(i,d.getItemStack(path.append("inv=").append(i).toString(),new ItemStack(Material.AIR,1)));
            }
            Data data = new Data();
            data.setInventory(inv);
            data.setFilename(Name);
            data.setUUID(uuid);
            playerdatas.add(data);

        }
    }

    public void deleteData(Data deletedata){
        playerdatas.stream()
                .filter(d->d.getFilename().equalsIgnoreCase(deletedata.getFilename()))
                .findAny()
                .ifPresent(delete->{
                    File deletefile = getSaveFile(delete.getUUID(),delete.getFilename());
                    deletefile.delete();
                    playerdatas.remove(delete);
                });
    }

    public Map<String,Integer> searchData(Material m){
        Map<String,Integer> r = new HashMap<>();
        if(m == null){
            return r;
        }
        for (Data d : playerdatas){
            if(d != null && d.getInventory() != null){
                int c = 0;
                for(ItemStack stack : d.getInventory()){
                    if(stack == null){
                        continue;
                    }
                    if(stack.getType().equals(m)){
                        c = c+stack.getAmount();
                    }
                }
                if(c > 0){
                    r.put(d.getFilename(),c);
                }
            }
        }
        return r;
    }

    public void saveData(Data savedata){
        File savefile = getSaveFile(savedata.getUUID(),savedata.getFilename());
        YamlConfiguration s = new YamlConfiguration();
        s.set("inv=name",savedata.getFilename());
        for (int i = 0; i < savedata.getInventory().getSize(); i++) {
            StringBuilder path = new StringBuilder();
            path.append("inv=");
            ItemStack slot = savedata.getInventory().getItem(i);
            if(slot != null){
                s.set(path.append(i).toString(),slot);
            }else{
                s.set(path.append(i).toString(),new ItemStack(Material.AIR,1));
            }
        }
        try {
            s.save(savefile);
            if(playerdatas.contains(savedata)){
                playerdatas.remove(savedata);
            }
            playerdatas.add(savedata);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getSaveFile(String uuid,String name){
        File dir = new File("plugins/Backpack/DataBase/"+uuid);
        if(!dir.exists() || !dir.isDirectory()){
            dir.mkdirs();
        }
        if(name == null){
           return dir;
        }
        return new File(dir,name);
    }

    public Optional<Data> getData(String name){
        if(name == null){
            return null;
        }
        return playerdatas
                .stream()
                .filter(d->name.equalsIgnoreCase(d.getFilename()))
                .findAny();
    }

    public List<String> getNameList(){
        return playerdatas.stream()
                .map(e->e.getFilename())
                .collect(Collectors.toList());

    }

    public void openGUI(String name,Player p){
        if(p == null){
            return;
        }
        getData(name).ifPresent(d->{
            if(d.getInventory() != null){
                p.openInventory(getData(name).get().getInventory());
            }
        });
    }



}
