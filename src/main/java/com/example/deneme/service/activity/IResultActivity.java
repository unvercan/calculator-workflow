package com.example.deneme.service.activity;

import com.example.deneme.model.entity.Result;
import com.example.deneme.model.request.CreateResultRequest;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

import java.util.UUID;

@ActivityInterface
public interface IResultActivity {

    @ActivityMethod
    Result createResult(CreateResultRequest request);

}
