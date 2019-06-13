package adapters;

import android.support.annotation.NonNull;
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

import models.GetTrailersResponseData;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder> {

    private final TrailersAdapter.ClickListener clickListener;

    private List<GetTrailersResponseData.Result> data;

    @Inject
    public TrailersAdapter(TrailersAdapter.ClickListener clickListener) {
        this.clickListener = clickListener;
        data = new ArrayList<>();
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new TrailerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder trailerViewHolder, int i) {

        String url = String.format("https://img.youtube.com/vi/%s/sddefault.jpg", data.get(i).getKey());
        Picasso.with(trailerViewHolder.itemView.getContext()).load(url).into(trailerViewHolder.imageTrailer);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<GetTrailersResponseData.Result> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public interface ClickListener {
        void launchIntent(String movieKey);
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageTrailer;

        public TrailerViewHolder(@NonNull View itemView) {

            super(itemView);

            imageTrailer = itemView.findViewById(R.id.imageViewThumbnail);

            imageTrailer.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    clickListener.launchIntent(data.get(getAdapterPosition()).getKey());
                }
            });
        }
    }
}
