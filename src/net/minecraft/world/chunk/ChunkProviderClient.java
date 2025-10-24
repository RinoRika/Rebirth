
package net.minecraft.world.chunk;

import net.minecraft.world.IChunkLoader;
import net.minecraft.world.IChunkProvider;
import net.minecraft.client.gui.IProgressUpdate;
import net.minecraft.world.World;

public class ChunkProviderClient implements IChunkProvider {

    private final Chunk[] blankChunk;
    private final World worldObj;
    private final IChunkLoader chunkLoader;
    byte[] byteSomething;

    public ChunkProviderClient(final World fe, final IChunkLoader bg) {
        this.blankChunk = new Chunk[256];
        this.byteSomething = new byte[32768];
        this.worldObj = fe;
        this.chunkLoader = bg;
    }

    public boolean chunkExists(final int integer1, final int integer2) {
        final int n = (integer1 & 0xF) | (integer2 & 0xF) * 16;
        return this.blankChunk[n] != null && this.blankChunk[n].isAtLocation(integer1, integer2);
    }

    public Chunk provideChunk(final int integer1, final int integer2) {
        final int n = (integer1 & 0xF) | (integer2 & 0xF) * 16;
        try {
            if (!this.chunkExists(integer1, integer2)) {
                Chunk c = this.getChunk(integer1, integer2);
                if (c == null) {
                    c = new Chunk(this.worldObj, this.byteSomething, integer1, integer2);
                    c.q = true;
                    c.neverSave = true;
                }
                this.blankChunk[n] = c;
            }
            return this.blankChunk[n];
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private synchronized Chunk getChunk(final int integer1, final int integer2) {
        return this.chunkLoader.loadChunk(this.worldObj, integer1, integer2);
    }

    public void populate(final IChunkProvider ch, final int integer2, final int integer3) {
    }

    public boolean saveChunks(final boolean boolean1, final IProgressUpdate jd) {
        return true;
    }

    public boolean unload100OldestChunks() {
        return false;
    }

    public boolean canSave() {
        return false;
    }
}
