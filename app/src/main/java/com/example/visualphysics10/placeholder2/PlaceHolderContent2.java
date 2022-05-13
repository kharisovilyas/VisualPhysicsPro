package com.example.visualphysics10.placeholder2;

import androidx.annotation.NonNull;

import com.example.visualphysics10.R;
import com.example.visualphysics10.database.PhysicsData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaceHolderContent2 {

    public static final List<PlaceHolderContent2.PlaceHolderItem2> ITEMS = new ArrayList<PlaceHolderContent2.PlaceHolderItem2>();

    public static final Map<String, PlaceHolderContent2.PlaceHolderItem2> ITEM_MAP = new HashMap<String, PlaceHolderContent2.PlaceHolderItem2>();

    private static final int COUNT = 8;

    static {
        for (int i = 1; i <= COUNT; i++) {
            addItem(createPlaceHolderItem2(i));
        }
    }

    private static void addItem(PlaceHolderContent2.PlaceHolderItem2 item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static PlaceHolderContent2.PlaceHolderItem2 createPlaceHolderItem2(int position) {
        return new PlaceHolderContent2.PlaceHolderItem2(String.valueOf(position), switchLesson(position), switchProgress(position), switchDetails(position), switchImageView(position));
    }

    private static String switchProgress(int position) {
        switch (position){
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                return "Прогресс: " + PhysicsData.getSpeed();
            default:
                return "";
        }
    }

    private static String switchLesson(int position) {
        switch (position){
            case 1:
                return "Ускорение";
            case 2:
                return "Движение по Окружности";
            case 3:
                return "II Закон Ньютона";
            case 4:
                return "Движение под углом к горизонту";
            case 5:
                return "Законы Сохранения Импульса";
            case 6:
                return "Сила Трения";
            case 7:
                return "Колебания";
            case 8:
                return "Приломление света";
            default: return "";
        }
    }

    private static String switchDetails(int position) {
        switch (position){
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                return "Решено задач: " + PhysicsData.getSpeed();

            default: return "";
        }
    }


    static int switchImageView(int position) {
        switch (position){
            case 1: return R.drawable.lesson_1;
            case 2: return R.drawable.lesson_2;
            case 3: return R.drawable.lesson_3;
            case 4: return R.drawable.lesson_4;
            case 5: return R.drawable.lesson_5;
            default: return 0;
        }
    }

    public static class PlaceHolderItem2 {
        public final String id;
        public final String title;
        public final String task;
        public final String progress;
        public final int imageView;


        public PlaceHolderItem2(String id, String content, String task, String progress, int imageView) {
            this.id = id;
            this.title = content;
            this.task = task;
            this.progress = progress;
            this.imageView = imageView;
        }

        @NonNull
        @Override
        public String toString() {
            return title;
        }
    }

}
