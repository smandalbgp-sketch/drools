package com.vd.drools.service;

import com.vd.drools.request.ProvidentFundRequest;
import com.vd.drools.response.BaseResponse;

public interface UserService {

    BaseResponse getProvidentFund(ProvidentFundRequest request);
}
