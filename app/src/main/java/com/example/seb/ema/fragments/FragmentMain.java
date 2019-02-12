package com.example.seb.ema.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.seb.ema.R;
import com.example.seb.ema.Utils.Utils;



public class FragmentMain extends Fragment implements FragmentTabHost.OnTabChangeListener{
    private FragmentTabHost mTabHost;
    private static final String TAG = "FragmentMain";

    public FragmentMain() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mTabHost = rootView.findViewById(R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);
        Bundle bundle = new Bundle();

        mTabHost.addTab(mTabHost.newTabSpec(Utils.TAB_PAGER_ADAPTER).setIndicator(Utils.TAB_PAGER_ADAPTER), PagerAdapterFragment.class, bundle);

        TextView tv =   mTabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
        tv.setAllCaps(false);

        return mTabHost;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTabHost = null;
    }

    @Override
    public void onTabChanged(String tabId) {
        Log.i(TAG, "onTabChanged*** tabId:"+tabId);
    }

}

