package interfaces;

import models.GetAddFavoritesResponseData;
import models.GetFavoritesMoviesResponseData;
import models.GetMovieDetailResponseData;
import models.GetMovieResponseData;
import models.GetRequestTokenData;
import models.GetTrailersResponseData;
import models.GetUserSessionIdData;
import models.SetRequestSessionData;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IServices {

    @GET("movie/{type_movie}")
    Call<GetMovieResponseData> getMovies(
            @Path("type_movie") String movieType,
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("movie/{movie_id}")
    Call<GetMovieDetailResponseData> getMovieDetail(
            @Path("movie_id") String movieId,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("authentication/token/new")
    Call<GetRequestTokenData> getRequestToken(
            @Query("api_key") String apiKey
    );

    @POST("authentication/session/new")
    Call<GetUserSessionIdData> getSession(
            @Query("api_key") String apiKey,
            @Body SetRequestSessionData requestSessionData
    );

    @GET("account/{account_id}/favorite")
    Call<GetAddFavoritesResponseData> setFavorite(
            @Path("account_id") String accountId,
            @Query("api_key") String apiKey,
            @Query("session_id") String sessionId
    );

    @GET("account/{account_id}/favorite/movies")
    Call<GetFavoritesMoviesResponseData> getFavorites(
            @Path("account_id") String accountId,
            @Query("api_key") String apiKey,
            @Query("session_id") String sessionId,
            @Query("language") String language,
            @Query("sort_by") String page
    );

    @GET("account/{movie_id}/favorite/movies")
    Call<GetTrailersResponseData> getTrailers(
            @Path("movie_id") String movieId,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );
}