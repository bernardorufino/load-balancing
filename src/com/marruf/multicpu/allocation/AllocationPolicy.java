package com.marruf.multicpu.allocation;

import com.marruf.multicpu.core.Node;
import com.marruf.multicpu.core.Task;
import com.marruf.multicpu.log.Log;

import java.util.List;
import java.util.PriorityQueue;

public interface AllocationPolicy {

    public static final NoAllocationPolicy NO_POLICY = new NoAllocationPolicy();
    public static final ReceiverInitiatedPolicy RECEIVER_INITIATED = new ReceiverInitiatedPolicy();
    public static final SenderInitiatedPolicy SENDER_INITIATED = new SenderInitiatedPolicy();
    public static final ReceiverAndSenderPolicy RECEIVER_AND_SENDER = new ReceiverAndSenderPolicy();

    public void afterTaskCompletion(Node node, PriorityQueue<Node> workerNodes, List<Node> allNodes, Log.Event log);

    public Node changeOwnerNodeForTask(Task task, Node node, PriorityQueue<Node> workerNodes, List<Node> allNodes, Log.Event log);
}
