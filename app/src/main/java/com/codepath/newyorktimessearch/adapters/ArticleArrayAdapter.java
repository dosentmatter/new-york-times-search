package com.codepath.newyorktimessearch.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.newyorktimessearch.R;
import com.codepath.newyorktimessearch.models.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by kevinlau on 10/24/16.
 */

public class ArticleArrayAdapter extends ArrayAdapter<Article>  {

    private static class ViewHolder {
        ImageView ivImage;
        TextView tvTitle;
    }

    public ArticleArrayAdapter(Context context, List<Article> articles) {
        super(context, R.layout.item_article, articles);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Article article = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_article, parent,
                                           false);

            viewHolder.ivImage
                    = (ImageView)convertView.findViewById(R.id.ivImage);
            viewHolder.tvTitle
                    = (TextView)convertView.findViewById(R.id.tvTitle);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.ivImage.setImageResource(0);
        String thumbnail = article.getThumbnail();
        if (!TextUtils.isEmpty(thumbnail)) {
            Picasso.with(getContext()).load(thumbnail)
                   .placeholder(R.mipmap.ic_launcher).into(viewHolder.ivImage);
        } else {
            viewHolder.ivImage.setImageResource(R.mipmap.ic_launcher);
        }

        viewHolder.tvTitle.setText(article.getHeadLine());

        return convertView;
    }
}
