package com.example.pexelsapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private List<Photo> photos;
    private Context context;

    public PhotoAdapter(Context context, List<Photo> photos) {
        this.context = context;
        this.photos = photos;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Photo photo = photos.get(position);
        Picasso.get().load(photo.getSrc().getLarge()).into(holder.photoImageView);
        holder.photographerName.setText(photo.getPhotographer());

        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FullScreenActivity.class);
            ArrayList<String> imageUrls = new ArrayList<>();
            for (Photo p : photos) {
                imageUrls.add(p.getSrc().getLarge());
            }
            intent.putStringArrayListExtra("IMAGE_URLS", imageUrls);
            intent.putExtra("POSITION", position);
            context.startActivity(intent);
        });

        holder.downloadButton.setOnClickListener(v -> {
            downloadImage(photo.getSrc().getLarge());
        });
    }

    private void downloadImage(String url) {
        new Thread(() -> {
            try {
                URL downloadUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) downloadUrl.openConnection();
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), Uri.parse(url).getLastPathSegment());
                FileOutputStream outputStream = new FileOutputStream(file);

                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }

                outputStream.close();
                inputStream.close();
                connection.disconnect();

                ((Activity) context).runOnUiThread(() -> {
                    Toast.makeText(context, "Image downloaded", Toast.LENGTH_SHORT).show();
                });

            } catch (Exception e) {
                e.printStackTrace();
                ((Activity) context).runOnUiThread(() -> {
                    Toast.makeText(context, "Failed to download image", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView photoImageView;
        TextView photographerName;
        Button downloadButton;
        CardView cardView;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            photoImageView = itemView.findViewById(R.id.photoImageView);
            photographerName = itemView.findViewById(R.id.photographerName);
            downloadButton = itemView.findViewById(R.id.downloadButton);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
