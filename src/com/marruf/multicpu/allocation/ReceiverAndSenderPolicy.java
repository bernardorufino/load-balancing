package com.marruf.multicpu.allocation;

import com.marruf.multicpu.core.Node;
import com.marruf.multicpu.core.Task;
import com.marruf.multicpu.log.Log;

import java.util.List;
import java.util.PriorityQueue;

public class ReceiverAndSenderPolicy implements AllocationPolicy {

    @Override
    public void afterTaskCompletion(Node node, PriorityQueue<Node> workerNodes, List<Node> allNodes, Log.Event log) {
        AllocationPolicy.RECEIVER_INITIATED.afterTaskCompletion(node, workerNodes, allNodes, log);
    }

    @Override
    public Node changeOwnerNodeForTask(Task task, Node node, PriorityQueue<Node> workerNodes, List<Node> allNodes, Log.Event log) {
        return AllocationPolicy.SENDER_INITIATED.changeOwnerNodeForTask(task, node, workerNodes, allNodes, log);
    }

    @Override
    public String toString() {
        return "Receiver and Sender Initiated";
    }
}
