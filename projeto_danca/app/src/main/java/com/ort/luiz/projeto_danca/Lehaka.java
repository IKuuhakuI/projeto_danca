package com.ort.luiz.projeto_danca;

import java.util.ArrayList;

/**
 * Created by luiz.carrion on 17/10/2018.
 */

public class Lehaka {

    String name;
    String image;
    boolean isVotable;
    int kapaim;

    String facebook;
    String instagram;
    String website;

    ArrayList<Integer> events_ids;

    public Lehaka(String name, String image, boolean isVotable, int kapaim, String facebook, String instagram, String website, ArrayList<Integer> events_ids){
        this.name = name;
    }

}
