package com.a24.shaunak.quakereport;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

/**
 * Created by Shaunak on 06-06-2018.
 */

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    String mUrl;

    public EarthquakeLoader(Context context , String url) {
        super(context);
        mUrl = url;
    }

    public List<Earthquake> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        List result = QueryUtils.fetchEarthquakeData(mUrl);
        return result;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

}
