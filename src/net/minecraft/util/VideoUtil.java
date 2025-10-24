package net.minecraft.util;

import net.minecraft.Minecraft;
import net.minecraft.client.MinecraftRunner;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.nio.ByteBuffer;

public class VideoUtil {
    private static FFmpegFrameGrabber frameGrabber;
    private static double frameRate;
    private static int ticks;
    private static boolean flag;
    private static long time;
    public static volatile boolean suspended = false;
    private static volatile boolean stopped = false;

    public static void init(File file) throws FFmpegFrameGrabber.Exception {
        Frame frame;

        frameGrabber = new FFmpegFrameGrabber(file.getPath());
        frameGrabber.setPixelFormat(2);
        frameGrabber.setOption("loglevel", "quiet");
        frameGrabber.setOption("threads", "4");
        frameGrabber.setOption("hwaccel", "auto");
    //    frameGrabber.setOption("fflags", "nobuffer");

        time = 0L;
        ticks = 0;
        flag = false;
        stopped = false;
        frameGrabber.start();
        frameRate = frameGrabber.getFrameRate();
        frameGrabber.grab();

        while ((frame = frameGrabber.grab()) == null || frame.image == null) {}

        RenderUtil.setBuffer((ByteBuffer)frame.image[0], frame.imageWidth, frame.imageHeight);

        time = System.currentTimeMillis();
        ticks++;
        startPlaybackThread();
    }

    private static void startPlaybackThread() {
        Thread thread = new Thread("Video Background"){

            @Override
            public void run() {
                try {
                    while (!stopped) {
                        if (flag && (!((double)(System.currentTimeMillis() - time) > 700.0 / frameRate) || suspended)) continue;
                        if (MinecraftRunner.minecraft.currentScreen == null) continue;
                        doGetBuffer();
                    }
                }
                catch (Exception e) {
                    ticks++;
                }
                this.interrupt();
            }
        };

        thread.setDaemon(true);
        thread.start();
    }

    private static void doGetBuffer() throws FFmpegFrameGrabber.Exception {
        int fLength = frameGrabber.getLengthInFrames() - 5;
        if (ticks < fLength) {
            Frame frame = frameGrabber.grab();
            if (frame != null && frame.image != null) {
                RenderUtil.setBuffer((ByteBuffer)frame.image[0], frame.imageWidth, frame.imageHeight);
                time = System.currentTimeMillis();
                ticks++;
            }
        } else {
            ticks = 0;
            frameGrabber.setFrameNumber(0);
        }
        if (!flag) {
            flag = true;
        }
    }

    public static void render(float left, float top, float right, float bottom) {
        if (!stopped) {
            suspended = false;
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

            RenderUtil.bindTexture();

            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            GL11.glDepthMask(false);

            GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(0.0f, 1.0f);
            GL11.glVertex3f(left, bottom, 0.0f);
            GL11.glTexCoord2f(1.0f, 1.0f);
            GL11.glVertex3f(right, bottom, 0.0f);
            GL11.glTexCoord2f(1.0f, 0.0f);
            GL11.glVertex3f(right, top, 0.0f);
            GL11.glTexCoord2f(0.0f, 0.0f);
            GL11.glVertex3f(left, top, 0.0f);
            GL11.glEnd();

            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(true);
            GL11.glPopMatrix();
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
}
