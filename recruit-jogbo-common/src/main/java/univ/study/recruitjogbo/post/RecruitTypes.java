package univ.study.recruitjogbo.post;

public enum RecruitTypes {

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

    public String value() {
        return value;
    }

}
