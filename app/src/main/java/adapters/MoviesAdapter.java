package adapters;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.prueba.rappi.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import models.GetMovieResponseData;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private MoviesAdapter.ClickListener clickListener;

    private List<GetMovieResponseData.Result> data;

    @Inject
    public MoviesAdapter(ClickListener clickListener) {

        this.clickListener = clickListener;
        data = new ArrayList<>();
    }

    public interface ClickListener {
        void launchIntent(int movieId, ImageView movieImage);
    }

    public void setData(List<GetMovieResponseData.Result> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        return new MovieViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieViewHolder movieViewHolder, int i) {

        String url = String.format("https://image.tmdb.org/t/p/w500/%s", data.get(i).getPosterPath());
        Picasso.with(movieViewHolder.itemView.getContext()).load(url).into(movieViewHolder.imageMovie);

        ViewCompat.setTransitionName(movieViewHolder.imageMovie, "movieDetail");

        movieViewHolder.imageMovie.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                clickListener.launchIntent(data.get(movieViewHolder.getAdapterPosition()).getId(), movieViewHolder.imageMovie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageMovie;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            imageMovie = itemView.findViewById(R.id.imageViewPoster);
        }
    }
}
