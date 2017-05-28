package dev.rism.dobscraper;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by risha on 21-05-2017.
 */

public class ListAdapter extends BaseAdapter {
    Context context;
    ArrayList<ListModel> list;
    ListAdapter(Context context, ArrayList<ListModel> list)
    {
        this.context=context;
        this.list=list;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        LayoutInflater inflater;
        if (convertView==null)
        {
          inflater=LayoutInflater.from(context);
            convertView=inflater.inflate(R.layout.list_item,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.iv= (ImageView) convertView.findViewById(R.id.thumbnail);
            viewHolder.tvtitle= (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(viewHolder);
        }
        viewHolder= (ViewHolder) convertView.getTag();
        Picasso.with(context).load(list.get(position).getImgurl()).placeholder(R.mipmap.ic_launcher).into(viewHolder.iv);
        viewHolder.tvtitle.setText(list.get(position).getName());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=list.get(position).getName();
                name.replace(' ','_');
                Intent intent=new Intent(context,WebActivity.class);
                intent.putExtra("tag",name);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    static class ViewHolder
    {
        TextView tvtitle;
        ImageView iv;
    }
}
