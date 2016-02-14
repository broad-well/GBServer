package com.Gbserver.listener;

import java.util.HashMap;

/**
 * Created by michael on 1/30/16.
 */
public interface ChatModule {
    String getName();

    HashMap<String, String> passThru(HashMap<String, String> hs);
}
