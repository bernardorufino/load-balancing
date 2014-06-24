package com.marruf.multicpu.allocation;

import com.marruf.multicpu.core.Node;

import java.util.Collection;

public class OverloadedPolicy {

    public static boolean isOverloaded(Node node, Collection<Node> nodes) {
        return node.getLoad() > 1;
    }

    // Prevents instantiation
    private OverloadedPolicy() {
        throw new AssertionError("Cannot instantiate object from " + this.getClass());
    }
}
