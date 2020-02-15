package com.example.uilearning;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.TimeAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.uilearning.animation.MyObjectAnimator;
import com.example.uilearning.canvas_transform.SplashView;
import com.example.uilearning.custome_event.MyView;
import com.example.uilearning.path_bezier.PathView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(new PathView(this));
        final MyView myView = new MyView(this);
        myView.setBackgroundColor(Color.RED);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(500, 500);
        setContentView(myView,layoutParams);

//        myView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Toast.makeText(MainActivity.this, "OnTouchListener", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });


        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyObjectAnimator xScaleAnimator = MyObjectAnimator.ofFloat(myView, "scaleX", 0.5f);
                xScaleAnimator.setDuration(3000);
                xScaleAnimator.start();
            }
        });
    }
}
