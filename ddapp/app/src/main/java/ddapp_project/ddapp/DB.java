package ddapp_project.ddapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DB {

    private static final String DB_NAME = "db_student";
    private static final int DB_VERSION = 1;
    private static final String DB_TABLE = "mytab";

    public static int limit = 0;
    public static int offset = 20;

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_STUDENT_FIRST_NAME = "first_name";
    public static final String COLUMN_STUDENT_LAST_NAME = "last_name";
    public static final String COLUMN_STUDENT_ID = "student_id";
    public static final String COLUMN_BIRTHDAY = "birthday";
    public static final String COLUNM_COURCE_0 = "cource_0";
    public static final String COLUNM_COURCE_1 = "cource_1";
    public static final String COLUNM_COURCE_2 = "cource_2";
    public static final String COLUNM_COURCE_3 = "cource_3";
    public static final String COLUNM_GPA = "gpa";


    private static final String DB_CREATE =
            "create table " + DB_TABLE + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_STUDENT_FIRST_NAME + " text, " +
                    COLUMN_STUDENT_LAST_NAME + " text, " +
                    COLUMN_STUDENT_ID + " text, " +
                    COLUMN_BIRTHDAY + " text, " +
                    COLUNM_COURCE_0 + " integer, " +
                    COLUNM_COURCE_1 + " integer, " +
                    COLUNM_COURCE_2 + " integer, " +
                    COLUNM_COURCE_3 + " integer, " +
                    COLUNM_GPA + " float" + ");";

    private final Context mCtx;


    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public DB(Context ctx) {
        mCtx = ctx;
    }

    // открыть подключение
    public void open() {
        mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }

    // закрыть подключение
    public void close() {
        if (mDBHelper!=null) mDBHelper.close();
    }

    // получить все данные из таблицы DB_TABLE
    public Cursor getAllData() {
        return mDB.query(DB_TABLE, null, null, null, null, null, null,String.valueOf(limit)+", "+String.valueOf(offset));
    }

    // добавить запись в DB_TABLE
    public void addRec(String first_name, String last_name, String id, String birthday, int cource_0, int cource_1, int cource_2, int cource_3, float gpa) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_STUDENT_FIRST_NAME, first_name);
        cv.put(COLUMN_STUDENT_LAST_NAME, last_name);
        cv.put(COLUMN_STUDENT_ID, id);
        cv.put(COLUMN_BIRTHDAY, birthday);
        cv.put(COLUNM_COURCE_0, cource_0);
        cv.put(COLUNM_COURCE_1, cource_1);
        cv.put(COLUNM_COURCE_2, cource_2);
        cv.put(COLUNM_COURCE_3, cource_3);
        cv.put(COLUNM_GPA, gpa);
        mDB.insert(DB_TABLE, null, cv);
    }

    // удалить запись из DB_TABLE
    public void delRec(long id) {
        mDB.delete(DB_TABLE, COLUMN_ID + " = " + id, null);
    }

    //удалить все записи
    public Cursor delALL() {mDB.delete(DB_TABLE,null, null);
        return null;
    }

    // класс по созданию и управлению БД
    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, CursorFactory factory,
                        int version) {
            super(context, name, factory, version);
        }

        // создаем и заполняем БД
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
