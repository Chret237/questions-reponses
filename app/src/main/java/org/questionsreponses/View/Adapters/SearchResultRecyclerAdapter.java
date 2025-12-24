package org.questionsreponses.View.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.github.kiulian.downloader.model.videos.formats.Format;
import java.util.Hashtable;
import java.util.List;
import org.questionsreponses.C0598R;
import org.questionsreponses.Model.Ressource;
import org.questionsreponses.Presenter.CommonPresenter;
import org.questionsreponses.Presenter.SearchResultPresenter;
import org.questionsreponses.View.Interfaces.SearchResultView;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Adapters/SearchResultRecyclerAdapter.class */
public class SearchResultRecyclerAdapter extends RecyclerView.Adapter<MyViewHolder> implements SearchResultView.ISearchResultRecycler {
    private Context context;
    private SearchResultView.ISearchResult iSearchResult;
    private List<Ressource> ressourceItems;
    private String typeRessource;
    private int positionSelected = -1;
    private int previousVideoPosition = -1;
    private int nextVideoPosition = -1;
    private int previousAudioPosition = -1;
    private int nextAudioPosition = -1;
    private Hashtable<Integer, MyViewHolder> mViewHolder = new Hashtable<>();

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Adapters/SearchResultRecyclerAdapter$MyViewHolder.class */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        View container;
        ImageView itemImage;
        TextView itemsSubTitle;
        TextView itemsTitle;
        int positionItem;
        final SearchResultRecyclerAdapter this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public MyViewHolder(SearchResultRecyclerAdapter searchResultRecyclerAdapter, View view) {
            super(view);
            this.this$0 = searchResultRecyclerAdapter;
            this.container = view.findViewById(2131296359);
            this.itemImage = (ImageView) view.findViewById(C0598R.id.item_image);
            this.itemsTitle = (TextView) view.findViewById(C0598R.id.item_title);
            this.itemsSubTitle = (TextView) view.findViewById(C0598R.id.item_subtitle);
            this.container.setOnClickListener(new View.OnClickListener(this, searchResultRecyclerAdapter) { // from class: org.questionsreponses.View.Adapters.SearchResultRecyclerAdapter.MyViewHolder.1
                final MyViewHolder this$1;
                final SearchResultRecyclerAdapter val$this$0;

                {
                    this.this$1 = this;
                    this.val$this$0 = searchResultRecyclerAdapter;
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view2) throws Resources.NotFoundException {
                    this.this$1.this$0.positionSelected = this.this$1.positionItem;
                    this.this$1.addFocusToItemSelection(view2);
                    SearchResultPresenter searchResultPresenter = new SearchResultPresenter(this.this$1.this$0.iSearchResult);
                    if (this.this$1.this$0.typeRessource.equalsIgnoreCase(Format.VIDEO)) {
                        this.this$1.this$0.previousVideoPosition = CommonPresenter.getPreviousRessourceValue(this.this$1.positionItem);
                        this.this$1.this$0.nextVideoPosition = CommonPresenter.getNextRessourceValue(this.this$1.positionItem, this.this$1.this$0.ressourceItems.size());
                        searchResultPresenter.playQRVideoPlayer(view2.getContext(), (Ressource) this.this$1.this$0.ressourceItems.get(this.this$1.this$0.positionSelected), this.this$1.this$0.positionSelected);
                        return;
                    }
                    if (this.this$1.this$0.typeRessource.equalsIgnoreCase(Format.AUDIO)) {
                        this.this$1.this$0.previousAudioPosition = CommonPresenter.getPreviousRessourceValue(this.this$1.positionItem);
                        this.this$1.this$0.nextAudioPosition = CommonPresenter.getNextRessourceValue(this.this$1.positionItem, this.this$1.this$0.ressourceItems.size());
                        searchResultPresenter.playQRAudioPlayer(view2.getContext(), (Ressource) this.this$1.this$0.ressourceItems.get(this.this$1.this$0.positionSelected), this.this$1.this$0.positionSelected);
                    }
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

    public SearchResultRecyclerAdapter(Context context, List<Ressource> list, String str, SearchResultView.ISearchResult iSearchResult) {
        this.context = context;
        this.iSearchResult = iSearchResult;
        this.ressourceItems = list;
        this.typeRessource = str;
        new SearchResultPresenter(iSearchResult).retrieveAndSetISearchResultRecyclerReference(this);
    }

    @Override // org.questionsreponses.View.Interfaces.SearchResultView.ISearchResultRecycler
    public void addQRAudioToFavorite(Context context) {
    }

    @Override // org.questionsreponses.View.Interfaces.SearchResultView.ISearchResultRecycler
    public void downloadQRAudio(Context context) {
        boolean z = true;
        if (CommonPresenter.getSettingObjectFromSharePreferences(context, CommonPresenter.KEY_SETTING_WIFI_EXCLUSIF).getChoice()) {
            if (CommonPresenter.isMobileWIFIConnected(context)) {
                z = true;
            } else {
                CommonPresenter.showMessage(context, context.getResources().getString(C0598R.string.lb_wifi_only), context.getResources().getString(C0598R.string.lb_wifi_exclusif_message), false);
                z = false;
            }
        }
        if (z) {
            Ressource ressource = this.ressourceItems.get(this.positionSelected);
            CommonPresenter.getFileByDownloadManager(context, ressource.getUrlacces() + ressource.getSrc(), ressource.getSrc(), "QR-APP-DOWNLOADER (" + ressource.getDuree() + " | " + ressource.getAuteur() + ")", Format.AUDIO);
            Toast.makeText(context, context.getResources().getString(C0598R.string.lb_downloading), 0).show();
        }
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.ressourceItems.size();
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        this.mViewHolder.put(Integer.valueOf(i), myViewHolder);
        myViewHolder.container.setBackgroundResource(this.positionSelected == i ? 2131230839 : 2131230813);
        if (this.typeRessource.equalsIgnoreCase(Format.VIDEO)) {
            myViewHolder.itemImage.setImageResource(C0598R.mipmap.video);
        } else if (this.typeRessource.equalsIgnoreCase(Format.AUDIO)) {
            myViewHolder.itemImage.setImageResource(C0598R.mipmap.audio);
        }
        myViewHolder.positionItem = i;
        myViewHolder.itemsTitle.setText(this.ressourceItems.get(i).getTitre());
        String[] strArrSplit = this.ressourceItems.get(i).getDuree().split(":");
        myViewHolder.itemsSubTitle.setText(((Integer.parseInt(strArrSplit[0]) * 60) + Integer.parseInt(strArrSplit[1])) + "min | " + this.ressourceItems.get(i).getAuteur());
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(this, LayoutInflater.from(viewGroup.getContext()).inflate(C0598R.layout.item_audio, viewGroup, false));
    }

    @Override // org.questionsreponses.View.Interfaces.SearchResultView.ISearchResultRecycler
    public void playNextQRAudio() {
        new SearchResultPresenter(this.iSearchResult).srcollResourceDataItemsToPosition(CommonPresenter.getScrollToNextValue(this.nextAudioPosition, this.ressourceItems.size()));
        this.mViewHolder.get(Integer.valueOf(this.nextAudioPosition)).container.performClick();
    }

    @Override // org.questionsreponses.View.Interfaces.SearchResultView.ISearchResultRecycler
    public void playNextQRVideo() {
        new SearchResultPresenter(this.iSearchResult).srcollResourceDataItemsToPosition(CommonPresenter.getScrollToNextValue(this.nextVideoPosition, this.ressourceItems.size()));
        this.mViewHolder.get(Integer.valueOf(this.nextVideoPosition)).container.performClick();
    }

    @Override // org.questionsreponses.View.Interfaces.SearchResultView.ISearchResultRecycler
    public void playPreviousQRAudio() {
        new SearchResultPresenter(this.iSearchResult).srcollResourceDataItemsToPosition(CommonPresenter.getScrollToPreviousValue(this.previousAudioPosition, this.ressourceItems.size()));
        this.mViewHolder.get(Integer.valueOf(this.previousAudioPosition)).container.performClick();
    }

    @Override // org.questionsreponses.View.Interfaces.SearchResultView.ISearchResultRecycler
    public void playPreviousQRVideo() {
        new SearchResultPresenter(this.iSearchResult).srcollResourceDataItemsToPosition(CommonPresenter.getScrollToPreviousValue(this.previousVideoPosition, this.ressourceItems.size()));
        this.mViewHolder.get(Integer.valueOf(this.previousVideoPosition)).container.performClick();
    }

    @Override // org.questionsreponses.View.Interfaces.SearchResultView.ISearchResultRecycler
    public void playQRNotifAudioPlayer(Context context) throws Resources.NotFoundException {
        new SearchResultPresenter(this.iSearchResult).playQRNotifAudioPlayer(context, this.positionSelected);
    }

    @Override // org.questionsreponses.View.Interfaces.SearchResultView.ISearchResultRecycler
    public void shareQRAudio(Context context) throws Resources.NotFoundException {
        CommonPresenter.shareRessource(context, this.ressourceItems.get(this.positionSelected), Format.AUDIO);
    }
}
