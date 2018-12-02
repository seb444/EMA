package com.example.seb.ema.fragmentpagerefresh;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by noor on 01/04/15.
 */
public class Utils {

    public static  String EXERSICE_NAME = "ExerciseName";
    public static  String TAB_PAGER_ADAPTER="PagerAdapter";
    public static final String TAB_FRAGMENT_PAGER_ADAPTER="FragmentPagerAdapter";
    public static final String TAB_FRAGMENT_STATE_PAGER_ADAPTER="FragmentStatePagerAdapter";
    public static final String EXTRA_TITLE ="title";
    public static final String EXTRA_IMAGE_URL ="exersiceName";

    public static class DummyItem{
        private String exerciseName;
        private Double weight;
        private int id;
        private int sets;
        private Date startDate;
        private Date endDate;
        private double increaseWeightTime;
        private double weightIncrease;
        private String youtubeUrl;
        private static boolean first;

        public DummyItem(String exerciseName, Double weight, int id, int sets, Date startDate, Date endDate, double increaseWeightTime, double weightIncrease, String youtubeUrl) {
            this.exerciseName = exerciseName;
            this.weight = weight;
            this.id = id;
            this.sets = sets;
            this.startDate = startDate;
            this.endDate = endDate;
            this.increaseWeightTime = increaseWeightTime;
            this.weightIncrease = weightIncrease;
            this.youtubeUrl = youtubeUrl;
        }


        public String getExerciseName() {
            return exerciseName;
        }

        public double getWeight() { return weight; }

        public void setWeight(double weight) {
            this.weight = weight;
        }



        public static void setFirst(boolean first) {
            DummyItem.first = first;
        }

        public static boolean getFirst(){
            return first;
        }

        public void setSets(int sets) {
            this.sets = sets;
        }

        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        public void setEndDate(Date endDate) {
            this.endDate = endDate;
        }

        public void setIncreaseWeightTime(double increaseWeightTime) {
            this.increaseWeightTime = increaseWeightTime;
        }

        public void setWeightIncrease(double weightIncrease) {
            this.weightIncrease = weightIncrease;
        }

        public void setYoutubeUrl(String youtubeUrl) {
            this.youtubeUrl = youtubeUrl;
        }


        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if(getClass() != obj.getClass()){
                return false;
            }

            final DummyItem other = (DummyItem) obj;

            if (!this.exerciseName.equals(other.exerciseName)) {
                return false;
            }
            if (!this.weight.equals(other.weight)) {
                return false;
            }

            /*if(obj == this){
                return true;
            }*/
            return true;
        }



        //The hashCode() method of objects is used when you insert them into a HashTable, HashMap or HashSet.
        //Since we are using these objects to store in List, we are not going to override it.
        /*@Override
        public int hashCode() {
            return super.hashCode();
        }*/
    }

    public  static ArrayList<String> exerciseNames = new ArrayList<String>() {
        {

        }
    };

    public  static ArrayList<Double> weights = new ArrayList<Double>() {{

    }

    };

    public static void setExerciseNames(ArrayList<String> exerciseNames) {
        Utils.exerciseNames = exerciseNames;
    }

    public static void setWeights(ArrayList<Double> weights) {
        Utils.weights=weights;
    }

//    public static ArrayList<DummyItem> getThumbImageList(){
//        ArrayList<DummyItem> imageThumbsList = new ArrayList<>();
//
//        for (int i = 0; i < exerciseNames.size(); i++) {
//            imageThumbsList.add(new DummyItem(exerciseNames.get(i), weights.get(i),i));
//        }
//
//        return imageThumbsList;
//    }
//
//
//    public static ArrayList<DummyItem> getFullImageList(){
//        ArrayList<DummyItem> fullImageList = new ArrayList<>();
//
//        for (int i = 0; i < weights.size(); i++) {
//           fullImageList.add(new DummyItem(exerciseNames.get(i), weights.get(i),i));
//       }
//
//        return fullImageList;
//    }

}
