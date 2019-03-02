package com.maiajam.calljots.adapter;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.maiajam.calljots.R;
import com.maiajam.calljots.ui.fragment.AllNotestFrag;

import java.util.ArrayList;
import java.util.List;

public class pageAdapter extends FragmentPagerAdapter {

    List<Fragment> fragmentList = new ArrayList<>();
    List<String> fragmentListTitle = new ArrayList<>();
    private String Name;
    private String PhoneNo;
    private String Image_uri;
    private int Contact_Id;
    Context context ;
    public pageAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setContactInfo(Context con,String name, String phoneNo, String image_uri, int contact_Id)
    {
     context = con ;
     Name = name ;
     PhoneNo = phoneNo ;
     Image_uri = image_uri ;
     Contact_Id = contact_Id ;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment f = new Fragment();
        switch (position){
            case 0 :
                f = new AllNotestFrag();
                Bundle bundle = new Bundle();
                bundle.putString("name",Name);
                bundle.putString("phoneNo",PhoneNo);
                bundle.putString("image_uri",Image_uri);
                bundle.putInt(context.getResources().getString(R.string.Contact_Id),Contact_Id);
                ((AllNotestFrag) f).SetFromWhere(Name,PhoneNo);
                f = fragmentList.get(position);
                f.setArguments(bundle);
                break;
            case 1 :
                f = new Fragment();
                Bundle bundle2 = new Bundle();
                bundle2.putString("name",Name);
                bundle2.putString("phoneNo",PhoneNo);
                bundle2.putInt(context.getResources().getString(R.string.Contact_Id),Contact_Id);
                f = fragmentList.get(position);
                f.setArguments(bundle2);
                break;
        }

        return f;
    }

    @Override
    public int getCount() {
        return fragmentListTitle.size();
    }

    public void AddFragment(Fragment fragment, String FragmentTitle)
    {
        fragmentList.add(fragment);
        fragmentListTitle.add(FragmentTitle);
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return fragmentListTitle.get(position);
    }
}