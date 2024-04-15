package com.example.thithu_and;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
    String DOMAIN = "http://10.0.2.2:3000/";

    @GET("/list")
    Call<ArrayList<XeMay>> getXe();

    @GET("/list-by-id/{id}")
    Call<XeMay> getXeId(@Path("id") String id);

    @Multipart
    @POST("/add")
    Call<XeMay> addXe(@PartMap Map<String, RequestBody> requestBodyMap,
                     @Part MultipartBody.Part image);

    @DELETE("/delete/{id}")
    Call<XeMay> deleteXe(@Path("id") String id);

    @Multipart
    @PUT("/update/{id}")
    Call<XeMay> updateXe(@PartMap Map<String, RequestBody> requestBodyMap,
                        @Path("id") String id,
                        @Part MultipartBody.Part imageCay
    );

    @Multipart
    @PUT("/update-no-image/{id}")
    Call<XeMay> updateNoImage(@PartMap Map<String, RequestBody> requestBodyMap,
                            @Path("id") String id
    );

    @GET("/search")
    Call<ArrayList<XeMay>> searchXe(@Query("key") String query);
}
