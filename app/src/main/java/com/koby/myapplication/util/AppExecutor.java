package com.koby.myapplication.util;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutor {
    private static final AppExecutor ourInstance = new AppExecutor();

    public static AppExecutor getInstance() {
        return ourInstance;
    }

    private AppExecutor() {}

    private Executor mDiskIO = Executors.newSingleThreadExecutor();
    private final Executor mMainThreadExecutor = new MainThreadExecutor();

    public Executor diskIO() {
        return mDiskIO;
    }
    public Executor mainThread(){ return mMainThreadExecutor; }

    private static class MainThreadExecutor implements Executor{

        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
