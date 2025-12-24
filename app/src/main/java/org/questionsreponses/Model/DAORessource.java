package org.questionsreponses.Model;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import java.util.ArrayList;
import org.questionsreponses.Presenter.CommonPresenter;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/Model/DAORessource.class */
public class DAORessource {
    private Context context;
    private final String TABLE_NAME = "QR_TABLE_RESSOURCE";
    private final String COL_1 = "ID";
    private final String COL_2 = "TITRE";
    private final String COL_3 = "ETAT";
    private final String COL_4 = "SRC";
    private final String COL_5 = "TYPE_RESSOURCE";
    private final String COL_6 = "AUTEUR";
    private final String COL_7 = "URL_ACCES";
    private final String COL_8 = "DUREE";
    private final String COL_9 = "DATE";

    public DAORessource(Context context) {
        this.context = context;
    }

    public void createTable() throws SQLException {
        CommonPresenter.buildDataBase(this.context);
        CommonPresenter.getDb().execSQL("CREATE TABLE IF NOT EXISTS QR_TABLE_RESSOURCE (ID INTEGER PRIMARY KEY AUTOINCREMENT, TITRE VARCHAR, ETAT INTEGER, SRC VARCHAR, TYPE_RESSOURCE VARCHAR, AUTEUR VARCHAR, URL_ACCES VARCHAR, DUREE VARCHAR, DATE);");
    }

    public void deleteDataBy(int i) throws SQLException {
        createTable();
        CommonPresenter.getDb().execSQL("DELETE FROM QR_TABLE_RESSOURCE WHERE ID = '" + i + "'");
    }

    public void dropAllData() throws SQLException {
        createTable();
        CommonPresenter.getDb().execSQL("DROP TABLE IF EXISTS QR_TABLE_RESSOURCE");
    }

    public ArrayList<Ressource> getAllByTypeRessource(String str) throws SQLException {
        createTable();
        ArrayList<Ressource> arrayList = new ArrayList<>();
        Cursor cursorRawQuery = CommonPresenter.getDb().rawQuery("SELECT * FROM QR_TABLE_RESSOURCE WHERE TYPE_RESSOURCE LIKE '" + str + "' ORDER BY ID DESC", null);
        int count = cursorRawQuery.getCount();
        cursorRawQuery.moveToFirst();
        int iValueOf = 0;
        while (true) {
            Integer num = iValueOf;
            if (num.intValue() >= count) {
                CommonPresenter.getDb().close();
                return arrayList;
            }
            int i = cursorRawQuery.getInt(cursorRawQuery.getColumnIndex("ID"));
            String string = cursorRawQuery.getString(cursorRawQuery.getColumnIndex("TITRE"));
            int i2 = cursorRawQuery.getInt(cursorRawQuery.getColumnIndex("ETAT"));
            String string2 = cursorRawQuery.getString(cursorRawQuery.getColumnIndex("SRC"));
            String string3 = cursorRawQuery.getString(cursorRawQuery.getColumnIndex("TYPE_RESSOURCE"));
            arrayList.add(new Ressource(i, string, i2 > 0, string2, cursorRawQuery.getString(cursorRawQuery.getColumnIndex("DATE")), cursorRawQuery.getString(cursorRawQuery.getColumnIndex("AUTEUR")), cursorRawQuery.getString(cursorRawQuery.getColumnIndex("URL_ACCES")), cursorRawQuery.getString(cursorRawQuery.getColumnIndex("DUREE")), string3));
            cursorRawQuery.moveToNext();
            iValueOf = Integer.valueOf(num.intValue() + 1);
        }
    }

    public void insertData(String str, boolean z, String str2, String str3, String str4, String str5, String str6) {
        createTable();
        CommonPresenter.getDb().execSQL("INSERT INTO QR_TABLE_RESSOURCE (TITRE, ETAT, SRC, TYPE_RESSOURCE, AUTEUR, URL_ACCES, DUREE, DATE) VALUES ('" + str.replace("'", "''") + "', '" + (z ? 1 : 0) + "', '" + str2.replace("'", "''") + "', '" + str3.replace("'", "''") + "', '" + str4.replace("'", "''") + "', '" + str5.replace("'", "''") + "', '" + str6.replace("'", "''") + "', CURRENT_DATE);");
    }

    public boolean isRessourceExists(String str) throws SQLException {
        createTable();
        ArrayList arrayList = new ArrayList();
        Cursor cursorRawQuery = CommonPresenter.getDb().rawQuery("SELECT * FROM QR_TABLE_RESSOURCE WHERE SRC LIKE '" + str + "'", null);
        int count = cursorRawQuery.getCount();
        cursorRawQuery.moveToFirst();
        int iValueOf = 0;
        while (true) {
            Integer num = iValueOf;
            if (num.intValue() >= count) {
                break;
            }
            int i = cursorRawQuery.getInt(cursorRawQuery.getColumnIndex("ID"));
            String string = cursorRawQuery.getString(cursorRawQuery.getColumnIndex("TITRE"));
            int i2 = cursorRawQuery.getInt(cursorRawQuery.getColumnIndex("ETAT"));
            String string2 = cursorRawQuery.getString(cursorRawQuery.getColumnIndex("SRC"));
            String string3 = cursorRawQuery.getString(cursorRawQuery.getColumnIndex("TYPE_RESSOURCE"));
            arrayList.add(new Ressource(i, string, i2 > 0, string2, cursorRawQuery.getString(cursorRawQuery.getColumnIndex("DATE")), cursorRawQuery.getString(cursorRawQuery.getColumnIndex("AUTEUR")), cursorRawQuery.getString(cursorRawQuery.getColumnIndex("URL_ACCES")), cursorRawQuery.getString(cursorRawQuery.getColumnIndex("DUREE")), string3));
            cursorRawQuery.moveToNext();
            iValueOf = Integer.valueOf(num.intValue() + 1);
        }
        CommonPresenter.getDb().close();
        return arrayList.size() > 0;
    }
}
