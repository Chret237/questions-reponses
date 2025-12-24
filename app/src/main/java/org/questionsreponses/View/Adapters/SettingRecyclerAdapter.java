package org.questionsreponses.View.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import java.util.List;
import org.questionsreponses.C0598R;
import org.questionsreponses.Model.Setting;
import org.questionsreponses.Presenter.CommonPresenter;
import org.questionsreponses.Presenter.SettingsPresenter;
import org.questionsreponses.View.Interfaces.SettingsView;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Adapters/SettingRecyclerAdapter.class */
public class SettingRecyclerAdapter extends RecyclerView.Adapter<MyViewHolder> implements SettingsView.ISettingRecycler {
    private MenuItem action_quizz_level_difficulty_1;
    private MenuItem action_quizz_level_difficulty_2;
    private MenuItem action_quizz_level_difficulty_3;
    private MenuItem action_quizz_level_difficulty_4;
    private MenuItem action_quizz_level_difficulty_5;
    private MenuItem action_quizz_level_difficulty_6;
    private MenuItem action_quizz_reinitialization_1;
    private MenuItem action_quizz_reinitialization_2;
    private MenuItem action_quizz_reinitialization_3;
    private MenuItem action_quizz_reinitialization_4;
    private Context context;
    private SettingsView.ISettings iSettings;
    private String keySelected;
    private Hashtable<Integer, MyViewHolder> mViewHolder = new Hashtable<>();
    private int positionSelected;
    private List<Setting> settingItems;

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Adapters/SettingRecyclerAdapter$MyViewHolder.class */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBoxSetting;
        TextView itemLineBar;
        TextView itemSubtitle;
        TextView itemTitle;
        View linearLayout;
        int positionItem;
        final SettingRecyclerAdapter this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public MyViewHolder(SettingRecyclerAdapter settingRecyclerAdapter, View view) {
            super(view);
            this.this$0 = settingRecyclerAdapter;
            this.linearLayout = view.findViewById(C0598R.id.container_setting);
            this.itemTitle = (TextView) view.findViewById(C0598R.id.item_title);
            this.itemSubtitle = (TextView) view.findViewById(C0598R.id.item_subtitle);
            this.itemLineBar = (TextView) view.findViewById(C0598R.id.item_line_bar);
            this.checkBoxSetting = (CheckBox) view.findViewById(C0598R.id.item_checkBox);
            this.linearLayout.setOnClickListener(new View.OnClickListener(this, settingRecyclerAdapter) { // from class: org.questionsreponses.View.Adapters.SettingRecyclerAdapter.MyViewHolder.1
                final MyViewHolder this$1;
                final SettingRecyclerAdapter val$this$0;

                {
                    this.this$1 = this;
                    this.val$this$0 = settingRecyclerAdapter;
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view2) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                    this.this$1.this$0.positionSelected = this.this$1.positionItem;
                    Setting setting = (Setting) this.this$1.this$0.settingItems.get(this.this$1.positionItem);
                    this.this$1.this$0.keySelected = CommonPresenter.KEY_SETTINGS[this.this$1.positionItem];
                    if (setting.getTotal() == 0) {
                        CommonPresenter.saveSettingObjectInSharePreferences(this.this$1.this$0.context, this.this$1.this$0.keySelected, !setting.getChoice(), 0);
                        ((MyViewHolder) this.this$1.this$0.mViewHolder.get(Integer.valueOf(this.this$1.positionItem))).checkBoxSetting.setChecked(!setting.getChoice());
                        ((Setting) this.this$1.this$0.settingItems.get(this.this$1.positionItem)).setChoice(!setting.getChoice());
                    } else if (setting.getTotal() > 0) {
                        SettingsPresenter settingsPresenter = new SettingsPresenter(this.this$1.this$0.iSettings);
                        if (setting.getTotal() < 500) {
                            settingsPresenter.displayNumberOfTheQuizzList(view2);
                        } else if (setting.getTotal() == 1000) {
                            this.this$1.this$0.showPopupLevelDifficulty(view2, settingsPresenter);
                        } else if (setting.getTotal() == 1500) {
                            this.this$1.this$0.showPopupQuizzReinitialization(view2, settingsPresenter);
                        }
                    }
                }
            });
        }
    }

    public SettingRecyclerAdapter(Context context, List<Setting> list, SettingsView.ISettings iSettings) {
        this.context = context;
        this.iSettings = iSettings;
        this.settingItems = list;
        new SettingsPresenter(iSettings).setSettingsISettingRecycler(this);
    }

    private void addIconOnLevelDifficultyItems(int[] iArr) {
        this.action_quizz_level_difficulty_1.setIcon(iArr[0]);
        this.action_quizz_level_difficulty_2.setIcon(iArr[1]);
        this.action_quizz_level_difficulty_3.setIcon(iArr[2]);
        this.action_quizz_level_difficulty_4.setIcon(iArr[3]);
        this.action_quizz_level_difficulty_5.setIcon(iArr[4]);
        this.action_quizz_level_difficulty_6.setIcon(iArr[5]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:10:0x0036, code lost:
    
        r0.setAccessible(true);
        r0 = r0.get(r0);
        java.lang.Class.forName(r0.getClass().getName()).getMethod("setForceShowIcon", java.lang.Boolean.TYPE).invoke(r0, true);
     */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /* JADX WARN: Multi-variable type inference failed */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void showPopupLevelDifficulty(android.view.View r8, org.questionsreponses.Presenter.SettingsPresenter r9) throws java.lang.IllegalAccessException, java.lang.IllegalArgumentException, java.lang.reflect.InvocationTargetException {
        /*
            Method dump skipped, instructions count: 521
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.questionsreponses.View.Adapters.SettingRecyclerAdapter.showPopupLevelDifficulty(android.view.View, org.questionsreponses.Presenter.SettingsPresenter):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:10:0x0036, code lost:
    
        r0.setAccessible(true);
        r0 = r0.get(r0);
        java.lang.Class.forName(r0.getClass().getName()).getMethod("setForceShowIcon", java.lang.Boolean.TYPE).invoke(r0, true);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void showPopupQuizzReinitialization(android.view.View r8, org.questionsreponses.Presenter.SettingsPresenter r9) throws java.lang.IllegalAccessException, java.lang.IllegalArgumentException, java.lang.reflect.InvocationTargetException {
        /*
            Method dump skipped, instructions count: 220
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.questionsreponses.View.Adapters.SettingRecyclerAdapter.showPopupQuizzReinitialization(android.view.View, org.questionsreponses.Presenter.SettingsPresenter):void");
    }

    @Override // org.questionsreponses.View.Interfaces.SettingsView.ISettingRecycler
    public void changeNumberOfQuizz(Context context, int i) {
        Setting setting = this.settingItems.get(this.positionSelected);
        this.mViewHolder.get(Integer.valueOf(this.positionSelected)).itemSubtitle.setText(setting.getLibelle() + " " + i);
        CommonPresenter.saveSettingObjectInSharePreferences(context, this.keySelected, setting.getChoice(), i);
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.settingItems.size();
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        myViewHolder.positionItem = i;
        this.mViewHolder.put(Integer.valueOf(i), myViewHolder);
        Setting setting = this.settingItems.get(i);
        myViewHolder.itemTitle.setText(setting.getTitle());
        if (setting.getTotal() == 0) {
            myViewHolder.itemSubtitle.setText(setting.getLibelle());
            myViewHolder.checkBoxSetting.setVisibility(0);
            myViewHolder.itemLineBar.setVisibility(0);
            myViewHolder.checkBoxSetting.setChecked(setting.getChoice());
            return;
        }
        if (setting.getTotal() > 0) {
            if (setting.getTotal() < 500) {
                myViewHolder.itemSubtitle.setText(setting.getLibelle() + " " + setting.getTotal());
            } else if (setting.getTotal() == 1000 || setting.getTotal() == 1500) {
                myViewHolder.itemSubtitle.setText(setting.getLibelle());
            }
            myViewHolder.checkBoxSetting.setVisibility(8);
            myViewHolder.itemLineBar.setVisibility(8);
        }
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(this, LayoutInflater.from(viewGroup.getContext()).inflate(C0598R.layout.item_setting, viewGroup, false));
    }
}
