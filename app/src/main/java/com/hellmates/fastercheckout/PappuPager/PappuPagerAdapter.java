package com.hellmates.fastercheckout.PappuPager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.hellmates.fastercheckout.R;

import java.util.List;

public class PappuPagerAdapter extends PagerAdapter{

    private Context context;
    private LayoutInflater layoutInflater;
    private List<SliderUtils> sliderImgs;
    private ImageLoader imageLoader;
    //private Integer [] images = {R.drawable.image1,R.drawable.image2,R.drawable.image3};

    public PappuPagerAdapter(List<SliderUtils> sliderImgs,Context context) {
        this.sliderImgs = sliderImgs;
        this.context = context;
    }

    @Override
    public int getCount() {
        return sliderImgs.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.advert_image_layout, null);

        SliderUtils utils = sliderImgs.get(position);

        ImageView imageView = (ImageView) view.findViewById(R.id.advertImageView);
        //imageView.setImageResource(images[position]);

        imageLoader = PappuVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(utils.getSliderImageUrl(),ImageLoader.getImageListener(imageView,R.drawable.loading,R.drawable.error));


        ViewPager vp = (ViewPager) container;
        vp.addView(view,0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }
}
