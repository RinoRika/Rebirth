
package net.minecraft.client.sound;

import java.net.URL;

public class SoundPoolEntry {

    public String soundName;
    public URL soundUrl;

    public SoundPoolEntry(final String string, final URL uRL) {
        this.soundName = string;
        this.soundUrl = uRL;
    }
}
