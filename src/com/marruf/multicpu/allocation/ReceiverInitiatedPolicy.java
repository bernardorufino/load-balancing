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
        if (OverloadedPolicy.isOverloaded(node, allNodes)) return;

        Node worker = null;
        for (Node p : workerNodes) {
            node.sendMessage();
            p.receiveMessage();
            if (OverloadedPolicy.isOverloaded(p, allNodes)) {
                worker = p;
                break;
            }
        }
        if (worker == null) return;

        Deque<Task> taskQueue = worker.getTaskQueue();
        Task task = taskQueue.removeLast();
        node.submit(task);
        if (node.getLoad() == 1) workerNodes.add(node);
        log.actions.add(new Log.TaskOwnerChange(task.getId(), worker.getId(), node.getId()));
    }

    @Override
    public String toString() {
        return "Receiver Initiated";
    }
}
