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
import android.widget.ProgressBar;
import com.github.kiulian.downloader.model.videos.formats.Format;
import java.util.List;
import org.questionsreponses.C0598R;
import org.questionsreponses.Model.Ressource;
import org.questionsreponses.Presenter.HomePresenter;
import org.questionsreponses.View.Activities.HomeActivity;
import org.questionsreponses.View.Adapters.AudioRecyclerAdapter;
import org.questionsreponses.View.Interfaces.HomeView;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Fragments/AudioFragment.class */
public class AudioFragment extends Fragment implements HomeView.IAudioFrag {
    private AudioRecyclerAdapter adapter;
    private View audio_content_download;
    private View audio_content_else;
    private View audio_content_favorite;
    private View audio_content_home;
    private HomePresenter homePresenter;
    private HomeView.IHome iHome;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    @Override // org.questionsreponses.View.Interfaces.HomeView.IAudioFrag
    public void events() {
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IAudioFrag
    public void hideProgressBar() {
        this.progressBar.setVisibility(8);
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IAudioFrag
    public void initialize() {
        this.progressBar = (ProgressBar) getActivity().findViewById(C0598R.id.audioProgress);
        this.recyclerView = (RecyclerView) getActivity().findViewById(C0598R.id.audioRecyclerView);
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IAudioFrag
    public void initializeAllAudios(List<Ressource> list) {
        this.iHome.initializeResourceList(Format.AUDIO, list);
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IAudioFrag
    public void loadAudioData(List<Ressource> list, int i, int i2) {
        this.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), i));
        this.recyclerView.setHasFixedSize(true);
        AudioRecyclerAdapter audioRecyclerAdapter = new AudioRecyclerAdapter(getActivity(), list, this.iHome, this);
        this.adapter = audioRecyclerAdapter;
        this.recyclerView.setAdapter(audioRecyclerAdapter);
        this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(this) { // from class: org.questionsreponses.View.Fragments.AudioFragment.1
            final AudioFragment this$0;

            {
                this.this$0 = this;
            }

            @Override // android.support.v7.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i3, int i4) {
                super.onScrolled(recyclerView, i3, i4);
            }
        });
        this.recyclerView.scrollToPosition(i2);
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        HomePresenter homePresenter = new HomePresenter(this);
        this.homePresenter = homePresenter;
        homePresenter.loadAudioData(getActivity());
    }

    @Override // android.support.v4.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        KeyEvent.Callback activity = getActivity();
        this.iHome = (HomeView.IHome) activity;
        ((HomeActivity) activity).setiAudioFrag(this);
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(C0598R.layout.fragment_audio, viewGroup, false);
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IAudioFrag
    public List<Ressource> retrieveAllAudiosList() {
        return this.iHome.retrieveResourceList(Format.AUDIO);
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IAudioFrag
    public void showProgressBar() {
        this.progressBar.setVisibility(0);
    }
}
