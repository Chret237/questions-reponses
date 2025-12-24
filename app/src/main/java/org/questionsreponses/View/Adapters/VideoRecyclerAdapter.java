package org.questionsreponses.View.Adapters;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.github.clans.fab.BuildConfig;
import java.util.Hashtable;
import java.util.List;
import org.questionsreponses.C0598R;
import org.questionsreponses.Model.Ressource;
import org.questionsreponses.Presenter.CommonPresenter;
import org.questionsreponses.Presenter.HomePresenter;
import org.questionsreponses.View.Interfaces.HomeView;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Adapters/VideoRecyclerAdapter.class */
public class VideoRecyclerAdapter extends RecyclerView.Adapter<MyViewHolder> implements HomeView.IVideoRecycler {
    private HomeView.IVideoFrag iVideoFrag;
    private List<Ressource> ressourceItems;
    private int positionSelected = -1;
    private int previousVideoPosition = -1;
    private int nextVideoProsition = -1;
    private Hashtable<Integer, MyViewHolder> mViewHolder = new Hashtable<>();

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Adapters/VideoRecyclerAdapter$MyViewHolder.class */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        View container;
        TextView itemsSubTitle;
        TextView itemsTitle;
        int positionItem;
        Ressource ressource;
        final VideoRecyclerAdapter this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public MyViewHolder(VideoRecyclerAdapter videoRecyclerAdapter, View view) {
            super(view);
            this.this$0 = videoRecyclerAdapter;
            this.container = view.findViewById(2131296359);
            this.itemsTitle = (TextView) view.findViewById(C0598R.id.item_title);
            this.itemsSubTitle = (TextView) view.findViewById(C0598R.id.item_subtitle);
            this.container.setOnClickListener(new View.OnClickListener(this, videoRecyclerAdapter) { // from class: org.questionsreponses.View.Adapters.VideoRecyclerAdapter.MyViewHolder.1
                final MyViewHolder this$1;
                final VideoRecyclerAdapter val$this$0;

                {
                    this.this$1 = this;
                    this.val$this$0 = videoRecyclerAdapter;
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view2) throws Resources.NotFoundException {
                    this.this$1.this$0.positionSelected = this.this$1.positionItem;
                    this.this$1.addFocusToItemSelection(view2);
                    this.this$1.this$0.previousVideoPosition = CommonPresenter.getPreviousRessourceValue(this.this$1.positionItem);
                    this.this$1.this$0.nextVideoProsition = CommonPresenter.getNextRessourceValue(this.this$1.positionItem, this.this$1.this$0.ressourceItems.size());
                    CommonPresenter.saveDataInSharePreferences(view2.getContext(), CommonPresenter.VALUE_POSITION_VIDEO_SELECTED, BuildConfig.FLAVOR + this.this$1.positionItem);
                    new HomePresenter(this.this$1.this$0.iVideoFrag).playQRVideoPlayer(view2.getContext(), (Ressource) this.this$1.this$0.ressourceItems.get(this.this$1.this$0.positionSelected), this.this$1.this$0.positionSelected);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addFocusToItemSelection(View view) {
            for (int size = this.this$0.ressourceItems.size() - 1; size >= 0; size--) {
                if (this.this$0.mViewHolder.containsKey(Integer.valueOf(size))) {
                    ((MyViewHolder) this.this$0.mViewHolder.get(Integer.valueOf(size))).container.setBackgroundResource(C0598R.drawable.btn);
                }
            }
            view.setBackgroundResource(C0598R.drawable.btn_radius_hover);
        }
    }

    public VideoRecyclerAdapter(List<Ressource> list, HomeView.IVideoFrag iVideoFrag) {
        this.iVideoFrag = iVideoFrag;
        this.ressourceItems = list;
        new HomePresenter(iVideoFrag).retrieveAndSetIVideoRecyclerReference(this);
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.ressourceItems.size();
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        this.mViewHolder.put(Integer.valueOf(i), myViewHolder);
        myViewHolder.container.setBackgroundResource(this.positionSelected == i ? 2131230839 : 2131230813);
        myViewHolder.ressource = this.ressourceItems.get(i);
        myViewHolder.positionItem = i;
        myViewHolder.itemsTitle.setText(this.ressourceItems.get(i).getTitre());
        String[] strArrSplit = this.ressourceItems.get(i).getDuree().split(":");
        myViewHolder.itemsSubTitle.setText(((Integer.parseInt(strArrSplit[0]) * 60) + Integer.parseInt(strArrSplit[1])) + "min | " + this.ressourceItems.get(i).getAuteur());
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(this, LayoutInflater.from(viewGroup.getContext()).inflate(C0598R.layout.item_video, viewGroup, false));
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IVideoRecycler
    public void playNextQRVideo() {
        new HomePresenter(this.iVideoFrag).srcollVideoDataItemsToPosition(CommonPresenter.getScrollToNextValue(this.nextVideoProsition, this.ressourceItems.size()));
        this.mViewHolder.get(Integer.valueOf(this.nextVideoProsition)).container.performClick();
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IVideoRecycler
    public void playPreviousQRVideo() {
        new HomePresenter(this.iVideoFrag).srcollVideoDataItemsToPosition(CommonPresenter.getScrollToPreviousValue(this.previousVideoPosition, this.ressourceItems.size()));
        this.mViewHolder.get(Integer.valueOf(this.previousVideoPosition)).container.performClick();
    }
}
