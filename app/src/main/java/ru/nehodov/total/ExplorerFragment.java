package ru.nehodov.total;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExplorerFragment extends Fragment implements ExplorerListener {

    private static final String FILE_PROVIDER_AUTHORITY = "ru.nehodov.total.file_provider";

    private RecyclerView recycler;

    private ExplorerAdapter adapter;
    private TextView toolbarTitle;

    private File rootPath;

    public ExplorerFragment() {
    }

    public static ExplorerFragment newInstance() {
        return new ExplorerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explorer, container, false);

        recycler = view.findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(requireActivity()));
        adapter = new ExplorerAdapter(this);
        recycler.setAdapter(adapter);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener((click) -> goToPrevious());
        toolbarTitle = view.findViewById(R.id.toolbarTitle);

        rootPath = Environment.getRootDirectory();
        toolbarTitle.setText(rootPath.getAbsolutePath());

        getAndSetFilesToAdapter();

        return view;
    }

    @Override
    public void goToDirectory(File directory) {
        this.rootPath = directory;
        getAndSetFilesToAdapter();
    }

    public void goToPrevious() {
        if (rootPath.getParentFile() != null) {
            this.rootPath = rootPath.getParentFile();
            getAndSetFilesToAdapter();
        }
    }

    public void getAndSetFilesToAdapter() {
        List<File> files = new ArrayList<>();
        if (rootPath.listFiles() != null) {
            files = Arrays.asList(rootPath.listFiles());
        }
        toolbarTitle.setText(rootPath.getAbsolutePath());
        adapter.setFiles(files);
    }

    @Override
    public void playMusicFile(Intent intent) {
        startActivity(intent);
    }

    static class ExplorerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final String MP3_EXTENSION = "mp3";

        private ExplorerListener listener;

        private List<File> files;

        public ExplorerAdapter(ExplorerListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            final View view = inflater.inflate(R.layout.explorer_item, parent, false);
            return new RecyclerView.ViewHolder(view) {
            };
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            final ImageView itemImage = holder.itemView.findViewById(R.id.itemImage);
            final TextView itemName = holder.itemView.findViewById(R.id.itemName);
            itemName.setText(files.get(position).getName());
            if (files.get(position).isDirectory()) {
                itemImage.setImageResource(R.drawable.ic_baseline_folder_64);
                holder.itemView.setOnClickListener(
                        click -> listener.goToDirectory(files.get(position)));
            } else if (getFileExtension(files.get(position)).toLowerCase().equals(MP3_EXTENSION)) {
                itemImage.setImageResource(R.drawable.ic_baseline_music_file_64);
                holder.itemView.setOnClickListener(
                        click -> {
                            Log.d(
                                    RecyclerView.Adapter.class.getSimpleName(),
                                    files.get(position).getName() + " setMp3Listener");
                            Intent musicIntent = new Intent();
                            musicIntent.setAction(Intent.ACTION_VIEW);
                            musicIntent.setDataAndType(
                                    FileProvider.getUriForFile(
                                            listener.getContext(),
                                            FILE_PROVIDER_AUTHORITY,
                                            files.get(position)),
                                    "audio/mp3");
                            musicIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            listener.playMusicFile(musicIntent);
                        }
                );
            } else {
                itemImage.setImageResource(R.drawable.ic_baseline_insert_drive_file_64);
                holder.itemView.setOnClickListener(click -> {
                });
            }

        }

        @Override
        public int getItemCount() {
            if (files != null) {
                return files.size();
            }
            return 0;
        }

        public void setFiles(List<File> files) {
            this.files = files;
            notifyDataSetChanged();
        }

        private static String getFileExtension(File file) {
            String fileName = file.getName();
            // если в имени файла есть точка и она не является первым символом в названии файла
            if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
                // то вырезаем все знаки после последней точки в названии файла,
                // то есть ХХХХХ.txt -> txt
                return fileName.substring(fileName.lastIndexOf(".") + 1);
            } else { // в противном случае возвращаем заглушку, то есть расширение не найдено
                return "";
            }
        }
    }
}