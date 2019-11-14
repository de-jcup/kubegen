package de.jcup.kubegen;

import java.util.Map;

public class SystemPropertyMapDataProvider implements MapDataProvider{

    @Override
    public Map<Object,Object> getMap() {
        return System.getProperties();
    }


}
