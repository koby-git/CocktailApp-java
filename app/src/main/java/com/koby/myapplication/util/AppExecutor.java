package com.koby.myapplication.util;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutor {
    private static final AppExecutor ourInstance = new AppExecutor();

    public static AppExecutor getInstance() {
        return ourInstance;
    }

    private AppExecutor() {}

    private Executor diskIO = Executors.newSingleThreadExecutor();

    public Executor getDiskIO() {
        return diskIO;
    }
}
