package de.techgamez.pleezon;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0EPacketClickWindow;

import java.lang.reflect.Field;

public class PacketHandler {
    public static boolean shouldSendPacket(Packet<?> packet) {
        boolean b = !isPacketBlacklist(packet);
        if(b)logSend(packet);
        return b;

        // net.minecraft.network.play.client.C07PacketPlayerDigging             hit & drop
        // net.minecraft.network.play.client.C0APacketAnimation                 drop
        // net.minecraft.network.play.client.C02PacketUseEntity                 drop
        // net.minecraft.network.play.client.C0EPacketClickWindow               drop
    }
    private static void logSend(Packet<?> packet){
        if(!packet.getClass().getCanonicalName().contains("C03PacketPlayer"))
        System.out.println("sending " + packet.getClass().getCanonicalName());
    }

    private static boolean isPacketBlacklist(Packet<?> packet){
        boolean ret = false;


        if(AutoBonze.lockDuraAndQDrop.getValue() && Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().thePlayer.inventory != null && Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem() != null && Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().getItem().equals(Items.diamond_sword)){
            ret = packet instanceof C07PacketPlayerDigging || packet instanceof C0APacketAnimation || packet instanceof C02PacketUseEntity;
        }

        return ret;
    }
}
