package com.Gbserver.variables;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

/**
 * Created by michael on 1/16/16.
 */
public class SwiftDumpOptions {
    public static Yaml BLOCK_STYLE() {
        DumperOptions Do = new DumperOptions();
        Do.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        return new Yaml(Do);
    }
}
