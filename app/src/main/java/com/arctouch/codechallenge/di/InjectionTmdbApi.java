package com.arctouch.codechallenge.di;

import com.arctouch.codechallenge.api.TmdbApi;

public class InjectionTmdbApi {

    public static TmdbApi inject(){
        return TmdbApi.Builder.build();
    }

}
