
package net.minecraft.world.chunk.storage;

import net.minecraft.world.World;
import net.minecraft.world.IChunkProvider;
import net.minecraft.client.canvas.CanvasIsomPreview;
import net.minecraft.world.chunk.storage.ChunkLoader;
import net.minecraft.world.chunk.ChunkProviderClient;
import java.io.File;

public class SaveHandler extends World {

    public final /* synthetic */ CanvasIsomPreview q;

    public SaveHandler(final CanvasIsomPreview be, final File file, final String string) {
        super(file, string);
        this.q = be;
    }

    @Override
    protected IChunkProvider a(final File file) {
        return new ChunkProviderClient(this, new ChunkLoader(file, false));
    }
}
