package dev.rism.dobscraper;

/**
 * Created by risha on 23-05-2017.
 */

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by risha on 16-09-l2016.
 */
public class ViewPagerAdapter extends PagerAdapter {
    private  Context context;
    private ArrayList<SliderModel> list = new ArrayList<>();


    public ViewPagerAdapter(Context context, ArrayList<SliderModel> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }


    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public Object instantiateItem(View collection, final int position) {
        LayoutInflater inflater = (LayoutInflater) collection.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.pager_layout,null);
        ((ViewPager) collection).addView(view);
        String s="";
        String type=list.get(position).getType();
        String text=list.get(position).getText();
        String year=list.get(position).getYear();
        switch (type)
        {
            case "Events":
                s="In "+year+" , "+text;
                break;
            case "Births":
                s="Today in "+year+" , "+text+"was born .";
                break;
            case "Deaths":
                s=text+" died this day in "+year;
                break;
            default:
                Log.d("Tag","Error");
        }
        Typeface typeface=Typeface.createFromAsset(context.getAssets(),"Quicksand-Regular.otf");
       TextView tv= ((TextView)view.findViewById(R.id.tv_facts));
        tv.setText("\""+s+"\"");
        tv.setTypeface(typeface);
        ImageView iv= (ImageView) view.findViewById(R.id.iv_share);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check()) {
                    Bitmap bmp = BitmapGenerator.getBitmapFromView(view);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("image/png");
                    Uri bitmapUri = BitmapGenerator.getImageUri(context, bmp);
                    intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
                    context.startActivity(Intent.createChooser(intent, "Share"));
                }
                else
                {
                    Toast.makeText(context,"Provide write permission from settings !",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
    public Boolean check()
    {
        String permission = "android.permission.WRITE_EXTERNAL_STORAGE";
        int res = context.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
}