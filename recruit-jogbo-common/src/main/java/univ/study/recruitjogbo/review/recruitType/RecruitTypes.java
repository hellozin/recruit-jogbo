package univ.study.recruitjogbo.review.recruitType;

import univ.study.recruitjogbo.util.EnumModel;

public enum RecruitTypes implements EnumModel {

    /*
    * RESUME : 서류
    * CODING : 코딩테스트
    * APTITUDE : 인적성 테스트
    * NCS : NCS
    * INTERVIEW : 면접
    * ETC : 기타
    * */

    RESUME("서류"),
    CODING("코딩테스트"),
    APTITUDE("인적성"),
    NCS("NCS"),
    INTERVIEW("면접"),
    ETC("기타");

    private String value;

    RecruitTypes(String value) {
        this.value = value;
    }

    @Override
    public String getKey() {
        return name();
    }

    @Override
    public String getValue() {
        return value;
    }
}
