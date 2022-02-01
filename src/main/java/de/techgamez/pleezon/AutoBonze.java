package de.techgamez.pleezon;

import de.techgamez.pleezon.config.ConfigBoolean;
import de.techgamez.pleezon.config.ConfigElement;
import de.techgamez.pleezon.config.ConfigInt;

import net.labymod.api.LabyModAddon;
import net.labymod.api.events.MessageSendEvent;
import net.labymod.api.events.MouseInputEvent;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.NumberElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Consumer;
import net.labymod.utils.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

public class AutoBonze extends LabyModAddon {

    public static boolean isEnabled = false;
    public static ConfigInt slotBow = new ConfigInt("slotBow",0);
    public static ConfigInt slotSword = new ConfigInt("slotSword",1);
    public static ConfigInt cycleDelay = new ConfigInt("delay",5000);
    public static ConfigInt swapDelay = new ConfigInt("swapDelay",10);
    public static ConfigBoolean lockDuraAndQDrop = new ConfigBoolean("lockDuraAndQDrop",false);

    public void onEnable() {
        ConfigElement.init();
        getApi().registerForgeListener(this);

    }




    public void cycle(){
        LabyMod.getInstance().displayMessageInChat("started cycle");
        PlayerController.sendSlotChange(slotBow.getValue());
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LabyMod.getInstance().displayMessageInChat("winding bow");
        PlayerController.sendShootBow(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(swapDelay.getValue());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                LabyMod.getInstance().displayMessageInChat("shot bow");
                PlayerController.sendSlotChange(slotSword.getValue());
                if (isEnabled) {
                    LabyMod.getInstance().displayMessageInChat("Repeating cycle in: " + cycleDelay.getValue());
                    try {
                        Thread.sleep(cycleDelay.getValue());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    AutoBonze.this.cycle();
                }
            }
        });
    }

    public void loadConfig() {

    }

    protected void fillSettings(List<SettingsElement> list) {
        list.add(new BooleanElement("Active", new ControlElement.IconData(Material.REDSTONE), new Consumer<Boolean>() {
            @Override
            public void accept(Boolean v) {
                isEnabled = v;
                System.out.println("enabled!");
                if (isEnabled) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            AutoBonze.this.cycle();
                        }
                    }).start();
                }
            }
        },false));

        list.add(new BooleanElement("lock durability & quick-dropping", new ControlElement.IconData(Material.DIAMOND_SWORD), new Consumer<Boolean>() {
            @Override
            public void accept(Boolean v) {
                lockDuraAndQDrop.setValue(v);
            }
        },lockDuraAndQDrop.getValue()));



        NumberElement slotBowElement = new NumberElement("slotBow",new ControlElement.IconData(Material.BOW),slotBow.getValue());
        slotBowElement.setMaxValue(8);
        slotBowElement.setMinValue(0);
        slotBowElement.addCallback(new Consumer<Integer>() {
            @Override
            public void accept(Integer v) {
                slotBow.setValue(v);
            }
        });
        list.add(slotBowElement);

        NumberElement slotSwordElement = new NumberElement("slotSword",new ControlElement.IconData(Material.DIAMOND_SWORD),slotSword.getValue());
        slotSwordElement.setMaxValue(8);
        slotSwordElement.setMinValue(0);
        slotSwordElement.addCallback(new Consumer<Integer>() {
            @Override
            public void accept(Integer v) {
                slotSword.setValue(v);
            }
        });
        list.add(slotSwordElement);

        NumberElement cycleDelayElement = new NumberElement("cycleDelay (MS)",new ControlElement.IconData(Material.WATCH),cycleDelay.getValue());
        cycleDelayElement.setMaxValue(20000);
        cycleDelayElement.setMinValue(0);
        cycleDelayElement.setSteps(50);
        cycleDelayElement.addCallback(new Consumer<Integer>() {
            @Override
            public void accept(Integer v) {
                cycleDelay.setValue(v);
            }
        });
        list.add(cycleDelayElement);

        NumberElement swapDelayElement = new NumberElement("swapDelay (MS)",new ControlElement.IconData(Material.ARROW),swapDelay.getValue());
        swapDelayElement.setMaxValue(10000);
        swapDelayElement.setMinValue(0);
        swapDelayElement.addCallback(new Consumer<Integer>() {
            @Override
            public void accept(Integer v) {
                swapDelay.setValue(v);
            }
        });
        list.add(swapDelayElement);




    }
}
