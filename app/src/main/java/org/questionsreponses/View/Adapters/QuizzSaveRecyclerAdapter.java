package org.questionsreponses.View.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import org.questionsreponses.C0598R;
import org.questionsreponses.Model.DAOSurvey;
import org.questionsreponses.Model.Survey;
import org.questionsreponses.Presenter.CommonPresenter;
import org.questionsreponses.View.Activities.QuizzSaveListActivity;
import org.questionsreponses.View.Interfaces.HomeView;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Adapters/QuizzSaveRecyclerAdapter.class */
public class QuizzSaveRecyclerAdapter extends RecyclerView.Adapter<MyViewHolder> implements HomeView.IQuizzSaveRecycler {
    private HomeView.IHome iHome;
    private List<Survey> quizzItems;
    private String titleOfQuizzSaved;
    private Hashtable<Integer, String> quizzToDelete = new Hashtable<>();
    private Hashtable<Integer, MyViewHolder> mViewHolder = new Hashtable<>();

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Adapters/QuizzSaveRecyclerAdapter$MyViewHolder.class */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox itemCheckBox;
        TextView itemDate;
        ImageView itemImage;
        RatingBar itemRating;
        TextView itemTitle;
        View linearLayout;
        int positionItem;
        Survey survey;
        final QuizzSaveRecyclerAdapter this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public MyViewHolder(QuizzSaveRecyclerAdapter quizzSaveRecyclerAdapter, View view) {
            super(view);
            this.this$0 = quizzSaveRecyclerAdapter;
            this.linearLayout = view.findViewById(C0598R.id.item_linearlayout);
            this.itemCheckBox = (CheckBox) view.findViewById(C0598R.id.item_checkBox);
            this.itemImage = (ImageView) view.findViewById(C0598R.id.item_image);
            this.itemTitle = (TextView) view.findViewById(C0598R.id.item_title);
            this.itemDate = (TextView) view.findViewById(C0598R.id.item_date);
            this.itemRating = (RatingBar) view.findViewById(C0598R.id.item_rating);
            this.linearLayout.setOnClickListener(new View.OnClickListener(this, quizzSaveRecyclerAdapter) { // from class: org.questionsreponses.View.Adapters.QuizzSaveRecyclerAdapter.MyViewHolder.1
                final MyViewHolder this$1;
                final QuizzSaveRecyclerAdapter val$this$0;

                {
                    this.this$1 = this;
                    this.val$this$0 = quizzSaveRecyclerAdapter;
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view2) throws SQLException {
                    if (this.this$1.survey.getClef_groupe().indexOf("-" + view2.getContext().getResources().getString(C0598R.string.lb_key_group_to_add)) >= 0) {
                        this.this$1.this$0.iHome.launchQuizzGameThatNotFinished(new DAOSurvey(view2.getContext()).getAllByKeyGroup(this.this$1.survey.getClef_groupe()).toString(), this.this$1.survey.getTitre_quizz(), this.this$1.survey.getClef_groupe());
                    } else {
                        Intent intent = new Intent(view2.getContext(), (Class<?>) QuizzSaveListActivity.class);
                        intent.putExtra(CommonPresenter.KEY_QUIZZ_ACTIVITY_LIST, this.this$1.survey.getClef_groupe());
                        view2.getContext().startActivity(intent);
                    }
                }
            });
            this.linearLayout.setOnLongClickListener(new View.OnLongClickListener(this, quizzSaveRecyclerAdapter) { // from class: org.questionsreponses.View.Adapters.QuizzSaveRecyclerAdapter.MyViewHolder.2
                final MyViewHolder this$1;
                final QuizzSaveRecyclerAdapter val$this$0;

                {
                    this.this$1 = this;
                    this.val$this$0 = quizzSaveRecyclerAdapter;
                }

                @Override // android.view.View.OnLongClickListener
                public boolean onLongClick(View view2) {
                    this.this$1.checkedQuizzSaveItemsToDelete(view2);
                    this.this$1.this$0.setTitleOfQuizzSaved(view2, this.this$1.survey);
                    return true;
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void checkedQuizzSaveItemsToDelete(View view) {
            if (this.this$0.quizzToDelete.containsKey(Integer.valueOf(this.positionItem))) {
                this.this$0.quizzToDelete.remove(Integer.valueOf(this.positionItem));
                ((MyViewHolder) this.this$0.mViewHolder.get(Integer.valueOf(this.positionItem))).itemImage.setVisibility(0);
                ((MyViewHolder) this.this$0.mViewHolder.get(Integer.valueOf(this.positionItem))).itemCheckBox.setVisibility(8);
                ((MyViewHolder) this.this$0.mViewHolder.get(Integer.valueOf(this.positionItem))).itemCheckBox.setChecked(false);
                view.setBackgroundResource(C0598R.drawable.btn_orange);
            } else {
                this.this$0.quizzToDelete.put(Integer.valueOf(this.positionItem), this.survey.getClef_groupe());
                ((MyViewHolder) this.this$0.mViewHolder.get(Integer.valueOf(this.positionItem))).itemImage.setVisibility(8);
                ((MyViewHolder) this.this$0.mViewHolder.get(Integer.valueOf(this.positionItem))).itemCheckBox.setVisibility(0);
                ((MyViewHolder) this.this$0.mViewHolder.get(Integer.valueOf(this.positionItem))).itemCheckBox.setChecked(true);
                view.setBackgroundResource(C0598R.drawable.btn_radius_orange_hover);
            }
            this.this$0.showButtonDelete();
        }
    }

    public QuizzSaveRecyclerAdapter(List<Survey> list, HomeView.IHome iHome) {
        this.quizzItems = list;
        this.iHome = iHome;
        iHome.setIQuizzSaveRecyclerReference(this);
    }

    private void maintainedCheckedOrUnCheckedQuizz(int i) {
        try {
            if (this.quizzToDelete.size() <= 0) {
                this.mViewHolder.get(Integer.valueOf(i)).linearLayout.setBackgroundResource(C0598R.drawable.btn_orange);
                this.mViewHolder.get(Integer.valueOf(i)).itemImage.setVisibility(0);
                this.mViewHolder.get(Integer.valueOf(i)).itemCheckBox.setVisibility(8);
                this.mViewHolder.get(Integer.valueOf(i)).itemCheckBox.setChecked(false);
            } else if (this.quizzToDelete.containsKey(Integer.valueOf(i))) {
                this.mViewHolder.get(Integer.valueOf(i)).itemImage.setVisibility(8);
                this.mViewHolder.get(Integer.valueOf(i)).itemCheckBox.setVisibility(0);
                this.mViewHolder.get(Integer.valueOf(i)).itemCheckBox.setChecked(true);
                this.mViewHolder.get(Integer.valueOf(i)).linearLayout.setBackgroundResource(C0598R.drawable.btn_radius_orange_hover);
            } else if (!this.quizzToDelete.containsKey(Integer.valueOf(i))) {
                this.mViewHolder.get(Integer.valueOf(i)).itemImage.setVisibility(0);
                this.mViewHolder.get(Integer.valueOf(i)).itemCheckBox.setVisibility(8);
                this.mViewHolder.get(Integer.valueOf(i)).itemCheckBox.setChecked(false);
                this.mViewHolder.get(Integer.valueOf(i)).linearLayout.setBackgroundResource(C0598R.drawable.btn_orange);
            }
        } catch (Exception e) {
        }
    }

    private void removeCheckedItemQuizzSave(Context context) {
        try {
            Log.i("TAG_CHECK_QUIZZ", "TOTAL BEFORE = " + this.quizzToDelete.size());
            if (this.quizzToDelete.size() > 0) {
                ArrayList arrayList = new ArrayList();
                for (int size = this.quizzItems.size() - 1; size >= 0; size--) {
                    if (this.quizzToDelete.containsKey(Integer.valueOf(size))) {
                        arrayList.add(this.quizzToDelete.get(Integer.valueOf(size)));
                        this.quizzToDelete.remove(Integer.valueOf(size));
                        this.mViewHolder.remove(Integer.valueOf(size));
                        this.quizzItems.remove(size);
                        notifyItemRemoved(size);
                        notifyItemRangeChanged(size, this.quizzItems.size());
                        Log.i("TAG_POSITION_FINAL", "positionFinal = " + size);
                    }
                }
                Log.i("TAG_CHECK_QUIZZ", "TOTAL AFTER = " + this.quizzToDelete.size());
                for (int i = 0; i < arrayList.size(); i++) {
                    new DAOSurvey(context).deleteDataBy((String) arrayList.get(i));
                }
                showButtonDelete();
            }
        } catch (Exception e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setTitleOfQuizzSaved(View view, Survey survey) {
        if (this.titleOfQuizzSaved == null) {
            if (survey.getClef_groupe().indexOf("-" + view.getContext().getResources().getString(C0598R.string.lb_key_group_to_add)) >= 0) {
                this.titleOfQuizzSaved = view.getContext().getResources().getString(C0598R.string.lb_menu_quizz_not_end);
            } else {
                this.titleOfQuizzSaved = view.getContext().getResources().getString(C0598R.string.lb_menu_quizz_end);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showButtonDelete() {
        this.iHome.deleteQuizzSavedVisibility(this.quizzToDelete.size() > 0 ? 0 : 8);
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IQuizzSaveRecycler
    public void deleteAllCheckedQuizz(Context context) {
        removeCheckedItemQuizzSave(context);
        this.iHome.modifyTitleOfSaveQuizzGame(this.titleOfQuizzSaved, this.mViewHolder.size());
        Log.i("TAG_QUIZZ_CHECKED", "IQuizzSaveRecycler :: deleteAllCheckedQuizz()");
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.quizzItems.size();
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        this.mViewHolder.put(Integer.valueOf(i), myViewHolder);
        myViewHolder.survey = this.quizzItems.get(i);
        myViewHolder.positionItem = i;
        myViewHolder.itemTitle.setText("Q/R : " + this.quizzItems.get(i).getTitre_quizz());
        myViewHolder.itemDate.setText(CommonPresenter.changeFormatDate(this.quizzItems.get(i).getDate()));
        float total_trouve = ((float) this.quizzItems.get(i).getTotal_trouve()) / ((float) this.quizzItems.get(i).getTotal_question());
        myViewHolder.itemRating.setNumStars(10);
        myViewHolder.itemRating.setRating(total_trouve * 10.0f);
        maintainedCheckedOrUnCheckedQuizz(i);
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(this, LayoutInflater.from(viewGroup.getContext()).inflate(C0598R.layout.item_quizz_save, viewGroup, false));
    }
}
