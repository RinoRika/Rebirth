package net.minecraft.client.texture;

import net.minecraft.Minecraft;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.UnexpectedThrowable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

public class TextureAtlasSprite
{
    private final String iconName;
    protected List<int[][]> framesTextureData = new ArrayList<>();
    protected int[][] interpolatedFrameData;
  //  private AnimationMetadataSection animationMetadata;
    protected boolean rotated;
    protected int originX;
    protected int originY;
    protected int width;
    protected int height;
    private float minU;
    private float maxU;
    private float minV;
    private float maxV;
    protected int frameCounter;
    protected int tickCounter;
    private static String locationNameClock = "builtin/clock";
    private static String locationNameCompass = "builtin/compass";
    private int indexInMap = -1;
    public float baseU;
    public float baseV;
    public int sheetWidth;
    public int sheetHeight;
    public int glSpriteTextureId = -1;
    public TextureAtlasSprite spriteSingle = null;
    public boolean isSpriteSingle = false;
    public int mipmapLevels = 0;
    public TextureAtlasSprite spriteNormal = null;
    public TextureAtlasSprite spriteSpecular = null;
    public boolean isShadersSprite = false;
    public boolean isEmissive = false;
    public TextureAtlasSprite spriteEmissive = null;
    private int animationIndex = -1;
    private boolean animationActive = false;

    private TextureAtlasSprite(String p_i7_1_, boolean p_i7_2_)
    {
        this.iconName = p_i7_1_;
        this.isSpriteSingle = p_i7_2_;
    }

    public TextureAtlasSprite(String spriteName)
    {
        this.iconName = spriteName;
        this.spriteSingle = new TextureAtlasSprite(this.getIconName() + ".spriteSingle", true);
    }

    protected static TextureAtlasSprite makeAtlasSprite(ResourceLocation spriteResourceLocation)
    {
        String s = spriteResourceLocation.toString();
        return new TextureAtlasSprite(s);
    }

    public static void setLocationNameClock(String clockName)
    {
        locationNameClock = clockName;
    }

    public static void setLocationNameCompass(String compassName)
    {
        locationNameCompass = compassName;
    }

    public void initSprite(int inX, int inY, int originInX, int originInY, boolean rotatedIn)
    {
        this.originX = originInX;
        this.originY = originInY;
        this.rotated = rotatedIn;
        float f = (float)(0.009999999776482582D / (double)inX);
        float f1 = (float)(0.009999999776482582D / (double)inY);
        this.minU = (float)originInX / (float)((double)inX) + f;
        this.maxU = (float)(originInX + this.width) / (float)((double)inX) - f;
        this.minV = (float)originInY / (float)inY + f1;
        this.maxV = (float)(originInY + this.height) / (float)inY - f1;
        this.baseU = Math.min(this.minU, this.maxU);
        this.baseV = Math.min(this.minV, this.maxV);

        if (this.spriteSingle != null)
        {
            this.spriteSingle.initSprite(this.width, this.height, 0, 0, false);
        }

        if (this.spriteNormal != null)
        {
            this.spriteNormal.copyFrom(this);
        }

        if (this.spriteSpecular != null)
        {
            this.spriteSpecular.copyFrom(this);
        }
    }

    public void copyFrom(TextureAtlasSprite atlasSpirit)
    {
        this.originX = atlasSpirit.originX;
        this.originY = atlasSpirit.originY;
        this.width = atlasSpirit.width;
        this.height = atlasSpirit.height;
        this.rotated = atlasSpirit.rotated;
        this.minU = atlasSpirit.minU;
        this.maxU = atlasSpirit.maxU;
        this.minV = atlasSpirit.minV;
        this.maxV = atlasSpirit.maxV;
        this.baseU = atlasSpirit.baseU;
        this.baseV = atlasSpirit.baseV;
        this.sheetWidth = atlasSpirit.sheetWidth;
        this.sheetHeight = atlasSpirit.sheetHeight;
        this.glSpriteTextureId = atlasSpirit.glSpriteTextureId;
        this.mipmapLevels = atlasSpirit.mipmapLevels;

        if (this.spriteSingle != null)
        {
            this.spriteSingle.initSprite(this.width, this.height, 0, 0, false);
        }

        this.animationIndex = atlasSpirit.animationIndex;
    }

    public int getOriginX()
    {
        return this.originX;
    }

    public int getOriginY()
    {
        return this.originY;
    }

    public int getIconWidth()
    {
        return this.width;
    }

    public int getIconHeight()
    {
        return this.height;
    }

    public float getMinU()
    {
        return this.minU;
    }

    public float getMaxU()
    {
        return this.maxU;
    }

    public float getInterpolatedU(double u)
    {
        float f = this.maxU - this.minU;
        return this.minU + f * (float)u / 16.0F;
    }

    public float getMinV()
    {
        return this.minV;
    }

    public float getMaxV()
    {
        return this.maxV;
    }

    public float getInterpolatedV(double v)
    {
        float f = this.maxV - this.minV;
        return this.minV + f * ((float)v / 16.0F);
    }

    public String getIconName()
    {
        return this.iconName;
    }

 /*   public void updateAnimation()
    {
        if (this.animationMetadata != null)
        {
            this.animationActive = !SmartAnimations.isActive() || SmartAnimations.isSpriteRendered(this.animationIndex);
            ++this.tickCounter;

            if (this.tickCounter >= this.animationMetadata.getFrameTimeSingle(this.frameCounter))
            {
                int i = this.animationMetadata.getFrameIndex(this.frameCounter);
                int j = this.animationMetadata.getFrameCount() == 0 ? this.framesTextureData.size() : this.animationMetadata.getFrameCount();
                this.frameCounter = (this.frameCounter + 1) % j;
                this.tickCounter = 0;
                int k = this.animationMetadata.getFrameIndex(this.frameCounter);
                boolean flag = false;
                boolean flag1 = this.isSpriteSingle;

                if (!this.animationActive)
                {
                    return;
                }

                if (i != k && k >= 0 && k < this.framesTextureData.size())
                {
                    synchronized (SplashScreen.renderLock) {
                        TextureUtil.uploadTextureMipmap(this.framesTextureData.get(k), this.width, this.height, this.originX, this.originY, flag, flag1);
                    }
                }
            }
            else if (this.animationMetadata.isInterpolate())
            {
                if (!this.animationActive)
                {
                    return;
                }

                this.updateAnimationInterpolated();
            }
        }
    }

    private void updateAnimationInterpolated()
    {
        double d0 = 1.0D - (double)this.tickCounter / (double)this.animationMetadata.getFrameTimeSingle(this.frameCounter);
        int i = this.animationMetadata.getFrameIndex(this.frameCounter);
        int j = this.animationMetadata.getFrameCount() == 0 ? this.framesTextureData.size() : this.animationMetadata.getFrameCount();
        int k = this.animationMetadata.getFrameIndex((this.frameCounter + 1) % j);

        if (i != k && k >= 0 && k < this.framesTextureData.size())
        {
            int[][] aint = (int[][])((int[][])this.framesTextureData.get(i));
            int[][] aint1 = (int[][])((int[][])this.framesTextureData.get(k));

            if (this.interpolatedFrameData == null || this.interpolatedFrameData.length != aint.length)
            {
                this.interpolatedFrameData = new int[aint.length][];
            }

            for (int l = 0; l < aint.length; ++l)
            {
                if (this.interpolatedFrameData[l] == null)
                {
                    this.interpolatedFrameData[l] = new int[aint[l].length];
                }

                if (l < aint1.length && aint1[l].length == aint[l].length)
                {
                    for (int i1 = 0; i1 < aint[l].length; ++i1)
                    {
                        int j1 = aint[l][i1];
                        int k1 = aint1[l][i1];
                        int l1 = (int)((double)((j1 & 16711680) >> 16) * d0 + (double)((k1 & 16711680) >> 16) * (1.0D - d0));
                        int i2 = (int)((double)((j1 & 65280) >> 8) * d0 + (double)((k1 & 65280) >> 8) * (1.0D - d0));
                        int j2 = (int)((double)(j1 & 255) * d0 + (double)(k1 & 255) * (1.0D - d0));
                        this.interpolatedFrameData[l][i1] = j1 & -16777216 | l1 << 16 | i2 << 8 | j2;
                    }
                }
            }

            TextureUtil.uploadTextureMipmap(this.interpolatedFrameData, this.width, this.height, this.originX, this.originY, false, false);
        }
    }

    public int[][] getFrameTextureData(int index)
    {
        return (int[][])((int[][])this.framesTextureData.get(index));
    }

    public int getFrameCount()
    {
        return this.framesTextureData.size();
    }

    public void setIconWidth(int newWidth)
    {
        this.width = newWidth;

        if (this.spriteSingle != null)
        {
            this.spriteSingle.setIconWidth(this.width);
        }
    }

    public void setIconHeight(int newHeight)
    {
        this.height = newHeight;

        if (this.spriteSingle != null)
        {
            this.spriteSingle.setIconHeight(this.height);
        }
    } */

    private BufferedImage scaleImage(BufferedImage bi, int w2)
    {
        int i = bi.getWidth();
        int j = bi.getHeight();
        int k = j * w2 / i;
        BufferedImage bufferedimage = new BufferedImage(w2, k, 2);
        Graphics2D graphics2d = bufferedimage.createGraphics();
        Object object = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;

        if (w2 < i || w2 % i != 0)
        {
            object = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
        }

        graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, object);
        graphics2d.drawImage(bi, 0, 0, w2, k, (ImageObserver)null);
        return bufferedimage;
    }

    public void loadSprite(BufferedImage[] images) throws IOException
    {
        this.resetSprite();
        int i = images[0].getWidth();
        int j = images[0].getHeight();
        this.width = i;
        this.height = j;

        if (this.spriteSingle != null)
        {
            this.spriteSingle.width = this.width;
            this.spriteSingle.height = this.height;
        }

        int[][] aint = new int[images.length][];

        for (int k = 0; k < images.length; ++k)
        {
            BufferedImage bufferedimage = images[k];

            if (bufferedimage != null)
            {
                if (this.width >> k != bufferedimage.getWidth())
                {
                    bufferedimage = scaleImage(bufferedimage, this.width >> k);
                }

                if (k > 0 && (bufferedimage.getWidth() != i >> k || bufferedimage.getHeight() != j >> k))
                {
                    throw new RuntimeException(String.format("Unable to load miplevel: %d, image is size: %dx%d, expected %dx%d", new Object[] {Integer.valueOf(k), Integer.valueOf(bufferedimage.getWidth()), Integer.valueOf(bufferedimage.getHeight()), Integer.valueOf(i >> k), Integer.valueOf(j >> k)}));
                }

                aint[k] = new int[bufferedimage.getWidth() * bufferedimage.getHeight()];
                bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), aint[k], 0, bufferedimage.getWidth());
            }
        }

            if (j != i)
            {
                throw new RuntimeException("broken aspect ratio and not an animation");
            }

            this.framesTextureData.add(aint);

        if (!this.isShadersSprite)
        {

            for (int[][] framesTextureDatum : this.framesTextureData) {

                if (framesTextureDatum != null && !this.iconName.startsWith("minecraft:blocks/leaves_")) {
                    for (int[] aint2 : framesTextureDatum) {
                        this.fixTransparentColor(aint2);
                    }
                }
            }

            if (this.spriteSingle != null)
            {
                this.spriteSingle.loadSprite(images);
            }
        }
    }

    public void generateMipmaps(int level)
    {
        List<int[][]> list = new ArrayList<>();

        for (int i = 0; i < this.framesTextureData.size(); ++i)
        {
            final int[][] aint = this.framesTextureData.get(i);

            if (aint != null)
            {
                try
                {
                    list.add(TextureUtil.generateMipmapData(level, this.width, aint));
                }
                catch (Throwable throwable)
                {
                    Minecraft.getMinecraft().displayUnexpectedThrowable(new UnexpectedThrowable("Generating mipmaps", throwable));
                }
            }
        }

        this.setFramesTextureData(list);

        if (this.spriteSingle != null)
        {
            this.spriteSingle.generateMipmaps(level);
        }
    }

    private void allocateFrameTextureData(int index)
    {
        if (this.framesTextureData.size() <= index)
        {
            for (int i = this.framesTextureData.size(); i <= index; ++i)
            {
                this.framesTextureData.add((int[][])((int[][])null));
            }
        }

        if (this.spriteSingle != null)
        {
            this.spriteSingle.allocateFrameTextureData(index);
        }
    }

    private static int[][] getFrameTextureData(int[][] data, int rows, int columns, int p_147962_3_)
    {
        int[][] aint = new int[data.length][];

        for (int i = 0; i < data.length; ++i)
        {
            int[] aint1 = data[i];

            if (aint1 != null)
            {
                aint[i] = new int[(rows >> i) * (columns >> i)];
                System.arraycopy(aint1, p_147962_3_ * aint[i].length, aint[i], 0, aint[i].length);
            }
        }

        return aint;
    }

    public void clearFramesTextureData()
    {
        this.framesTextureData.clear();

        if (this.spriteSingle != null)
        {
            this.spriteSingle.clearFramesTextureData();
        }
    }

  /*  public boolean hasAnimationMetadata()
    {
        return this.animationMetadata != null;
    } */

    public void setFramesTextureData(List<int[][]> newFramesTextureData)
    {
        this.framesTextureData = newFramesTextureData;

        if (this.spriteSingle != null)
        {
            this.spriteSingle.setFramesTextureData(newFramesTextureData);
        }
    }

    private void resetSprite()
    {
    //    this.animationMetadata = null;
        this.setFramesTextureData(new ArrayList<>());
        this.frameCounter = 0;
        this.tickCounter = 0;

        if (this.spriteSingle != null)
        {
            this.spriteSingle.resetSprite();
        }
    }

    public String toString()
    {
        return "TextureAtlasSprite{name='" + this.iconName + "'" + ", frameCount=" + this.framesTextureData.size() + ", rotated=" + this.rotated + ", x=" + this.originX + ", y=" + this.originY + ", height=" + this.height + ", width=" + this.width + ", u0=" + this.minU + ", u1=" + this.maxU + ", v0=" + this.minV + ", v1=" + this.maxV + '}';
    }

    public int getIndexInMap()
    {
        return this.indexInMap;
    }

    public void setIndexInMap(int p_setIndexInMap_1_)
    {
        this.indexInMap = p_setIndexInMap_1_;
    }

    public void updateIndexInMap(AtomicInteger p_updateIndexInMap_1_)
    {
        if (this.indexInMap < 0)
        {
            this.indexInMap = p_updateIndexInMap_1_.get();
        }
    }

    public int getAnimationIndex()
    {
        return this.animationIndex;
    }

    public void setAnimationIndex(int p_setAnimationIndex_1_)
    {
        this.animationIndex = p_setAnimationIndex_1_;

        if (this.spriteNormal != null)
        {
            this.spriteNormal.setAnimationIndex(p_setAnimationIndex_1_);
        }

        if (this.spriteSpecular != null)
        {
            this.spriteSpecular.setAnimationIndex(p_setAnimationIndex_1_);
        }
    }

    public boolean isAnimationActive()
    {
        return this.animationActive;
    }

    private void fixTransparentColor(int[] p_fixTransparentColor_1_)
    {
        if (p_fixTransparentColor_1_ != null)
        {
            long i = 0L;
            long j = 0L;
            long k = 0L;
            long l = 0L;

            for (int j1 : p_fixTransparentColor_1_) {
                int k1 = j1 >> 24 & 255;

                if (k1 >= 16) {
                    int l1 = j1 >> 16 & 255;
                    int i2 = j1 >> 8 & 255;
                    int j2 = j1 & 255;
                    i += (long) l1;
                    j += (long) i2;
                    k += (long) j2;
                    ++l;
                }
            }

            if (l > 0L)
            {
                int l2 = (int)(i / l);
                int i3 = (int)(j / l);
                int j3 = (int)(k / l);
                int k3 = l2 << 16 | i3 << 8 | j3;

                for (int l3 = 0; l3 < p_fixTransparentColor_1_.length; ++l3)
                {
                    int i4 = p_fixTransparentColor_1_[l3];
                    int k2 = i4 >> 24 & 255;

                    if (k2 <= 16)
                    {
                        p_fixTransparentColor_1_[l3] = k3;
                    }
                }
            }
        }
    }

    public double getSpriteU16(float p_getSpriteU16_1_)
    {
        float f = this.maxU - this.minU;
        return (double)((p_getSpriteU16_1_ - this.minU) / f * 16.0F);
    }

    public double getSpriteV16(float p_getSpriteV16_1_)
    {
        float f = this.maxV - this.minV;
        return (double)((p_getSpriteV16_1_ - this.minV) / f * 16.0F);
    }

    public void bindSpriteTexture()
    {
        if (this.glSpriteTextureId < 0)
        {
            this.glSpriteTextureId = TextureUtil.glGenTextures();
            TextureUtil.allocateTextureImpl(this.glSpriteTextureId, this.mipmapLevels, this.width, this.height);
            TextureUtil.applyAnisotropicLevel();
        }

        TextureUtil.bindTexture(this.glSpriteTextureId);
    }

    public void deleteSpriteTexture()
    {
        if (this.glSpriteTextureId >= 0)
        {
            TextureUtil.deleteTexture(this.glSpriteTextureId);
            this.glSpriteTextureId = -1;
        }
    }

    public float toSingleU(float p_toSingleU_1_)
    {
        p_toSingleU_1_ = p_toSingleU_1_ - this.baseU;
        float f = (float)this.sheetWidth / (float)this.width;
        p_toSingleU_1_ = p_toSingleU_1_ * f;
        return p_toSingleU_1_;
    }

    public float toSingleV(float p_toSingleV_1_)
    {
        p_toSingleV_1_ = p_toSingleV_1_ - this.baseV;
        float f = (float)this.sheetHeight / (float)this.height;
        p_toSingleV_1_ = p_toSingleV_1_ * f;
        return p_toSingleV_1_;
    }

    public List<int[][]> getFramesTextureData()
    {
        List<int[][]> list = new ArrayList();
        list.addAll(this.framesTextureData);
        return list;
    }

 /*   public AnimationMetadataSection getAnimationMetadata()
    {
        return this.animationMetadata;
    }

    public void setAnimationMetadata(AnimationMetadataSection p_setAnimationMetadata_1_)
    {
        this.animationMetadata = p_setAnimationMetadata_1_;
    }

    private void loadShadersSprites()
    {
        if (Shaders.configNormalMap)
        {
            String s = this.iconName + "_n";
            ResourceLocation resourcelocation = new ResourceLocation(s);
            resourcelocation = Config.getTextureMap().completeResourceLocation(resourcelocation);

            if (Config.hasResource(resourcelocation))
            {
                this.spriteNormal = new TextureAtlasSprite(s);
                this.spriteNormal.isShadersSprite = true;
                this.spriteNormal.copyFrom(this);
                this.spriteNormal.generateMipmaps(this.mipmapLevels);
            }
        }

        if (Shaders.configSpecularMap)
        {
            String s1 = this.iconName + "_s";
            ResourceLocation resourcelocation1 = new ResourceLocation(s1);
            resourcelocation1 = Config.getTextureMap().completeResourceLocation(resourcelocation1);

            if (Config.hasResource(resourcelocation1))
            {
                this.spriteSpecular = new TextureAtlasSprite(s1);
                this.spriteSpecular.isShadersSprite = true;
                this.spriteSpecular.copyFrom(this);
                this.spriteSpecular.generateMipmaps(this.mipmapLevels);
            }
        }
    } */
}
