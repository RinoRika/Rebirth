
package net.minecraft;

public class ScaledResolution {

    private int scaledWidth;
    private int scaledHeight;
    private int scaleFactor;

    public ScaledResolution(final int integer1, final int integer2) {
        this.scaledWidth = integer1;
        this.scaledHeight = integer2;

        // Stars: Add scaleFactor to adjust gui scale.
        // 1 = small (4k), 2 = mid (2k), 3 = large (1080p)
        int i = 2;

        while (this.scaleFactor < i && this.scaledWidth / (this.scaleFactor + 1) >= 320 && this.scaledHeight / (this.scaleFactor + 1) >= 240)
        {
            ++this.scaleFactor;
        }

        this.scaledWidth /= scaleFactor;
        this.scaledHeight /= scaleFactor;
    }

    public int getScaledWidth() {
        return this.scaledWidth;
    }

    public int getScaledHeight() {
        return this.scaledHeight;
    }
}
