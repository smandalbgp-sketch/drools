package com.vd.drools.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProvidentFundResponse {

    private String department;
    private Integer experience;
    private String providentCut;

}
