package com.marruf.multicpu.allocation;

import com.marruf.multicpu.core.Node;
import com.marruf.multicpu.core.Task;
import com.marruf.multicpu.log.Log;

import java.util.List;
import java.util.PriorityQueue;

public class NoAllocationPolicy implements AllocationPolicy {

    @Override
    public void afterTaskCompletion(Node node, PriorityQueue<Node> workerNodes, List<Node> allNodes, Log.Event log) {
        /* Override */
    }

    @Override
    public Node changeOwnerNodeForTask(Task task, Node node, PriorityQueue<Node> workerNodes, List<Node> allNodes, Log.Event log) {
        return node;
    }
}
