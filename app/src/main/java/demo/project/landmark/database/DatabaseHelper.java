package demo.project.landmark.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import b.laixuantam.myaarlibrary.helper.MyLog;
import demo.project.landmark.database.table_note.NoteSaved;


public class DatabaseHelper extends OrmLiteSqliteOpenHelper
{
    private static final String NAME = "db_landmark.db";
    private static final int VERSION = 1;

    public DatabaseHelper(Context context)
    {
        super(context.getApplicationContext(), DatabaseHelper.NAME, null, DatabaseHelper.VERSION);

        this.connectionSource = new AndroidConnectionSource(this);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource)
    {
        createAllTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion)
    {
        MyLog.d("DatabaseHelper", "Upgrade DB: " + oldVersion + " to " + newVersion);
        dropAllTable();
        createAllTable();
    }

    private void dropAllTable()
    {
        try
        {
            TableUtils.dropTable(connectionSource, NoteSaved.class, true);
        }
        catch (Exception e)
        {
            MyLog.e("DatabaseHelper", e.getMessage());
        }
    }

    private void createAllTable()
    {
        try
        {
            TableUtils.createTableIfNotExists(connectionSource, NoteSaved.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            MyLog.e("DatabaseHelper", e.getMessage());
        }
    }

    public void clear()
    {
        dropAllTable();
        createAllTable();
    }


    @Override
    public void close()
    {
        super.close();
    }

}
