package adapters;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.prueba.rappi.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import helpers.Utils;
import models.GetMovieResponseData;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

private FavoriteAdapter.ClickListener clickListener;

    private List<GetMovieResponseData.Result> data;

    public void setData(List<GetMovieResponseData.Result> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Inject
    public FavoriteAdapter(ClickListener clickListener) {
        this.clickListener = clickListener;
        data = new ArrayList<>();
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new FavoriteViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final FavoriteViewHolder favoriteViewHolder, int i) {

        final GetMovieResponseData.Result item = data.get(favoriteViewHolder.getAdapterPosition());

        String url = String.format("https://image.tmdb.org/t/p/w500/%s", item.getPosterPath());
        Picasso.with(favoriteViewHolder.itemView.getContext()).load(url).into(favoriteViewHolder.imageMovie);

        ViewCompat.setTransitionName(favoriteViewHolder.imageMovie, "movieDetail");

        favoriteViewHolder.imageMovie.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                clickListener.launchIntent(item.getId(), favoriteViewHolder.imageMovie);
            }
        });

        if (!item.getTitle().isEmpty()) {
            favoriteViewHolder.textTitle.setText(item.getTitle());
        }

        if (!item.getReleaseDate().isEmpty()) {
            try {
                favoriteViewHolder.textYear.setText(new SimpleDateFormat("yyyy").format(Utils.GetDateFormat("yyyy").parse(item.getReleaseDate())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (item.getVoteAverage() > 0) {
            favoriteViewHolder.ratingMovie.setRating(Float.parseFloat(item.getVoteAverage().toString()) * .5f);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface ClickListener {
        void launchIntent(int movieId, ImageView movieImage);
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageMovie;
        private TextView textTitle;
        private TextView textYear;
        private AppCompatRatingBar ratingMovie;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            imageMovie = itemView.findViewById(R.id.imageViewPoster);
            textTitle = itemView.findViewById(R.id.textTitle);
            textYear = itemView.findViewById(R.id.textYear);
            ratingMovie = itemView.findViewById(R.id.ratingMovie);
        }
    }
}
