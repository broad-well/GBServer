package com.Gbserver.variables;

/**
 * Created by michael on 11/6/15.
 */
public interface ReactionMath {
    Number calculate();
    ReactionMath generate();
    void broadcast();
    void close();
}
