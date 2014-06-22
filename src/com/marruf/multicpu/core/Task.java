package com.marruf.multicpu.core;

public class Task implements Seekable {

    private final String mId;
    private final int mNodeId;
    private final int mCreatedAt; // In seconds
    private final int mProcessingTimeRequired; // In seconds

    public Task(String id, int nodeId, int createdAt, int processingTimeRequired) {
        mId = id;
        mNodeId = nodeId;
        mCreatedAt = createdAt;
        mProcessingTimeRequired = processingTimeRequired;
    }

    public String getId() {
        return mId;
    }

    public int getNodeId() {
        return mNodeId;
    }

    public int getCreatedAt() {
        return mCreatedAt;
    }

    @Override
    public int getSeekTime() {
        return mCreatedAt;
    }

    public int getProcessingTimeRequired() {
        return mProcessingTimeRequired;
    }

    @Override
    public String toString() {
        return "Task< " +
                mId +
                ": node = " + mNodeId +
                ", createdAt = " + mCreatedAt +
                ", length = " + mProcessingTimeRequired +
                '>';
    }
}
