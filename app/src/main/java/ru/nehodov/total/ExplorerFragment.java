package ru.nehodov.total;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
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


    static class ExplorerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


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
            return new RecyclerView.ViewHolder(view) {};
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
            } else {
                itemImage.setImageResource(R.drawable.ic_baseline_insert_drive_file_64);
                holder.itemView.setOnClickListener(click -> {});
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

    }



}