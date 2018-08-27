package demo.project.landmark.model;

import javax.annotation.Nullable;

import b.laixuantam.myaarlibrary.api.BaseApiResponse;

/**
 * Created by laixuantam on 4/2/18.
 */

public class BaseResponseModel implements BaseApiResponse {

    public String success;

    @Nullable
    public String message;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    @Nullable
    public String getMessage() {
        return message;
    }

    public void setMessage(@Nullable String message) {
        this.message = message;
    }
}
