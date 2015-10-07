package com.etiennelawlor.loop.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etiennelawlor.loop.R;
import com.etiennelawlor.loop.network.models.response.Pictures;
import com.etiennelawlor.loop.network.models.response.Size;
import com.etiennelawlor.loop.network.models.response.Stats;
import com.etiennelawlor.loop.network.models.response.User;
import com.etiennelawlor.loop.network.models.response.Video;
import com.etiennelawlor.loop.ui.LoadingImageView;
import com.etiennelawlor.loop.utilities.LoopUtility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by etiennelawlor on 5/23/15.
 */

public class SuggestionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // region Constants
    public static final int ITEM = 0;
    // endregion

    // region Member Variables
    private List<String> mSuggestions;
    private OnItemClickListener mOnItemClickListener;
    private OnSearchSuggestionCompleteClickListener mOnSearchSuggestionCompleteClickListener;
    // endregion

    // region Listeners
    // endregion

    // region Interfaces
    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

    public interface OnSearchSuggestionCompleteClickListener {
        void onSearchSuggestionCompleteClickListener(int position, TextView textView);
    }
    // endregion

    // region Constructors
    public SuggestionsAdapter() {
        mSuggestions = new ArrayList<>();
    }
    // endregion

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM:
                return createSuggestionViewHolder(parent);
            default:
                Timber.e("[ERR] type is not supported!!! type is %d", viewType);
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)) {
            case ITEM:
                bindSuggestionViewHolder(viewHolder, position);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mSuggestions.size();
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM;
    }

    // region Helper Methods
    private void add(String item) {
        mSuggestions.add(item);
        notifyItemInserted(mSuggestions.size()-1);
    }

    public void addAll(List<String> suggestions) {
        for (String suggestion : suggestions) {
            add(suggestion);
        }
    }

    public void remove(String item) {
        int position = mSuggestions.indexOf(item);
        if (position > -1) {
            mSuggestions.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public String getItem(int position) {
        return mSuggestions.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnSearchSuggestionCompleteClickListener(OnSearchSuggestionCompleteClickListener onSearchSuggestionCompleteClickListener) {
        this.mOnSearchSuggestionCompleteClickListener = onSearchSuggestionCompleteClickListener;
    }

    private RecyclerView.ViewHolder createSuggestionViewHolder(ViewGroup parent){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.suggestion, parent, false);

        return new SuggestionViewHolder(v);
    }

    private void bindSuggestionViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final SuggestionViewHolder holder = (SuggestionViewHolder) viewHolder;

        final String suggestion = mSuggestions.get(position);
        if (!TextUtils.isEmpty(suggestion)) {
            setUpSuggestion(holder.mSuggestionTextView, suggestion);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(position, holder.itemView);
                    }
                }
            });

            holder.mSuggestionCompleteImageView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(mOnSearchSuggestionCompleteClickListener != null){
                        mOnSearchSuggestionCompleteClickListener.onSearchSuggestionCompleteClickListener(position, holder.mSuggestionTextView);
                    }
                }
            });
        }
    }

    private void setUpSuggestion(TextView tv, String suggestion){
        if(!TextUtils.isEmpty(suggestion)){
            tv.setText(suggestion);
        }
    }
    // endregion

    // region Inner Classes

    public static class SuggestionViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.suggestion_tv)
        TextView mSuggestionTextView;
        @Bind(R.id.search_suggest_complete_iv)
        ImageView mSuggestionCompleteImageView;

        SuggestionViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    // endregion

}