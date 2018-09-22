package assignment.panos_lab;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class BitmapAdapter extends RecyclerView.Adapter<BitmapAdapter.StatusViewHolder>{
    private List<Bitmap> mBitmaps;

    public BitmapAdapter(List<Bitmap> input){
        this.mBitmaps = input;
    }

    @Override
    public StatusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.image_list_item, parent, false);

        StatusViewHolder vh = new StatusViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(StatusViewHolder statusViewHolder, int position) {
        Bitmap status = mBitmaps.get(position);
        statusViewHolder.bind(status);
    }

    public void setmBitmaps(List<Bitmap> input){this.mBitmaps = input;}

    @Override
    public int getItemCount() {
        return mBitmaps.size();
    }

    public void setStatuses(List<Bitmap> statuses) {
        this.mBitmaps = statuses;
    }

    public class StatusViewHolder extends RecyclerView.ViewHolder {
        View mView;

        private ImageView showPicture;
        private TextView username;
        private TextView statusText;

        private Bitmap mBitmap;

        public StatusViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            showPicture = itemView.findViewById(R.id.displayImage);
        }

        public void bind(Bitmap input) {
            mBitmap = input;

            showPicture.setImageBitmap(input);
        }
    }

}