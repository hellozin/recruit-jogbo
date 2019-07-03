package univ.study.recruitjogbo.model;

import java.time.LocalDate;

public class Content {

    private final Long seq;

    private final String companyName;

    private final RecruitType recruitType;

    private final LocalDate from;

    private final LocalDate to;

    public Content(String companyName, RecruitType recruitType, LocalDate from, LocalDate to) {
        this(null, companyName, recruitType, from, to);
    }

    public Content(Long seq, String companyName, RecruitType recruitType, LocalDate from, LocalDate to) {
        this.seq = seq;
        this.companyName = companyName;
        this.recruitType = recruitType;
        this.from = from;
        this.to = to;
    }
}
