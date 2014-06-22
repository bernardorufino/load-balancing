package com.marruf.multicpu.core;

import java.util.Comparator;

public interface Seekable {

    public static Comparator<Seekable> COMPARATOR = new Comparator<Seekable>() {
        @Override
        public int compare(Seekable a, Seekable b) {
            return Integer.compare(a.getSeekTime(), b.getSeekTime());
        }
    };

    public int getSeekTime();
}
