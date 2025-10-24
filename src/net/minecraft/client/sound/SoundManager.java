
package net.minecraft.client.sound;

import java.io.File;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.MathHelper;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.codecs.CodecJOrbis;
import paulscode.sound.codecs.CodecWav;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;

public class SoundManager {

    private SoundSystem sndSystem;
    private SoundPool soundPoolSounds;
    private SoundPool soundPoolMusic;
    private int latestSoundID;
    private GameSettings options;
    private boolean loaded;

    public SoundManager() {
        this.soundPoolSounds = new SoundPool();
        this.soundPoolMusic = new SoundPool();
        this.latestSoundID = 0;
        this.loaded = false;
    }

    public void loadSoundSettings(final GameSettings ja) {
        this.options = ja;
        if (!this.loaded && (ja.sound || ja.music)) {
            this.tryToSetLibraryAndCodecs();
        }
    }

    private void tryToSetLibraryAndCodecs() {
        try {
            final boolean sound = this.options.sound;
            final boolean music = this.options.music;
            this.options.sound = false;
            this.options.music = false;
            this.options.saveOptions();
            SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
            SoundSystemConfig.setCodec("ogg", (Class) CodecJOrbis.class);
            SoundSystemConfig.setCodec("wav", (Class) CodecWav.class);
            this.sndSystem = new SoundSystem();
            this.options.sound = sound;
            this.options.music = music;
            this.options.saveOptions();
        } catch (Throwable t) {
            System.err.println("error linking with the LibraryJavaSound plug-in");
        }
        this.loaded = true;
    }

    public void onSoundOptionsChanged() {
        if (!this.loaded && (this.options.sound || this.options.music)) {
            this.tryToSetLibraryAndCodecs();
        }
        if (!this.options.music) {
            this.sndSystem.stop("BgMusic");
        }
    }

    public void closeMinecraft() {
        if (this.loaded) {
            this.sndSystem.cleanup();
        }
    }

    public void addSound(final String string, final File file) {
        this.soundPoolSounds.addSound(string, file);
    }

    public void addMusic(final String string, final File file) {
        this.soundPoolMusic.addSound(string, file);
    }

    public void setListener(final EntityLiving ka, final float float2) {
        if (!this.loaded || !this.options.sound) {
            return;
        }
        if (ka == null) {
            return;
        }
        final float n = ka.prevRotationPitch + (ka.rotationPitch - ka.prevRotationPitch) * float2;
        final float n2 = ka.prevRotationYaw + (ka.rotationYaw - ka.prevRotationYaw) * float2;
        final double n3 = ka.prevPosX + (ka.posX - ka.prevPosX) * float2;
        final double n4 = ka.prevPosY + (ka.posY - ka.prevPosY) * float2;
        final double n5 = ka.prevPosZ + (ka.posZ - ka.prevPosZ) * float2;
        final float cos = MathHelper.cos(-n2 * 0.017453292f - 3.1415927f);
        final float sin = MathHelper.sin(-n2 * 0.017453292f - 3.1415927f);
        final float cos2 = MathHelper.cos(-n * 0.017453292f);
        final float sin2 = MathHelper.sin(-n * 0.017453292f);
        final float float3 = -sin * cos2;
        final float float4 = sin2;
        final float float5 = -cos * cos2;
        final float float6 = -sin * sin2;
        final float float7 = cos2;
        final float float8 = -cos * sin2;
        this.sndSystem.setListenerPosition((float) n3, (float) n4, (float) n5);
        this.sndSystem.setListenerOrientation(float3, float4, float5, float6, float7, float8);
    }

    public void playSound(final String string, final float float2, final float float3, final float float4, float float5, final float float6) {
        if (!this.loaded || !this.options.sound) {
            return;
        }
        final SoundPoolEntry randomSoundFromSoundPool = this.soundPoolSounds.getRandomSoundFromSoundPool(string);
        if (randomSoundFromSoundPool != null && float5 > 0.0f) {
            this.latestSoundID = (this.latestSoundID + 1) % 256;
            final String string2 = new StringBuilder().append("sound_").append(this.latestSoundID).toString();
            float float7 = 16.0f;
            if (float5 > 1.0f) {
                float7 *= float5;
            }
            this.sndSystem.newSource(float5 > 1.0f, string2, randomSoundFromSoundPool.soundUrl, randomSoundFromSoundPool.soundName, false, float2, float3, float4, 2, float7);
            this.sndSystem.setPitch(string2, float6);
            if (float5 > 1.0f) {
                float5 = 1.0f;
            }
            this.sndSystem.setVolume(string2, float5);
            this.sndSystem.play(string2);
        }
    }

    public void playSoundFX(final String string, float float2, final float float3) {
        if (!this.loaded || !this.options.sound) {
            return;
        }
        final SoundPoolEntry randomSoundFromSoundPool = this.soundPoolSounds.getRandomSoundFromSoundPool(string);
        if (randomSoundFromSoundPool != null) {
            this.latestSoundID = (this.latestSoundID + 1) % 256;
            final String string2 = new StringBuilder().append("sound_").append(this.latestSoundID).toString();
            this.sndSystem.newSource(false, string2, randomSoundFromSoundPool.soundUrl, randomSoundFromSoundPool.soundName, false, 0.0f, 0.0f, 0.0f, 0, 0.0f);
            if (float2 > 1.0f) {
                float2 = 1.0f;
            }
            float2 *= 0.25f;
            this.sndSystem.setPitch(string2, float3);
            this.sndSystem.setVolume(string2, float2);
            this.sndSystem.play(string2);
        }
    }
}
