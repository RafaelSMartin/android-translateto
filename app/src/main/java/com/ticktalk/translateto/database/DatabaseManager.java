package com.ticktalk.translateto.database;

import android.content.Context;
import android.support.v4.util.Pair;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Rafael S. Martin
 */

public class DatabaseManager {

    private static final String LOG_TAG = "DatabaseManager";

    private static DatabaseManager instance;
    private DaoSession daoSession;
    private boolean isClearedDatabase;

    private ArrayList<Pair<Long, FromResult>> translations = new ArrayList<>();

    public static void init(Context context)
    {
        instance = new DatabaseManager(context);
    }

    public static DatabaseManager getInstance()
    {
        return instance;
    }


    private DatabaseManager(Context context){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "history-db");
        org.greenrobot.greendao.database.Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public List<FromResult> getAllResult()
    {
        FromResultDao fromResultDao = daoSession.getFromResultDao();
        List<FromResult> fromResults = fromResultDao.queryBuilder()
                .orderDesc(FromResultDao.Properties.ListPosition)
                .list();

        ToResultDao toResultDao = daoSession.getToResultDao();

        for(int i = 0; i != fromResults.size(); ++i)
        {
            FromResult fromResult = fromResults.get(i);
            fromResult.getToResultList().clear();

//            Log.d(LOG_TAG, String.format("from: %d, listPosition: %d, lang: %s, text: %s, Synonyms: %s"
//                    , fromResult.getId()
//                    , fromResult.getListPosition()
//                    , fromResult.getLanguageCode()
//                    , fromResult.getText()
//                    , fromResult.getSynonyms()));

            Log.d(LOG_TAG, String.format("from: %d, listPosition: %d, lang: %s, text: %s"
                    , fromResult.getId()
                    , fromResult.getListPosition()
                    , fromResult.getLanguageCode()
                    , fromResult.getText()
            ));


            List<ToResult> toResults = toResultDao.queryBuilder()
                    .orderAsc(ToResultDao.Properties.TranslationOrder)
                    .where(ToResultDao.Properties.Id.eq(fromResult.getId()))
                    .list();

            for(int j = 0; j != toResults.size(); ++j)
            {
                ToResult toResult = toResults.get(j);
                fromResult.addToResult(toResult);

                Log.d(LOG_TAG, String.format("to: %d, translationOrder: %d, lang: %s, text: %s"
                        , toResult.getId()
                        , toResult.getTranslationOrder()
                        , toResult.getLanguageCode()
                        , toResult.getText()));
            }

            //translationTemps.add(new Pair(Long.valueOf(i), fromResult));
        }
        return fromResults;
    }

    public List<FromResult> getFavoriteResult()
    {
        List<FromResult> fromResultTemps = getAllResult();
        List<FromResult> fromResults = new ArrayList<>();

        for(int i = 0; i != fromResultTemps.size(); ++i)
        {
            FromResult fromResult = fromResultTemps.get(i);


            if(fromResultTemps.get(i).getFavoriteAnimation())
            {
//                newTranslations.add(new Pair(index, fromResult));
//                ++index;
                fromResults.add(fromResult);
            }
        }
        return fromResults;
    }

    public void insertTranslation(FromResult fromResult)
    {
        Long listPosition = Long.valueOf(getAllResult().size());
        //fromResult.setId(null);
        fromResult.setListPosition(listPosition);

        FromResultDao fromResultDao = daoSession.getFromResultDao();
        fromResultDao.insert(fromResult);
        Log.d(LOG_TAG, "insert from: " + fromResult.getId() +  " text: " + fromResult.getText());


        ToResultDao toResultDao = daoSession.getToResultDao();

        List<ToResult> toResults = fromResult.getToResultList();

        for(int i = 0; i != toResults.size(); ++i)
        {
            ToResult toResult = toResults.get(i);
            //toResult.setId(null);
            toResult.setFromId(fromResult.getId());
            toResultDao.insert(toResult);
            Log.d(LOG_TAG, "insert to: " + toResult.getId() +  " text: " + toResult.getText());
        }
        //translations = getTranslations();
    }

    public void deleteTranslation(FromResult fromResult)
    {
        List<ToResult> toResultList = fromResult.getToResultList();
        for(int i = 0; i != toResultList.size(); ++i)
        {
            ToResult toResult = toResultList.get(i);

            ToResultDao toResultDao = daoSession.getToResultDao();
            toResultDao.delete(toResult);
        }

        FromResultDao fromResultDao = daoSession.getFromResultDao();
        fromResultDao.delete(fromResult);

        translations = getTranslations();
    }

    public ArrayList<Pair<Long, FromResult>> getTranslations()
    {
        reloadDatabase();
        return translations;
    }

    public void reloadDatabase()
    {
        Log.d(LOG_TAG, "reload database");

        //ArrayList<Pair<Long, FromResult>> translationTemps = new ArrayList<>();

        FromResultDao fromResultDao = daoSession.getFromResultDao();
        List<FromResult> fromResults = fromResultDao.queryBuilder()
                .orderDesc(FromResultDao.Properties.ListPosition)
                .list();

        ToResultDao toResultDao = daoSession.getToResultDao();

        for(int i = 0; i != fromResults.size(); ++i)
        {
            FromResult fromResult = fromResults.get(i);
            fromResult.getToResultList().clear();

//            Log.d(LOG_TAG, String.format("from: %d, listPosition: %d, lang: %s, text: %s"
//                    , fromResult.getId()
//                    , fromResult.getListPosition()
//                    , fromResult.getLanguageCode()
//                    , fromResult.getText()));


            List<ToResult> toResults = toResultDao.queryBuilder()
                    .orderAsc(ToResultDao.Properties.TranslationOrder)
                    .where(ToResultDao.Properties.Id.eq(fromResult.getId()))
                    .list();

            for(int j = 0; j != toResults.size(); ++j)
            {
                ToResult toResult = toResults.get(j);
                fromResult.addToResult(toResult);

//                Log.d(LOG_TAG, String.format("to: %d, translationOrder: %d, lang: %s, text: %s"
//                        , toResult.getId()
//                        , toResult.getTranslationOrder()
//                        , toResult.getLanguageCode()
//                        , toResult.getText()));
            }

            //translationTemps.add(new Pair(Long.valueOf(i), fromResult));
        }

        Collections.sort(fromResults, new Comparator<FromResult>(){
            public int compare(FromResult a, FromResult b) {
                return b.getListPosition().compareTo(a.getListPosition());
            }
        });

        translations.clear();
        for(int i = 0; i != fromResults.size(); ++i)
        {
            FromResult fromResult = fromResults.get(i);
            fromResult.setAds(false);
            //Log.d("db", i + " : " + res.toText);
            translations.add(new Pair<>(Long.valueOf(fromResult.getListPosition()), fromResult));
        }
    }

    public void updateFromResult(FromResult fromResult)
    {
        Log.d(LOG_TAG, String.format("update from: %d, listPosition: %d, lang: %s, text: %s"
                , fromResult.getId()
                , fromResult.getListPosition()
                , fromResult.getLanguageCode()
                , fromResult.getText()));
        FromResultDao fromResultDao = daoSession.getFromResultDao();
        fromResultDao.update(fromResult);
    }

    public ToResult updateSynonym(Long id, String synonym)
    {
//        Log.d(LOG_TAG, String.format("update from: %d, lang: %s, text: %s"
//                , toResult.getId()
//                , toResult.getLanguageCode()
//                , toResult.getText()));
        ToResultDao toResultDao = daoSession.getToResultDao();

        ToResult toResult = toResultDao.queryBuilder().where(ToResultDao.Properties.Id.eq(id)).uniqueOrThrow();
        toResult.setSynonyms(synonym);
        toResultDao.update(toResult);

        return toResult;
    }

    public void clear()
    {
        FromResultDao fromResultDao = daoSession.getFromResultDao();
        fromResultDao.deleteAll();

        ToResultDao toResultDao = daoSession.getToResultDao();
        toResultDao.deleteAll();

        isClearedDatabase = true;
    }

    public boolean isClearedDatabase() {
        return isClearedDatabase;
    }

    public void setClearedDatabase(boolean clearedDatabase) {
        isClearedDatabase = clearedDatabase;
    }
}
