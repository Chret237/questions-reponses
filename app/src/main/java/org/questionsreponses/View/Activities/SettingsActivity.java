package org.questionsreponses.View.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import java.util.List;
import org.questionsreponses.C0598R;
import org.questionsreponses.Model.Setting;
import org.questionsreponses.Presenter.SettingsPresenter;
import org.questionsreponses.View.Adapters.SettingRecyclerAdapter;
import org.questionsreponses.View.Interfaces.SettingsView;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Activities/SettingsActivity.class */
public class SettingsActivity extends AppCompatActivity implements SettingsView.ISettings {
    private SettingsView.ISettingRecycler iSettingRecycler;
    private SettingRecyclerAdapter recyclerAdapter;
    private ImageButton setting_back;
    private RecyclerView setting_recycler;
    private SettingsPresenter settingsPresenter;

    @Override // org.questionsreponses.View.Interfaces.SettingsView.ISettings
    public void closeActivity() {
        finish();
    }

    @Override // org.questionsreponses.View.Interfaces.SettingsView.ISettings
    public void events() {
        this.setting_back.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities.SettingsActivity.1
            final SettingsActivity this$0;

            {
                this.this$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                this.this$0.settingsPresenter.retrieveUserAction(view);
            }
        });
    }

    @Override // org.questionsreponses.View.Interfaces.SettingsView.ISettings
    public void hideHeader() {
        getSupportActionBar().hide();
    }

    @Override // org.questionsreponses.View.Interfaces.SettingsView.ISettings
    public void initialize() {
        this.setting_recycler = (RecyclerView) findViewById(C0598R.id.setting_recycler);
        this.setting_back = (ImageButton) findViewById(C0598R.id.setting_back);
    }

    @Override // org.questionsreponses.View.Interfaces.SettingsView.ISettings
    public void loadSettingData(List<Setting> list, int i, int i2) {
        this.setting_recycler.setLayoutManager(new GridLayoutManager(this, i));
        this.setting_recycler.setHasFixedSize(true);
        SettingRecyclerAdapter settingRecyclerAdapter = new SettingRecyclerAdapter(this, list, this);
        this.recyclerAdapter = settingRecyclerAdapter;
        this.setting_recycler.setAdapter(settingRecyclerAdapter);
        this.setting_recycler.scrollToPosition(i2);
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0598R.layout.activity_settings);
        SettingsPresenter settingsPresenter = new SettingsPresenter(this);
        this.settingsPresenter = settingsPresenter;
        settingsPresenter.loadSettingsData(this);
    }

    @Override // org.questionsreponses.View.Interfaces.SettingsView.ISettings
    public void setSettingsISettingRecycler(SettingsView.ISettingRecycler iSettingRecycler) {
        this.iSettingRecycler = iSettingRecycler;
    }

    @Override // org.questionsreponses.View.Interfaces.SettingsView.ISettings
    public void showListOfNumberOfTheQuizz(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(C0598R.menu.popup_number_quizz, popupMenu.getMenu());
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(this) { // from class: org.questionsreponses.View.Activities.SettingsActivity.2
            final SettingsActivity this$0;

            {
                this.this$0 = this;
            }

            @Override // android.support.v7.widget.PopupMenu.OnMenuItemClickListener
            public boolean onMenuItemClick(MenuItem menuItem) {
                this.this$0.settingsPresenter.modifyNumberOfTheQuizz(this.this$0.iSettingRecycler, Integer.parseInt(menuItem.getTitle().toString()), this.this$0);
                return false;
            }
        });
    }
}
