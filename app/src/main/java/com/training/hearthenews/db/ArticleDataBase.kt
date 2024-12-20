package com.training.hearthenews.db

import android.content.Context
import androidx.room.Database
import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.training.hearthenews.models.Article

@Database(entities = [Article::class], version = 1,exportSchema = false)
@TypeConverters(Converters::class)
abstract class ArticleDataBase :RoomDatabase(){

    abstract fun getArticleDao(): ArticleDao
    companion object{
        @Volatile
        private var instance:ArticleDataBase?=null
        private val LOCK=Any()


        operator fun invoke(context: Context)= instance?: synchronized(LOCK){
            instance?:createDatabase(context).also{ instance=it}
        }
        private fun createDatabase(context: Context)= Room.databaseBuilder(
            context.applicationContext,
            ArticleDataBase::class.java,
            "article_db.db"
        ).build()
    }
}