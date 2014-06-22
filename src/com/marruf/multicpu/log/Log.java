package com.marruf.multicpu.log;

import com.google.common.collect.Iterables;
import com.marruf.multicpu.core.Node;
import com.marruf.multicpu.core.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

public class Log {

    public static abstract class Action {

        public abstract String longDescription();

        public abstract String shortDescription();

        @Override
        public String toString() {
            return longDescription();
        }
    }

    public static class TaskSubmission extends Action {

        public String taskId;
        public int nodeId;

        public TaskSubmission(String taskId, int nodeId) {
            this.taskId = taskId;
            this.nodeId = nodeId;
        }

        @Override
        public String longDescription() {
            return String.format("Task %s submitted to node %d", taskId, nodeId);
        }

        @Override
        public String shortDescription() {
            return String.format("TASK_SUBMISSION(N%d <- T%s)", nodeId, taskId);
        }
    }

    public static class TaskCompletion extends Action {

        public int nodeId;
        public String taskId;

        public TaskCompletion(int nodeId, String taskId) {
            this.nodeId = nodeId;
            this.taskId = taskId;
        }

        @Override
        public String longDescription() {
            return String.format("Node %s completed task %s", nodeId, taskId);
        }

        @Override
        public String shortDescription() {
            return String.format("TASK_COMPLETION(N%d, T%s)", nodeId, taskId);
        }
    }

    public static class TaskOwnerChange extends Action {

        public String taskId;
        public int oldNodeId;
        public int newNodeId;

        public TaskOwnerChange(String taskId, int oldNodeId, int newNodeId) {
            this.taskId = taskId;
            this.oldNodeId = oldNodeId;
            this.newNodeId = newNodeId;
        }

        @Override
        public String longDescription() {
            return String.format("Task %s change from node %d to %d", taskId, oldNodeId, newNodeId);
        }

        @Override
        public String shortDescription() {
            return String.format("TASK_CHANGE(T%s, N%d -> N%d)", taskId, oldNodeId, newNodeId);
        }
    }

    public static class Event extends Action {

        public int time;
        public List<Action> actions = new ArrayList<>();
        public String nodesDump;

        public Event(int time, Action... actions) {
            this.time = time;
            Collections.addAll(this.actions, actions);
        }

        @Override
        public String longDescription() {
            StringBuilder string = new StringBuilder();
            string.append("== ").append(time).append(": ");
            string.append(actions.get(0).longDescription()).append("\n");
            for (Action action : actions.subList(1, actions.size())) {
                string.append("- ").append(action.longDescription()).append("\n");
            }
            return string.toString();
        }

        @Override
        public String shortDescription() {
            StringBuilder string = new StringBuilder();
            string.append(time).append(": ");
            if (actions.size() > 1) string.append("[");
            for (Action action : actions.subList(0, actions.size() - 1)) {
                string.append(action.shortDescription()).append(", ");
            }
            string.append(actions.get(actions.size() - 1).shortDescription());
            if (actions.size() > 1) string.append("]");
            return string.toString();
        }

        public void dumpNodes(List<Node> nodes) {
            StringBuilder string = new StringBuilder();
            for (Node node : nodes) {
                string.append(node.getId()).append(": ");
                Queue<Task> taskQueue = node.getTaskQueue();
                if (taskQueue.isEmpty()) {
                    string.append("-");
                } else {
                    for (Task task : Iterables.limit(taskQueue, taskQueue.size() - 1)) {
                        string.append(task.getId()).append(", ");
                    }
                    string.append(Iterables.getLast(taskQueue).getId());
                }
                string.append("\n");
            }

            nodesDump = string.toString();
        }
    }

    // Prevents instantiation
    private Log() {
        throw new AssertionError("Cannot instantiate object from " + this.getClass());
    }
}
