package com.example.seb.ema.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import com.example.seb.ema.fragmentpagerefresh.Utils;
import com.example.seb.ema.R;
import org.w3c.dom.Text;

/**
 * Created by noor on 01/04/15.
 */
public class MyPagerAdapter extends PagerAdapter {

    private static final String TAG = "MyPagerAdapter";
    private ArrayList<Utils.TrainingPlan> mTrainingPlans;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public MyPagerAdapter(ArrayList<Utils.TrainingPlan> trainingPlans, Context context) {
        this.mTrainingPlans = trainingPlans;
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    //Abstract method in PagerAdapter
    @Override
    public int getCount() {
        return mTrainingPlans.size();
    }


    //Abstract method in PagerAdapter

    /**
     * @return true if the value returned from {@link #instantiateItem(ViewGroup, int)} is the
     * same object as the {@link View} added to the {@link android.support.v4.view.ViewPager}.
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }

    /**
     * Instantiate the {@link View} which should be displayed at {@code position}. Here we
     * inflate a layout from the apps resources and then change the text view to signify the position.
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // Inflate a new layout from our resources
        View view = mLayoutInflater.inflate(R.layout.photo_layout, container, false);
        // Retrieve a TextView from the inflated View, and update it's text

        //Get All Input TextViews
        TextView enIn=view.getRootView().findViewById(R.id.mExerciseNOut);
        TextView weightIn =view.getRootView().findViewById(R.id.mWeightOut);
        TextView setsIn  =view.getRootView().findViewById(R.id.mSetsOut);
        TextView startDateIn=view.getRootView().findViewById(R.id.mStartDateOut);
        TextView endDateIn=view.getRootView().findViewById(R.id.mEndDateOut);
        TextView increaseWeightTimeIn=view.getRootView().findViewById(R.id.mIncreaseWeightTimeOut);
        TextView weightIncreaseIn=view.getRootView().findViewById(R.id.mIncreaseWeightOut);

        Utils.TrainingPlan trainingPlan = mTrainingPlans.get(position);
        enIn.setText(trainingPlan.getExerciseName());
        try{
            weightIn.setText(Double.toString(trainingPlan.getWeight()));
            setsIn.setText(Double.toString(trainingPlan.getSets()));
            setsIn.setText(Double.toString(trainingPlan.getSets()));
            startDateIn.setText(trainingPlan.getStartDate().toString());
            endDateIn.setText(trainingPlan.getEndDate().toString());
            increaseWeightTimeIn.setText(Double.toString(trainingPlan.getIncreaseWeightTime()));
            weightIncreaseIn.setText(Double.toString(trainingPlan.getWeightIncrease()));
        }catch (Exception e){
            e.printStackTrace();
        }

        view.setTag(trainingPlan);
        // Add the newly created View to the ViewPager
        container.addView(view);

        Log.i(TAG, "instantiateItem() [position: " + position + "]" + " childCount:" + container.getChildCount());
        // Return the View
        return view;
    }

    /**
     * Destroy the item from the {@link android.support.v4.view.ViewPager}. In our case this is simply removing the
     * {@link View}.
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        Log.i(TAG, "destroyItem() [position: " + position + "]" + " childCount:" + container.getChildCount());
    }

    /**
     * This method is only gets called when we invoke {@link #notifyDataSetChanged()} on this adapter.
     * Returns the index of the currently active fragments.
     * There could be minimum two and maximum three active fragments(suppose we have 3 or more  fragments to show).
     * If there is only one fragment to show that will be only active fragment.
     * If there are only two fragments to show, both will be in active state.
     * PagerAdapter keeps left and right fragments of the currently visible fragment in ready/active state so that it could be shown immediate on swiping.
     * Currently Active Fragments means one which is currently visible one is before it and one is after it.
     *
     * @param object Active Fragment reference
     * @return Returns the index of the currently active fragments.
     */
    @Override
    public int getItemPosition(Object object) {
        Utils.TrainingPlan trainingPlan = (Utils.TrainingPlan) ((View) object).getTag();
        int position = mTrainingPlans.indexOf(trainingPlan);
        if (position >= 0) {
            // The current data matches the data in this active fragment, so let it be as it is.
            return position;
        } else {
            // Returning POSITION_NONE means the current data does not matches the data this fragment is showing right now.  Returning POSITION_NONE constant will force the fragment to redraw its view layout all over again and show new data.
            return POSITION_NONE;
        }
    }


}
