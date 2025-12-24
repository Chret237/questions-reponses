package org.questionsreponses.View.Fragments;

import android.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.github.kiulian.downloader.model.videos.formats.Format;
import java.util.List;
import org.questionsreponses.C0598R;
import org.questionsreponses.Model.Ressource;
import org.questionsreponses.Presenter.CommonPresenter;
import org.questionsreponses.Presenter.HomePresenter;
import org.questionsreponses.View.Activities.HomeActivity;
import org.questionsreponses.View.Activities.VideoPlayerActivity;
import org.questionsreponses.View.Adapters.VideoRecyclerAdapter;
import org.questionsreponses.View.Interfaces.HomeView;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Fragments/VideoFragment.class */
public class VideoFragment extends Fragment implements HomeView.IVideoFrag {
    private VideoRecyclerAdapter adapter;
    private GridLayoutManager gridLayout;
    private HomePresenter homePresenter;
    private HomeView.IHome iHome;
    private HomeView.IVideoRecycler iVideoRecycler;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    @Override // org.questionsreponses.View.Interfaces.HomeView.IVideoFrag
    public void events() {
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IVideoFrag
    public void hideProgressBar() {
        this.progressBar.setVisibility(8);
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IVideoFrag
    public void initialize() {
        this.progressBar = (ProgressBar) getActivity().findViewById(C0598R.id.videoProgress);
        this.recyclerView = (RecyclerView) getActivity().findViewById(C0598R.id.videoRecyclerView);
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IVideoFrag
    public void initializeAllVideos(List<Ressource> list) {
        this.iHome.initializeResourceList(Format.VIDEO, list);
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IVideoFrag
    public void launchVideoToPlay(Ressource ressource, int i) {
        Intent intent = new Intent(getActivity(), (Class<?>) VideoPlayerActivity.class);
        intent.putExtra(CommonPresenter.KEY_VIDEO_PLAYER_SEND_DATA, ressource);
        intent.putExtra(CommonPresenter.VALUE_POSITION_VIDEO_SELECTED, i);
        startActivityForResult(intent, 10);
        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IVideoFrag
    public void loadVideoData(List<Ressource> list, int i, int i2) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), i);
        this.gridLayout = gridLayoutManager;
        this.recyclerView.setLayoutManager(gridLayoutManager);
        this.recyclerView.setHasFixedSize(true);
        VideoRecyclerAdapter videoRecyclerAdapter = new VideoRecyclerAdapter(list, this);
        this.adapter = videoRecyclerAdapter;
        this.recyclerView.setAdapter(videoRecyclerAdapter);
        this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(this) { // from class: org.questionsreponses.View.Fragments.VideoFragment.1
            final VideoFragment this$0;

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
        homePresenter.loadVideoData(getActivity());
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 10) {
            boolean z = -1;
            if (i2 == -1) {
                String stringExtra = intent.getStringExtra(CommonPresenter.KEY_VIDEO_PLAYER_RETURN_DATA);
                int iHashCode = stringExtra.hashCode();
                if (iHashCode != -1462737076) {
                    if (iHashCode == -809911600 && stringExtra.equals(CommonPresenter.VALUE_VIDEO_PLAY_PREVIOUS)) {
                        z = true;
                    }
                } else if (stringExtra.equals(CommonPresenter.VALUE_VIDEO_PLAY_NEXT)) {
                    z = false;
                }
                if (!z) {
                    this.homePresenter.playNextQRVideo(this.iVideoRecycler);
                } else if (z) {
                    this.homePresenter.playPreviousQRVideo(this.iVideoRecycler);
                }
            } else if (i2 == 0) {
                Log.i("TAG_VIDEO_FRAGMENT", "Activity.RESULT_CANCELED = " + i);
                Log.i("TAG_VIDEO_FRAGMENT", "Activity.RESULT_CANCELED = " + i2);
            }
        }
        super.onActivityResult(i, i2, intent);
    }

    @Override // android.support.v4.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        KeyEvent.Callback activity = getActivity();
        this.iHome = (HomeView.IHome) activity;
        ((HomeActivity) activity).setiVideoFrag(this);
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(C0598R.layout.fragment_video, viewGroup, false);
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IVideoFrag
    public void onFragmentVideoIVideoRecycler(HomeView.IVideoRecycler iVideoRecycler) {
        this.iVideoRecycler = iVideoRecycler;
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IVideoFrag
    public List<Ressource> retrieveAllVideosList() {
        return this.iHome.retrieveResourceList(Format.VIDEO);
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IVideoFrag
    public void scrollVideoDataToPosition(int i) {
        this.recyclerView.scrollToPosition(i);
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IVideoFrag
    public void showProgressBar() {
        this.progressBar.setVisibility(0);
    }
}
