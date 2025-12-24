package org.questionsreponses.View.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.github.clans.fab.BuildConfig;
import com.github.kiulian.downloader.model.videos.formats.Format;
import java.util.Hashtable;
import java.util.List;
import org.json.JSONException;
import org.questionsreponses.C0598R;
import org.questionsreponses.Model.Ressource;
import org.questionsreponses.Presenter.CommonPresenter;
import org.questionsreponses.Presenter.HomePresenter;
import org.questionsreponses.View.Interfaces.HomeView;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Adapters/AudioRecyclerAdapter.class */
public class AudioRecyclerAdapter extends RecyclerView.Adapter<MyViewHolder> implements HomeView.IAudioRecycler {
    private Context context;
    private HomeView.IAudioFrag iAudioFrag;
    private HomeView.IHome iHome;
    private List<Ressource> ressourceItems;
    private int positionSelected = -1;
    private int previousAudioPosition = -1;
    private int nextAudioProsition = -1;
    private Hashtable<Integer, MyViewHolder> mViewHolder = new Hashtable<>();

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Adapters/AudioRecyclerAdapter$MyViewHolder.class */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        View container;
        TextView itemsSubTitle;
        TextView itemsTitle;
        int positionItem;
        Ressource ressource;
        final AudioRecyclerAdapter this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public MyViewHolder(AudioRecyclerAdapter audioRecyclerAdapter, View view) {
            super(view);
            this.this$0 = audioRecyclerAdapter;
            this.container = view.findViewById(2131296359);
            this.itemsTitle = (TextView) view.findViewById(C0598R.id.item_title);
            this.itemsSubTitle = (TextView) view.findViewById(C0598R.id.item_subtitle);
            this.container.setOnClickListener(new View.OnClickListener(this, audioRecyclerAdapter) { // from class: org.questionsreponses.View.Adapters.AudioRecyclerAdapter.MyViewHolder.1
                final MyViewHolder this$1;
                final AudioRecyclerAdapter val$this$0;

                {
                    this.this$1 = this;
                    this.val$this$0 = audioRecyclerAdapter;
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view2) throws JSONException, Resources.NotFoundException {
                    this.this$1.this$0.positionSelected = this.this$1.positionItem;
                    HomePresenter homePresenter = new HomePresenter(this.this$1.this$0.iHome);
                    this.this$1.addFocusToItemSelection(view2);
                    this.this$1.this$0.previousAudioPosition = CommonPresenter.getPreviousRessourceValue(this.this$1.positionItem);
                    this.this$1.this$0.nextAudioProsition = CommonPresenter.getNextRessourceValue(this.this$1.positionItem, this.this$1.this$0.ressourceItems.size());
                    CommonPresenter.saveDataInSharePreferences(this.this$1.this$0.context, CommonPresenter.KEY_NOTIF_AUDIO_PLAYER_LIST, CommonPresenter.getDataFromSharePreferences(this.this$1.this$0.context, CommonPresenter.KEY_AUDIO_RETRIEVE_FROM_LVE_SERVER));
                    CommonPresenter.saveNotificationParameters(this.this$1.this$0.context, this.this$1.this$0.positionSelected);
                    CommonPresenter.saveDataInSharePreferences(this.this$1.this$0.context, CommonPresenter.VALUE_POSITION_AUDIO_SELECTED, BuildConfig.FLAVOR + this.this$1.positionItem);
                    homePresenter.retrieveAudioSelected(this.this$1.this$0.context, this.this$1.ressource, this.this$1.positionItem);
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

    public AudioRecyclerAdapter(Context context, List<Ressource> list, HomeView.IHome iHome, HomeView.IAudioFrag iAudioFrag) {
        this.context = context;
        this.iHome = iHome;
        this.iAudioFrag = iAudioFrag;
        this.ressourceItems = list;
        new HomePresenter(iHome).retrieveAndSetIAudioRecyclerAdapteur(this);
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IAudioRecycler
    public void addQRAudioToFavorite(Context context) {
        new HomePresenter(this.iHome).saveRessourceAudioData(context, this.ressourceItems.get(this.positionSelected));
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IAudioRecycler
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
        myViewHolder.ressource = this.ressourceItems.get(i);
        myViewHolder.positionItem = i;
        myViewHolder.itemsTitle.setText(this.ressourceItems.get(i).getTitre());
        String[] strArrSplit = this.ressourceItems.get(i).getDuree().split(":");
        myViewHolder.itemsSubTitle.setText(((Integer.parseInt(strArrSplit[0]) * 60) + Integer.parseInt(strArrSplit[1])) + "min | " + this.ressourceItems.get(i).getAuteur());
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(this, LayoutInflater.from(viewGroup.getContext()).inflate(C0598R.layout.item_audio, viewGroup, false));
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IAudioRecycler
    public void playNextQRAudio() throws JSONException {
        new HomePresenter(this.iAudioFrag).srcollAudioDataItemsToPosition(CommonPresenter.getScrollToNextValue(this.nextAudioProsition, this.ressourceItems.size()));
        CommonPresenter.saveNotificationParameters(this.context, this.nextAudioProsition);
        this.mViewHolder.get(Integer.valueOf(this.nextAudioProsition)).container.performClick();
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IAudioRecycler
    public void playPreviousQRAudio() throws JSONException {
        new HomePresenter(this.iAudioFrag).srcollAudioDataItemsToPosition(CommonPresenter.getScrollToPreviousValue(this.previousAudioPosition, this.ressourceItems.size()));
        CommonPresenter.saveNotificationParameters(this.context, this.previousAudioPosition);
        this.mViewHolder.get(Integer.valueOf(this.previousAudioPosition)).container.performClick();
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IAudioRecycler
    public void shareQRAudio(Context context) throws Resources.NotFoundException {
        CommonPresenter.shareRessource(context, this.ressourceItems.get(this.positionSelected), Format.AUDIO);
    }
}
