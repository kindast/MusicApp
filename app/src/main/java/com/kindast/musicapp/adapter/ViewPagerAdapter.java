package com.kindast.musicapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.kindast.musicapp.fragment.SearchFragment;
import com.kindast.musicapp.fragment.HomeFragment;
import com.kindast.musicapp.fragment.LibraryFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new SearchFragment();
            case 2:
                return new LibraryFragment();
            default:
                return null;
        }
    }


    @Override
    public int getItemCount() {
        return 3;
    }
}
