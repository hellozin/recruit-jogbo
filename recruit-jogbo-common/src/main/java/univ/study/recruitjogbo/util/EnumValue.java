package univ.study.recruitjogbo.util;

import lombok.Getter;

@Getter
public class EnumValue {

    private String key;

    private String value;

    public EnumValue(EnumModel enumModel) {
        this.key = enumModel.getKey();
        this.value = enumModel.getValue();
    }

}
