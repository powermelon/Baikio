package de.baikio;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

public class MainActivityTabs extends FragmentActivity {
    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_tabs);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        mTabHost.addTab(
                mTabHost.newTabSpec("tab1").setIndicator("Tab 1", null),
                FragmentTab.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab2").setIndicator("Tab 2", null),
                FragmentTab.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab3").setIndicator("Tab 3", null),
                FragmentTab.class, null);
    }
}