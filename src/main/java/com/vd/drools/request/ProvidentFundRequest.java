package com.vd.drools.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProvidentFundRequest {

    @NotNull
    @NotBlank
    private String department;

    @NotNull
    private Integer experience;
}
