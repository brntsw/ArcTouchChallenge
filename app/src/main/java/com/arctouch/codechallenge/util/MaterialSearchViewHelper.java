package com.arctouch.codechallenge.util;

import com.arctouch.codechallenge.listener.ISearchViewTextSubmitListener;
import com.arctouch.codechallenge.movies_list.presentation.MoviesListContract;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

public class MaterialSearchViewHelper {

    public static MaterialSearchView.OnQueryTextListener getQueryTextListener(ISearchViewTextSubmitListener searchViewTextSubmitListener){
        return new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchViewTextSubmitListener.onSearchTextSubmit(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        };
    }

}
