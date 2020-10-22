package com.francescobertamini.app_individuale.ui.gallery;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.francescobertamini.app_individuale.BuildConfig;
import com.francescobertamini.app_individuale.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import static androidx.core.content.FileProvider.getUriForFile;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    GalleryFragment parent;
    Context context;
    String[] images;
    ArrayList<String> listImages;

    public GalleryAdapter(GalleryFragment parent) throws IOException {
        this.parent = parent;
        this.context = parent.getContext();
        images = context.getAssets().list("champs_images");
        listImages = new ArrayList<>(Arrays.asList(images));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView _galleryListItemImage1;
        public ImageView _galleryListItemImage2;

        public ViewHolder(View itemView) {
            super(itemView);
            _galleryListItemImage1 = itemView.findViewById(R.id.galleryListItemImage1);
            _galleryListItemImage2 = itemView.findViewById(R.id.galleryListItemImage2);
        }

    }

    @NonNull
    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.list_item_gallery, parent, false);
        GalleryAdapter.ViewHolder viewHolder = null;
        viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryAdapter.ViewHolder holder, int position) {
        ImageView _galleryListItemImage1 = holder._galleryListItemImage1;
        ImageView _galleryListItemImage2 = holder._galleryListItemImage2;
        InputStream inputstream1 = null;
        InputStream inputstream2 = null;
        if (listImages.size() % 2 == 1 && 2 * position == listImages.size() - 1) {
            try {
                inputstream1 = context.getAssets().open("champs_images/" + listImages.get(2 * position));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Drawable drawable1 = Drawable.createFromStream(inputstream1, null);
            _galleryListItemImage1.setImageDrawable(drawable1);
            _galleryListItemImage2.setVisibility(View.GONE);
            _galleryListItemImage1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_image);
                    ImageView imageView = dialog.findViewById(R.id.imageDialogImage);
                    ImageButton shareButton = dialog.findViewById(R.id.imageDialogShareButton);
                    imageView.setImageDrawable(drawable1);
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    parent.getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    int width = displayMetrics.widthPixels;
                    imageView.getLayoutParams().width = width;
                    imageView.getLayoutParams().height = (width / 16) * 9;
                    shareButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("image/*");
                            File imagePath = new File(context.getFilesDir(), "champs_images");
                            File newFile = new File(imagePath, listImages.get(2 * position));
                            Uri contentUri = getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", newFile);
                            share.putExtra(Intent.EXTRA_STREAM, contentUri);
                            context.startActivity(Intent.createChooser(share, "Condividi immagine"));
                        }
                    });
                    dialog.show();
                }
            });
        } else {
            try {
                inputstream1 = context.getAssets().open("champs_images/" + listImages.get(2 * position));
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                inputstream2 = context.getAssets().open("champs_images/" + listImages.get(2 * position + 1));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Drawable drawable1 = Drawable.createFromStream(inputstream1, null);
        _galleryListItemImage1.setImageDrawable(drawable1);
        Drawable drawable2 = Drawable.createFromStream(inputstream2, null);
        _galleryListItemImage2.setImageDrawable(drawable2);
        _galleryListItemImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_image);
                ImageView imageView = dialog.findViewById(R.id.imageDialogImage);
                ImageButton shareButton = dialog.findViewById(R.id.imageDialogShareButton);
                imageView.setImageDrawable(drawable1);
                DisplayMetrics displayMetrics = new DisplayMetrics();
                parent.getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int width = displayMetrics.widthPixels;
                imageView.getLayoutParams().width = width;
                imageView.getLayoutParams().height = (width / 16) * 9;
                shareButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("image/*");
                        File imagePath = new File(context.getFilesDir(), "champs_images");
                        File newFile = new File(imagePath, listImages.get(2 * position));
                        Uri contentUri = getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", newFile);
                        share.putExtra(Intent.EXTRA_STREAM, contentUri);
                        context.startActivity(Intent.createChooser(share, "Condividi immagine"));
                    }
                });
                dialog.show();
            }
        });
        _galleryListItemImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_image);
                ImageView imageView = dialog.findViewById(R.id.imageDialogImage);
                ImageButton shareButton = dialog.findViewById(R.id.imageDialogShareButton);
                imageView.setImageDrawable(drawable2);
                DisplayMetrics displayMetrics = new DisplayMetrics();
                parent.getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int width = displayMetrics.widthPixels;
                imageView.getLayoutParams().width = width;
                imageView.getLayoutParams().height = (width / 16) * 9;
                shareButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("image/*");
                        File imagePath = new File(context.getFilesDir(), "champs_images");
                        File newFile = new File(imagePath, listImages.get(2 * position + 1));
                        Uri contentUri = getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", newFile);
                        share.putExtra(Intent.EXTRA_STREAM, contentUri);
                        context.startActivity(Intent.createChooser(share, "Condividi immagine"));
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listImages.size() % 2 == 0)
            return listImages.size() / 2;
        else return listImages.size() / 2 + 1;
    }
}
