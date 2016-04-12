package com.example.shannon.bmobmusic.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by ChenXianglong on 2015/12/29.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {



    //fragment集合
    private ArrayList<Fragment> al_fragment = new ArrayList<>();
    //标题集合
    private ArrayList<String> al_titles = new ArrayList<>();


    public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> al_fragment, ArrayList<String> al_titles) {
        super(fm);

        this.al_fragment = al_fragment;
        this.al_titles = al_titles;


    }




    @Override
    public CharSequence getPageTitle(int position) {
        if(al_titles!=null){

            return al_titles.get(position);

        }else{

            return null;
        }

    }

    @Override
    public Fragment getItem(int position) {
        return al_fragment.get(position);
    }

    @Override
    public int getCount() {

        if(al_fragment!=null) {

            return al_fragment.size();


        }else{


            return 0;

        }


    }
}
