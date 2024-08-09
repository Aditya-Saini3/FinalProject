package com.example.pexelsapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.List;

public class FullScreenImageAdapter extends FragmentStateAdapter {

    private List<String> imageUrls;

    public FullScreenImageAdapter(@NonNull FragmentActivity fragmentActivity, List<String> imageUrls) {
        super(fragmentActivity);
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return FullScreenImageFragment.newInstance(imageUrls.get(position));
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }
}
