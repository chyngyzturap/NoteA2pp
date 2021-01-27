package com.pharos.notea2pp;

public class Information {
    public int getIcon(int pos) {
        int[] icons = new int[] {
                R.raw.holidaypromotions,
                R.raw.fastdelivery,
                R.raw.bestservice
        };
        return icons[pos];
    }

    public String getTitle(int pos) {
        String[] titles = new String[] {
                "Holiday promotions",
                "Fast Delivery",
                "Best Service"
        };
        return titles[pos];
    }

    public String getDescription(int pos) {
        String[] descriptions = new String[] {
                "Holiday promotions every time!",
                "The fastest delivery in your town!",
                "We provide the best service!"
        };
        return descriptions[pos];
    }

}
