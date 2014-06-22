package com.marruf.multicpu.allocation;

import com.marruf.multicpu.core.Node;
import com.marruf.multicpu.core.Task;
import com.marruf.multicpu.log.Log;

import java.util.Deque;
import java.util.List;
import java.util.PriorityQueue;

public class ReceiverInitiatedPolicy extends NoAllocationPolicy {

    @Override
    public void afterTaskCompletion(Node node, PriorityQueue<Node> workerNodes, List<Node> allNodes, Log.Event log) {
        if (node.hasWork()) return;
        /* TODO: Log number of messages */

        Node donor = null;
        for (Node worker : workerNodes) {
            if (worker.getTaskQueue().size() >= 2) {
                donor = worker;
                break;
            }
        }
        if (donor == null) return;

        Deque<Task> taskQueue = donor.getTaskQueue();
        Task task = taskQueue.removeLast();
        node.submit(task);
        workerNodes.add(node);
        log.actions.add(new Log.TaskOwnerChange(task.getId(), donor.getId(), node.getId()));
    }
}
