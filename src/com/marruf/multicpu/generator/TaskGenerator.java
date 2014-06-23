package com.marruf.multicpu.generator;

import com.google.common.collect.Lists;
import com.marruf.multicpu.core.Task;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static com.google.common.base.Preconditions.checkArgument;

public class TaskGenerator implements Iterable<Task> {

    private static final float DEFAULT_STANDARD_DEVIATION = 2 / 3f;

    public static TaskGenerator heavyLoad(int nodes, int averageTaskLength, int duration) {
        int period = averageTaskLength / nodes;
        checkArgument(period > 0, "averageTaskLength must be greater than the number of nodes");
        return new TaskGenerator(
                nodes,
                period,
                averageTaskLength,
                (int) (averageTaskLength * DEFAULT_STANDARD_DEVIATION),
                duration);
    }

    public static TaskGenerator lightLoad(int nodes, int averageTaskLength, int duration) {
        int period = averageTaskLength / (nodes / 3);
        checkArgument(period > 0, "averageTaskLength must be greater than the number of nodes / 3");
        return new TaskGenerator(
                nodes,
                period,
                averageTaskLength,
                (int) (averageTaskLength * DEFAULT_STANDARD_DEVIATION),
                duration);
    }

    private final int mPeriod;
    private final int mDuration;
    private final int mTaskLengthMean;
    private final int mNodes;
    private final int mTaskLengthDeviation;
    private final List<Task> mIteratorMemory;

    public TaskGenerator(int nodes, int period, int taskLengthMean, int taskLengthDeviation, int duration) {
        mNodes = nodes;
        mPeriod = period;
        mTaskLengthMean = taskLengthMean;
        mTaskLengthDeviation = taskLengthDeviation;
        mDuration = duration;
        mIteratorMemory = Lists.newArrayList(new TaskIterator());
    }

    @Override
    public Iterator<Task> iterator() {
        return mIteratorMemory.iterator();
    }

    private class TaskIterator implements Iterator<Task> {

        private Random mRandom = new Random();
        private int mTime = 0;
        private int mCount = 0;

        @Override
        public boolean hasNext() {
            return mTime < mDuration;
        }

        @Override
        public Task next() {
            int node = mRandom.nextInt(mNodes);
            int taskLength;
            do {
                taskLength = (int) mRandom.nextGaussian() * mTaskLengthDeviation + mTaskLengthMean;
            } while (taskLength < 0);
            Task task = new Task(Integer.toString(mCount++), node, mTime, taskLength);
            mTime += mPeriod;
            return task;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
