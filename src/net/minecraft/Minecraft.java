
package net.minecraft;

import java.io.File;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.client.MinecraftRunner;
import net.minecraft.client.entity.PlayerController;
import net.minecraft.client.entity.PlayerControllerSP;
import net.minecraft.client.entity.PlayerControllerTest;
import net.minecraft.client.entity.model.ModelBiped;
import net.minecraft.client.font.FontRenderer;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiInventory;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiUnused;
import net.minecraft.client.input.MouseHelper;
import net.minecraft.client.input.MovementInputFromOptions;
import net.minecraft.client.input.MovingObjectPosition;
import net.minecraft.client.renderer.*;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.texture.TextureFlamesFX;
import net.minecraft.client.texture.TextureGearsFX;
import net.minecraft.client.texture.TextureLavaFX;
import net.minecraft.client.texture.TextureLavaFlowFX;
import net.minecraft.client.texture.TextureWaterFX;
import net.minecraft.client.texture.TextureWaterFlowFX;
import net.minecraft.entity.EntityPlayerSP;
import net.minecraft.entity.EntityRenderer;
import net.minecraft.inventory.recipe.CraftingManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldRenderer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public abstract class Minecraft implements Runnable {

    public final Logger logger = LogManager.getLogger();
    public static long[] tickTimes = new long[512];
    public static int numRecordedFrameTimes = 0;
    private static File minecraftDir = new File("minecraft");;
    private static final File dictionary = new File(minecraftDir, "background");
    private static final File backgroundVideoFile = new File(dictionary, "background.mp4");
    public PlayerController playerController;
    private boolean fullscreen;
    public int displayWidth;
    public int displayHeight;
    private OpenGlCapsChecker glCapabilities;
    public final Timer timer;
    public World theWorld;
    public RenderGlobal renderGlobal;
    public EntityPlayerSP thePlayer;
    public EffectRenderer effectRenderer;
    public Session sessionData;
    public boolean hideQuitButton;
    public volatile boolean isGamePaused;
    public RenderEngine renderEngine;
    public FontRenderer fontRenderer;
    public GuiScreen currentScreen;
    public LoadingScreenRenderer loadingScreen;
    public EntityRenderer entityRenderer;
    private ThreadDownloadResources downloadResourcesThread;
    private int ticksRan;
    private int leftClickCounter;
    private int tempDisplayWidth;
    private int tempDisplayHeight;
    public String objectMouseOverString;
    public int rightClickDelayTimer;
    public GuiIngame ingameGUI;
    public boolean skipRenderWorld;
    public MovingObjectPosition objectMouseOver;
    public GameSettings gameSettings;
    public SoundManager sndManager;
    public MouseHelper mouseHelper;
    public File mcDataDir;
    private final TextureWaterFX textureWaterFX;
    private final TextureLavaFX textureLavaFX;
    public volatile boolean running;
    public String debug;
    long prevFrameTime;
    public boolean inGameHasFocus;
    private int mouseTicksRan;
    long systemTime;

    static {
        if (!Minecraft.minecraftDir.exists()) {
            Minecraft.minecraftDir.mkdir();
        }
    }

    public Minecraft(final int integer4, final int integer5, final boolean boolean6) {
        this.playerController = new PlayerControllerSP(this);
        this.fullscreen = false;
        this.timer = new Timer(20.0f);
        this.sessionData = new Session("LWJGL2", "1488228");
        this.hideQuitButton = true;
        this.isGamePaused = false;
        this.currentScreen = null;
        this.loadingScreen = new LoadingScreenRenderer(this);
        this.entityRenderer = new EntityRenderer(this);
        this.ticksRan = 0;
        this.leftClickCounter = 0;
        this.objectMouseOverString = null;
        this.rightClickDelayTimer = 0;
        this.skipRenderWorld = false;
        this.objectMouseOver = null;
        this.sndManager = new SoundManager();
        this.textureWaterFX = new TextureWaterFX();
        this.textureLavaFX = new TextureLavaFX();
        this.running = true;
        this.debug = "";
        this.prevFrameTime = -1L;
        this.inGameHasFocus = false;
        this.mouseTicksRan = 0;
        this.systemTime = System.currentTimeMillis();
        this.tempDisplayWidth = integer4;
        this.tempDisplayHeight = integer5;
        this.fullscreen = boolean6;
        new TimerHackThread(this, "Timer hack thread").start();
        this.displayWidth = integer4;
        this.displayHeight = integer5;
        this.fullscreen = boolean6;
    }

    public abstract void displayUnexpectedThrowable(final UnexpectedThrowable g);

    public static Minecraft getMinecraft() {
        return MinecraftRunner.minecraft;
    }

    public void startGame() throws LWJGLException {
        if (this.fullscreen) {
            Display.setFullscreen(true);
            this.displayWidth = Display.getDisplayMode().getWidth();
            this.displayHeight = Display.getDisplayMode().getHeight();
            if (this.displayWidth <= 0) {
                this.displayWidth = 1;
            }
            if (this.displayHeight <= 0) {
                this.displayHeight = 1;
            }
        } else {
            Display.setDisplayMode(new DisplayMode(this.displayWidth, this.displayHeight));
        }
        Display.setTitle("Rebirth");
        Display.setResizable(true);
        Display.create();
        OpenGlHelper.initializeTextures();
        this.mcDataDir = getMinecraftDir();
        this.gameSettings = new GameSettings(this, this.mcDataDir);
        this.renderEngine = new RenderEngine(this.gameSettings);
        this.fontRenderer = new FontRenderer(this.gameSettings, "/default.png", this.renderEngine);
        CraftingManager.getInstance();
        this.loadScreen();
        Keyboard.create();
        Mouse.create();
        this.mouseHelper = new MouseHelper(null);
        this.checkGLError("Pre startup");
        GL11.glEnable(3553);
        GL11.glShadeModel(7425);
        GL11.glClearDepth(1.0);
        GL11.glEnable(2929);
        GL11.glDepthFunc(515);
        GL11.glEnable(3008);
        GL11.glAlphaFunc(516, 0.1f);
        GL11.glCullFace(1029);
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(5888);
        this.checkGLError("Startup");
        if (!dictionary.exists()) {
            dictionary.mkdirs();
        }
        if (!backgroundVideoFile.exists()) {
            FileUtil.unpackFile(backgroundVideoFile, "background.mp4");
        }
        if (backgroundVideoFile.exists()) {
            try {
                VideoUtil.init(backgroundVideoFile);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        this.glCapabilities = new OpenGlCapsChecker();
        this.sndManager.loadSoundSettings(this.gameSettings);
        this.renderEngine.registerTextureFX(this.textureLavaFX);
        this.renderEngine.registerTextureFX(this.textureWaterFX);
        this.renderEngine.registerTextureFX(new TextureWaterFlowFX());
        this.renderEngine.registerTextureFX(new TextureLavaFlowFX());
        this.renderEngine.registerTextureFX(new TextureFlamesFX(0));
        this.renderEngine.registerTextureFX(new TextureFlamesFX(1));
        this.renderEngine.registerTextureFX(new TextureGearsFX(0));
        this.renderEngine.registerTextureFX(new TextureGearsFX(1));
        this.renderGlobal = new RenderGlobal(this, this.renderEngine);
        GL11.glViewport(0, 0, this.displayWidth, this.displayHeight);
        this.displayGuiScreen(new GuiMainMenu());
        this.effectRenderer = new EffectRenderer(this.theWorld, this.renderEngine);
        try {
            (this.downloadResourcesThread = new ThreadDownloadResources(this.mcDataDir, this)).start();
        } catch (Exception ex4) {
            ex4.printStackTrace();
        }
        this.checkGLError("Post startup");
        this.ingameGUI = new GuiIngame(this);
    }

    private void loadScreen() throws LWJGLException {
        final ScaledResolution scaledResolution = new ScaledResolution(this.displayWidth, this.displayHeight);
        final int scaledWidth = scaledResolution.getScaledWidth();
        final int scaledHeight = scaledResolution.getScaledHeight();
        GL11.glClear(16640);
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0, scaledWidth, scaledHeight, 0.0, 1000.0, 3000.0);
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0f, 0.0f, -2000.0f);
        GL11.glViewport(0, 0, this.displayWidth, this.displayHeight);
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glDisable(2896);
        GL11.glDisable(2912);
        GL11.glEnable(3553);
        final Tessellator instance = Tessellator.instance;
        GlStateManager.bindTexture(this.renderEngine.getTexture("/dirt.png"));
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        final float n = 32.0f;
        instance.startDrawingQuads();
        instance.setColorOpaque_I(4210752);
        instance.addVertexWithUV(0.0, this.displayHeight, 0.0, 0.0, this.displayHeight / n + 0.0f);
        instance.addVertexWithUV(this.displayWidth, this.displayHeight, 0.0, this.displayWidth / n, this.displayHeight / n + 0.0f);
        instance.addVertexWithUV(this.displayWidth, 0.0, 0.0, this.displayWidth / n, 0.0);
        instance.addVertexWithUV(0.0, 0.0, 0.0, 0.0, 0.0);
        instance.draw();
        GL11.glEnable(3008);
        GL11.glAlphaFunc(516, 0.1f);
        this.fontRenderer.drawStringWithShadow2("Loading...", 8, this.displayHeight / 2 - 16, -1);
        Display.swapBuffers();
    }


    public void displayGuiScreen(GuiScreen var_1_2B) {
        if (this.currentScreen instanceof GuiUnused) {
            return;
        }
        if (this.currentScreen != null) {
            this.currentScreen.onGuiClosed();
        }
        if (var_1_2B == null && this.theWorld == null) {
            var_1_2B = new GuiMainMenu();
        } else if (var_1_2B == null && this.thePlayer.health <= 0) {
            var_1_2B = new GuiGameOver();
        }
        if ((this.currentScreen = var_1_2B) != null) {
            this.setIngameNotInFocus();
            final ScaledResolution scaledResolution = new ScaledResolution(this.displayWidth, this.displayHeight);
            var_1_2B.setWorldAndResolution(this, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());
            this.skipRenderWorld = false;
        } else {
            this.setIngameFocus();
        }
    }

    public void checkGLError(final String string) {
        final int glGetError = GL11.glGetError();
        if (glGetError != 0) {
            final String gluErrorString = GLU.gluErrorString(glGetError);
            LogManager.getLogger().error("########## GL ERROR ##########");
            LogManager.getLogger().error("@ {}", string);
            LogManager.getLogger().error("{}: {}", glGetError, gluErrorString);
        }
    }

    public void shutdownMinecraftApplet() {
        try {
            if (this.downloadResourcesThread != null) {
                this.downloadResourcesThread.closeMinecraft();
            }
        } catch (Exception ignored) {
        }
        try {
            LogManager.getLogger().info("Stopping!");
            this.changeWorld(null);
            try {
                GLAllocation.deleteTexturesAndDisplayLists();
            } catch (Exception ex2) {
            }
            this.sndManager.closeMinecraft();
            Mouse.destroy();
            Keyboard.destroy();
        } finally {
            Display.destroy();
        }
        Thread.currentThread().interrupt();
        System.gc();
        System.exit(0);
    }

    @Override
    public void run() {
        LogManager.getLogger().info("Running Minecraft!");
        this.running = true;
        try {
            this.startGame();
        } catch (Exception exception) {
            exception.printStackTrace();
            this.displayUnexpectedThrowable(new UnexpectedThrowable("Failed to start game", exception));
            return;
        }
        try {
            long currentTimeMillis = System.currentTimeMillis();
            int n = 0;
            while (this.running) {
                AxisAlignedBB.clearBoundingBoxPool();
                Vec3D.initialize();
                if (Display.isCloseRequested()) {
                    this.shutdown();
                }
                if (this.isGamePaused) {
                    final float renderPartialTicks = this.timer.renderPartialTicks;
                    this.timer.updateTimer();
                    this.timer.renderPartialTicks = renderPartialTicks;
                } else {
                    this.timer.updateTimer();
                }
                for (int i = 0; i < this.timer.elapsedTicks; ++i) {
                    ++this.ticksRan;
                    this.runTick();
                }
                this.checkGLError("Pre render");
                if (this.isGamePaused) {
                    this.timer.renderPartialTicks = 1.0f;
                }
                this.sndManager.setListener(this.thePlayer, this.timer.renderPartialTicks);
                GL11.glEnable(3553);
                if (this.theWorld != null) {
                    while (this.theWorld.updatingLighting()) {
                    }
                }
                if (!this.skipRenderWorld) {
                    this.playerController.setPartialTime(this.timer.renderPartialTicks);
                    this.entityRenderer.updateCameraAndRender(this.timer.renderPartialTicks);
                }
                if (!Display.isActive() && this.fullscreen) {
                    this.toggleFullscreen();
                }
                if (Keyboard.isKeyDown(64)) {
                    this.displayDebugInfo();
                } else {
                    this.prevFrameTime = System.nanoTime();
                }
//                Thread.yield();
                Display.update();
                if (!this.fullscreen && (Display.getWidth() != this.displayWidth || Display.getHeight() != this.displayHeight)) {
                    this.displayWidth = Display.getWidth();
                    this.displayHeight = Display.getHeight();
                    if (this.displayWidth <= 0) {
                        this.displayWidth = 1;
                    }
                    if (this.displayHeight <= 0) {
                        this.displayHeight = 1;
                    }
                    this.resize(this.displayWidth, this.displayHeight);
                }
                if (this.gameSettings.limitFramerate) {
                    Thread.sleep(5L);
                }
                this.checkGLError("Post render");
                ++n;
                this.isGamePaused = (this.currentScreen != null && this.currentScreen.doesGuiPauseGame());
                while (System.currentTimeMillis() >= currentTimeMillis + 1000L) {
                    this.debug = n + " fps, " + WorldRenderer.chunksUpdated + " chunk updates";
                    WorldRenderer.chunksUpdated = 0;
                    currentTimeMillis += 1000L;
                    n = 0;
                }
            }
        } catch (MinecraftError ignored) {

        } catch (Exception exception2) {
            exception2.printStackTrace();
            this.displayUnexpectedThrowable(new UnexpectedThrowable("Unexpected error", exception2));
        } finally {
            this.shutdownMinecraftApplet();
        }
    }

    private void displayDebugInfo() {
        if (this.prevFrameTime == -1L) {
            this.prevFrameTime = System.nanoTime();
        }
        final long nanoTime = System.nanoTime();
        Minecraft.tickTimes[Minecraft.numRecordedFrameTimes++ & Minecraft.tickTimes.length - 1] = nanoTime - this.prevFrameTime;
        this.prevFrameTime = nanoTime;
        GL11.glClear(256);
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0, this.displayWidth, this.displayHeight, 0.0, 1000.0, 3000.0);
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0f, 0.0f, -2000.0f);
        GL11.glLineWidth(1.0f);
        GL11.glDisable(3553);
        final Tessellator instance = Tessellator.instance;
        instance.startDrawing(7);
        instance.setColorOpaque_I(538968064);
        instance.addVertex(0.0, this.displayHeight - 100, 0.0);
        instance.addVertex(0.0, this.displayHeight, 0.0);
        instance.addVertex(Minecraft.tickTimes.length, this.displayHeight, 0.0);
        instance.addVertex(Minecraft.tickTimes.length, this.displayHeight - 100, 0.0);
        instance.draw();
        long n = 0L;
        for (int i = 0; i < Minecraft.tickTimes.length; ++i) {
            n += Minecraft.tickTimes[i];
        }
        int i = (int) (n / 200000L / Minecraft.tickTimes.length);
        instance.startDrawing(7);
        instance.setColorOpaque_I(541065216);
        instance.addVertex(0.0, this.displayHeight - i, 0.0);
        instance.addVertex(0.0, this.displayHeight, 0.0);
        instance.addVertex(Minecraft.tickTimes.length, this.displayHeight, 0.0);
        instance.addVertex(Minecraft.tickTimes.length, this.displayHeight - i, 0.0);
        instance.draw();
        instance.startDrawing(1);
        for (int j = 0; j < Minecraft.tickTimes.length; ++j) {
            final int n2 = (j - Minecraft.numRecordedFrameTimes & Minecraft.tickTimes.length - 1) * 255 / Minecraft.tickTimes.length;
            int n3 = n2 * n2 / 255;
            n3 = n3 * n3 / 255;
            int n4 = n3 * n3 / 255;
            n4 = n4 * n4 / 255;
            instance.setColorOpaque_I(-16777216 + n4 + n3 * 256 + n2 * 65536);
            instance.addVertex(j + 0.5f, this.displayHeight - (double) Minecraft.tickTimes[j] / 200000L + 0.5f, 0.0);
            instance.addVertex(j + 0.5f, this.displayHeight + 0.5f, 0.0);
        }
        instance.draw();
        GL11.glEnable(3553);
    }

    public void shutdown() {
        this.running = false;
    }

    public void setIngameFocus() {
        if (!Display.isActive()) {
            return;
        }
        if (this.inGameHasFocus) {
            return;
        }
        this.inGameHasFocus = true;
        this.mouseHelper.grabMouse();
        this.displayGuiScreen(null);
        this.mouseTicksRan = this.ticksRan + 10000;
    }

    public void setIngameNotInFocus() {
        if (!this.inGameHasFocus) {
            return;
        }
        if (this.thePlayer != null) {
            this.thePlayer.resetPlayerKeyState();
        }
        this.inGameHasFocus = false;
        this.mouseHelper.ungrabMouseCursor();
    }

    public void displayInGameMenu() {
        if (this.currentScreen != null) {
            return;
        }
        this.displayGuiScreen(new GuiIngameMenu());
    }

    private void leftClickMouse(final int integer, final boolean boolean2) {
        if (this.playerController.field_1064_b) {
            return;
        }
        if (integer == 0 && this.leftClickCounter > 0) {
            return;
        }
        if (boolean2 && this.objectMouseOver != null && this.objectMouseOver.typeOfHit == 0 && integer == 0) {
            final int blockX = this.objectMouseOver.blockX;
            final int blockY = this.objectMouseOver.blockY;
            final int blockZ = this.objectMouseOver.blockZ;
            this.playerController.sendBlockRemoving(blockX, blockY, blockZ, this.objectMouseOver.sideHit);
            this.effectRenderer.addBlockHitEffects(blockX, blockY, blockZ, this.objectMouseOver.sideHit);
        } else {
            this.playerController.resetBlockRemoving();
        }
    }

    private void clickMouse(final int integer) {
        if (integer == 0 && this.leftClickCounter > 0) {
            return;
        }
        if (integer == 0) {
            this.entityRenderer.itemRenderer.resetEquippedProgress();
        }
        if (this.objectMouseOver == null) {
            if (integer == 0 && !(this.playerController instanceof PlayerControllerTest)) {
                this.leftClickCounter = 10;
            }
        } else if (this.objectMouseOver.typeOfHit == 1) {
            if (integer == 0) {
                this.thePlayer.a(this.objectMouseOver.entityHit);
            }
            if (integer == 1) {
                this.thePlayer.c(this.objectMouseOver.entityHit);
            }
        } else if (this.objectMouseOver.typeOfHit == 0) {
            final int blockX = this.objectMouseOver.blockX;
            final int n = this.objectMouseOver.blockY;
            final int blockZ = this.objectMouseOver.blockZ;
            final int sideHit = this.objectMouseOver.sideHit;
            final Block block = Block.blocksList[this.theWorld.getBlockId(blockX, n, blockZ)];
            if (integer == 0) {
                this.theWorld.onBlockHit(blockX, n, blockZ, this.objectMouseOver.sideHit);
                if (block != Block.bedrock || this.thePlayer.unusedByte >= 100) {
                    this.playerController.clickBlock(blockX, n, blockZ);
                }
            } else {
                final ItemStack currentItem = this.thePlayer.inventory.getCurrentItem();
                final int blockId = this.theWorld.getBlockId(blockX, n, blockZ);
                if (blockId > 0 && Block.blocksList[blockId].blockActivated(this.theWorld, blockX, n, blockZ, this.thePlayer)) {
                    return;
                }
                if (currentItem == null) {
                    return;
                }
                final int stackSize = currentItem.stackSize;
                if (currentItem.useItem(this.thePlayer, this.theWorld, blockX, n, blockZ, sideHit)) {
                    this.entityRenderer.itemRenderer.resetEquippedProgress();
                }
                if (currentItem.stackSize == 0) {
                    this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = null;
                } else if (currentItem.stackSize != stackSize) {
                    this.entityRenderer.itemRenderer.b();
                }
            }
        }
        if (integer == 1) {
            final ItemStack currentItem2 = this.thePlayer.inventory.getCurrentItem();
            if (currentItem2 != null) {
                final int n = currentItem2.stackSize;
                final ItemStack useItemRightClick = currentItem2.useItemRightClick(this.theWorld, this.thePlayer);
                if (useItemRightClick != currentItem2 || (useItemRightClick != null && useItemRightClick.stackSize != n)) {
                    this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = useItemRightClick;
                    this.entityRenderer.itemRenderer.d();
                    if (useItemRightClick.stackSize == 0) {
                        this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = null;
                    }
                }
            }
        }
    }

    public void toggleFullscreen() {
        try {
            this.fullscreen = !this.fullscreen;
            LogManager.getLogger().info("Toggle fullscreen!");
            if (this.fullscreen) {
                Display.setDisplayMode(Display.getDesktopDisplayMode());
                this.displayWidth = Display.getDisplayMode().getWidth();
                this.displayHeight = Display.getDisplayMode().getHeight();
                if (this.displayWidth <= 0) {
                    this.displayWidth = 1;
                }
                if (this.displayHeight <= 0) {
                    this.displayHeight = 1;
                }
            } else {

                this.displayWidth = this.tempDisplayWidth = Display.getWidth();
                this.displayHeight = this.tempDisplayHeight = Display.getHeight();
                if (this.displayWidth <= 0) {
                    this.displayWidth = 1;
                }
                if (this.displayHeight <= 0) {
                    this.displayHeight = 1;
                }
                Display.setDisplayMode(new DisplayMode(this.tempDisplayWidth, this.tempDisplayHeight));
            }
            this.setIngameNotInFocus();
            Display.setFullscreen(this.fullscreen);
            Display.update();
            Thread.sleep(1000L);
            if (this.fullscreen) {
                this.setIngameFocus();
            }
            if (this.currentScreen != null) {
                this.setIngameNotInFocus();
                this.resize(this.displayWidth, this.displayHeight);
            }
            LogManager.getLogger().info("Size: {}, {}", this.displayWidth, this.displayHeight);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void resize(int integer1, int integer2) {
        if (integer1 <= 0) {
            integer1 = 1;
        }
        if (integer2 <= 0) {
            integer2 = 1;
        }
        this.displayWidth = integer1;
        this.displayHeight = integer2;
        if (this.currentScreen != null) {
            final ScaledResolution scaledResolution = new ScaledResolution(integer1, integer2);
            this.currentScreen.setWorldAndResolution(this, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());
        }
    }

    private void clickMiddleMouseButton() {
        if (this.objectMouseOver != null) {
            int integer = this.theWorld.getBlockId(this.objectMouseOver.blockX, this.objectMouseOver.blockY, this.objectMouseOver.blockZ);
            if (integer == Block.grass.blockID) {
                integer = Block.dirt.blockID;
            }
            if (integer == Block.slabDouble.blockID) {
                integer = Block.slabSingle.blockID;
            }
            if (integer == Block.bedrock.blockID) {
                integer = Block.stone.blockID;
            }
            this.thePlayer.inventory.setCurrentItem(integer, this.playerController instanceof PlayerControllerTest);
        }
    }

    public void runTick() {
        this.ingameGUI.updateTick();
        if (!this.isGamePaused && this.theWorld != null) {
            this.playerController.updateController();
        }
        GlStateManager.bindTexture(this.renderEngine.getTexture("/terrain.png"));
        if (!this.isGamePaused) {
            this.renderEngine.updateDynamicTextures();
        }
        if (this.currentScreen == null && this.thePlayer != null && this.thePlayer.health <= 0) {
            this.displayGuiScreen(null);
        }
        if (this.currentScreen == null || this.currentScreen.allowUserInput) {
            while (Mouse.next()) {
                if (System.currentTimeMillis() - this.systemTime > 200L) {
                    continue;
                }
                final int eventDWheel = Mouse.getEventDWheel();
                if (eventDWheel != 0) {
                    this.thePlayer.inventory.changeCurrentItem(eventDWheel);
                }
                if (this.currentScreen == null) {
                    if (!this.inGameHasFocus && Mouse.getEventButtonState()) {
                        this.setIngameFocus();
                    } else {
                        if (Mouse.getEventButton() == 0 && Mouse.getEventButtonState()) {
                            this.clickMouse(0);
                            this.mouseTicksRan = this.ticksRan;
                        }
                        if (Mouse.getEventButton() == 1 && Mouse.getEventButtonState()) {
                            this.clickMouse(1);
                            this.mouseTicksRan = this.ticksRan;
                        }
                        if (Mouse.getEventButton() != 2 || !Mouse.getEventButtonState()) {
                            continue;
                        }
                        this.clickMiddleMouseButton();
                    }
                } else {
                    this.currentScreen.handleMouseInput();
                }
            }
            if (this.leftClickCounter > 0) {
                --this.leftClickCounter;
            }
            while (Keyboard.next()) {
                this.thePlayer.handleKeyPress(Keyboard.getEventKey(), Keyboard.getEventKeyState());
                if (Keyboard.getEventKeyState()) {
                    if (Keyboard.getEventKey() == 87) {
                        this.toggleFullscreen();
                    } else {
                        if (this.currentScreen != null) {
                            this.currentScreen.handleKeyboardInput();
                        } else {
                            if (Keyboard.getEventKey() == 1) {
                                this.displayInGameMenu();
                            }
                            if (Keyboard.getEventKey() == 63) {
                                this.gameSettings.thirdPersonView++;
                                if (this.gameSettings.thirdPersonView >= 3) {
                                    this.gameSettings.thirdPersonView = 0;
                                }
                            }
                            if (Keyboard.getEventKey() == this.gameSettings.keyBindInventory.keyCode) {
                                this.displayGuiScreen(new GuiInventory(this.thePlayer.inventory));
                            }
                            if (Keyboard.getEventKey() == this.gameSettings.keyBindDrop.keyCode) {
                                if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
                                    this.thePlayer.dropPlayerItemWithRandomChoice(this.thePlayer.inventory.decrStackSize(this.thePlayer.inventory.currentItem, this.thePlayer.inventory.getCurrentItem().stackSize), false);
                                else
                                    this.thePlayer.dropPlayerItemWithRandomChoice(this.thePlayer.inventory.decrStackSize(this.thePlayer.inventory.currentItem, 1), false);
                            }
                        }
                        for (int i = 0; i < 9; ++i) {
                            if (Keyboard.getEventKey() == 2 + i) {
                                this.thePlayer.inventory.currentItem = i;
                            }
                        }
                        if (Keyboard.getEventKey() != this.gameSettings.keyBindToggleFog.keyCode) {
                            continue;
                        }
                        this.gameSettings.setOptionFloatValue(4, (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54)) ? -1 : 1);
                    }
                }
            }
            if (this.currentScreen == null) {
                if (Mouse.isButtonDown(0) && this.ticksRan - this.mouseTicksRan >= this.timer.timerSpeed / 4.0f && this.inGameHasFocus) {
                    this.clickMouse(0);
                    this.mouseTicksRan = this.ticksRan;
                }
                if (Mouse.isButtonDown(1) && this.ticksRan - this.mouseTicksRan >= this.timer.timerSpeed / 4.0f && this.inGameHasFocus) {
                    this.clickMouse(1);
                    this.mouseTicksRan = this.ticksRan;
                }
            }
            this.leftClickMouse(0, this.currentScreen == null && Mouse.isButtonDown(0) && this.inGameHasFocus);
        }
        if (this.currentScreen != null) {
            this.mouseTicksRan = this.ticksRan + 10000;
        }
        if (this.currentScreen != null) {
            this.currentScreen.handleInput();
            if (this.currentScreen != null) {
                this.currentScreen.updateScreen();
            }
        }
        if (this.theWorld != null) {
            this.theWorld.difficultySetting = this.gameSettings.difficulty;
            if (!this.isGamePaused) {
                this.entityRenderer.updateRenderer();
                this.renderGlobal.updateClouds();
                this.theWorld.updateEntities();
                this.theWorld.tick();
                this.theWorld.randomDisplayUpdates(MathHelper.floor_double(this.thePlayer.posX), MathHelper.floor_double(this.thePlayer.posY), MathHelper.floor_double(this.thePlayer.posZ));
                this.effectRenderer.updateEffects();
            }
            if (MinecraftRunner.debug) {
                this.theWorld.setWorldTime(0);
                this.theWorld.setRaining(true);
            }
        }
        this.systemTime = System.currentTimeMillis();
    }

    public void startWorld(final String string) {
        this.changeWorld(null);
        System.gc();
        final World world = new World(new File(getMinecraftDir(), "saves"), string);
        if (world.isNewWorld) {
            this.changeWorld(world, "Generating level");
        } else {
            this.changeWorld(world, "Loading level");
        }
    }

    public void changeWorld(final World fe) {
        this.changeWorld(fe, "");
    }

    public void changeWorld(final World world, final String string) {
        if (this.theWorld != null) {
            this.theWorld.saveWorldIndirectly(this.loadingScreen);
        }
        if ((this.theWorld = world) != null) {
            this.playerController.func_717_a(world);
            this.thePlayer = null;
            world.player = null;
            this.worldInit(string);
            if (this.thePlayer == null) {
                (this.thePlayer = new EntityPlayerSP(this, world, this.sessionData)).preparePlayerToSpawn();
                this.playerController.flipPlayer(this.thePlayer);
            }
            this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
            if (this.renderGlobal != null) {
                this.renderGlobal.changeWorld(world);
            }
            if (this.effectRenderer != null) {
                this.effectRenderer.clearEffects(world);
            }
            this.playerController.func_6473_b(this.thePlayer);
            world.player = this.thePlayer;
            world.spawnPlayerWithLoadedChunks();
            if (world.isNewWorld) {
                world.saveWorldIndirectly(this.loadingScreen);
            }
        }
        System.gc();
        this.systemTime = 0L;
    }

    private void worldInit(final String string) {
        this.loadingScreen.printText(string);
        this.loadingScreen.displayLoadingString("Building terrain");
        final int n = 128;
        int n2 = 0;
        int n3 = n * 2 / 16 + 1;
        n3 *= n3;
        for (int i = -n; i <= n; i += 16) {
            int x = this.theWorld.x;
            int z = this.theWorld.z;
            if (this.theWorld.player != null) {
                x = (int) this.theWorld.player.posX;
                z = (int) this.theWorld.player.posZ;
            }
            for (int j = -n; j <= n; j += 16) {
                this.loadingScreen.setLoadingProgress(n2++ * 100 / n3);
                this.theWorld.getBlockId(x + i, 64, z + j);
                while (this.theWorld.updatingLighting()) {
                }
            }
        }
        this.loadingScreen.displayLoadingString("Simulating world for a bit");
        n3 = 2000;
        BlockSand.fallInstantly = true;
        for (int i = 0; i < n3; ++i) {
            this.theWorld.tickUpdates(true);
        }
        this.theWorld.func_656_j();
        BlockSand.fallInstantly = false;
    }

    public void installResource(String string, final File file) {
        final int index = string.indexOf("/");
        final String substring = string.substring(0, index);
        string = string.substring(index + 1);
        if (substring.equalsIgnoreCase("sound")) {
            this.sndManager.addSound(string, file);
        } else if (substring.equalsIgnoreCase("newsound")) {
            this.sndManager.addSound(string, file);
        } else if (substring.equalsIgnoreCase("music")) {
            this.sndManager.addMusic(string, file);
        }
    }

    public OpenGlCapsChecker getOpenGlCapsChecker() {
        return this.glCapabilities;
    }

    public String debugInfoRenders() {
        return this.renderGlobal.getDebugInfoRenders();
    }

    public String func_6262_n() {
        return this.renderGlobal.getDebugInfoEntities();
    }

    public String debugInfoEntities() {
        return "P: " + this.effectRenderer.getStatistics() + ". T: " + this.theWorld.func_687_d();
    }

    public void respawn() {
        if (this.thePlayer != null && this.theWorld != null) {
            this.theWorld.setEntityDead(this.thePlayer);
        }
        this.theWorld.a();
        (this.thePlayer = new EntityPlayerSP(this, this.theWorld, this.sessionData)).preparePlayerToSpawn();
        this.playerController.flipPlayer(this.thePlayer);
        if (this.theWorld != null) {
            this.theWorld.player = this.thePlayer;
            this.theWorld.spawnPlayerWithLoadedChunks();
        }
        this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
        this.playerController.func_6473_b(this.thePlayer);
        this.worldInit("Respawning");
    }

    public static File getMinecraftDir() {
        if (Minecraft.minecraftDir == null) {
            Minecraft.minecraftDir = getAppDir("minecraft");
        }
        return Minecraft.minecraftDir;
    }

    public static File getAppDir(final String string) {
        final String property = System.getProperty("user.home", ".");
        File file = null;
        switch (EnumOSMappingHelper.enumOSMappingArray[getOs().ordinal()]) {
            case 1:
            case 2: {
                file = new File(property, '.' + string + '/');
                break;
            }
            case 3: {
                final String getenv = System.getenv("APPDATA");
                if (getenv != null) {
                    file = new File(getenv, "." + string + '/');
                    break;
                }
                file = new File(property, '.' + string + '/');
                break;
            }
            case 4: {
                file = new File(property, "Library/Application Support/" + string);
                break;
            }
            default: {
                file = new File(property, string + '/');
                break;
            }
        }
        if (!file.exists() && !file.mkdirs()) {
            throw new RuntimeException(new StringBuilder().append("The working directory could not be created: ").append(file).toString());
        }
        return file;
    }

    private static EnumOS2 getOs() {
        final String lowerCase = System.getProperty("os.name").toLowerCase();
        if (lowerCase.contains("win")) {
            return EnumOS2.windows;
        }
        if (lowerCase.contains("mac")) {
            return EnumOS2.macos;
        }
        if (lowerCase.contains("solaris")) {
            return EnumOS2.solaris;
        }
        if (lowerCase.contains("sunos")) {
            return EnumOS2.solaris;
        }
        if (lowerCase.contains("linux")) {
            return EnumOS2.linux;
        }
        if (lowerCase.contains("unix")) {
            return EnumOS2.linux;
        }
        return EnumOS2.unknown;
    }
}
