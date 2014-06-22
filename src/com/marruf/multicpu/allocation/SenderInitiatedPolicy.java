package com.marruf.multicpu.allocation;

import com.marruf.multicpu.core.Node;
import com.marruf.multicpu.core.Task;
import com.marruf.multicpu.log.Log;

import java.util.List;
import java.util.PriorityQueue;

public class SenderInitiatedPolicy extends NoAllocationPolicy {

    @Override
    public Node changeOwnerNodeForTask(Task task, Node node, PriorityQueue<Node> workerNodes, List<Node> allNodes, Log.Event log) {
        if (!node.hasWork()) return node;
        /* TODO: log messages */

        Node receiver = node;
        for (Node p : allNodes) {
            node.sendMessage();
            p.receiveMessage();
            if (!p.hasWork()) {
                receiver = p;
                break;
            }
        }
        if (receiver != node) {
            log.actions.add(new Log.TaskOwnerChange(task.getId(), node.getId(), receiver.getId()));
        }
        return receiver;
    }
}
