package assignment.panos_lab;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>{
    public class ViewHolder extends RecyclerView.ViewHolder {
        public Image mImage;
        public View view;
        public TextView name;
        public TextView description;
        public ImageView image;
        public ViewHolder (View view){
            super(view);
            this.view = view;

            this.image=view.findViewById(R.id.displayImage);
        }

    }

    List<Image> imageList;

    public ImageAdapter(List<Image> imageList){ this.imageList = imageList; }

    @Override
    public int getItemCount() { return this.imageList.size(); }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.image_list_item,parent,false);

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Image currImage = imageList.get(position);
        holder.name.setText(currImage.name);
        holder.description.setText(currImage.description);
        while(currImage.picture==null){

        }
        holder.image.setImageBitmap(currImage.picture);

        //holder.image.setImageBitmap(BitmapDownloader.getBitmapFromURL(currImage.url));
        new Thread(new Runnable(){
            public void run(){
            }
        }).start();
        System.out.print(holder);
        System.out.print(holder);
        //GET IMAGE FROM BITMAP HERE
//        holder.image.setImageBitmap();
    }
}
