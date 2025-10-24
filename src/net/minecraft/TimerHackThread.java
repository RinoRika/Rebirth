
package net.minecraft;

public class TimerHackThread extends Thread {

    public final Minecraft mc;

    public TimerHackThread(final Minecraft mc, final String string) {
        super(string);
        this.mc = mc;
        this.setDaemon(true);
        this.start();
    }

    @Override
    public void run() {
        while (this.mc.running) {
            try {
                Thread.sleep(2147483647L);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
