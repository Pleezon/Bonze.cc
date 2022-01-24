package de.techgamez.pleezon;

import net.labymod.main.LabyMod;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class PlayerController {

    public static void sendShootBow(Runnable runnable) {

        ItemStack s = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(AutoBonze.slotBow.getValue());
        sendPacket(new C08PacketPlayerBlockPlacement(s));
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        runnable.run();

    }

    public static void sendSlotChange(int slot) {
        C09PacketHeldItemChange p = new C09PacketHeldItemChange(slot);
        sendPacket(p);
        Minecraft.getMinecraft().thePlayer.inventory.currentItem = slot;
        LabyMod.getInstance().displayMessageInChat("Changed slot to: " +slot);

    }

    public static void sendPacket(Packet<?> p) {
        Minecraft.getMinecraft().getNetHandler().addToSendQueue(p);
    }
}
