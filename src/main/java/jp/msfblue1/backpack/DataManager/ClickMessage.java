package jp.msfblue1.backpack.DataManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * Created by msfblue1 on 2017/11/08.
 */
public class ClickMessage {
    public static void sendCilckableMessages(Player p,String Command,String Mes,String HeadMes,String hover){
        Player player = p;
        try{
            String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
            Object nmsPlayer = player.getClass().getMethod("getHandle").invoke(player);
            Object connection = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
            Class<?> chatSerializer = Class.forName("net.minecraft.server." + version + ".IChatBaseComponent$ChatSerializer");
            Class<?> chatComponent = Class.forName("net.minecraft.server." + version + ".IChatBaseComponent");
            Class<?> packet = Class.forName("net.minecraft.server." + version + ".PacketPlayOutChat");
            Constructor constructor = packet.getConstructor(chatComponent);

            Object text = chatSerializer.getMethod("a", String.class).invoke(chatSerializer, "{\"text\":\""+HeadMes+"\",\"extra\":[{\"text\":\""+Mes+"\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\""+hover+"\"},\"clickEvent\":{\"action\":\"run_command\",\"value\":\""+Command+"\"}}]}");
            Object packetFinal = constructor.newInstance(text);

            Field field = packetFinal.getClass().getDeclaredField("a");
            field.setAccessible(true);
            field.set(packetFinal, text);
            connection.getClass().getMethod("sendPacket", Class.forName("net.minecraft.server." + version + ".Packet")).invoke(connection, packetFinal);

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
