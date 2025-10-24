package net.minecraft.util;

import net.minecraft.Minecraft;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class FileUtil {
    public static void unpackFile(File file, String name) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            IOUtils.copy(Objects.requireNonNull(Minecraft.class.getClassLoader().getResourceAsStream(name)), fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
