package facci.pm.ta3.sqlite.trabajoautonomo3sqlite.database.entities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import facci.pm.ta3.sqlite.trabajoautonomo3sqlite.database.helper.ShoppingElementHelper;
import facci.pm.ta3.sqlite.trabajoautonomo3sqlite.database.model.ShoppingItem;
import java.util.ArrayList;

public class ShoppingItemDB {

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    private ShoppingElementHelper dbHelper;

    public ShoppingItemDB(Context context) {
        // Create new helper
        dbHelper = new ShoppingElementHelper(context);
    }

    /* Inner class that defines the table contents */
    public static abstract class ShoppingElementEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_TITLE = "title";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                COLUMN_NAME_TITLE + TEXT_TYPE + " )";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }


    public void insertElement(String productName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ShoppingElementEntry.COLUMN_NAME_TITLE,productName);
        db.insert(ShoppingElementEntry.TABLE_NAME,null,contentValues);
        //TODO: Todo el código necesario para INSERTAR un Item a la Base de datos
    }


    public ArrayList<ShoppingItem> getAllItems() {

        ArrayList<ShoppingItem> shoppingItems = new ArrayList<>();

        String[] allColumns = { ShoppingElementEntry._ID,
            ShoppingElementEntry.COLUMN_NAME_TITLE};

        Cursor cursor = dbHelper.getReadableDatabase().query(
            ShoppingElementEntry.TABLE_NAME,    // The table to query
            allColumns,                         // The columns to return
            null,                               // The columns for the WHERE clause
            null,                               // The values for the WHERE clause
            null,                               // don't group the rows
            null,                               // don't filter by row groups
            null                                // The sort order
        );

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            ShoppingItem shoppingItem = new ShoppingItem(getItemId(cursor), getItemName(cursor));
            shoppingItems.add(shoppingItem);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        dbHelper.getReadableDatabase().close();
        return shoppingItems;
    }

    private long getItemId(Cursor cursor) {
        return cursor.getLong(cursor.getColumnIndexOrThrow(ShoppingElementEntry._ID));
    }

    private String getItemName(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndexOrThrow(ShoppingElementEntry.COLUMN_NAME_TITLE));
    }


    public void clearAllItems() {
        //TODO: Todo el código necesario para ELIMINAR todos los Items de la Base de dato
        SQLiteDatabase db =dbHelper.getReadableDatabase();
        db.delete(ShoppingElementEntry.TABLE_NAME,"",null);
        db.close();



    }

    public void updateItem(ShoppingItem shoppingItem) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ContentValues contentValues =new ContentValues();
        contentValues.put(ShoppingElementEntry.COLUMN_NAME_TITLE,shoppingItem.getName());
        db.update(ShoppingElementEntry.TABLE_NAME,contentValues,ShoppingElementEntry._ID + " = "+ shoppingItem.getId(),null);
        db.close();
    }

    public void deleteItem(ShoppingItem shoppingItem) {
        //TODO: Todo el código necesario para ELIMINAR un Item de la Base de datos
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String stringFilter = ShoppingElementEntry._ID+ "=" + shoppingItem.getId();
        db.delete(ShoppingElementEntry.TABLE_NAME,stringFilter,null);
        db.close();
    }
}
