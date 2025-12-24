package org.questionsreponses.View.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import org.questionsreponses.C0598R;
import org.questionsreponses.Presenter.HomePresenter;
import org.questionsreponses.View.Interfaces.HomeView;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Adapters/QuizzRecyclerAdapter.class */
public class QuizzRecyclerAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private HomeView.IHome iHome;
    private List<String> quizzItems;

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Adapters/QuizzRecyclerAdapter$MyViewHolder.class */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        View cardView;
        TextView detailSummary;
        TextView itemTitle;
        View linearLayout;
        int positionItem;
        final QuizzRecyclerAdapter this$0;
        TextView titleSummary;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public MyViewHolder(QuizzRecyclerAdapter quizzRecyclerAdapter, View view) {
            super(view);
            this.this$0 = quizzRecyclerAdapter;
            this.linearLayout = view.findViewById(C0598R.id.item_linearlayout);
            this.itemTitle = (TextView) view.findViewById(C0598R.id.item_title);
            this.cardView = view.findViewById(C0598R.id.item_cardview);
            this.titleSummary = (TextView) view.findViewById(C0598R.id.title_summary);
            this.detailSummary = (TextView) view.findViewById(C0598R.id.detail_summary);
            this.linearLayout.setOnClickListener(new View.OnClickListener(this, quizzRecyclerAdapter) { // from class: org.questionsreponses.View.Adapters.QuizzRecyclerAdapter.MyViewHolder.1
                final MyViewHolder this$1;
                final QuizzRecyclerAdapter val$this$0;

                {
                    this.this$1 = this;
                    this.val$this$0 = quizzRecyclerAdapter;
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    new HomePresenter(this.this$1.this$0.iHome).retrieveMenuQuizzSelected(this.this$1.itemTitle.getText().toString(), this.this$1.positionItem);
                }
            });
        }
    }

    public QuizzRecyclerAdapter(List<String> list, HomeView.IHome iHome) {
        this.iHome = iHome;
        this.quizzItems = list;
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.quizzItems.size();
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        myViewHolder.positionItem = i;
        if (i <= 6) {
            myViewHolder.linearLayout.setVisibility(0);
            myViewHolder.cardView.setVisibility(8);
            myViewHolder.itemTitle.setText(this.quizzItems.get(i));
        } else if (i >= 7) {
            myViewHolder.linearLayout.setVisibility(8);
            myViewHolder.cardView.setVisibility(0);
            myViewHolder.titleSummary.setText(this.quizzItems.get(i - 6).toUpperCase());
            myViewHolder.detailSummary.setText(this.quizzItems.get(i));
        }
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(this, LayoutInflater.from(viewGroup.getContext()).inflate(C0598R.layout.item_quizz_menu, viewGroup, false));
    }
}
