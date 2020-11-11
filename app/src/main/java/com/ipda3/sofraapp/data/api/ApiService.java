package com.ipda3.sofraapp.data.api;

import androidx.cardview.widget.CardView;

import com.ipda3.sofraapp.data.model.clientNewOrder.ClientNewOrder;
import com.ipda3.sofraapp.data.model.clientOrders.ClientOrders;
import com.ipda3.sofraapp.data.model.clientReview.ClientReview;
import com.ipda3.sofraapp.data.model.commissions.Commissions;
import com.ipda3.sofraapp.data.model.contactUs.ContactUs;
import com.ipda3.sofraapp.data.model.foodCategory.FoodCategory;
import com.ipda3.sofraapp.data.model.foodItem.FoodItem;
import com.ipda3.sofraapp.data.model.generalResponse.GeneralResponse;
import com.ipda3.sofraapp.data.model.myCategories.MyCategories;
import com.ipda3.sofraapp.data.model.notification.Notification;
import com.ipda3.sofraapp.data.model.restaurantAddFoodItem.RestaurantAddFoodItem;
import com.ipda3.sofraapp.data.model.restaurantDetails.RestaurantDetails;
import com.ipda3.sofraapp.data.model.resetPassword.ResetPassword;
import com.ipda3.sofraapp.data.model.restaurantItems.RestaurantItems;
import com.ipda3.sofraapp.data.model.restaurantNewOrders.RestaurantNewOrders;
import com.ipda3.sofraapp.data.model.offers.Offers;
import com.ipda3.sofraapp.data.model.restaurantOrders.RestaurantOrders;
import com.ipda3.sofraapp.data.model.restaurantReviews.RestaurantReviews;
import com.ipda3.sofraapp.data.model.restaurantsList.RestaurantsList;
import com.ipda3.sofraapp.data.model.settings.Settings;
import com.ipda3.sofraapp.data.model.user.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {

    @FormUrlEncoded
    @POST("restaurant/login")
    Call<User> restaurantLogin(@Field("email") String email,
                               @Field("password") String password);

    @FormUrlEncoded
    @POST("client/login")
    Call<User> clientLogin(@Field("email") String email,
                           @Field("password") String password);


    @POST("restaurant/sign-up")
    @Multipart
    Call<User> restaurantRegister(@Part("name") RequestBody name,
                                  @Part("email") RequestBody email,
                                  @Part("password") RequestBody password,
                                  @Part("password_confirmation") RequestBody passwordConfirmation,
                                  @Part("phone") RequestBody phone,
                                  @Part("whatsapp") RequestBody whatsapp,
                                  @Part("region_id") RequestBody regionId,
                                  @Part("delivery_cost") RequestBody deliveryCost,
                                  @Part("minimum_charger") RequestBody minimumCharger,
                                  @Part MultipartBody.Part photo,
                                  @Part("delivery_time") RequestBody deliveryTime);

    @POST("client/sign-up")
    @Multipart
    Call<User> clientRegister(@Part("name") RequestBody name,
                              @Part("email") RequestBody email,
                              @Part("password") RequestBody password,
                              @Part("password_confirmation") RequestBody passwordConfirmation,
                              @Part("phone") RequestBody phone,
                              @Part("region_id") RequestBody regionId,
                              @Part MultipartBody.Part profileImage);



    @FormUrlEncoded
    @POST("restaurant/change-password")
    Call<ResetPassword> restaurantChangePassword(@Field("api_token") String apiToken,
                                                 @Field("old_password") String oldPassword,
                                                 @Field("password") String password,
                                                 @Field("password_confirmation") String passwordConfirmation);
    @FormUrlEncoded
    @POST("restaurant/reset-password")
    Call<ResetPassword> restaurantResetPassword(@Field("email") String email);

    @FormUrlEncoded
    @POST("restaurant/new-password")
    Call<ResetPassword> restaurantNewPassword(@Field("code") String code,
                                              @Field("password") String password,
                                              @Field("password_confirmation") String passwordConfirmation);

    @FormUrlEncoded
    @POST("client/reset-password")
    Call<ResetPassword> clientResetPassword(@Field("email") String email);

    @FormUrlEncoded
    @POST("client/change-password")
    Call<ResetPassword> clientChangePassword(@Field("api_token") String apiToken,
                                             @Field("old_password") String oldPassword,
                                             @Field("password") String password,
                                             @Field("password_confirmation") String passwordConfirmation);

    @FormUrlEncoded
    @POST("restaurant/new-password")
    Call<ResetPassword> clientNewPassword(@Field("code") String code,
                                          @Field("password") String password,
                                          @Field("password_confirmation") String passwordConfirmation);

    @FormUrlEncoded
    @POST("contact")
    Call<ContactUs> contactUs(@Field("name") String name,
                              @Field("email") String email,
                              @Field("phone") String phone,
                              @Field("type") String type,
                              @Field("content") String content);

    @FormUrlEncoded
    @POST("client/restaurant/review")
    Call<ClientReview> addRestaurantReview(@Field("rate") int rate,
                                           @Field("comment") String comment,
                                           @Field("restaurant_id") int restaurantId,
                                           @Field("api_token") String apiToken);

    @POST("restaurant/new-category")
    @Multipart
    Call<MyCategories> addNewRestaurantCategory(@Part("name") RequestBody name,
                                                @Part MultipartBody.Part photo,
                                                @Part("api_token") RequestBody apiToken);

    @POST("restaurant/profile")
    @Multipart
    Call<User> editRestaurantProfile(@Part("email") RequestBody email,
                                     @Part("name") RequestBody name,
                                     @Part("phone") RequestBody phone,
                                     @Part("region_id") RequestBody regionId,
                                     @Part("delivery_cost") RequestBody deliveryCost,
                                     @Part("minimum_charger") RequestBody minimumCharger,
                                     @Part("availability") RequestBody availability,
                                     @Part MultipartBody.Part photo,
                                     @Part("api_token") RequestBody apiToken,
                                     @Part("delivery_time") RequestBody deliveryTime);

    @POST("restaurant/update-category")
    @Multipart
    Call<MyCategories> updateRestaurantCategory(@Part("name") RequestBody name,
                                                @Part MultipartBody.Part photo,
                                                @Part("api_token") RequestBody apiToken,
                                                @Part("category_id") RequestBody categoryId);

    @FormUrlEncoded
    @POST("restaurant/delete-category")
    Call<MyCategories> deleteRestaurantCategory(@Field("api_token") String apiToken,
                                                @Field("category_id") int categoryId);

    @POST("restaurant/new-item")
    @Multipart
    Call<RestaurantAddFoodItem> restaurantAddFoodItem(@Part("description") RequestBody description,
                                                      @Part("price") RequestBody price,
                                                      @Part("preparing_time") RequestBody preparingTime,
                                                      @Part("name") RequestBody name,
                                                      @Part MultipartBody.Part photo,
                                                      @Part("api_token") RequestBody apiToken,
                                                      @Part("offer_price") RequestBody offerPrice,
                                                      @Part("category_id") RequestBody categoryId);

    @FormUrlEncoded
    @POST("restaurant/delete-item")
    Call<FoodItem> restaurantDeleteFoodItem(@Field("item_id") int item_id,
                                            @Field("api_token") String apiToken);

    @POST("restaurant/update-item")
    @Multipart
    Call<RestaurantAddFoodItem> restaurantUpdateFoodItem(@Part("description") RequestBody description,
                                                         @Part("price") RequestBody price,
                                                         @Part("preparing_time") RequestBody preparingTime,
                                                         @Part("name") RequestBody name,
                                                         @Part MultipartBody.Part photo,
                                                         @Part("item_id") RequestBody itemId,
                                                         @Part("api_token") RequestBody apiToken,
                                                         @Part("offer_price") RequestBody offerPrice,
                                                         @Part("category_id") RequestBody categoryId);

    @POST("restaurant/new-offer")
    @Multipart
    Call<Offers> restaurantAddOffer(@Part("description") RequestBody description,
                                    @Part("price") RequestBody price,
                                    @Part("starting_at") RequestBody startingAt,
                                    @Part("name") RequestBody name,
                                    @Part MultipartBody.Part photo,
                                    @Part("ending_at") RequestBody endingAt,
                                    @Part("api_token") RequestBody apiToken,
                                    @Part("offer_price") RequestBody offerPrice);

    @POST("restaurant/update-offer")
    @Multipart
    Call<Offers> restaurantUpdateOffer(@Part("description") RequestBody description,
                                       @Part("price") RequestBody price,
                                       @Part("starting_at") RequestBody startingAt,
                                       @Part("name") RequestBody name,
                                       @Part MultipartBody.Part photo,
                                       @Part("ending_at") RequestBody endingAt,
                                       @Part("offer_id") RequestBody offerPrice,
                                       @Part("api_token") RequestBody apiToken);


    @FormUrlEncoded
    @POST("restaurant/delete-offer")
    Call<Offers> restaurantDeleteOffer(@Field("offer_id") int offerId,
                                       @Field("api_token") String apiToken);

    @FormUrlEncoded
    @POST("client/new-order")
    Call<ClientNewOrder> clientNewOrderRequest(@Field("restaurant_id") String restaurantId,
                                               @Field("note") String note,
                                               @Field("address") String address,
                                               @Field("payment_method_id") String paymentMethodId,
                                               @Field("phone") String phone,
                                               @Field("name") String name,
                                               @Field("api_token") String apiToken,
                                               @Field("items[]") List<String> items,
                                               @Field("quantities[]") List<String> quantities,
                                               @Field("notes[]") List<String> notes);

    @POST("client/profile")
    @Multipart
    Call<User> clientEditProfile(@Part("api_token") RequestBody apiToken,
                                 @Part("name") RequestBody name,
                                 @Part("phone") RequestBody phone,
                                 @Part("email") RequestBody email,
                                 @Part("region_id") RequestBody regionId,
                                 @Part MultipartBody.Part profileImage);

    @FormUrlEncoded
    @POST("client/confirm-order")
    Call<GeneralResponse> clientConfirmOrderDelivery(@Field("order_id") int orderId,
                                                     @Field("api_token") String apiToken);

    @FormUrlEncoded
    @POST("client/decline-order")
    Call<GeneralResponse> clientDeclineOrder(@Field("order_id") int orderId,
                                             @Field("api_token") String apiToken);

    @FormUrlEncoded
    @POST("restaurant/accept-order")
    Call<GeneralResponse> restaurantAcceptOrder(@Field("api_token") String apiToken,
                                                @Field("order_id") int orderId);

    @FormUrlEncoded
    @POST("restaurant/reject-order")
    Call<GeneralResponse> restaurantRejectOrder(@Field("api_token") String apiToken,
                                                @Field("order_id") int orderId,
                                                @Field("refuse_reason") String refuseReason);

    @FormUrlEncoded
    @POST("restaurant/confirm-order")
    Call<GeneralResponse> restaurantConfirmOrder(@Field("order_id") int orderId,
                                                 @Field("api_token") String apiToken);

    @FormUrlEncoded
    @POST("restaurant/change-state")
    Call<RestaurantDetails> changeRestaurantState(@Field("state") String state,
                                                  @Field("api_token") String api_Token);

    @GET("cities-not-paginated")
    Call<GeneralResponse> getCities();

    @GET("regions-not-paginated")
    Call<GeneralResponse> getRegions(@Query("city_id") int cityId);

    @GET("restaurants")
    Call<RestaurantsList> restaurantsList(@Query("page") int page);

    @GET("restaurants")
    Call<RestaurantsList> restaurantsList(@Query("keyword") String keyword,
                                          @Query("region_id") int regionId);
    @GET("categories")
    Call<FoodCategory> foodCategoryList(@Query("restaurant_id") int restaurantId);

    @GET("items")
    Call<FoodItem> restaurantFood(@Query("restaurant_id") int restaurantId,
                                  @Query("category_id") int categoryId);

    @GET("restaurant")
    Call<RestaurantDetails> restaurantDetails(@Query("restaurant_id") int restaurantId);

    @GET("restaurant/reviews")
    Call<RestaurantReviews> restaurantReviews(@Query("restaurant_id") int restaurantId,
                                              @Query("page") int page);

    @GET("restaurant/my-categories")
    Call<MyCategories> getMyCategoriesList(@Query("api_token") String apiToken);

    @GET("restaurant/my-items")
    Call<RestaurantItems> getRestaurantItemsList(@Query("api_token") String apiToken,
                                                 @Query("category_id") int categoryId);


    @GET("restaurant/my-offers")
    Call<Offers> getRestaurantOffers(@Query("api_token") String apiToken,
                                     @Query("page") int page);

    @GET("client/my-orders")
    Call<ClientOrders> getClientOrders(@Query("api_token") String apiToken,
                                       @Query("state") String state,
                                       @Query("page") int page);

    @GET("settings")
    Call<Settings> getSettings();

    @GET("offers")
    Call<Offers> getClientOffers(@Query("page") int page);

    @GET("client/notifications")
    Call<Notification> getClientNotificationsList(@Query("api_token") String apiToken);

    @GET("restaurant/notifications")
    Call<Notification> getRestaurantNotificationsList(@Query("api_token") String apiToken);


    @GET("restaurant/my-orders")
    Call<RestaurantOrders> getRestaurantOrders(@Query("api_token") String apiToken,
                                               @Query("state") String state,
                                               @Query("page") int page);

    @GET("restaurant/commissions")
    Call<Commissions> getRestaurantCommissions(@Query("api_token") String apiToken);
}
