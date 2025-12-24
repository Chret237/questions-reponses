package org.questionsreponses.View.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.github.kiulian.downloader.model.videos.formats.Format;
import java.util.ArrayList;
import org.questionsreponses.C0598R;
import org.questionsreponses.Model.Youtube;
import org.questionsreponses.Presenter.CommonPresenter;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Adapters/FormatVideoAdapter.class */
public class FormatVideoAdapter extends RecyclerView.Adapter<VideoHolder> {
    private Context context;
    private Dialog dialog;
    private LayoutInflater mInflater;
    private ArrayList<Youtube> youtubes;

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Adapters/FormatVideoAdapter$VideoHolder.class */
    public class VideoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View formatContainer;
        int positionItem;
        final FormatVideoAdapter this$0;
        TextView videoTitleFormat;
        Youtube youtube;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public VideoHolder(FormatVideoAdapter formatVideoAdapter, View view) {
            super(view);
            this.this$0 = formatVideoAdapter;
            this.videoTitleFormat = (TextView) view.findViewById(C0598R.id.video_title_format);
            View viewFindViewById = view.findViewById(C0598R.id.format_container);
            this.formatContainer = viewFindViewById;
            viewFindViewById.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view.getId() != 2131296427) {
                return;
            }
            Youtube youtube = (Youtube) this.this$0.youtubes.get(this.positionItem);
            String url = youtube.getUrl();
            String str = youtube.getFilename().trim() + "." + youtube.getExtension().toLowerCase();
            String upperCase = youtube.getSize().toUpperCase();
            CommonPresenter.getFileByDownloadManager(this.this$0.context, url, str, "QR-APP-DOWNLOADER (" + youtube.getExtension().toUpperCase() + "/" + upperCase + ")", Format.VIDEO);
            this.this$0.dialog.dismiss();
        }
    }

    public FormatVideoAdapter(Context context, ArrayList<Youtube> arrayList, Dialog dialog) {
        this.context = context;
        this.youtubes = arrayList;
        this.dialog = dialog;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.youtubes.size();
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public void onBindViewHolder(VideoHolder videoHolder, int i) {
        videoHolder.youtube = this.youtubes.get(i);
        videoHolder.positionItem = i;
        String resolution = videoHolder.youtube.getResolution();
        String upperCase = videoHolder.youtube.getSize().toUpperCase();
        String upperCase2 = videoHolder.youtube.getExtension().toUpperCase();
        videoHolder.videoTitleFormat.setText(resolution + " (" + upperCase2 + "/" + upperCase + ")");
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public VideoHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new VideoHolder(this, this.mInflater.inflate(C0598R.layout.item_format_video, viewGroup, false));
    }
}
