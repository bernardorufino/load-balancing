package com.marruf.multicpu.core;

import com.google.common.collect.ImmutableList;
import com.marruf.multicpu.allocation.AllocationPolicy;
import com.marruf.multicpu.log.Log;

import java.util.List;

public class Main {

    public static final boolean DUMP = true;
    public static final boolean SHORT = true;

    private static final List<Task> NORMAL = ImmutableList.of(
            new Task("1", 0,  5, 50),
            new Task("2", 0, 10, 20),
            new Task("3", 1, 15, 10),
            new Task("4", 2, 20, 5),
            new Task("5", 3, 25, 70),
            new Task("6", 1, 30, 10),
            new Task("7", 2, 35, 10),
            new Task("8", 2, 40,  5),
            new Task("9", 3, 45, 10));

    private static final List<Task> IDLE = ImmutableList.of(
            new Task("1", 0,  5, 10),
            new Task("2", 0, 20, 10));

    public static void main(String[] args) {
        SystemSimulation system = new SystemSimulation(4, NORMAL, AllocationPolicy.RECEIVER_INITIATED);
        system.run();
        printHistory(system.getHistory());
    }

    private static void printHistory(List<Log.Event> history) {
        for (Log.Event entry : history) {
            System.out.println((SHORT) ? entry.shortDescription() : entry.longDescription());
            if (DUMP && entry.nodesDump != null) {
                System.out.println(entry.nodesDump);
            }
        }
    }


}
