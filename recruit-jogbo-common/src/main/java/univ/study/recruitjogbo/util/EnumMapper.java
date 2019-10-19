package univ.study.recruitjogbo.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EnumMapper {

    private Map<String, List<EnumValue>> factory = new HashMap<>();

    private List<EnumValue> toEnumValues(Class<? extends EnumModel> e) {
        return Arrays
                .stream(e.getEnumConstants())
                .map(EnumValue::new)
                .collect(Collectors.toList());
    }

    public void put(String key, Class<? extends EnumModel> enumModel) {
        factory.put(key, toEnumValues(enumModel));
    }

    public List<EnumValue> getEnumValues(String key) {
        return factory.get(key);
    }

}
