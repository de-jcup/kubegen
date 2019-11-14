package de.jcup.kubegen;

import java.util.Map;

public class SystemEnvironmentMapDataProvider implements MapDataProvider{

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Map<Object,Object> getMap() {
        return (Map)System.getenv();
    }


}
