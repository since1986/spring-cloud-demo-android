package com.github.since1986.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.github.since1986.demo.helper.AppHelper;
import com.github.since1986.demo.util.BottomNavigationViewUtils;
import com.github.since1986.demo.util.FragmentUtils;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_EXIT_APP = "EXIT_APP";

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Bundle bundle = getIntent().getExtras();
        final String[] fragmentTags = new String[]{Test1Fragment.class.getName(), Test2Fragment.class.getName(), Test3Fragment.class.getName(), ProfileFragment.class.getName()};
        //初始化内容列表
        FragmentUtils.showBeforeAddByTagAndHideOthers(MainActivity.this, R.id.frame_layout_fragments_container_activity_main, Test1Fragment.class.getName(), bundle, fragmentTags[1], fragmentTags[2], fragmentTags[3]);

        //初始化底部导航
        bottomNavigation = findViewById(R.id.bottom_navigation_view_activity_main);
        BottomNavigationViewUtils.disableShiftMode(bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_map_menu_bottom_navigation:
                        FragmentUtils.showBeforeAddByTagAndHideOthers(MainActivity.this, R.id.frame_layout_fragments_container_activity_main, Test1Fragment.class.getName(), bundle, fragmentTags[1], fragmentTags[2], fragmentTags[3]);
                        break;
                    case R.id.item_description_menu_bottom_navigation:
                        FragmentUtils.showBeforeAddByTagAndHideOthers(MainActivity.this, R.id.frame_layout_fragments_container_activity_main, Test2Fragment.class.getName(), bundle, fragmentTags[0], fragmentTags[2], fragmentTags[3]);
                        break;
                    case R.id.item_notifications_menu_bottom_navigation:
                        FragmentUtils.showBeforeAddByTagAndHideOthers(MainActivity.this, R.id.frame_layout_fragments_container_activity_main, Test3Fragment.class.getName(), bundle, fragmentTags[0], fragmentTags[1], fragmentTags[3]);
                        break;
                    case R.id.item_profile_menu_bottom_navigation:
                        FragmentUtils.showBeforeAddByTagAndHideOthers(MainActivity.this, R.id.frame_layout_fragments_container_activity_main, ProfileFragment.class.getName(), bundle, fragmentTags[0], fragmentTags[1], fragmentTags[2]);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            if (intent.getBooleanExtra(KEY_EXIT_APP, false)) {
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        new AppHelper(this).showExitDialog();
    }
}
