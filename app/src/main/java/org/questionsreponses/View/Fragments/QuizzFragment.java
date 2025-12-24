package org.questionsreponses.View.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import org.questionsreponses.C0598R;
import org.questionsreponses.Model.Quizz;
import org.questionsreponses.Presenter.HomePresenter;
import org.questionsreponses.View.Activities.HomeActivity;
import org.questionsreponses.View.Adapters.QuizzRecyclerAdapter;
import org.questionsreponses.View.Interfaces.HomeView;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Fragments/QuizzFragment.class */
public class QuizzFragment extends Fragment implements HomeView.IQuizzFrag {
    private GridLayoutManager gridLayout;
    private HomePresenter homePresenter;
    private HomeView.IHome iHome;
    private HomeView.IQuizzSaveRecycler iQuizzSaveRecycler;
    private QuizzRecyclerAdapter quizzAdapter;
    private RecyclerView recyclerQuizzView;

    @Override // org.questionsreponses.View.Interfaces.HomeView.IQuizzFrag
    public void events() {
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IQuizzFrag
    public void initialize() {
        this.recyclerQuizzView = (RecyclerView) getActivity().findViewById(C0598R.id.quizzRecyclerView);
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IQuizzFrag
    public void loadQuizzMenu(List<String> list, int i, int i2) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), i);
        this.gridLayout = gridLayoutManager;
        this.recyclerQuizzView.setLayoutManager(gridLayoutManager);
        this.recyclerQuizzView.setHasFixedSize(true);
        QuizzRecyclerAdapter quizzRecyclerAdapter = new QuizzRecyclerAdapter(list, this.iHome);
        this.quizzAdapter = quizzRecyclerAdapter;
        this.recyclerQuizzView.setAdapter(quizzRecyclerAdapter);
        this.recyclerQuizzView.addOnScrollListener(new RecyclerView.OnScrollListener(this) { // from class: org.questionsreponses.View.Fragments.QuizzFragment.1
            final QuizzFragment this$0;

            {
                this.this$0 = this;
            }

            @Override // android.support.v7.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i3, int i4) {
                super.onScrolled(recyclerView, i3, i4);
            }
        });
        this.recyclerQuizzView.scrollToPosition(i2);
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        HomePresenter homePresenter = new HomePresenter(this);
        this.homePresenter = homePresenter;
        homePresenter.loadQuizzData(getActivity());
    }

    @Override // android.support.v4.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        KeyEvent.Callback activity = getActivity();
        this.iHome = (HomeView.IHome) activity;
        ((HomeActivity) activity).setiQuizzFrag(this);
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(C0598R.layout.fragment_quizz, viewGroup, false);
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IQuizzFrag
    public ArrayList<Quizz> retrieveAllQuizzData() {
        return new HomePresenter(this.iHome).retrieveAllQuizzDataFromHome();
    }
}
