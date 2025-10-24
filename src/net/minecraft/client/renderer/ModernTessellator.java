package net.minecraft.client.renderer;

import lombok.Getter;

public class ModernTessellator
{
    @Getter
    private final ModernWorldRenderer worldRenderer;
    private final WorldVertexBufferUploader vboUploader = new WorldVertexBufferUploader();
    public static final ModernTessellator instance = new ModernTessellator(2097152);

    public ModernTessellator(int bufferSize)
    {
        this.worldRenderer = new ModernWorldRenderer(bufferSize);
    }

    public void draw()
    {
        if (this.worldRenderer.animatedSprites != null)
        {
            SmartAnimations.spritesRendered(this.worldRenderer.animatedSprites);
        }

        this.worldRenderer.finishDrawing();
        this.vboUploader.draw(this.worldRenderer);
    }

}
