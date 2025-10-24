
package net.minecraft;

public class Timer {

    float tps;
    private double f;
    public int elapsedTicks;
    public float renderPartialTicks;
    public float speed;
    public float elapsedDelta;
    private long lastSysClock;
    private long lastHRClock;
    private double adjustment;

    public Timer(final float tps) {
        this.speed = 1.0f;
        this.elapsedDelta = 0.0f;
        this.adjustment = 1.0;
        this.tps = tps;
        this.lastSysClock = System.currentTimeMillis();
        this.lastHRClock = System.nanoTime() / 1000000L;
    }

    public void updateTimer() {
        final long currentTimeMillis = System.currentTimeMillis();
        final long n = currentTimeMillis - this.lastSysClock;
        final long n2 = System.nanoTime() / 1000000L;
        if (n > 1000L) {
            final double n3 = n / (double) (n2 - this.lastHRClock);
            this.adjustment += (n3 - this.adjustment) * 0.20000000298023224;
            this.lastSysClock = currentTimeMillis;
            this.lastHRClock = n2;
        }
        if (n < 0L) {
            this.lastSysClock = currentTimeMillis;
            this.lastHRClock = n2;
        }
        final double f = n2 / 1000.0;
        double n3 = (f - this.f) * this.adjustment;
        this.f = f;
        if (n3 < 0.0) {
            n3 = 0.0;
        }
        if (n3 > 1.0) {
            n3 = 1.0;
        }
        this.elapsedDelta += (float) (n3 * this.speed * this.tps);
        this.elapsedTicks = (int) this.elapsedDelta;
        this.elapsedDelta -= this.elapsedTicks;
        if (this.elapsedTicks > 10) {
            this.elapsedTicks = 10;
        }
        this.renderPartialTicks = this.elapsedDelta;
    }
}
