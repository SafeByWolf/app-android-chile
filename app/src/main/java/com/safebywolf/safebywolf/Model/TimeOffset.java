package com.safebywolf.safebywolf.Model;

public class TimeOffset {
    private static long offset;

    public TimeOffset(long offset) {
        this.offset=offset;
    }

    public static long getOffset() {
        return offset;
    }

    public static void setOffset(long offset) {
        TimeOffset.offset = offset;
    }
}
