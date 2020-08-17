package com.piloterr.android.piloterr.executors;


import io.reactivex.Scheduler;

public interface PostExecutionThread {
    Scheduler getScheduler();
}
