package adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import models.GetTrailersResponseData;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder> {

    private final MoviesAdapter.ClickListener clickListener;

    private List<GetTrailersResponseData.Result> data;

    @Inject
    public TrailersAdapter(MoviesAdapter.ClickListener clickListener) {
        this.clickListener = clickListener;
        data = new ArrayList<>();
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder trailerViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface ClickListener {
        void launchIntent(String movieKey);
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {
        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
