
package net.minecraft.client.renderer;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.texture.TextureFX;
import net.minecraft.ei;
import net.minecraft.p;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderEngine {
    private final HashMap<String, Integer> textureMap;
    private final HashMap<ResourceLocation, BufferedImage> modernTextureMap;
    private final HashMap<Integer, BufferedImage> textureImageMap;
    private final IntBuffer intBuffer;
    private final ByteBuffer byteBuffer;
    private List e;
    private Map f;
    private final GameSettings options;
    private boolean h;

    public RenderEngine(final GameSettings gameSettings) {
        this.textureMap = new HashMap<>();
        this.textureImageMap = new HashMap<>();
        this.modernTextureMap = new HashMap<>();
        this.intBuffer = GLAllocation.createIntBuffer(1);
        this.byteBuffer = GLAllocation.createDirectByteBuffer(262144);
        this.e = new ArrayList<>();
        this.f = new HashMap<>();
        this.h = false;
        this.options = gameSettings;
    }

    public int getTexture(final String string) {
        if (this.textureMap.containsKey(string)) {
            return this.textureMap.get(string);
        }
        try {
            this.intBuffer.clear();
            GLAllocation.generateDisplayLists(this.intBuffer);
            final int value = this.intBuffer.get(0);
            if (string.startsWith("##")) {
                this.a(this.fixBufferedImage(ImageIO.read(RenderEngine.class.getResourceAsStream(string.substring(2)))), value);
            } else if (string.startsWith("%%")) {
                this.h = true;
                this.a(ImageIO.read(RenderEngine.class.getResourceAsStream(string.substring(2))), value);
                this.h = false;
            } else {
                this.a(ImageIO.read(RenderEngine.class.getResourceAsStream(string)), value);
            }
            this.textureMap.put(string, value);
            return value;
        } catch (IOException ex) {
            throw new RuntimeException("!!");
        }
    }

    public int getTexture(final ResourceLocation resourceLocation) {
        return getTexture(resourceLocation.getAbsolutePath());
    }

    public BufferedImage getBufferedImageTexture(ResourceLocation resourceLocation) {
        if (this.modernTextureMap.containsKey(resourceLocation)) {
            return this.modernTextureMap.get(resourceLocation);
        }
        try {
            BufferedImage bufferedImage = ImageIO.read(resourceLocation.getFileInputStream());
            this.modernTextureMap.put(resourceLocation, bufferedImage);
            return bufferedImage;
        } catch (IOException ex) {
            throw new RuntimeException("!!");
        }
    }

    private BufferedImage fixBufferedImage(final BufferedImage bufferedImage) {
        final int n = bufferedImage.getWidth() / 16;
        final BufferedImage bufferedImage2 = new BufferedImage(16, bufferedImage.getHeight() * n, 2);
        final Graphics graphics = bufferedImage2.getGraphics();
        for (int i = 0; i < n; ++i) {
            graphics.drawImage(bufferedImage, -i * 16, i * bufferedImage.getHeight(), (ImageObserver) null);
        }
        graphics.dispose();
        return bufferedImage2;
    }

    public int a(final BufferedImage bufferedImage) {
        this.intBuffer.clear();
        GLAllocation.generateDisplayLists(this.intBuffer);
        final int value = this.intBuffer.get(0);
        this.a(bufferedImage, value);
        this.textureImageMap.put(value, bufferedImage);
        return value;
    }

    public void a(final BufferedImage bufferedImage, final int integer) {
        GlStateManager.bindTexture(integer);
        GL11.glTexParameteri(3553, 10241, 9728);
        GL11.glTexParameteri(3553, 10240, 9728);
        if (this.h) {
            GL11.glTexParameteri(3553, 10242, 10496);
            GL11.glTexParameteri(3553, 10243, 10496);
        } else {
            GL11.glTexParameteri(3553, 10242, 10497);
            GL11.glTexParameteri(3553, 10243, 10497);
        }
        final int width = bufferedImage.getWidth();
        final int height = bufferedImage.getHeight();
        final int[] array = new int[width * height];
        final byte[] array2 = new byte[width * height * 4];
        bufferedImage.getRGB(0, 0, width, height, array, 0, width);
        for (int i = 0; i < array.length; ++i) {
            final int n = array[i] >> 24 & 0xFF;
            int n2 = array[i] >> 16 & 0xFF;
            int n3 = array[i] >> 8 & 0xFF;
            int n4 = array[i] & 0xFF;
            if (this.options != null && this.options.anaglyph) {
                final int n5 = (n2 * 30 + n3 * 59 + n4 * 11) / 100;
                final int n6 = (n2 * 30 + n3 * 70) / 100;
                final int n7 = (n2 * 30 + n4 * 70) / 100;
                n2 = n5;
                n3 = n6;
                n4 = n7;
            }
            array2[i * 4] = (byte) n2;
            array2[i * 4 + 1] = (byte) n3;
            array2[i * 4 + 2] = (byte) n4;
            array2[i * 4 + 3] = (byte) n;
        }
        this.byteBuffer.clear();
        this.byteBuffer.put(array2);
        this.byteBuffer.position(0).limit(array2.length);
        GL11.glTexImage2D(3553, 0, 6408, width, height, 0, 6408, 5121, this.byteBuffer);
    }

    public void a(final int integer) {
        this.textureImageMap.remove(integer);
        this.intBuffer.clear();
        this.intBuffer.put(integer);
        this.intBuffer.flip();
        GL11.glDeleteTextures(this.intBuffer);
    }

    public int a(final String string1, final String string2) {
        final ei ei = (ei) this.f.get(string1);
        if (ei != null && ei.a != null && !ei.d) {
            if (ei.c < 0) {
                ei.c = this.a(ei.a);
            } else {
                this.a(ei.a, ei.c);
            }
            ei.d = true;
        }
        if (ei == null || ei.c < 0) {
            return this.getTexture(string2);
        }
        return ei.c;
    }

    public ei a(final String string, final p p) {
        final ei ei = (ei) this.f.get(string);
        if (ei == null) {
            this.f.put(string, new ei(string, p));
        } else {
            final ei ei2 = ei;
            ++ei2.b;
        }
        return ei;
    }

    public void b(final String string) {
        final ei ei = (ei) this.f.get(string);
        if (ei != null) {
            final ei ei2 = ei;
            --ei2.b;
            if (ei.b == 0) {
                if (ei.c >= 0) {
                    this.a(ei.c);
                }
                this.f.remove(string);
            }
        }
    }

    public void registerTextureFX(final TextureFX at) {
        this.e.add(at);
        at.onTick();
    }

    public void updateDynamicTextures() {
        for (Object o : this.e) {
            final TextureFX textureFX = (TextureFX) o;
            textureFX.anaglyphEnabled = this.options.anaglyph;
            textureFX.onTick();
            this.byteBuffer.clear();
            this.byteBuffer.put(textureFX.imageData);
            this.byteBuffer.position(0).limit(textureFX.imageData.length);
            for (int j = 0; j < textureFX.tileSize; ++j) {
                for (int k = 0; k < textureFX.tileSize; ++k) {
                    GL11.glTexSubImage2D(3553, 0, textureFX.iconIndex % 16 * 16 + j * 16, textureFX.iconIndex / 16 * 16 + k * 16, 16, 16, 6408, 5121, this.byteBuffer);
                }
            }
        }
        for (Object o : this.e) {
            final TextureFX textureFX2 = (TextureFX) o;
            if (textureFX2.textureId > 0) {
                this.byteBuffer.clear();
                this.byteBuffer.put(textureFX2.imageData);
                this.byteBuffer.position(0).limit(textureFX2.imageData.length);
                GlStateManager.bindTexture(textureFX2.textureId);
                GL11.glTexSubImage2D(3553, 0, 0, 0, 16, 16, 6408, 5121, this.byteBuffer);
            }
        }
    }

    public void refreshTextures() {
        for (final int intValue : this.textureImageMap.keySet()) {
            this.a((BufferedImage) this.textureImageMap.get(intValue), intValue);
        }
        final Iterator iterator2 = this.f.values().iterator();
        while (iterator2.hasNext()) {
            ((ei) iterator2.next()).d = false;
        }
        for (final String s : this.textureMap.keySet()) {
            try {
                BufferedImage bufferedImage;
                if (s.startsWith("##")) {
                    bufferedImage = this.fixBufferedImage(ImageIO.read(RenderEngine.class.getResourceAsStream(s.substring(2))));
                } else if (s.startsWith("%%")) {
                    this.h = true;
                    bufferedImage = ImageIO.read(RenderEngine.class.getResourceAsStream(s.substring(2)));
                    this.h = false;
                } else {
                    bufferedImage = ImageIO.read(RenderEngine.class.getResourceAsStream(s));
                }
                this.a(bufferedImage, (int) this.textureMap.get(s));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void bindTexture(final int integer) {
        if (integer < 0) {
            return;
        }
        GlStateManager.bindTexture(integer);
    }
}
