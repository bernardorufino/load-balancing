package com.marruf.multicpu;

import com.marruf.multicpu.allocation.AllocationPolicy;
import com.marruf.multicpu.core.Node;
import com.marruf.multicpu.core.SystemSimulation;
import com.marruf.multicpu.generator.TaskGenerator;
import com.marruf.multicpu.log.Log;

import java.util.List;

import static com.google.common.base.Preconditions.checkState;

public class Main {

    public static final int NODES = 8;
    public static final int AVERAGE_TASK_LENGTH = 80; // In seconds
    public static final int DURATION = 1000; // In seconds
    public static final TaskGenerator GENERATOR = TaskGenerator.heavyLoad(NODES, AVERAGE_TASK_LENGTH, DURATION);
    public static final AllocationPolicy[] POLICIES = {
            AllocationPolicy.NO_POLICY,
            AllocationPolicy.RECEIVER_INITIATED,
            AllocationPolicy.SENDER_INITIATED,
            AllocationPolicy.RECEIVER_AND_SENDER
    };

    public static final boolean HISTORY = false;
    public static final boolean DUMP = false;
    public static final boolean SHORT = true;

    public static void main(String[] args) {
        for (AllocationPolicy policy : POLICIES) {
            SystemSimulation system = new SystemSimulation(NODES, GENERATOR, policy);
            system.run();

            System.out.println("\n==== " + policy + " ===\n");
            System.out.println("Finish time = " + system.getFinishTime());
            System.out.println();
            printMessageSummary(system.getNodes());
            System.out.println();
            if (HISTORY) printHistory(system.getHistory());

        }
    }

    private static void printMessageSummary(List<Node> nodes) {
        int sentMessages = 0;
        int receivedMessages = 0;
        Node mostSent = nodes.get(0);
        Node leastSent = nodes.get(0);
        Node mostReceived = nodes.get(0);
        Node leastReceived = nodes.get(0);
        System.out.println("Node id: Sent messages, Received messages");
        for (Node node : nodes) {
            int sents = node.getSentMessages();
            int receiveds = node.getReceivedMessages();
            sentMessages += sents;
            receivedMessages += receiveds;
            if (sents > mostSent.getSentMessages()) mostSent = node;
            if (sents < leastSent.getSentMessages()) leastSent = node;
            if (receiveds > mostReceived.getReceivedMessages()) mostReceived = node;
            if (receiveds < leastReceived.getReceivedMessages()) leastReceived = node;
            System.out.println("Node " + node.getId() + ": " + sents + " sents, " + receiveds + " receiveds");
        }
        System.out.println("The node with most sent messages was " + mostSent.getId() + " with " + mostSent.getSentMessages() + " messages");
        System.out.println("The node with least sent messages was " + leastSent.getId() + " with " + leastSent.getSentMessages() + " messages");
        System.out.println("The node with most received messages was " + mostReceived.getId() + " with " + mostReceived.getReceivedMessages() + " messages");
        System.out.println("The node with least received messages was " + leastReceived.getId() + " with " + leastReceived.getReceivedMessages() + " messages");
        checkState(receivedMessages == sentMessages);
        System.out.println("The total of sent and received messages was " + sentMessages);
    }

    private static void printHistory(List<Log.Event> history) {
        for (Log.Event entry : history) {
            System.out.println((SHORT) ? entry.shortDescription() : entry.longDescription());
            if (DUMP && entry.nodesDump != null) {
                System.out.println(entry.nodesDump);
            }
        }
    }

//    private static final List<Task> NORMAL = ImmutableList.of(
//            new Task("1", 0, 5, 50),
//            new Task("2", 0, 10, 20),
//            new Task("3", 1, 15, 10),
//            new Task("4", 2, 20, 5),
//            new Task("5", 3, 25, 70),
//            new Task("6", 1, 30, 10),
//            new Task("7", 2, 35, 10),
//            new Task("8", 2, 40, 5),
//            new Task("9", 3, 45, 10));
//
//    private static final List<Task> IDLE = ImmutableList.of(
//            new Task("1", 0, 5, 10),
//            new Task("2", 0, 20, 10));
}
