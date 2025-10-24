
package net.minecraft.util;

public class UnexpectedThrowable {

    public final String description;
    public final Throwable exception;

    public UnexpectedThrowable(final String string, final Throwable exception) {
        this.description = string;
        this.exception = exception;
    }
}
