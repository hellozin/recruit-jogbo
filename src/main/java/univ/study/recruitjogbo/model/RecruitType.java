package univ.study.recruitjogbo.model;

public enum RecruitType {

    RESUME("RESUME"),       /* 서류 */
    CODING("CODING"),       /* 코딩테스트 */
    APTITUDE("APTITUDE"),   /* 인적성 테스트 */
    NCS("NCS"),             /* NCS */
    INTERVIEW("INTERVIEW"), /* 면접 */
    ETC("ETC");             /* 기타 */

    private String value;

    RecruitType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

}
