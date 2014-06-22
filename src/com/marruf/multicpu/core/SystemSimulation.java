package com.marruf.multicpu.core;

import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;
import com.marruf.multicpu.allocation.AllocationPolicy;
import com.marruf.multicpu.log.Log;
import com.marruf.multicpu.utils.Utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import static com.google.common.base.Preconditions.checkState;

public class SystemSimulation {

    private final List<Node> mNodes;
    private final PriorityQueue<Node> mWorkers;
    private final PeekingIterator<Task> mTaskFlow;
    private final List<Log.Event> mHistory = new ArrayList<>();
    private final AllocationPolicy mAllocation;
    private int mTime;

    public SystemSimulation(int numberOfNodes, Iterable<Task> tasks, AllocationPolicy allocation) {
        mNodes = new ArrayList<>(numberOfNodes);
        mWorkers = new PriorityQueue<>(numberOfNodes, NODE_COMPARATOR);
        for (int i = 0; i < numberOfNodes; i++) {
            Node node = new Node(i);
            mNodes.add(node);
        }
        mTaskFlow = Iterators.peekingIterator(tasks.iterator());
        mAllocation = allocation;
    }

    public void run() {
        while (mTaskFlow.hasNext() || !mWorkers.isEmpty()) {
            Seekable seekable = Utils.getSmallest(Seekable.COMPARATOR, mWorkers.peek(), mTaskFlow.hasNext() ? mTaskFlow.peek() : null);
            mTime = seekable.getSeekTime();
            Log.Event log = new Log.Event(mTime);

            if (seekable instanceof Node) { // A task has just been completed
                Node node = mWorkers.poll();
                checkState(node.hasWork(), "node doesn't have work");

                node.runNextTask();
                Task task = node.getLastCompletedTask();
                log.actions.add(new Log.TaskCompletion(node.getId(), task.getId()));

                if (node.hasWork()) {
                    mWorkers.add(node);
                }
                mAllocation.afterTaskCompletion(node, mWorkers, mNodes, log);

            } else if (seekable instanceof Task) { // New task has been submitted
                Task task = mTaskFlow.next();
                Node node = mNodes.get(task.getNodeId());
                log.actions.add(new Log.TaskSubmission(task.getId(), node.getId()));

                node = mAllocation.changeOwnerNodeForTask(task, node, mWorkers, mNodes, log);

                boolean wasIdle = !node.hasWork();
                node.submit(task);
                if (wasIdle) {
                    node.setTimeAfterIdle(mTime);
                    mWorkers.add(node);
                }
            }

            log.dumpNodes(mNodes);
            // System.out.println(log.shortDescription());
            // System.out.println(log.nodesDump);
            mHistory.add(log);
        }
    }

    public List<Log.Event> getHistory() {
        return mHistory;
    }

    public int getFinishTime() {
        return mTime;
    }

    public List<Node> getNodes() {
        return mNodes;
    }

    private final static Comparator<? super Node> NODE_COMPARATOR = new Comparator<Node>() {
        @Override
        public int compare(Node a, Node b) {
            return Integer.compare(a.getTimeAfterNextTask(), b.getTimeAfterNextTask());
        }
    };
}
