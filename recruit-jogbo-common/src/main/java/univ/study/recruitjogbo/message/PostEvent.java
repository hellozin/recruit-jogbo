package univ.study.recruitjogbo.message;

import lombok.*;
import univ.study.recruitjogbo.util.EnumValue;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class PostEvent {

    private Long id;

    private String companyName;

    private List<EnumValue> recruitTypes;

}
