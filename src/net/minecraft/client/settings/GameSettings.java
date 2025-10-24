
package net.minecraft.client.settings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import net.minecraft.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.input.Keyboard;

public class GameSettings {

    private static final String[] RENDER_DISTANCES;
    private static final String[] DIFFICULTIES;
    public boolean music;
    public boolean sound;
    public boolean invertMouse;
    public boolean showDebugInfo;
    public int renderDistance;
    public boolean viewBobbing;
    public boolean anaglyph;
    public boolean limitFramerate;
    public boolean fancyGraphics;
    public boolean modernBackground;
    public KeyBinding keyBindForward;
    public KeyBinding keyBindLeft;
    public KeyBinding keyBindBack;
    public KeyBinding keyBindRight;
    public KeyBinding keyBindJump;
    public KeyBinding keyBindInventory;
    public KeyBinding keyBindDrop;
    public KeyBinding keyBindSprint;
    public KeyBinding keyBindToggleFog;
    public KeyBinding keyBindSave;
    public KeyBinding keyBindLoad;
    public KeyBinding[] keyBindings;
    protected Minecraft mc;
    private final File optionsFile;
    public int numberOfOptions;
    public int difficulty;
    public int thirdPersonView;

    static {
        RENDER_DISTANCES = new String[]{"FAR", "NORMAL", "SHORT", "TINY"};
        DIFFICULTIES = new String[]{"Peaceful", "Easy", "Normal", "Hard"};
    }

    public GameSettings(final Minecraft mc, final File file) {
        this.music = true;
        this.sound = true;
        this.invertMouse = false;
        this.showDebugInfo = false;
        this.renderDistance = 0;
        this.viewBobbing = true;
        this.anaglyph = false;
        this.limitFramerate = false;
        this.fancyGraphics = true;
        this.modernBackground = false;
        this.keyBindForward = new KeyBinding("Forward", 17);
        this.keyBindLeft = new KeyBinding("Left", 30);
        this.keyBindBack = new KeyBinding("Back", 31);
        this.keyBindRight = new KeyBinding("Right", 32);
        this.keyBindJump = new KeyBinding("Jump", 57);
        this.keyBindInventory = new KeyBinding("Inventory", 23);
        this.keyBindDrop = new KeyBinding("Drop", 16);
        this.keyBindSprint = new KeyBinding("Sprint", 20);
        this.keyBindToggleFog = new KeyBinding("Toggle fog", 33);
        this.keyBindSave = new KeyBinding("Save location", 28);
        this.keyBindLoad = new KeyBinding("Load location", 19);
        this.keyBindings = new KeyBinding[]{this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindDrop, this.keyBindInventory, this.keyBindSprint, this.keyBindToggleFog, this.keyBindSave, this.keyBindLoad};
        this.numberOfOptions = 11;
        this.difficulty = 2;
        this.thirdPersonView = 0;
        this.mc = mc;
        this.optionsFile = new File(file, "options.txt");
        this.loadOptions();
    }

    public String getOptionDisplayString(final int integer) {
        return this.keyBindings[integer].keyDescription + ": " + Keyboard.getKeyName(this.keyBindings[integer].keyCode);
    }

    public void setKeyBinding(final int integer1, final int integer2) {
        this.keyBindings[integer1].keyCode = integer2;
        this.saveOptions();
    }

    public void setOptionFloatValue(final int integer1, final int integer2) {
        if (integer1 == 0) {
            this.music = !this.music;
            this.mc.sndManager.onSoundOptionsChanged();
        }
        if (integer1 == 1) {
            this.sound = !this.sound;
            this.mc.sndManager.onSoundOptionsChanged();
        }
        if (integer1 == 2) {
            this.invertMouse = !this.invertMouse;
        }
        if (integer1 == 3) {
            this.showDebugInfo = !this.showDebugInfo;
        }
        if (integer1 == 4) {
            this.renderDistance = (this.renderDistance + integer2 & 0x3);
        }
        if (integer1 == 5) {
            this.viewBobbing = !this.viewBobbing;
        }
        if (integer1 == 6) {
            this.anaglyph = !this.anaglyph;
            this.mc.renderEngine.refreshTextures();
        }
        if (integer1 == 7) {
            this.limitFramerate = !this.limitFramerate;
        }
        if (integer1 == 8) {
            this.difficulty = (this.difficulty + integer2 & 0x3);
        }
        if (integer1 == 9) {
            this.fancyGraphics = !this.fancyGraphics;
            this.mc.renderGlobal.fancyGraphics();
        }
        if (integer1 == 10) {
            this.modernBackground = !this.modernBackground;
        }
        this.saveOptions();
    }

    public String getOptionString(final int integer) {
        if (integer == 0) {
            return "Music: " + (this.music ? "ON" : "OFF");
        }
        if (integer == 1) {
            return "Sound: " + (this.sound ? "ON" : "OFF");
        }
        if (integer == 2) {
            return "Invert mouse: " + (this.invertMouse ? "ON" : "OFF");
        }
        if (integer == 3) {
            return "Show FPS: " + (this.showDebugInfo ? "ON" : "OFF");
        }
        if (integer == 4) {
            return "Render distance: " + GameSettings.RENDER_DISTANCES[this.renderDistance];
        }
        if (integer == 5) {
            return "View bobbing: " + (this.viewBobbing ? "ON" : "OFF");
        }
        if (integer == 6) {
            return "3d anaglyph: " + (this.anaglyph ? "ON" : "OFF");
        }
        if (integer == 7) {
            return "Limit framerate: " + (this.limitFramerate ? "ON" : "OFF");
        }
        if (integer == 8) {
            return "Difficulty: " + GameSettings.DIFFICULTIES[this.difficulty];
        }
        if (integer == 9) {
            return "Graphics: " + (this.fancyGraphics ? "FANCY" : "FAST");
        }
        if (integer == 10) {
            return "Modern background: " + (this.modernBackground ? "ON" : "OFF");
        }
        return "";
    }

    public void loadOptions() {
        try {
            if (!this.optionsFile.exists()) {
                return;
            }
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(this.optionsFile))) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    final String[] split = line.split(":");
                    if (split[0].equals("music")) {
                        this.music = split[1].equals("true");
                    }
                    if (split[0].equals("sound")) {
                        this.sound = split[1].equals("true");
                    }
                    if (split[0].equals("invertYMouse")) {
                        this.invertMouse = split[1].equals("true");
                    }
                    if (split[0].equals("showFrameRate")) {
                        this.showDebugInfo = split[1].equals("true");
                    }
                    if (split[0].equals("viewDistance")) {
                        this.renderDistance = Integer.parseInt(split[1]);
                    }
                    if (split[0].equals("bobView")) {
                        this.viewBobbing = split[1].equals("true");
                    }
                    if (split[0].equals("anaglyph3d")) {
                        this.anaglyph = split[1].equals("true");
                    }
                    if (split[0].equals("limitFramerate")) {
                        this.limitFramerate = split[1].equals("true");
                    }
                    if (split[0].equals("difficulty")) {
                        this.difficulty = Integer.parseInt(split[1]);
                    }
                    if (split[0].equals("fancyGraphics")) {
                        this.fancyGraphics = split[1].equals("true");
                    }
                    if (split[0].equals("modernBackground")) {
                        this.modernBackground = split[1].equals("true");
                    }
                    for (KeyBinding keyBinding : this.keyBindings) {
                        if (split[0].equals(("key_" + keyBinding.keyDescription))) {
                            keyBinding.keyCode = Integer.parseInt(split[1]);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            LogManager.getLogger().error("Failed to load options");
            ex.printStackTrace();
        }
    }

    public void saveOptions() {
        try {
            try (PrintWriter printWriter = new PrintWriter(new FileWriter(this.optionsFile))) {
                printWriter.println("music:" + this.music);
                printWriter.println("sound:" + this.sound);
                printWriter.println("invertYMouse:" + this.invertMouse);
                printWriter.println("showFrameRate:" + this.showDebugInfo);
                printWriter.println("viewDistance:" + this.renderDistance);
                printWriter.println("bobView:" + this.viewBobbing);
                printWriter.println("anaglyph3d:" + this.anaglyph);
                printWriter.println("limitFramerate:" + this.limitFramerate);
                printWriter.println("difficulty:" + this.difficulty);
                printWriter.println("fancyGraphics:" + this.fancyGraphics);
                printWriter.println("modernBackground:" + this.modernBackground);
                for (KeyBinding keyBinding : this.keyBindings) {
                    printWriter.println("key_" + keyBinding.keyDescription + ":" + keyBinding.keyCode);
                }
            }
        } catch (Exception ex) {
            LogManager.getLogger().error("Failed to save options");
            ex.printStackTrace();
        }
    }

}
