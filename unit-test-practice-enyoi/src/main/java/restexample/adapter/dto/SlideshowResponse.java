package restexample.adapter.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SlideshowResponse {
    @SerializedName("slideshow")
    public SlideshowData slideshow;

    public static class SlideshowData {
        public String author;
        public String date;
        public String title;
        public List<Slide> slides;

    }

    public static class Slide {
        public String title;
        public String type;
        public List<String> items;
    }
}

