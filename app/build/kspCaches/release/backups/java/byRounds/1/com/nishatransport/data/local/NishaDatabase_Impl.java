package com.nishatransport.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.nishatransport.data.local.dao.LoadDao;
import com.nishatransport.data.local.dao.LoadDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class NishaDatabase_Impl extends NishaDatabase {
  private volatile LoadDao _loadDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `loads` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `date` INTEGER NOT NULL, `fromLocation` TEXT NOT NULL, `toLocation` TEXT NOT NULL, `vehicleNumber` TEXT NOT NULL, `notes` TEXT NOT NULL, `loadPrice` REAL NOT NULL, `loadingCharge` REAL NOT NULL, `dieselCost` REAL NOT NULL, `policeExpense` REAL NOT NULL, `tollFee` REAL NOT NULL, `driverCharge` REAL NOT NULL, `unloadingCharge` REAL NOT NULL, `otherExpense` REAL NOT NULL, `totalExpense` REAL NOT NULL, `profit` REAL NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'e5605b02f6fd806a099238d768a9b0df')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `loads`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsLoads = new HashMap<String, TableInfo.Column>(18);
        _columnsLoads.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLoads.put("date", new TableInfo.Column("date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLoads.put("fromLocation", new TableInfo.Column("fromLocation", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLoads.put("toLocation", new TableInfo.Column("toLocation", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLoads.put("vehicleNumber", new TableInfo.Column("vehicleNumber", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLoads.put("notes", new TableInfo.Column("notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLoads.put("loadPrice", new TableInfo.Column("loadPrice", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLoads.put("loadingCharge", new TableInfo.Column("loadingCharge", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLoads.put("dieselCost", new TableInfo.Column("dieselCost", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLoads.put("policeExpense", new TableInfo.Column("policeExpense", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLoads.put("tollFee", new TableInfo.Column("tollFee", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLoads.put("driverCharge", new TableInfo.Column("driverCharge", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLoads.put("unloadingCharge", new TableInfo.Column("unloadingCharge", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLoads.put("otherExpense", new TableInfo.Column("otherExpense", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLoads.put("totalExpense", new TableInfo.Column("totalExpense", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLoads.put("profit", new TableInfo.Column("profit", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLoads.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLoads.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysLoads = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesLoads = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoLoads = new TableInfo("loads", _columnsLoads, _foreignKeysLoads, _indicesLoads);
        final TableInfo _existingLoads = TableInfo.read(db, "loads");
        if (!_infoLoads.equals(_existingLoads)) {
          return new RoomOpenHelper.ValidationResult(false, "loads(com.nishatransport.data.local.entity.Load).\n"
                  + " Expected:\n" + _infoLoads + "\n"
                  + " Found:\n" + _existingLoads);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "e5605b02f6fd806a099238d768a9b0df", "17badc218708e49b8201eb0f7ac06ed2");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "loads");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `loads`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(LoadDao.class, LoadDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public LoadDao loadDao() {
    if (_loadDao != null) {
      return _loadDao;
    } else {
      synchronized(this) {
        if(_loadDao == null) {
          _loadDao = new LoadDao_Impl(this);
        }
        return _loadDao;
      }
    }
  }
}
