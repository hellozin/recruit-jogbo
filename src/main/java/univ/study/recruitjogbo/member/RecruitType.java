package univ.study.recruitjogbo.member;

public enum RecruitType {

    /*
    * RESUME : 서류
    * CODING : 코딩테스트
    * APTITUDE : 인적성 테스트
    * NCS : NCS
    * INTERVIEW : 면접
    * ETC : 기타
    * */

    RESUME("RESUME"),
    CODING("CODING"),
    APTITUDE("APTITUDE"),
    NCS("NCS"),
    INTERVIEW("INTERVIEW"),
    ETC("ETC");

    private String value;

    RecruitType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static boolean support(String value) {
        for (RecruitType val : values()) {
            if (val.value().equals(value)) {
                return true;
            }
        }
        return false;
    }

}
