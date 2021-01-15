package com.example.moviesapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.moviesapp.R;
import com.example.moviesapp.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.ContentValues.TAG;


public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    private List<Movie> moviesData;
    private  HandleClickListener mClickLister;

    public  MoviesAdapter(HandleClickListener handleClickListener) {
        mClickLister = handleClickListener;
    }

    public void setMoviesData(List<Movie> MovieData) {
        this.moviesData = MovieData;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public MoviesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movies_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapter.ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (moviesData == null) {
            return  0;
        } else {
            return moviesData.size();
        }
    }
    public interface  HandleClickListener {
        void onClick(Movie movie);
    }
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageView;
        public View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
             this.view = itemView;
            view.setOnClickListener(this);
        }
        void bind(int position) {
            if (moviesData == null) return;
            String dataValue = moviesData.get(position).getmMovietitle();
//            textView.setText(dataValue.toString());

            Picasso.with(itemView.getContext())
                    .load(Movie.getImageFullPath(moviesData.get(position).getmMovieImageURL()))
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(imageView);
        }
        @Override
        public void onClick(View view) {
            int adapterPosition  = getAdapterPosition();
            Log.e(TAG, "onClick: " + adapterPosition );;
            Movie movieAtPosition = moviesData.get(adapterPosition);
            mClickLister.onClick(movieAtPosition);
        }
    }
}
