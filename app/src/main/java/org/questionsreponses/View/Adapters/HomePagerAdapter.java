package org.questionsreponses.View.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.List;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Adapters/HomePagerAdapter.class */
public class HomePagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private List<String> titres;

    public HomePagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public HomePagerAdapter(FragmentManager fragmentManager, List<Fragment> list) {
        super(fragmentManager);
        this.fragments = list;
    }

    public HomePagerAdapter(FragmentManager fragmentManager, List<Fragment> list, List<String> list2) {
        super(fragmentManager);
        this.fragments = list;
        this.titres = list2;
    }

    @Override // android.support.v4.view.PagerAdapter
    public int getCount() {
        return this.fragments.size();
    }

    @Override // android.support.v4.app.FragmentPagerAdapter
    public Fragment getItem(int i) {
        return this.fragments.get(i);
    }

    @Override // android.support.v4.view.PagerAdapter
    public CharSequence getPageTitle(int i) {
        List<String> list = this.titres;
        return (list == null || list.size() <= 0) ? super.getPageTitle(i) : this.titres.get(i);
    }
}
