package com.example.seb.ema.Utils;


public class Utils {

    public static  String TAB_PAGER_ADAPTER="Trainingsplan";

    public static class TrainingPlan {
        private String exerciseName;
        private Double weight;
        private int id;
        private int sets;
        private String startDate;
        private String endDate;
        private double increaseWeightTime;
        private double weightIncrease;
        private String youtubeUrl;


        public TrainingPlan(String exerciseName, Double weight, int id, int sets, String startDate, String endDate, double increaseWeightTime, double weightIncrease, String youtubeUrl) {
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

        //Important for Recivieng data from firebase
        public TrainingPlan(){}

        public String getExerciseName() {
            return exerciseName;
        }

        public double getWeight() { return weight; }

        public int getId() {
            return id;
        }

        public int getSets() {
            return sets;
        }

        public String getStartDate() {
            return startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public double getIncreaseWeightTime() {
            return increaseWeightTime;
        }

        public double getWeightIncrease() {
            return weightIncrease;
        }


        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if(getClass() != obj.getClass()){
                return false;
            }

            final TrainingPlan other = (TrainingPlan) obj;

            if (!this.exerciseName.equals(other.exerciseName)) {
                return false;
            }
            if (!this.weight.equals(other.weight)) {
                return false;
            }
            return true;
        }
    }
}
