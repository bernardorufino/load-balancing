package com.marruf.multicpu.core;

import java.util.Deque;
import java.util.LinkedList;

import static com.google.common.base.Preconditions.checkState;

public class Node implements Seekable {

    private final Deque<Task> mTaskQueue = new LinkedList<Task>();
    private final int mId;
    private Task mLastCompletedTask = null;
    private int mTime = 0;
    private int mSeekTime;
    private int mReceivedMessages = 0;
    private int mSentMessages = 0;

    public Node(int id) {
        mId = id;
    }

    public void submit(Task task) {
        mTaskQueue.add(task);
    }

    public void runNextTask() {
        checkState(hasWork(), "there is no task to execute");

        mLastCompletedTask = mTaskQueue.poll();
        mTime += mLastCompletedTask.getProcessingTimeRequired();
    }

    public int getTimeAfterNextTask() {
        if (mTaskQueue.isEmpty()) {
            return Integer.MAX_VALUE;
        }
        Task nextTask = mTaskQueue.peek();
        return mTime + nextTask.getProcessingTimeRequired();
    }

    public void setTimeAfterIdle(int time) {
        mTime = time;
    }

    public int getSeekTime() {
        return getTimeAfterNextTask();
    }

    public Task getLastCompletedTask() {
        return mLastCompletedTask;
    }

    public int getLoad() {
        return mTaskQueue.size();
    }

    public boolean hasWork() {
        return !mTaskQueue.isEmpty();
    }

    public int getId() {
        return mId;
    }

    public Deque<Task> getTaskQueue() {
        return mTaskQueue;
    }

    public void sendMessage() {
        mSentMessages++;
    }

    public int getSentMessages() {
        return mSentMessages;
    }

    public void receiveMessage() {
        mReceivedMessages++;
    }

    public int getReceivedMessages() {
        return mReceivedMessages;
    }

    @Override
    public String toString() {
        if (hasWork()) {
            return String.format("%s [%d] finish at %d", mId, mTaskQueue.size(), getTimeAfterNextTask());
        } else {
            return String.format("%s [IDLE]", mId);
        }
    }
}
