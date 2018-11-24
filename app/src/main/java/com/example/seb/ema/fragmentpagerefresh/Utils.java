package com.example.seb.ema.fragmentpagerefresh;

import java.util.ArrayList;

/**
 * Created by noor on 01/04/15.
 */
public class Utils {

    public static  String IMAGE_URL = "imageUrl";
    public static  String TAB_PAGER_ADAPTER="PagerAdapter";
    public static final String TAB_FRAGMENT_PAGER_ADAPTER="FragmentPagerAdapter";
    public static final String TAB_FRAGMENT_STATE_PAGER_ADAPTER="FragmentStatePagerAdapter";
    public static final String EXTRA_TITLE ="title";
    public static final String EXTRA_IMAGE_URL ="imageUrl";

    public static class DummyItem{
        private String imageUrl;
        private String imageTitle;

        public DummyItem(String imageUrl, String imageTitle) {
            this.imageUrl = imageUrl;
            this.imageTitle = imageTitle;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getImageTitle() {
            return imageTitle;
        }

        public void setImageTitle(String imageTitle) {
            this.imageTitle = imageTitle;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
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
            if (!this.imageUrl.equals(other.imageUrl)) {
                return false;
            }
            if (!this.imageTitle.equals(other.imageTitle)) {
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

    public  static ArrayList<String> imageThumbUrls = new ArrayList<String>() {
        {
            add("TESST");
            add("TESST");
            add("TESST");
            add("TESST");
        }
    };

    public  static ArrayList<String> imageUrls = new ArrayList<String>() {{
        add("TESST");
        add("TESST");
        add("TESST");
        add("TESST");
    }

    };

    public static void setImageThumbUrls(ArrayList<String> imageThumbUrls) {
        Utils.imageThumbUrls = imageThumbUrls;
    }

    public static void setImageUrls(ArrayList<String> imageUrls) {
        Utils.imageUrls = imageUrls;
    }

    public static ArrayList<DummyItem> getThumbImageList(){
        ArrayList<DummyItem> imageThumbsList = new ArrayList<>();

        for (int i = 0; i < imageThumbUrls.size(); i++) {
            imageThumbsList.add(new DummyItem(imageThumbUrls.get(i),imageThumbUrls.get(i)+i));
        }

        return imageThumbsList;
    }


    public static ArrayList<DummyItem> getFullImageList(){
        ArrayList<DummyItem> fullImageList = new ArrayList<>();

        for (int i = 0; i < imageUrls.size(); i++) {
            fullImageList.add(new DummyItem(imageUrls.get(i), "Full Image:"+i));
        }

        return fullImageList;
    }

}
