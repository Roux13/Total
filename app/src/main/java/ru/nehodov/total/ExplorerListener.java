package ru.nehodov.total;

import android.content.Context;
import android.content.Intent;

import java.io.File;

public interface ExplorerListener {
    void goToDirectory(File directory);

    void playMusicFile(Intent intent);

    Context getContext();
}