package jp.msfblue1.backpack;

import jp.msfblue1.backpack.DataManager.ClickMessage;
import jp.msfblue1.backpack.DataManager.Data;
import jp.msfblue1.backpack.DataManager.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * Created by msfblue1 on 2017/11/07.
 */
public class CommandManager implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            DataManager manager = new DataManager(((Player)sender).getUniqueId().toString());
            switch (args.length){
                case 0:
                    sender.sendMessage(ChatColor.BLUE+"[INFO] "+ChatColor.GREEN+"/pack help でヘルプを表示します");
                    sender.sendMessage(ChatColor.BLUE+"[INFO] "+ChatColor.GREEN+"一覧が表示されているチャット欄をクリックしてパックを開くこともできます");
                    if(manager.getNameList().size() > 0){
                        sender.sendMessage(Backpack.PREFIX+ChatColor.GOLD+"++++++  バックパック一覧  +++++");
                        for (int i = 0; i < manager.getNameList().size(); i++) {
                            String name = manager.getNameList().get(i);
                            ClickMessage.sendCilckableMessages((Player)sender,"/pack "+name,ChatColor.YELLOW + name,ChatColor.BLUE+"["+String.valueOf(i+1)+"] ","クリックでオープン");
                        }
                        sender.sendMessage(Backpack.PREFIX+ChatColor.GOLD+"++++++  バックパック一覧  +++++");
                    }else{
                        sender.sendMessage(Backpack.PREFIX+ChatColor.AQUA +"バックパックは空です！新しく作りましょう！");
                    }
                    break;
                case 1:
                    if("help".equalsIgnoreCase(args[0])){
                        sender.sendMessage(Backpack.PREFIX+ChatColor.BLUE+"/pack : 保有中のパックの一覧を表示します");
                        sender.sendMessage(Backpack.PREFIX+ChatColor.BLUE+"/pack help : このプラグインのヘルプを表示します");
                        sender.sendMessage(Backpack.PREFIX+ChatColor.BLUE+"/pack パック名 : パックのGUIを表示します");
                        sender.sendMessage(Backpack.PREFIX+ChatColor.BLUE+"/pack delete パック名 : 指定したパックを削除します");
                        sender.sendMessage(Backpack.PREFIX+ChatColor.BLUE+"/pack new パック名 : 指定した名前で新しいパックを作成します");
                        sender.sendMessage(Backpack.PREFIX+ChatColor.BLUE+"/pack search アイテムの種類 : パック内を指定したアイテムの種類で検索します");
                    }else{
                        if(manager.getNameList().contains(args[0])){
                            manager.openGUI(args[0],(Player)sender);
                        }else{
                            sender.sendMessage(Backpack.PREFIX+ChatColor.RED+"該当するバックパックが見つかりません");
                        }
                    }
                    break;
                case 2:
                    switch (args[0]){
                        case "delete":
                            if(manager.getNameList().contains(args[1])){
                                manager.getData(args[1]).ifPresent(d->{
                                    manager.deleteData(d);
                                });
                                sender.sendMessage(Backpack.PREFIX+ChatColor.GREEN+"操作に成功しました");
                            }else{
                                sender.sendMessage(Backpack.PREFIX+ChatColor.RED+"該当するバックパックが見つかりません");
                            }
                            break;
                        case "new":
                            if("help".equalsIgnoreCase(args[1])){
                                sender.sendMessage(Backpack.PREFIX+ChatColor.RED+"コマンドを名前に設定することはできません");
                                return true;
                            }
                            if(!(manager.getNameList().size() < Backpack.howmany)){
                                sender.sendMessage(Backpack.PREFIX+ChatColor.RED+"所持可能なバックパックの数が制限を超えています");
                                return true;
                            }
                            if(!manager.getNameList().contains(args[1])) {
                                Data data = new Data();
                                data.setInventory(Bukkit.createInventory(null, 27, args[1]));
                                data.setFilename(args[1]);
                                data.setUUID(((Player) sender).getUniqueId().toString());
                                manager.saveData(data);
                                sender.sendMessage(Backpack.PREFIX+ChatColor.GREEN+"操作に成功しました");
                            }else{
                                sender.sendMessage(Backpack.PREFIX+ChatColor.RED+"すでに同じ名前のバックパックが存在します");
                            }
                            break;
                        case "search":
                            Material m = Material.getMaterial(args[1].toUpperCase());
                            if(m == null){
                                sender.sendMessage(Backpack.PREFIX+ChatColor.RED+"指定した種類のアイテムは見つかりませんでした");
                                return true;
                            }
                            sender.sendMessage(Backpack.PREFIX+ChatColor.GOLD+"++++++  "+args[1]+" の検索結果  +++++");
                            Map<String,Integer > result = manager.searchData(m);
                            int c = 0;
                            for(String key : result.keySet()){
                                String click = ChatColor.YELLOW + key;
                                String Mes = ChatColor.BLUE+String.valueOf(c+1)+"件目 "+ChatColor.GOLD+"個数: "+String.valueOf(result.get(key))+" "+ ChatColor.YELLOW+"名前: ";
                                ClickMessage.sendCilckableMessages((Player)sender,"/pack "+key,click,Mes,"クリックでオープン");
                                c += 1;
                            }
                    }
            }
            return false;
        }else{
            sender.sendMessage(Backpack.PREFIX+ChatColor.RED+"プレーヤーから実行してください");
        }
        return true;
    }
}
