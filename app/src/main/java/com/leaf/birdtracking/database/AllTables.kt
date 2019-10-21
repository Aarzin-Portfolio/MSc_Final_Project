package com.leaf.birdtracking.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.leaf.birdtracking.Models.Bird
import java.util.*
import kotlin.collections.ArrayList


class AllTables(context: Context, factory: SQLiteDatabase.CursorFactory?)
    : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {


    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "Bird_Tracking_Database"
        val TABLE_NAME_USER = "UserInfo"
        val TABLE_NAME_BIRD_INSERT = "InsertBird"

        val KEY_ROWID = "_id"

        val USERID = "user_id"
        val USERNAME = "username"
        val USEREMAIL = "useremail"
        val USEREPASS = "userpassword"
        val USEREADD = "useraddress"
        val USEREPOSTCODE = "userpostcode"
        val USEREBIRTHDATE = "userbithdate"

        val BIRDINSERTID = "bird_insert_id"
        val DATE = "date"
        val L_USERNAME = "l_username"
        val LATITUDE = "lattitude"
        val LONGITUDE = "longitude"
        val BIRDNAME = "Birdname"
        val BIRDCATEGORY = "Birdcategory"
        val IMAGE = "IMAGE"
        val IMAGE_NAME = "image_name"
        val BIRD_DESC = "bird_desc"

    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_USER_TABLE = ("CREATE TABLE " +
                TABLE_NAME_USER + "("
                + USERID + " INTEGER PRIMARY KEY," +
                USERNAME + " TEXT," +
                USEREMAIL + " TEXT," +
                USEREPASS + " TEXT," +
                USEREADD + " TEXT," +
                USEREPOSTCODE + " TEXT," +
                USEREBIRTHDATE + " TEXT" + ")")


        val CREATE_BIRDINSERT_TABLE = ("CREATE TABLE " +
                TABLE_NAME_BIRD_INSERT + "("
                + BIRDINSERTID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DATE + " TEXT," +
                L_USERNAME + " TEXT," +
                LATITUDE + " TEXT," +
                LONGITUDE + " TEXT," +
                BIRDNAME + " TEXT," +
                BIRDCATEGORY + " TEXT," +
                IMAGE + " TEXT," +
                BIRD_DESC + " TEXT," +
                IMAGE_NAME + " TEXT" + ")")

        db.execSQL(CREATE_USER_TABLE)
        db.execSQL(CREATE_BIRDINSERT_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USER);
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_BIRD_INSERT);
    }

    fun UserInsert(username: String,
                   useremail: String,
                   userpassword: String,
                   useraddress: String,
                   userpostcode: String,
                   userbithdate: String): Long {

        val values = ContentValues()
        values.put(USERNAME, username)
        values.put(USEREMAIL, useremail)
        values.put(USEREPASS, userpassword)
        values.put(USEREADD, useraddress)
        values.put(USEREPOSTCODE, userpostcode)
        values.put(USEREBIRTHDATE, userbithdate)
        val db = this.writableDatabase
        val i = db.insert(TABLE_NAME_USER, null, values)
        db.close()
        return i

    }


    fun birdInsert(date: String,
                   username: String,
                   latitude: String,
                   longitude: String,
                   birdname: String,
                   bird_category: String,
                   image: String,
                   bird_desc: String,
                   image_name: String): Long {

        val values = ContentValues()
        values.put(DATE, date)
        values.put(L_USERNAME, username)
        values.put(LATITUDE, latitude)
        values.put(LONGITUDE, longitude)
        values.put(BIRDNAME, birdname)
        values.put(BIRDCATEGORY, bird_category)
        values.put(IMAGE, image)
        values.put(BIRD_DESC, bird_desc)
        values.put(IMAGE_NAME, image_name)
        val db = this.writableDatabase
        val i = db.insert(TABLE_NAME_BIRD_INSERT, null, values)
        db.close()
        return i

    }


    fun login_verification(useremail: String, userpassword: String): Boolean {

        var login = false
        val selectQuery = "SELECT  * FROM " + TABLE_NAME_USER + " WHERE " + USEREMAIL + " = '" + useremail + "' AND " +
                USEREPASS + " = '" + userpassword + "' ; "

        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            login = true
        } else {
            login = false
        }
        return login


    }


    fun getBirdData(): ArrayList<Bird> {
        val bird_list: ArrayList<Bird> = ArrayList()
        bird_list.clear()
        // Select All Query
        val selectQuery = "SELECT  * FROM $TABLE_NAME_BIRD_INSERT"

        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                val bird = Bird()
                bird.date = cursor.getString(cursor.getColumnIndexOrThrow(DATE))
                bird.userName = cursor.getString(cursor.getColumnIndexOrThrow(L_USERNAME))
                bird.bird_name = cursor.getString(cursor.getColumnIndexOrThrow(BIRDNAME))
                bird.latitude = cursor.getString(cursor.getColumnIndexOrThrow(LATITUDE))
                bird.longitude = cursor.getString(cursor.getColumnIndexOrThrow(LONGITUDE))
                bird.category = cursor.getString(cursor.getColumnIndexOrThrow(BIRDCATEGORY))
                bird.bird_image = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE))
                bird.bird_desc = cursor.getString(cursor.getColumnIndexOrThrow(BIRD_DESC))
                bird_list.add(bird)
            } while (cursor.moveToNext())
        }

        // close db connection
        db.close()
        // return notes list
        return bird_list
    }


    fun getBirdCursor(): Cursor? {
        val db = this.writableDatabase
        val selectQuery = "SELECT  rowid as " +
                KEY_ROWID + "," +
                DATE + "," +
                BIRDNAME + "," +
                LATITUDE + "," +
                LONGITUDE + "," +
                BIRDCATEGORY + "," +
                IMAGE +
                " FROM " + TABLE_NAME_BIRD_INSERT

        val cursor = db.rawQuery(selectQuery, null)

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        // return notes list
        return cursor
    }


    fun getBirdListByKeyword(search: String): Cursor? {
        //Open connection to read only
        val db = this.writableDatabase
        val selectQuery = "SELECT  rowid as " +
                KEY_ROWID + "," +
                DATE + "," +
                BIRDNAME + "," +
                LATITUDE + "," +
                LONGITUDE + "," +
                BIRDCATEGORY + "," +
                IMAGE +
                " FROM " + TABLE_NAME_BIRD_INSERT +
                " WHERE " + BIRDNAME + "  LIKE  '%" + search + "%' "


        val cursor = db.rawQuery(selectQuery, null)
        // looping through all rows and adding to list

        if (cursor != null) {
            cursor.moveToFirst()
        }
        return cursor
    }

}