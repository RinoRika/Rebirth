
package net.minecraft.client.sound;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SoundPool {

    private Random rand;
    private Map nameToSoundPoolEntriesMapping;
    public int numberOfSoundPoolEntries;

    public SoundPool() {
        this.rand = new Random();
        this.nameToSoundPoolEntriesMapping = (Map) new HashMap();
        this.numberOfSoundPoolEntries = 0;
    }

    public SoundPoolEntry addSound(String string, final File file) {
        try {
            final String string2 = string;
            for (string = string.substring(0, string.indexOf(".")); Character.isDigit(string.charAt(string.length() - 1)); string = string.substring(0, string.length() - 1)) {
            }
            string = string.replaceAll("/", ".");
            if (!this.nameToSoundPoolEntriesMapping.containsKey(string)) {
                this.nameToSoundPoolEntriesMapping.put(string, new ArrayList());
            }
            final SoundPoolEntry soundPoolEntry = new SoundPoolEntry(string2, file.toURI().toURL());
            ((List) this.nameToSoundPoolEntriesMapping.get(string)).add(soundPoolEntry);
            ++this.numberOfSoundPoolEntries;
            return soundPoolEntry;
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            throw new RuntimeException((Throwable) ex);
        }
    }

    public SoundPoolEntry getRandomSoundFromSoundPool(final String string) {
        final List list = (List) this.nameToSoundPoolEntriesMapping.get(string);
        if (list == null) {
            return null;
        }
        return (SoundPoolEntry) list.get(this.rand.nextInt(list.size()));
    }
}
