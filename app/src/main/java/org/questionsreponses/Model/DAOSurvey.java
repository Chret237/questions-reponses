package org.questionsreponses.Model;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import org.questionsreponses.C0598R;
import org.questionsreponses.Presenter.CommonPresenter;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/Model/DAOSurvey.class */
public class DAOSurvey {
    private Context context;
    private final String TABLE_NAME = "QR_TABLE_SURVEY";
    private final String COL_1 = "ID";
    private final String COL_2 = "QUESTION";
    private final String COL_3 = "CATEGORIE";
    private final String COL_4 = "EXPLICATION";
    private final String COL_5 = "PROPOSITION_1";
    private final String COL_6 = "PROPOSITION_2";
    private final String COL_7 = "PROPOSITION_3";
    private final String COL_8 = "REPONSE";
    private final String COL_9 = "REPONSE_CHOISIE";
    private final String COL_10 = "CLEF_GROUPE";
    private final String COL_11 = "TOTAL_QUESTION";
    private final String COL_12 = "TOTAL_TROUVE";
    private final String COL_13 = "TOTAL_ERREUR";
    private final String COL_14 = "TITRE_QUIZZ";
    private final String COL_15 = "DATE";

    public DAOSurvey(Context context) {
        this.context = context;
    }

    public void createTable() throws SQLException {
        CommonPresenter.buildDataBase(this.context);
        CommonPresenter.getDb().execSQL("CREATE TABLE IF NOT EXISTS QR_TABLE_SURVEY (ID INTEGER PRIMARY KEY AUTOINCREMENT, QUESTION VARCHAR, CATEGORIE VARCHAR, EXPLICATION VARCHAR, PROPOSITION_1 VARCHAR, PROPOSITION_2 VARCHAR, PROPOSITION_3 VARCHAR, REPONSE VARCHAR, REPONSE_CHOISIE VARCHAR, CLEF_GROUPE VARCHAR, TOTAL_QUESTION INTEGER, TOTAL_TROUVE INTEGER, TOTAL_ERREUR INTEGER, TITRE_QUIZZ VARCHAR, DATE VARCHAR);");
    }

    public void deleteDataBy(String str) throws SQLException {
        createTable();
        CommonPresenter.getDb().execSQL("DELETE FROM QR_TABLE_SURVEY WHERE CLEF_GROUPE LIKE '" + str + "'");
    }

    public void deleteQuizzFinished() throws SQLException {
        createTable();
        CommonPresenter.getDb().execSQL("DELETE FROM QR_TABLE_SURVEY WHERE CLEF_GROUPE NOT LIKE '%" + this.context.getResources().getString(C0598R.string.lb_key_group_to_add) + "%' AND CLEF_GROUPE NOT LIKE '%" + this.context.getResources().getString(C0598R.string.lb_key_group_spiritual_growth) + "%'");
    }

    public void deleteQuizzNotFinished() throws SQLException {
        createTable();
        CommonPresenter.getDb().execSQL("DELETE FROM QR_TABLE_SURVEY WHERE CLEF_GROUPE LIKE '%" + this.context.getResources().getString(C0598R.string.lb_key_group_to_add) + "%'");
    }

    public void deleteQuizzSpiritualGrowth() throws SQLException {
        createTable();
        CommonPresenter.getDb().execSQL("DELETE FROM QR_TABLE_SURVEY WHERE CLEF_GROUPE LIKE '%" + this.context.getResources().getString(C0598R.string.lb_key_group_spiritual_growth) + "%'");
    }

    public void dropAllData() throws SQLException {
        createTable();
        CommonPresenter.getDb().execSQL("DROP TABLE IF EXISTS QR_TABLE_SURVEY");
    }

    public ArrayList<Survey> getAllByKeyGroup(String str) throws SQLException {
        createTable();
        ArrayList<Survey> arrayList = new ArrayList<>();
        Cursor cursorRawQuery = CommonPresenter.getDb().rawQuery("SELECT * FROM QR_TABLE_SURVEY WHERE CLEF_GROUPE LIKE '%" + str + "%' ORDER BY ID ASC", null);
        int count = cursorRawQuery.getCount();
        cursorRawQuery.moveToFirst();
        int iIntValue = 0;
        while (true) {
            Integer numValueOf = Integer.valueOf(iIntValue);
            if (numValueOf.intValue() >= count) {
                CommonPresenter.getDb().close();
                return arrayList;
            }
            arrayList.add(new Survey(cursorRawQuery.getInt(cursorRawQuery.getColumnIndex("ID")), cursorRawQuery.getString(cursorRawQuery.getColumnIndex("QUESTION")), cursorRawQuery.getString(cursorRawQuery.getColumnIndex("CATEGORIE")), cursorRawQuery.getString(cursorRawQuery.getColumnIndex("EXPLICATION")), cursorRawQuery.getString(cursorRawQuery.getColumnIndex("PROPOSITION_1")), cursorRawQuery.getString(cursorRawQuery.getColumnIndex("PROPOSITION_2")), cursorRawQuery.getString(cursorRawQuery.getColumnIndex("PROPOSITION_3")), cursorRawQuery.getString(cursorRawQuery.getColumnIndex("REPONSE")), cursorRawQuery.getString(cursorRawQuery.getColumnIndex("REPONSE_CHOISIE")), cursorRawQuery.getString(cursorRawQuery.getColumnIndex("CLEF_GROUPE")), cursorRawQuery.getInt(cursorRawQuery.getColumnIndex("TOTAL_QUESTION")), cursorRawQuery.getInt(cursorRawQuery.getColumnIndex("TOTAL_TROUVE")), cursorRawQuery.getInt(cursorRawQuery.getColumnIndex("TOTAL_ERREUR")), cursorRawQuery.getString(cursorRawQuery.getColumnIndex("TITRE_QUIZZ")), cursorRawQuery.getString(cursorRawQuery.getColumnIndex("DATE"))));
            cursorRawQuery.moveToNext();
            iIntValue = numValueOf.intValue() + 1;
        }
    }

    public ArrayList<Survey> getAllFinishedKeyGroup() {
        String string = this.context.getResources().getString(C0598R.string.lb_key_group_to_add);
        String string2 = this.context.getResources().getString(C0598R.string.lb_key_group_spiritual_growth);
        createTable();
        ArrayList<Survey> arrayList = new ArrayList<>();
        Cursor cursorRawQuery = CommonPresenter.getDb().rawQuery("SELECT * FROM QR_TABLE_SURVEY ORDER BY ID DESC", null);
        int count = cursorRawQuery.getCount();
        cursorRawQuery.moveToFirst();
        Hashtable hashtable = new Hashtable();
        int iIntValue = 0;
        while (true) {
            Integer numValueOf = Integer.valueOf(iIntValue);
            if (numValueOf.intValue() >= count) {
                CommonPresenter.getDb().close();
                return arrayList;
            }
            int i = cursorRawQuery.getInt(cursorRawQuery.getColumnIndex("ID"));
            String string3 = cursorRawQuery.getString(cursorRawQuery.getColumnIndex("CLEF_GROUPE"));
            int i2 = cursorRawQuery.getInt(cursorRawQuery.getColumnIndex("TOTAL_QUESTION"));
            int i3 = cursorRawQuery.getInt(cursorRawQuery.getColumnIndex("TOTAL_TROUVE"));
            int i4 = cursorRawQuery.getInt(cursorRawQuery.getColumnIndex("TOTAL_ERREUR"));
            String string4 = cursorRawQuery.getString(cursorRawQuery.getColumnIndex("TITRE_QUIZZ"));
            String string5 = cursorRawQuery.getString(cursorRawQuery.getColumnIndex("DATE"));
            if (!hashtable.containsKey(string3) && string3.indexOf(string) < 0 && string3.indexOf(string2) < 0) {
                hashtable.put(string3, string3);
                arrayList.add(new Survey(i, string3, i2, i3, i4, string4, string5));
            }
            cursorRawQuery.moveToNext();
            iIntValue = numValueOf.intValue() + 1;
        }
    }

    public ArrayList<Survey> getAllNotFinishedKeyGroup() {
        String string = this.context.getResources().getString(C0598R.string.lb_key_group_to_add);
        createTable();
        ArrayList<Survey> arrayList = new ArrayList<>();
        Cursor cursorRawQuery = CommonPresenter.getDb().rawQuery("SELECT * FROM QR_TABLE_SURVEY ORDER BY ID DESC", null);
        int count = cursorRawQuery.getCount();
        cursorRawQuery.moveToFirst();
        Hashtable hashtable = new Hashtable();
        int iIntValue = 0;
        while (true) {
            Integer numValueOf = Integer.valueOf(iIntValue);
            if (numValueOf.intValue() >= count) {
                CommonPresenter.getDb().close();
                return arrayList;
            }
            int i = cursorRawQuery.getInt(cursorRawQuery.getColumnIndex("ID"));
            String string2 = cursorRawQuery.getString(cursorRawQuery.getColumnIndex("CLEF_GROUPE"));
            int i2 = cursorRawQuery.getInt(cursorRawQuery.getColumnIndex("TOTAL_QUESTION"));
            int i3 = cursorRawQuery.getInt(cursorRawQuery.getColumnIndex("TOTAL_TROUVE"));
            int i4 = cursorRawQuery.getInt(cursorRawQuery.getColumnIndex("TOTAL_ERREUR"));
            String string3 = cursorRawQuery.getString(cursorRawQuery.getColumnIndex("TITRE_QUIZZ"));
            String string4 = cursorRawQuery.getString(cursorRawQuery.getColumnIndex("DATE"));
            if (!hashtable.containsKey(string2) && string2.indexOf(string) >= 0) {
                hashtable.put(string2, string2);
                arrayList.add(new Survey(i, string2, i2, i3, i4, string3, string4));
            }
            cursorRawQuery.moveToNext();
            iIntValue = numValueOf.intValue() + 1;
        }
    }

    public ArrayList<Survey> getAllSpiritualGrowthKeyGroupByLevel(int i) throws Resources.NotFoundException, SQLException {
        String string = this.context.getResources().getString(C0598R.string.lb_key_group_spiritual_growth);
        createTable();
        ArrayList<Survey> arrayList = new ArrayList<>();
        Cursor cursorRawQuery = CommonPresenter.getDb().rawQuery("SELECT * FROM QR_TABLE_SURVEY ORDER BY ID ASC", null);
        int count = cursorRawQuery.getCount();
        cursorRawQuery.moveToFirst();
        Hashtable hashtable = new Hashtable();
        int iIntValue = 0;
        while (true) {
            Integer numValueOf = Integer.valueOf(iIntValue);
            if (numValueOf.intValue() >= count) {
                CommonPresenter.getDb().close();
                return arrayList;
            }
            int i2 = cursorRawQuery.getInt(cursorRawQuery.getColumnIndex("ID"));
            String string2 = cursorRawQuery.getString(cursorRawQuery.getColumnIndex("CLEF_GROUPE"));
            int i3 = cursorRawQuery.getInt(cursorRawQuery.getColumnIndex("TOTAL_QUESTION"));
            int i4 = cursorRawQuery.getInt(cursorRawQuery.getColumnIndex("TOTAL_TROUVE"));
            int i5 = cursorRawQuery.getInt(cursorRawQuery.getColumnIndex("TOTAL_ERREUR"));
            String string3 = cursorRawQuery.getString(cursorRawQuery.getColumnIndex("TITRE_QUIZZ"));
            String string4 = cursorRawQuery.getString(cursorRawQuery.getColumnIndex("DATE"));
            if (!hashtable.containsKey(string2)) {
                if (string2.indexOf(string + "-level-" + i) >= 0) {
                    hashtable.put(string2, string2);
                    arrayList.add(new Survey(i2, string2, i3, i4, i5, string3, string4));
                }
            }
            cursorRawQuery.moveToNext();
            iIntValue = numValueOf.intValue() + 1;
        }
    }

    public void insertData(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, int i, int i2, int i3, String str10) throws SQLException {
        createTable();
        CommonPresenter.getDb().execSQL("INSERT INTO QR_TABLE_SURVEY (QUESTION, CATEGORIE, EXPLICATION, PROPOSITION_1, PROPOSITION_2, PROPOSITION_3, REPONSE, REPONSE_CHOISIE, CLEF_GROUPE, TOTAL_QUESTION, TOTAL_TROUVE, TOTAL_ERREUR, TITRE_QUIZZ, DATE) VALUES ('" + str.replace("'", "''") + "', '" + str2.replace("'", "''") + "', '" + str3.replace("'", "''") + "', '" + str4.replace("'", "''") + "', '" + str5.replace("'", "''") + "', '" + str6.replace("'", "''") + "', '" + str7.replace("'", "''") + "', '" + str8.replace("'", "''") + "', '" + str9.replace("'", "''") + "', '" + i + "', '" + i2 + "', '" + i3 + "', '" + str10.replace("'", "''") + "', CURRENT_DATE);");
    }
}
