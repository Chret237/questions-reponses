package org.questionsreponses.View.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Hashtable;
import org.questionsreponses.C0598R;
import org.questionsreponses.Model.Survey;
import org.questionsreponses.Presenter.CommonPresenter;
import org.questionsreponses.Presenter.SpiritualGrowthPresenter;
import org.questionsreponses.View.Activities.QuizzSaveListActivity;
import org.questionsreponses.View.Interfaces.SpiritualGrowthView;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Adapters/SpiritualGrowthRecyclerAdapter.class */
public class SpiritualGrowthRecyclerAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context context;
    private SpiritualGrowthView.ISpiritualGrowth iSpiritualGrowth;
    private Hashtable<Integer, MyViewHolder> mViewHolder = new Hashtable<>();
    private ArrayList<Survey> surveyItems;

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Adapters/SpiritualGrowthRecyclerAdapter$MyViewHolder.class */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView itemDate;
        ImageView itemImage;
        RatingBar itemRating;
        TextView itemTitle;
        View linearLayout;
        int positionItem;
        Survey survey;
        final SpiritualGrowthRecyclerAdapter this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public MyViewHolder(SpiritualGrowthRecyclerAdapter spiritualGrowthRecyclerAdapter, View view) {
            super(view);
            this.this$0 = spiritualGrowthRecyclerAdapter;
            this.linearLayout = view.findViewById(C0598R.id.item_linearlayout);
            this.itemImage = (ImageView) view.findViewById(C0598R.id.item_image);
            this.itemTitle = (TextView) view.findViewById(C0598R.id.item_title);
            this.itemDate = (TextView) view.findViewById(C0598R.id.item_date);
            this.itemRating = (RatingBar) view.findViewById(C0598R.id.item_rating);
            this.linearLayout.setOnClickListener(new View.OnClickListener(this, spiritualGrowthRecyclerAdapter) { // from class: org.questionsreponses.View.Adapters.SpiritualGrowthRecyclerAdapter.MyViewHolder.1
                final MyViewHolder this$1;
                final SpiritualGrowthRecyclerAdapter val$this$0;

                {
                    this.this$1 = this;
                    this.val$this$0 = spiritualGrowthRecyclerAdapter;
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    if (this.this$1.survey.getTotal_trouve() != 0) {
                        Intent intent = new Intent(view2.getContext(), (Class<?>) QuizzSaveListActivity.class);
                        intent.putExtra(CommonPresenter.KEY_QUIZZ_ACTIVITY_LIST, this.this$1.survey.getClef_groupe());
                        view2.getContext().startActivity(intent);
                    } else {
                        SpiritualGrowthPresenter spiritualGrowthPresenter = new SpiritualGrowthPresenter(this.this$1.this$0.iSpiritualGrowth);
                        spiritualGrowthPresenter.launchQuizzSpiritualGrowth(view2.getContext(), this.this$1.survey.getTitre_quizz(), this.this$1.survey.getClef_groupe(), this.this$1.survey.getMinScoreToUnlocked());
                        spiritualGrowthPresenter.saveKeyGroupeSelectedInSharesPreferences(view2.getContext(), this.this$1.survey.getClef_groupe());
                        spiritualGrowthPresenter.saveLevelSelectedInSharesPreferences(view2.getContext(), this.this$1.positionItem + 1);
                        spiritualGrowthPresenter.saveMinScoreToUnlockedInSharesPreferences(view2.getContext(), this.this$1.survey.getMinScoreToUnlocked());
                    }
                }
            });
        }
    }

    public SpiritualGrowthRecyclerAdapter(Context context, ArrayList<Survey> arrayList, SpiritualGrowthView.ISpiritualGrowth iSpiritualGrowth) {
        this.context = context;
        this.iSpiritualGrowth = iSpiritualGrowth;
        this.surveyItems = arrayList;
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.surveyItems.size();
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        myViewHolder.survey = this.surveyItems.get(i);
        myViewHolder.positionItem = i;
        myViewHolder.itemImage.setImageResource(this.surveyItems.get(i).getEnable() ? 2131230910 : 2131230909);
        myViewHolder.itemTitle.setText(this.surveyItems.get(i).getTitre_quizz().split("-")[1].trim());
        myViewHolder.itemDate.setText(CommonPresenter.changeFormatDate(this.surveyItems.get(i).getDate()));
        myViewHolder.itemRating.setNumStars(10);
        myViewHolder.itemRating.setRating((this.surveyItems.get(i).getTotal_trouve() / this.surveyItems.get(i).getTotal_question()) * 10.0f);
        myViewHolder.linearLayout.setEnabled(this.surveyItems.get(i).getEnable());
        this.mViewHolder.put(Integer.valueOf(i), myViewHolder);
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(this, LayoutInflater.from(viewGroup.getContext()).inflate(C0598R.layout.item_spiritual_growth, viewGroup, false));
    }
}
