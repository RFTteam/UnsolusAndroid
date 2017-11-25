package rft.unideb.unsolus.others;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import rft.unideb.unsolus.entities.ApiError;
import rft.unideb.unsolus.network.RetrofitBuilder;

/**
 * Created by Tibor on 2017. 11. 25..
 */

public class Utils {

    public static ApiError convertErrors(ResponseBody response){
        Converter<ResponseBody, ApiError> converter = RetrofitBuilder.getRetrofit().responseBodyConverter(ApiError.class, new Annotation[0]);

        ApiError apiError = null;

        try {
            apiError = converter.convert(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return apiError;
    }
}
