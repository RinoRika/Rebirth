package net.minecraft.client;

import java.io.File;
import net.minecraft.Minecraft;
import net.minecraft.util.UnexpectedThrowable;
import org.apache.logging.log4j.LogManager;


/**
 * Minecraft Inf-dev 20100618
 * @author Stars
 */
@SuppressWarnings("all")
public class MinecraftRunner {
    // Access minecraft here or through Minecraft.getMinecraft().
    public static Minecraft minecraft;
    private static Thread mainThread;

    public static void main(String[] args) {
        LogManager.getLogger().info("=== Stars: We are starting here... ===");
        System.setProperty("org.lwjgl.librarypath", new File("./natives").getAbsolutePath());

        // Initialize minecraft.
        minecraft = new Minecraft(800, 480, false) {
            @Override
            public void displayUnexpectedThrowable(UnexpectedThrowable ex) {
                ex.exception.printStackTrace();
            }
        };

        // Launch mincraft thread.
        minecraft.run();
    }

}
