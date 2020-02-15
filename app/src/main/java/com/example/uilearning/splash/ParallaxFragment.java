package com.example.uilearning.splash;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class ParallaxFragment extends Fragment {

    //此Fragment上所有的需要实现视差动画的视图
    private List<View> parallaxViews = new ArrayList<View>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        int layoutId = args.getInt("layoutId");
        ParallaxLayoutInflater layoutInflater = new ParallaxLayoutInflater(inflater, getActivity(), this);
        return layoutInflater.inflate(layoutId, null);
    }

    public List<View> getParallaxViews() {
        return parallaxViews;
    }

}
